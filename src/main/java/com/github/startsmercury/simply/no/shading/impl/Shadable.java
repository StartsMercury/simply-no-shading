package com.github.startsmercury.simply.no.shading.impl;

import static net.fabricmc.api.EnvType.CLIENT;

import org.jetbrains.annotations.ApiStatus;

import com.github.startsmercury.simply.no.shading.config.ShadingRule;

import net.fabricmc.api.Environment;

/**
 * Represents an object that can be shaded, and the shading behavior is
 * manipulable via a {@link ShadingRule}.
 *
 * @since 5.0.0
 */
@ApiStatus.Internal
@Environment(CLIENT)
public interface Shadable {
	/**
	 * Returns the set shading rule.
	 *
	 * @return the set shading rule
	 * @since 5.0.0
	 * @see ShadingRule
	 */
	ShadingRule getShadingRule();

	/**
	 * Returns {@code true} if shading is applied.
	 *
	 * @return {@code true} if shading is applied
	 * @since 5.0.0
	 * @see #getShadingRule()
	 * @implSpec this is being manipulated by the set shading rules
	 */
	boolean isShade();

	/**
	 * Changes the current shading rule to a new one.
	 *
	 * @param shadingRule the new shading rule
	 * @since 5.0.0
	 */
	void setShadingRule(ShadingRule shadingRule);
}
