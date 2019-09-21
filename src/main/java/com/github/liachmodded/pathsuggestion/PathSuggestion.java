package com.github.liachmodded.pathsuggestion;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;

/**
 * Utilities to shrink the suggestions.
 */
public final class PathSuggestion {

  private static final char[] SEPARATORS = new char[]{'.', ':', '/'};

  private PathSuggestion() {}

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
      final int startIndex = iterator.previousIndex();

      Text resultTooltip = null;
      while (iterator.hasNext()) {
        final Suggestion seek = iterator.next();
        if (!seek.getText().startsWith(prefix)) {
          iterator.previous();
          break;
        }
        final Message tooltip = seek.getTooltip();
        if (tooltip != null) {
          final Text tooltipText = Texts.toText(tooltip);
          if (resultTooltip == null) {
            resultTooltip = tooltipText;
          } else {
            resultTooltip.append(tooltipText);
          }
        }
      }

      final Suggestion tail = iterator.previous();
      final int endIndex = iterator.nextIndex();
      iterator.next();

      if (startIndex == endIndex) {
        updated.add(tail); // Just skip through if there is only one option available
      } else {
        final int firstEffectiveSeparator = locateLastSeparator(text, getCommonPrefix(text, tail.getText(), loc + 1));
        final String actualSuggested = text.substring(0, firstEffectiveSeparator + 1);

        updated.add(new ShrinkedSuggestion(current.getRange(), actualSuggested, resultTooltip));
      }
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
