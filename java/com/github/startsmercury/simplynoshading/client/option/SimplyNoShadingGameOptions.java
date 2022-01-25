package com.github.startsmercury.simplynoshading.client.option;

import static net.fabricmc.api.EnvType.CLIENT;

import net.fabricmc.api.Environment;
import net.minecraft.client.option.KeyBinding;

@Environment(CLIENT)
public interface SimplyNoShadingGameOptions {
	default void cycleShadeAll() {
		setShadeAll(!isShadeAll());
	}

	default void cycleShadeBlocks() {
		setShadeBlocks(!isShadeBlocks());
	}

	default void cycleShadeFluids() {
		setShadeFluids(!isShadeFluids());
	}

	boolean isShadeAll();

	boolean isShadeBlocks();

	boolean isShadeFluids();

	KeyBinding keyCycleShadeAll();

	KeyBinding keyCycleShadeBlocks();

	KeyBinding keyCycleShadeFluids();

	void setShadeAll(boolean shadeAll);

	void setShadeBlocks(boolean shadeBlocks);

	void setShadeFluids(boolean shadeFluids);
}
