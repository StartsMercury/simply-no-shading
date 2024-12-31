package io.github.startsmercury.simply_no_shading.impl.client;

import com.mojang.blaze3d.platform.InputConstants;

public class KeyMapping extends net.minecraft.client.KeyMapping implements AwareKeyMapping {
    protected boolean pressed;
    protected boolean released;

    public KeyMapping(final String name, final int keyCode, final String category) {
        super(name, InputConstants.Type.KEYSYM, keyCode, category);
    }

    public boolean consumeAction() {
        final boolean action = this.pressed || this.released;
        this.pressed = this.released = false;
        return action;
    }

    public boolean consumePressed() {
        final boolean pressed = this.pressed;
        this.pressed = false;
        return pressed;
    }

    public boolean consumeReleased() {
        final boolean released = this.released;
        this.released = false;
        return released;
    }

    @Override
    public void onSetDown(boolean isDown) {
        if (this.isDown()) {
            if (!isDown) {
                this.released = true;
            }
        } else {
            if (isDown) {
                this.pressed = true;
            }
        }
    }
}
