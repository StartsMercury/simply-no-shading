package io.github.startsmercury.simply_no_shading.impl.client;

public final class GameContext {
    private boolean shadersEnabled;
    private boolean sodiumLoaded;
    private boolean itemActivationItem;

    public boolean shadersEnabled() {
        return shadersEnabled;
    }

    public void setShadersEnabled(final boolean shadersEnabled) {
        this.shadersEnabled = shadersEnabled;
    }

    public boolean sodiumLoaded() {
        return sodiumLoaded;
    }

    public void setSodiumLoaded(final boolean sodiumLoaded) {
        this.sodiumLoaded = sodiumLoaded;
    }

    public boolean itemActivationItem() {
        return itemActivationItem;
    }

    public void setItemActivationItem(final boolean itemActivationItem) {
        this.itemActivationItem = itemActivationItem;
    }
}
