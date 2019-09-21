package com.github.liachmodded.pathsuggestion.mixin;

import net.minecraft.client.gui.screen.ingame.AbstractCommandBlockScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractCommandBlockScreen.class)
public interface CommandScreenAccessor {

  @Invoker
  void callUpdateCommand();
}
