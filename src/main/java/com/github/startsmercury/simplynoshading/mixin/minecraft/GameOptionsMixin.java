package com.github.startsmercury.simplynoshading.mixin.minecraft;

import static net.fabricmc.api.EnvType.CLIENT;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingGameOptions;
import com.github.startsmercury.simplynoshading.client.option.SimplyNoShadingKeyBindings;

import net.fabricmc.api.Environment;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;

@Environment(CLIENT)
@Mixin(GameOptions.class)
public class GameOptionsMixin implements SimplyNoShadingGameOptions {
	private boolean shadeAll;

	private boolean shadeBlocks;

	private boolean shadeFluids;

	@Override
	public boolean isShadeAll() {
		return this.shadeAll;
	}

	@Override
	public boolean isShadeBlocks() {
		return this.shadeBlocks;
	}

	@Override
	public boolean isShadeFluids() {
		return this.shadeFluids;
	}

	@Override
	public final KeyBinding keyCycleShadeAll() {
		return SimplyNoShadingKeyBindings.KEY_CYCLE_SHADE_ALL;
	}

	@Override
	public KeyBinding keyCycleShadeBlocks() {
		return SimplyNoShadingKeyBindings.KEY_CYCLE_SHADE_BLOCKS;
	}

	@Override
	public KeyBinding keyCycleShadeFluids() {
		return SimplyNoShadingKeyBindings.KEY_CYCLE_SHADE_FLUIDS;
	}

	@Inject(method = "accept", at = @At("HEAD"))
	private final void onAcceptHead(final GameOptions.Visitor visitor, final CallbackInfo callback) {
		this.shadeAll = visitor.visitBoolean("shadeAll", this.shadeAll);
		this.shadeBlocks = visitor.visitBoolean("shadeBlocks", this.shadeBlocks);
		this.shadeFluids = visitor.visitBoolean("shadeFluids", this.shadeFluids);
	}

	@Override
	public void setShadeAll(final boolean shadeAll) {
		this.shadeAll = shadeAll;
	}

	@Override
	public void setShadeBlocks(final boolean shadeBlocks) {
		this.shadeBlocks = shadeBlocks;
	}

	@Override
	public void setShadeFluids(final boolean shadeFluids) {
		this.shadeFluids = shadeFluids;
	}
}
