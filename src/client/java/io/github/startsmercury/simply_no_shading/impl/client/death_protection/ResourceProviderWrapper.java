package io.github.startsmercury.simply_no_shading.impl.client.death_protection;

import java.io.IOException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.jetbrains.annotations.NotNull;

public final class ResourceProviderWrapper implements ResourceProvider {
    public final ResourceProvider inner;

    public ResourceProviderWrapper(final ResourceProvider inner) {
        this.inner = inner;
    }

    @Override
    public @NotNull Resource getResource(final ResourceLocation resourceLocation) throws IOException {
        return this.inner.getResource(resourceLocation);
    }
}
