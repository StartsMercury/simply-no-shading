package io.github.startsmercury.simply_no_shading.impl.client;

import net.minecraft.client.Minecraft;

public enum ReloadLevel {
    NONE,
    NEEDS_UPDATE,
    ALL_CHANGED,
    RESOURCE_PACKS;

    public void applyTo(final Minecraft minecraft) {
        switch (this) {
            case NONE -> {}
            case NEEDS_UPDATE -> minecraft.levelRenderer.cloudRenderer().markForRebuild();
            case ALL_CHANGED -> minecraft.levelExtractor.allChanged();
            case RESOURCE_PACKS -> minecraft.reloadResourcePacks();
        }
    }
}
