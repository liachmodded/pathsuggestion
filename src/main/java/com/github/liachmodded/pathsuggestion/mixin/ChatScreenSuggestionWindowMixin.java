package com.github.liachmodded.pathsuggestion.mixin;

import com.github.liachmodded.pathsuggestion.ShrinkedSuggestion;
import com.mojang.brigadier.suggestion.Suggestion;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(targets = "net/minecraft/client/gui/screen/ChatScreen$SuggestionWindow")
public abstract class ChatScreenSuggestionWindowMixin {

  @Shadow(aliases = {"field_2397", "this$0"}) // javap the class to check out the field name!
  @Final
  private ChatScreen field_2397;

  @Shadow
  public abstract void close();

  @Inject(method = "complete()V",
      at = @At("RETURN"),
      locals = LocalCapture.CAPTURE_FAILHARD)
  public void onComplete(CallbackInfo ci, Suggestion suggestion, int cursor) {
    if (suggestion instanceof ShrinkedSuggestion) {
      close();
      ((ChatScreenAccessor) field_2397).callUpdateCommand();
    }
  }
}
