package com.github.liachmodded.pathsuggestion.mixin;

import com.github.liachmodded.pathsuggestion.ShrinkedSuggestion;
import com.mojang.brigadier.suggestion.Suggestion;
import net.minecraft.client.gui.screen.ingame.AbstractCommandBlockScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(targets = "net/minecraft/client/gui/screen/ingame/AbstractCommandBlockScreen$SuggestionWindow")
public abstract class CommandScreenSuggestionWindowMixin {

  @Shadow(aliases = {"field_2770", "this$0"}) // see chat screen one
  @Final
  private AbstractCommandBlockScreen field_2770;

  @Shadow
  public abstract void discard();

  @Inject(method = "complete()V",
      at = @At("RETURN"),
      locals = LocalCapture.CAPTURE_FAILHARD)
  public void onComplete(CallbackInfo ci, Suggestion suggestion, int cursor) {
    if (suggestion instanceof ShrinkedSuggestion) {
      discard();
      ((CommandScreenAccessor) field_2770).callUpdateCommand();
    }
  }
}
