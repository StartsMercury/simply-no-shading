package io.github.startsmercury.simply_no_shading.impl.client;

public final class GameContext {
    private boolean shadersEnabled;
    private boolean sodiumLoaded;

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
}
