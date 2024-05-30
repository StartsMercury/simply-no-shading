package io.github.startsmercury.simply_no_shading.impl.client;

import net.minecraft.client.renderer.LevelRenderer;

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

    public ReloadType compose(final ReloadType rhs) {
        return switch (this) {
            case ALL_CHANGED -> switch (rhs) {
                case ALL_CHANGED, NEEDS_UPDATE, NONE -> this;
            };
            case NEEDS_UPDATE -> switch (rhs) {
                case ALL_CHANGED -> rhs;
                case NEEDS_UPDATE, NONE -> this;
            };
            case NONE -> switch (rhs) {
                case ALL_CHANGED, NEEDS_UPDATE, NONE -> rhs;
            };
        };
    }
}
