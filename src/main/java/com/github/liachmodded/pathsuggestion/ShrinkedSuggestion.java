package com.github.liachmodded.pathsuggestion;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;

/**
 * Ugly hack for the game to auto request renewal when the shrinked suggestion is used.
 */
public final class ShrinkedSuggestion extends Suggestion {

  ShrinkedSuggestion(StringRange range, String text, Message tooltip) {
    super(range, text, tooltip);
  }
}
