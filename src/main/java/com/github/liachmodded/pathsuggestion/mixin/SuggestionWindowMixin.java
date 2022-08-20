package com.github.liachmodded.pathsuggestion.mixin;

import com.github.liachmodded.pathsuggestion.ChatInputSuggestorAccessor;
import com.github.liachmodded.pathsuggestion.ShrinkedSuggestion;
import com.mojang.brigadier.suggestion.Suggestion;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChatInputSuggestor.SuggestionWindow.class)
public abstract class SuggestionWindowMixin {

    @Shadow(aliases = {"field_21615", "this$0"})
    @Final
    private ChatInputSuggestor field_21615;

    @Inject(method = "complete()V", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onComplete(CallbackInfo ci, Suggestion suggestion, int cursor) {
        if (suggestion instanceof ShrinkedSuggestion) {
            ((ChatInputSuggestorAccessor) field_21615).refreshable$clearWindow();
            ((ChatInputSuggestorAccessor) field_21615).refreshable$refresh();
        }
    }
}
