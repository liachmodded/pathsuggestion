package com.github.liachmodded.pathsuggestion.mixin;

import com.github.liachmodded.pathsuggestion.PathSuggestion;
import com.github.liachmodded.pathsuggestion.RefreshableCommandSuggestor;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.gui.screen.CommandSuggestor;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CommandSuggestor.class)
@Implements(@Interface(iface = RefreshableCommandSuggestor.class, prefix = "refreshable$"))
public abstract class CommandSuggestorMixin {

  @Shadow
  public abstract void refresh();

  @Inject(method = "showSuggestions(Z)V", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/suggestion/Suggestions;isEmpty()Z", remap = false), locals = LocalCapture.CAPTURE_FAILHARD)
  public void onCreatingSuggestionWindow(boolean showFirstSuggestion, CallbackInfo ci, Suggestions suggestions) {
    if (!suggestions.isEmpty()) {
      PathSuggestion.shrinkSuggestions(suggestions);
    }
  }

  @Intrinsic
  public void refreshable$refresh() {
    this.refresh();
  }
}
