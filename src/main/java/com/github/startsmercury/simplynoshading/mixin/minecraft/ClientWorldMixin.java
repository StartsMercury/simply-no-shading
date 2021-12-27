package com.github.startsmercury.simplynoshading.mixin.minecraft;

import static net.fabricmc.api.EnvType.CLIENT;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.startsmercury.simplynoshading.SimplyNoShading;

import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

@Environment(CLIENT)
@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {
	public ClientWorldMixin(final MutableWorldProperties properties, final RegistryKey<World> registryRef,
			final DimensionType dimensionType, final Supplier<Profiler> profiler, final boolean isClient,
			final boolean debugWorld, final long seed) {
		super(properties, registryRef, dimensionType, profiler, isClient, debugWorld, seed);
	}

	@Inject(method = "getBrightness", at = @At("HEAD"), cancellable = true)
	private final void onGetBrightnessHead(final Direction direction, final boolean shaded,
			final CallbackInfoReturnable<Float> callback) {
		if (!SimplyNoShading.getBakedConfig().shading() && shaded) {
			callback.setReturnValue(getBrightness(direction, false));
		}
	}
}
