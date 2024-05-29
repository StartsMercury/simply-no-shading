package io.github.startsmercury.simply_no_shading.impl.client;

import net.minecraft.client.renderer.LevelRenderer;

/**
 * @since 7.0.0
 */
public enum ReloadType {
    ALL_CHANGED,
    NEEDS_UPDATE,
    NONE;

    public static ReloadType blocks() {
        return ReloadType.ALL_CHANGED;
    }

    public static ReloadType clouds() {
        return ReloadType.NEEDS_UPDATE;
    }

    public void applyTo(final LevelRenderer levelRenderer) {
        switch (this) {
            case NONE -> {}
            case NEEDS_UPDATE -> levelRenderer.needsUpdate();
            case ALL_CHANGED -> levelRenderer.allChanged();
        }
    }

    public ReloadType max(final ReloadType rhs) {
        if (this.compareTo(rhs) <= 0) {
            return this;
        } else {
            return rhs;
        }
    }
}
