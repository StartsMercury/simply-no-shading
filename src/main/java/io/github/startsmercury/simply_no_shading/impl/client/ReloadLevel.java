package io.github.startsmercury.simply_no_shading.impl.client;

import net.minecraft.client.Minecraft;

public enum ReloadLevel {
    NONE {
        @Override
        public void applyTo(Minecraft minecraft) {
            // DO NOTHING
        }
    },
    NEEDS_UPDATE {
        @Override
        public void applyTo(Minecraft minecraft) {
            minecraft.levelRenderer.needsUpdate();
        }
    },
    ALL_CHANGED {
        @Override
        public void applyTo(Minecraft minecraft) {
            minecraft.levelRenderer.allChanged();
        }
    },
    RESOURCE_PACKS {
        @Override
        public void applyTo(Minecraft minecraft) {
            minecraft.reloadResourcePacks();
        }
    };

    public abstract void applyTo(final Minecraft minecraft);
}
