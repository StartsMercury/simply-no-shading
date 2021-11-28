package com.github.startsmercury.simply_no_shading.mixin.indigo;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import com.github.startsmercury.simply_no_shading.util.ShadelessBlockRenderView;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.indigo.renderer.aocalc.AoCalculator;
import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo;

/**
 * Changes {@link net.fabricmc.fabric.impl.client.indigo indigo}'s rendering to
 * adapt the lack of distinct shading.
 *
 * @see com.github.startsmercury.simply_no_shading.mixin.indigo
 * @see AoCalculator
 * @see Mixin
 * @see #modifyWorld()
 */
@Environment(EnvType.CLIENT)
@Mixin(AoCalculator.class)
public abstract class AoCalculatorMixin {
	@Shadow
	@Final
	private BlockRenderInfo blockInfo;

	private AoCalculatorMixin() {
	}

	/**
	 * Wraps the stored world value as an instance of
	 * {@link ShadelessBlockRenderView}.
	 *
	 * @see BlockRenderInfo#blockView
	 * @see #blockInfo
	 * @see AoCalculatorMixin
	 * @see Inject
	 * @see AoCalculator#compute(MutableQuadViewImpl, boolean)
	 * @see ShadelessBlockRenderView#of
	 */
	@Inject(method = "computeFace", at = @At("HEAD"))
	private final void modifyWorld() {
		this.blockInfo.blockView = ShadelessBlockRenderView.of(this.blockInfo.blockView);
	}
}
