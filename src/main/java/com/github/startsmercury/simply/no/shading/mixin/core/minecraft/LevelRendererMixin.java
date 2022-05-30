package com.github.startsmercury.simply.no.shading.mixin.core.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.github.startsmercury.simply.no.shading.impl.CloudRenderer;

import net.minecraft.client.renderer.LevelRenderer;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin implements CloudRenderer {
	@Shadow
	private boolean generateClouds;

	@Override
	public void generateClouds() {
		this.generateClouds = true;
	}
}
