package com.github.startsmercury.simply_no_shading.mixin.indigo;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import com.github.startsmercury.simply_no_shading.util.ShadelessBlockRenderView;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.AbstractQuadRenderer;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo;

/**
 * Changes {@link net.fabricmc.fabric.impl.client.indigo indigo}'s rendering to
 * remove shading.
 *
 * @see com.github.startsmercury.simply_no_shading.mixin.indigo
 * @see AbstractQuadRenderer
 * @see Mixin
 * @see #modifyWorld()
 */
@Environment(EnvType.CLIENT)
@Mixin(AbstractQuadRenderer.class)
public abstract class AbstractQuadRendererMixin {
	/**
	 * @see AbstractQuadRenderer#blockInfo
	 * @see Shadow
	 * @see Final
	 */
	@Shadow
	@Final
	private BlockRenderInfo blockInfo;

	private AbstractQuadRendererMixin() {
	}

	/**
	 * Wraps the stored world value as an instance of
	 * {@link ShadelessBlockRenderView}.
	 *
	 * @see BlockRenderInfo#blockView
	 * @see #blockInfo
	 * @see AbstractQuadRendererMixin
	 * @see Inject
	 * @see AbstractQuadRenderer#normalShade(float, float, float, boolean)
	 * @see AbstractQuadRenderer#shadeFlatQuad(MutableQuadViewImpl)
	 * @see ShadelessBlockRenderView#of
	 */
	@Inject(method = { "normalShade", "shadeFlatQuad" }, at = @At("HEAD"), remap = false)
	private final void modifyWorld() {
		this.blockInfo.blockView = ShadelessBlockRenderView.of(this.blockInfo.blockView);
	}
}
