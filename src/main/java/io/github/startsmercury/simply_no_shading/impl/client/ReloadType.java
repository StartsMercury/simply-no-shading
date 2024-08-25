package io.github.startsmercury.simply_no_shading.impl.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

public enum ReloadType {
    NONE,
    NEEDS_UPDATE,
    ALL_CHANGED,
    RESOURCE_PACKS;

    private static final ReloadType[] VALUES = values();

    @SuppressWarnings("SameReturnValue")
    public static ReloadType blocks() {
        return ReloadType.ALL_CHANGED;
    }

    public static ReloadType clouds() {
        if (FabricLoader.getInstance().isModLoaded("sodium")) {
            return ReloadType.ALL_CHANGED;
        } else {
            return ReloadType.NEEDS_UPDATE;
        }
    }

    @SuppressWarnings("SameReturnValue")
    public static ReloadType entities() {
        return ReloadType.RESOURCE_PACKS;
    }

    public void applyTo(final Minecraft minecraft) {
        switch (this) {
            case NONE -> {}
            case NEEDS_UPDATE -> minecraft.levelRenderer.needsUpdate();
            case ALL_CHANGED -> minecraft.levelRenderer.allChanged();
            case RESOURCE_PACKS -> minecraft.reloadResourcePacks();
        }
    }

    public ReloadType compose(final ReloadType rhs) {
        return VALUES[Math.max(this.ordinal(), rhs.ordinal())];
    }
}
