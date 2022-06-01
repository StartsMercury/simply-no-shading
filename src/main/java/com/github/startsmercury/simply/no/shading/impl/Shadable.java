package com.github.startsmercury.simply.no.shading.impl;

import static net.fabricmc.api.EnvType.CLIENT;

import org.jetbrains.annotations.ApiStatus;

import com.github.startsmercury.simply.no.shading.config.ShadingRule;

import net.fabricmc.api.Environment;

/**
 * Represents a object that shading can be applied bounded to a
 * {@link ShadingRule}.
 *
 * @since 5.0.0
 */
@ApiStatus.Internal
@Environment(CLIENT)
public interface Shadable {
	/**
	 * @return the current shading rule
	 * @since 5.0.0
	 */
	ShadingRule getShadingRule();

	/**
	 * @return the shade with the shading rule applied
	 * @since 5.0.0
	 */
	boolean isShade();

	/**
	 * Sets the current shading rule to a new one.
	 *
	 * @param shadingRule the new shading rule
	 * @since 5.0.0
	 */
	void setShadingRule(ShadingRule shadingRule);
}
