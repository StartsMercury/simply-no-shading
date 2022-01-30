package com.github.startsmercury.simplynoshading.mixin.minecraft;

import static net.fabricmc.api.EnvType.CLIENT;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.startsmercury.simplynoshading.client.SimplyNoShadingKeyMappings;
import com.github.startsmercury.simplynoshading.client.SimplyNoShadingOptions;

import net.fabricmc.api.Environment;

/**
 * Contains implementation of the extensions to the class {@link Options}.
 * <p>
 * <table border=1 style="margin:0;padding:0;">
 * <tr>
 * <td align=center>{@link SimplyNoShadingKeyMappings#KEY_CYCLE_SHADE_ALL
 * keyCycleShadeAll}</td>
 * <td align=center colspan=2>{@link #keyCycleShadeAll() get}</td>
 * </tr>
 * <tr>
 * <td align=center>{@link SimplyNoShadingKeyMappings#KEY_CYCLE_SHADE_BLOCKS
 * keyCycleShadeBlocks}</td>
 * <td align=center colspan=2>{@link #keyCycleShadeBlocks() get}</td>
 * </tr>
 * <tr>
 * <td align=center>keyCycleShadeClouds</td>
 * <td align=center colspan=2>{@link #keyCycleShadeClouds() get}</td>
 * </tr>
 * <tr>
 * <td align=center>keyCycleShadeFluids</td>
 * <td align=center colspan=2>{@link #keyCycleShadeFluids() get}</td>
 * </tr>
 * <tr>
 * <td align=center>shadeAll</td>
 * <td align=center>{@link #isShadeAll() get}</td>
 * <td align=center>{@link #setShadeAll(boolean) set</td>
 * </tr>
 * <tr>
 * <td align=center>shadeBlocks</td>
 * <td align=center>{@link #isShadeBlocks() get}</td>
 * <td align=center>{@link #setShadeBlocks(boolean) set</td>
 * </tr>
 * <tr>
 * <td align=center>shadeClouds</td>
 * <td align=center>{@link #isShadeClouds() get}</td>
 * <td align=center>{@link #setShadeClouds(boolean) set</td>
 * </tr>
 * <tr>
 * <td align=center>shadeFluids</td>
 * <td align=center>{@link #isShadeFluids() get}</td>
 * <td align=center>{@link #setShadeFluids(boolean) set</td>
 * </tr>
 * </table>
 */
@Environment(CLIENT)
@Mixin(Options.class)
public class OptionsMixin implements SimplyNoShadingOptions {
	/**
	 * Complementary flag to other shadeXxxs properties.
	 * <p>
	 * Default value is {@code false}.
	 *
	 * @see #isShadeAll()
	 * @see #setShadeAll(boolean)
	 * @see #shadeBlocks
	 * @see #shadeFluids
	 */
	private boolean shadeAll;

	/**
	 * Complementary flag to the shadeAll property.
	 * <p>
	 * Default value is {@code false}.
	 *
	 * @see #isShadeBlocks()
	 * @see #setShadeBlocks(boolean)
	 * @see #shadeAll
	 */
	private boolean shadeBlocks;

	/**
	 * Complementary flag to the shadeAll property.
	 * <p>
	 * Default value is {@code false}.
	 *
	 * @see #isShadeClouds()
	 * @see #setShadeClouds(boolean)
	 * @see #shadeAll
	 */
	private boolean shadeClouds;

	/**
	 * Complementary flag to the shadeAll property.
	 * <p>
	 * Default value is {@code false}.
	 *
	 * @see #isShadeFluids()
	 * @see #setShadeFluids(boolean)
	 * @see #shadeAll
	 */
	private boolean shadeFluids;

	/**
	 * @see #shadeAll
	 * @see #setShadeAll(boolean)
	 */
	@Override
	public boolean isShadeAll() {
		return this.shadeAll;
	}

	/**
	 * @see #shadeBlocks
	 * @see #setShadeBlocks(boolean)
	 */
	@Override
	public boolean isShadeBlocks() {
		return this.shadeBlocks;
	}

	/**
	 * @see #shadeClouds
	 * @see #setShadeClouds(boolean)
	 */
	@Override
	public boolean isShadeClouds() {
		return this.shadeClouds;
	}

	/**
	 * @see #shadeFluids
	 * @see #setShadeFluids(boolean)
	 */
	@Override
	public boolean isShadeFluids() {
		return this.shadeFluids;
	}

	/**
	 * @see SimplyNoShadingKeyMappings#KEY_CYCLE_SHADE_ALL
	 */
	@Override
	public final KeyMapping keyCycleShadeAll() {
		return SimplyNoShadingKeyMappings.KEY_CYCLE_SHADE_ALL;
	}

	/**
	 * @see SimplyNoShadingKeyMappings#KEY_CYCLE_SHADE_BLOCKS
	 */
	@Override
	public KeyMapping keyCycleShadeBlocks() {
		return SimplyNoShadingKeyMappings.KEY_CYCLE_SHADE_BLOCKS;
	}

	/**
	 * @see SimplyNoShadingKeyMappings#KEY_CYCLE_SHADE_CLOUDS
	 */
	@Override
	public KeyMapping keyCycleShadeClouds() {
		return SimplyNoShadingKeyMappings.KEY_CYCLE_SHADE_CLOUDS;
	}

	/**
	 * @see SimplyNoShadingKeyMappings#KEY_CYCLE_SHADE_FLUIDS
	 */
	@Override
	public KeyMapping keyCycleShadeFluids() {
		return SimplyNoShadingKeyMappings.KEY_CYCLE_SHADE_FLUIDS;
	}

	/**
	 * Allows the extensions to be incorporated even in the serialization process.
	 *
	 * @param fieldAccess  the serialization api
	 * @param ci the method callback
	 */
	@Inject(method = "processOptions", at = @At("HEAD"))
	private final void onAcceptHead(Options.FieldAccess fieldAccess, CallbackInfo ci) {
		this.shadeAll = fieldAccess.process("shadeAll", this.shadeAll);
		this.shadeBlocks = fieldAccess.process("shadeBlocks", this.shadeBlocks);
		this.shadeFluids = fieldAccess.process("shadeFluids", this.shadeFluids);
	}

	/**
	 * @see #shadeAll
	 * @see #isShadeAll()
	 */
	@Override
	public void setShadeAll(final boolean shadeAll) {
		this.shadeAll = shadeAll;
	}

	/**
	 * @see #shadeBlocks
	 * @see #isShadeBlocks()
	 */
	@Override
	public void setShadeBlocks(final boolean shadeBlocks) {
		this.shadeBlocks = shadeBlocks;
	}

	/**
	 * @see #shadeClouds
	 * @see #isShadeClouds()
	 */
	@Override
	public void setShadeClouds(final boolean shadeClouds) {
		this.shadeClouds = shadeClouds;
	}

	/**
	 * @see #shadeFluids
	 * @see #isShadeFluids()
	 */
	@Override
	public void setShadeFluids(final boolean shadeFluids) {
		this.shadeFluids = shadeFluids;
	}
}
