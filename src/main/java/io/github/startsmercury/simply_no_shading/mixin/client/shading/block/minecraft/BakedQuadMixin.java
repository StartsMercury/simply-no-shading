package io.github.startsmercury.simply_no_shading.mixin.client.shading.block.minecraft;

import io.github.startsmercury.simply_no_shading.api.client.SimplyNoShading;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BakedQuad;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BakedQuad.class)
public abstract class BakedQuadMixin {
    @Unique
    private int color0;

    @Unique
    private int color1;

    @Unique
    private int color2;

    @Unique
    private int color3;

    @Final
    @Shadow
    protected int[] vertices;

    private BakedQuadMixin() {
    }

    @Inject(method = "getVertices()[I", at = @At("HEAD"))
    private void changeReturnedShade(final CallbackInfoReturnable<int[]> callback) {
        if (SimplyNoShading.instance().config().blockShadingEnabled()) {
            this.vertices[0x03] = this.color0;
            this.vertices[0x0B] = this.color1;
            this.vertices[0x13] = this.color2;
            this.vertices[0x1B] = this.color3;
        } else {
            this.vertices[0x03] = 0xFFFFFFFF;
            this.vertices[0x0B] = 0xFFFFFFFF;
            this.vertices[0x13] = 0xFFFFFFFF;
            this.vertices[0x1B] = 0xFFFFFFFF;
        }
    }

    @Inject(
        method = "<init> (" +
            "[I" +
            "I" +
            "Lnet/minecraft/core/Direction;" +
            "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;" +
        ") V",
        at = @At("RETURN")
    )
    private void onInitReturn(final CallbackInfo callback) {
        this.color0 = this.vertices[0x03];
        this.color1 = this.vertices[0x0B];
        this.color2 = this.vertices[0x13];
        this.color3 = this.vertices[0x1B];
    }
}
