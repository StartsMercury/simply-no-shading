package io.github.startsmercury.simply_no_shading.impl.client.death_protection;

import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
import io.github.startsmercury.simply_no_shading.impl.client.ShaderPreprocessor;
import io.github.startsmercury.simply_no_shading.impl.client.extension.GetShaderPreprocessor;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Set;
import net.minecraft.FileUtil;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.apache.commons.io.IOUtils;

public class SnsGlslPreprocessor extends GlslPreprocessor implements GetShaderPreprocessor {
    private final ResourceLocation val$resourceLocation2;

    private final Map<ResourceLocation, Resource> val$map;

    private final Set<ResourceLocation> importedLocations = new ObjectArraySet<>();

    public SnsGlslPreprocessor(
        final ResourceLocation val$resourceLocation2,
        final Map<ResourceLocation, Resource> val$map
    ) {
        this.val$resourceLocation2 = val$resourceLocation2;
        this.val$map = val$map;
    }

    @Override
    public String applyImport(boolean bl, String string) {
        ResourceLocation resourceLocation;
        try {
            if (bl) {
                resourceLocation = val$resourceLocation2.withPath(
                    string2 -> FileUtil.normalizeResourcePath(string2 + string)
                );
            } else {
                resourceLocation = ResourceLocation.parse(string)
                    .withPrefix("shaders/include/");
            }
        } catch (ResourceLocationException var8) {
            ShaderManager.LOGGER.error(
                "Malformed GLSL import {}: {}",
                string,
                var8.getMessage()
            );
            return "#error " + var8.getMessage();
        }

        if (!this.importedLocations.add(resourceLocation)) {
            return null;
        } else {
            try {
                Reader reader = val$map.get(resourceLocation).openAsReader();

                String var5;
                try {
                    var5 = IOUtils.toString(reader);
                } catch (Throwable var9) {
                    try {
                        reader.close();
                    } catch (Throwable var7) {
                        var9.addSuppressed(var7);
                    }

                    throw var9;
                }

                reader.close();

                return var5;
            } catch (IOException var10) {
                ShaderManager.LOGGER.error(
                    "Could not open GLSL import {}: {}",
                    resourceLocation,
                    var10.getMessage()
                );
                return "#error " + var10.getMessage();
            }
        }
    }

    @Override
    public ShaderPreprocessor simply_no_shading$getShaderPreprocessor() {
        return ShaderPreprocessor.UNCONDITIONAL;
    }
}
