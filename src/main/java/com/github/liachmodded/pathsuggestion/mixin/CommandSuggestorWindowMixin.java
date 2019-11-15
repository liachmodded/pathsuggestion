package com.github.liachmodded.pathsuggestion.mixin;

import com.github.liachmodded.pathsuggestion.RefreshableCommandSuggestor;
import com.github.liachmodded.pathsuggestion.ShrinkedSuggestion;
import com.mojang.brigadier.suggestion.Suggestion;
import net.minecraft.client.gui.screen.CommandSuggestor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CommandSuggestor.SuggestionWindow.class)
public abstract class CommandSuggestorWindowMixin {

  @Shadow(aliases = {"field_21615", "this$0"})
  @Final
  private CommandSuggestor field_21615;

  @Shadow
  public abstract void discard();

  @Inject(method = "complete()V",
      at = @At("TAIL"),
      locals = LocalCapture.CAPTURE_FAILHARD)
  public void onComplete(CallbackInfo ci, Suggestion suggestion, int cursor) {
    if (suggestion instanceof ShrinkedSuggestion) {
      discard();
      ((RefreshableCommandSuggestor) field_21615).refresh();
    }
  }
}
