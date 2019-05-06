package com.github.liachmodded.pathsuggestion.mixin;

import com.github.liachmodded.pathsuggestion.PathSuggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.gui.AbstractCommandBlockScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractCommandBlockScreen.class)
public class CommandScreenMixin {

  @Inject(method = "method_2357()V", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/suggestion/Suggestions;isEmpty()Z"), locals = LocalCapture.CAPTURE_FAILHARD)
  public void onCreatingSuggestionWindow(CallbackInfo ci, Suggestions suggestions) {
    if (!suggestions.isEmpty()) {
      PathSuggestion.shrinkSuggestions(suggestions);
    }
  }

}
