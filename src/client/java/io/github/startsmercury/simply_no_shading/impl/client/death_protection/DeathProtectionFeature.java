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
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.Material;
import net.minecraft.network.chat.Component;
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
        final boolean bl2,
        final Operation<VertexConsumer> original
    ) {
        final var minecraft = Minecraft.getInstance();
        final var simplyNoShading = minecraft.getSimplyNoShading();

        if (!simplyNoShading.config().entityShadingEnabled()) {
            final var simplyNoShadingImpl = (SimplyNoShadingImpl) simplyNoShading;

            if (simplyNoShadingImpl.context().itemActivationItem()) {
                MaterialAltRenderType.altRenderType(instance);
                final var result = original.call(instance, multiBufferSource, renderType.compose(DeathProtectionFeature::tryMangle), bl, bl2);
                MaterialAltRenderType.altRenderType(instance);
                return result;
            }
        }

        return original.call(instance, multiBufferSource, renderType, bl, bl2);
    }

    public static VertexConsumer modifyBufferRenderType(
        final Material instance,
        final MultiBufferSource multiBufferSource,
        final Function<ResourceLocation, RenderType> renderType,
        final boolean bl,
        final ItemStackRenderState.FoilType foilType,
        final Operation<VertexConsumer> original
    ) {
        final var minecraft = Minecraft.getInstance();
        final var simplyNoShading = minecraft.getSimplyNoShading();

        if (!simplyNoShading.config().entityShadingEnabled()) {
            final var simplyNoShadingImpl = (SimplyNoShadingImpl) simplyNoShading;

            if (simplyNoShadingImpl.context().itemActivationItem()) {
                MaterialAltRenderType.altRenderType(instance);
                final var result = original.call(instance, multiBufferSource, renderType.compose(DeathProtectionFeature::tryMangle), bl, foilType);
                MaterialAltRenderType.altRenderType(instance);
                return result;
            }
        }

        return original.call(instance, multiBufferSource, renderType, bl, foilType);
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
        return ResourceLocation.fromNamespaceAndPath(SnsConstants.MODID, resourceLocation.getPath());
    }

    public static RenderType modifyItemRenderType(
        final SimplyNoShadingImpl simplyNoShading,
        final RenderType original
    ) {
        if (original == RenderType.translucent()) {
            return SnsRenderTypes.translucent();
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

                    final var player = Minecraft.getInstance().player;

                    if (player != null) {
                        final var message =
                            "[" + SnsConstants.NAME + "]: Detected unexpected render type (see logs)";
                        final var component = Component.literal(message)
                            .withStyle(style -> style.withColor(ChatFormatting.RED));
                        player.displayClientMessage(component, false);
                    }
                }
            }
        }

        if (unknowns.add(renderType)) {
            final var logger = simplyNoShading.logger();
            if (logger.isWarnEnabled()) {
                logger
                    .atWarn()
                    .setCause(new AssertionError("Unexpected render type"))
                    .setMessage("[" + SnsConstants.NAME + "] " + renderType)
                    .log();
            }
        }
    }

    private DeathProtectionFeature() {}
}
