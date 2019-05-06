package com.github.liachmodded.pathsuggestion;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.text.TextComponent;

public class PathSuggestion implements ClientModInitializer {

  private static final char[] SEPARATORS = new char[]{'.', ':', '/'};

  @Override
  public void onInitializeClient() {

  }

  public static void shrinkSuggestions(Suggestions suggestions) {
    final List<Suggestion> entries = suggestions.getList();
    Collections.sort(entries);

    final int sharedPrefix = getCommonPrefix(entries.get(0).getText(), entries.get(entries.size() - 1).getText(), 0);

    final List<Suggestion> updated = new ArrayList<>();
    final ListIterator<Suggestion> iterator = entries.listIterator();
    while (iterator.hasNext()) {
      final Suggestion current = iterator.next();
      final String text = current.getText();

      final int loc = locateFirstSeparator(text, sharedPrefix + 1);
      if (loc < 0) {
        updated.add(current);
        continue;
      }
      final String prefix = text.substring(0, loc + 1);

      TextComponent resultTooltip = null;
      while (iterator.hasNext()) {
        final Suggestion seek = iterator.next();
        if (!seek.getText().startsWith(prefix)) {
          iterator.previous();
          break;
        }
        final TextComponent tooltip = (TextComponent) seek.getTooltip();
        if (tooltip != null) {
          if (resultTooltip == null) {
            resultTooltip = tooltip;
          } else {
            resultTooltip.append(tooltip);
          }
        }
      }

      final Suggestion tail = iterator.previous();
      iterator.next();

      final int firstEffectiveSeparator = locateLastSeparator(text, getCommonPrefix(text, tail.getText(), loc + 1));
      final String actualSuggested = text.substring(0, firstEffectiveSeparator + 1);

      updated.add(new Suggestion(current.getRange(), actualSuggested, resultTooltip));
    }

    entries.clear();
    entries.addAll(updated);
  }

  private static int getCommonPrefix(final String a, final String b, final int start) {
    final int len = Math.min(a.length(), b.length());
    for (int i = start; i < len; i++) {
      if (a.charAt(i) != b.charAt(i)) {
        return i - 1;
      }
    }
    return len - 1;
  }

  private static int locateFirstSeparator(final String st) {
    return locateFirstSeparator(st, 0);
  }

  private static int locateFirstSeparator(final String st, final int start) {
    int min = -1;
    for (char c : SEPARATORS) {
      final int t = st.indexOf(c, start);
      if (t >= 0 && (min < 0 || t < min)) {
        min = t;
      }
    }
    return min;
  }

  private static int locateLastSeparator(final String st) {
    return locateLastSeparator(st, st.length() - 1);
  }

  private static int locateLastSeparator(final String st, final int start) {
    int max = -1;
    for (char c : SEPARATORS) {
      final int t = st.lastIndexOf(c, start);
      if (t > max) {
        max = t;
      }
    }
    return max;
  }
}
