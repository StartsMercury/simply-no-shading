package com.github.startsmercury.simply.no.shading.v6.impl;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simply.no.shading.v6.config.ShadingRule;

import net.fabricmc.api.Environment;

/**
 * Represents an object that can be shaded, and the shading behavior is
 * manipulable via a {@link ShadingRule}.
 *
 * @since 5.0.0
 */
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
