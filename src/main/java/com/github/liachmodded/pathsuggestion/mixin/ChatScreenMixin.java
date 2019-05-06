package com.github.liachmodded.pathsuggestion.mixin;

import com.github.liachmodded.pathsuggestion.PathSuggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.gui.ingame.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

  @Inject(method = "openSuggestionsWindow()V", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"), locals = LocalCapture.CAPTURE_FAILHARD)
  public void onCreatingSuggestionsWindow(CallbackInfo ci, int maxWidth, Suggestions suggestions) {
    if (!suggestions.isEmpty()) {
      PathSuggestion.shrinkSuggestions(suggestions);
    }
  }

}
