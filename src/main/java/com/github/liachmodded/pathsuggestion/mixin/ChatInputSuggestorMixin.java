package com.github.liachmodded.pathsuggestion.mixin;

import com.github.liachmodded.pathsuggestion.PathSuggestion;
import com.github.liachmodded.pathsuggestion.ChatInputSuggestorAccessor;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChatInputSuggestor.class)
public abstract class ChatInputSuggestorMixin implements ChatInputSuggestorAccessor {

    @Shadow public abstract void refresh();
    @Shadow public abstract void clearWindow();

    @Redirect(method = "showCommandSuggestions", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/suggestion/Suggestions;isEmpty()Z"))
    public boolean onCreatingSuggestionWindow(Suggestions suggestions) {
        if (!suggestions.isEmpty()) {
            PathSuggestion.shrinkSuggestions(suggestions);
            return false;
        }

        return true;
    }

    @Intrinsic
    public void refreshable$refresh() {
        this.refresh();
    }

    @Intrinsic
    public void refreshable$clearWindow() {
        this.clearWindow();
    }
}
