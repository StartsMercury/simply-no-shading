package io.github.startsmercury.simply_no_shading.impl.client;

public final class GameContext {
    private boolean bedrockifyLoaded;
    private boolean shadersEnabled;
    private boolean sodiumLoaded;

    public boolean isBedrockifyLoaded() {
        return this.bedrockifyLoaded;
    }

    public void setBedrockifyLoaded(final boolean bedrockifyLoaded) {
        this.bedrockifyLoaded = bedrockifyLoaded;
    }

    public boolean isShadersEnabled() {
        return shadersEnabled;
    }

    public void setShadersEnabled(final boolean shadersEnabled) {
        this.shadersEnabled = shadersEnabled;
    }

    public boolean isSodiumLoaded() {
        return sodiumLoaded;
    }

    public void setSodiumLoaded(final boolean sodiumLoaded) {
        this.sodiumLoaded = sodiumLoaded;
    }
}
