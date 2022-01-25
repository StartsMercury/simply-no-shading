package com.github.startsmercury.simplynoshading.client.option;

import static net.fabricmc.api.EnvType.CLIENT;

import net.fabricmc.api.Environment;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;

/**
 * Extension interface that contains members to be added to {@link GameOptions}.
 */
@Environment(CLIENT)
public interface SimplyNoShadingGameOptions {
	/**
	 * Cycles through the next values for the option shadeAll.
	 * <p>
	 * All possible values are just {@code false} and {@code true}.
	 */
	default void cycleShadeAll() {
		setShadeAll(!isShadeAll());
	}

	/**
	 * Cycles through the next values for the option shadeBlocks.
	 * <p>
	 * All possible values are just {@code false} and {@code true}.
	 */
	default void cycleShadeBlocks() {
		setShadeBlocks(!isShadeBlocks());
	}

	/**
	 * Cycles through the next values for the option shadeFluids.
	 * <p>
	 * All possible values are just {@code false} and {@code true}.
	 */
	default void cycleShadeFluids() {
		setShadeFluids(!isShadeFluids());
	}

	/**
	 * Getter for the option shadeAll.
	 *
	 * @return the value of the option shadeAll
	 */
	boolean isShadeAll();

	/**
	 * Getter for the option shadeBlocks.
	 *
	 * @return the value of the option shadeBlocks
	 */
	boolean isShadeBlocks();

	/**
	 * Getter for the option shadeFluids.
	 *
	 * @return the value of the option shadeFluids
	 */
	boolean isShadeFluids();

	/**
	 * Accessor for the option keyCycleShadeAll.
	 *
	 * @return the constant reference of the option keyCycleShadeAll
	 */
	KeyBinding keyCycleShadeAll();

	/**
	 * Accessor for the option keyCycleShadeBlocks.
	 *
	 * @return the constant reference of the option keyCycleShadeBlocks
	 */
	KeyBinding keyCycleShadeBlocks();

	/**
	 * Accessor for the option keyCycleShadeFluids.
	 *
	 * @return the constant reference of the option keyCycleShadeFluids
	 */
	KeyBinding keyCycleShadeFluids();

	/**
	 * Setter for the option shadeAll.
	 *
	 * @param shadeAll the new value
	 */
	void setShadeAll(boolean shadeAll);

	/**
	 * Setter for the option shadeBlocks.
	 *
	 * @param shadeBlocks the new value
	 */
	void setShadeBlocks(boolean shadeBlocks);

	/**
	 * Setter for the option shadeFluids.
	 *
	 * @param shadeFluids the new value
	 */
	void setShadeFluids(boolean shadeFluids);
}
