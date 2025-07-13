package io.github.startsmercury.simply_no_shading.impl.client.death_protection;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record ResourceProviderWrapper(ResourceProvider inner) implements ResourceProvider {
    @Override
    public @NotNull Optional<Resource> getResource(final ResourceLocation resourceLocation) {
        return this.inner.getResource(resourceLocation);
    }
}
