package io.github.startsmercury.simply_no_shading.impl.client.death_protection;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.startsmercury.simply_no_shading.impl.client.ShaderPreprocessor;
import io.github.startsmercury.simply_no_shading.impl.client.SimplyNoShadingImpl;
import io.github.startsmercury.simply_no_shading.impl.client.SnsConstants;
import io.github.startsmercury.simply_no_shading.impl.client.extension.MaterialAltRenderType;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * Totem of Undying use animation support.
 * <p>
 * The animation is considered a GUI element and thus not applicable under
 * {@link ShaderPreprocessor#CONDITIONAL}. A copy of the rendering objects are
 * handled here and among the sibling classes.
 */
public final class DeathProtectionFeature {
    public static VertexConsumer modifyBufferRenderType(
        final Material instance,
        final MultiBufferSource multiBufferSource,
        final Function<ResourceLocation, RenderType> renderType,
        final Operation<VertexConsumer> original
    ) {
        final var minecraft = Minecraft.getInstance();
        final var simplyNoShading = minecraft.getSimplyNoShading();

        if (!simplyNoShading.config().entityShadingEnabled()) {
            final var simplyNoShadingImpl = (SimplyNoShadingImpl) simplyNoShading;

            if (simplyNoShadingImpl.context().itemActivationItem()) {
                MaterialAltRenderType.altRenderType(instance);
                final var result = original.call(instance, multiBufferSource, renderType.compose(DeathProtectionFeature::tryMangle));
                MaterialAltRenderType.altRenderType(instance);
                return result;
            }
        }

        return original.call(instance, multiBufferSource, renderType);
    }

    public static VertexConsumer modifyBufferRenderType(
        final Material instance,
        final MultiBufferSource multiBufferSource,
        final Function<ResourceLocation, RenderType> renderType,
        final boolean bl,
        final Operation<VertexConsumer> original
    ) {
        final var minecraft = Minecraft.getInstance();
        final var simplyNoShading = minecraft.getSimplyNoShading();

        if (!simplyNoShading.config().entityShadingEnabled()) {
            final var simplyNoShadingImpl = (SimplyNoShadingImpl) simplyNoShading;

            if (simplyNoShadingImpl.context().itemActivationItem()) {
                MaterialAltRenderType.altRenderType(instance);
                final var result = original.call(instance, multiBufferSource, renderType.compose(DeathProtectionFeature::tryMangle), bl);
                MaterialAltRenderType.altRenderType(instance);
                return result;
            }
        }

        return original.call(instance, multiBufferSource, renderType, bl);
    }

    public static ResourceLocation tryMangle(final ResourceLocation original) {
        final var minecraft = Minecraft.getInstance();
        final var simplyNoShading = minecraft.getSimplyNoShading();
        if (simplyNoShading.config().entityShadingEnabled()) return original;

        final var simplyNoShadingImpl = (SimplyNoShadingImpl) simplyNoShading;
        if (!simplyNoShadingImpl.context().itemActivationItem()) return original;

        return DeathProtectionFeature.mangle(original);
    }

    public static ResourceLocation mangle(final ResourceLocation resourceLocation) {
        return new ResourceLocation(SnsConstants.MODID, resourceLocation.getPath());
    }

    public static RenderType modifyItemRenderType(
        final SimplyNoShadingImpl simplyNoShading,
        final RenderType original
    ) {
        if (original == Sheets.translucentCullBlockSheet()) {
            return SnsSheets.translucentCullBlockSheet();
        } else if (original == Sheets.translucentItemSheet()) {
            return SnsSheets.translucentItemSheet();
        } else if (original == Sheets.cutoutBlockSheet()) {
            return SnsSheets.cutoutBlockSheet();
        } else {
            handleUnexpectedRenderType(simplyNoShading, original);
            return original;
        }
    }

    private static ReferenceSet<RenderType> unknowns;

    private static void handleUnexpectedRenderType(
        final SimplyNoShadingImpl simplyNoShading,
        final RenderType renderType
    ) {
        var unknowns = DeathProtectionFeature.unknowns;

        if (unknowns == null) {
            synchronized (DeathProtectionFeature.class) {
                unknowns = DeathProtectionFeature.unknowns;

                if (unknowns == null) {
                    DeathProtectionFeature.unknowns = unknowns = new ReferenceOpenHashSet<>();

                    final var message =
                        "[" + SnsConstants.NAME + "]: Detected unexpected render type (see logs)";

                    Minecraft.getInstance().gui.getChat().addMessage(
                        new TextComponent(message)
                            .withStyle(style -> style.withColor(ChatFormatting.RED))
                    );
                }
            }
        }

        if (unknowns.add(renderType)) {
            simplyNoShading
                .logger()
                .warn("[" + SnsConstants.NAME + "] {}", renderType, new AssertionError("Unexpected render type"));
        }
    }

    public static ShaderInstance rendertypeEntitySolidShader;
    public static ShaderInstance rendertypeEntityCutoutShader;
    public static ShaderInstance rendertypeEntityCutoutNoCullShader;
    public static ShaderInstance rendertypeEntityCutoutNoCullZOffsetShader;
    public static ShaderInstance rendertypeItemEntityTranslucentCullShader;
    public static ShaderInstance rendertypeEntityTranslucentCullShader;
    public static ShaderInstance rendertypeEntityNoOutlineShader;

    public static ShaderInstance getRendertypeEntitySolidShader() {
        return rendertypeEntitySolidShader;
    }

    public static ShaderInstance getRendertypeEntityCutoutShader() {
        return rendertypeEntityCutoutShader;
    }

    public static ShaderInstance getRendertypeEntityCutoutNoCullShader() {
        return rendertypeEntityCutoutNoCullShader;
    }

    public static ShaderInstance getRendertypeEntityCutoutNoCullZOffsetShader() {
        return rendertypeEntityCutoutNoCullZOffsetShader;
    }

    public static ShaderInstance getRendertypeItemEntityTranslucentCullShader() {
        return rendertypeItemEntityTranslucentCullShader;
    }

    public static ShaderInstance getRendertypeEntityTranslucentCullShader() {
        return rendertypeEntityTranslucentCullShader;
    }

    public static ShaderInstance getRendertypeEntityNoOutlineShader() {
        return rendertypeEntityNoOutlineShader;
    }

    private DeathProtectionFeature() {}
}
