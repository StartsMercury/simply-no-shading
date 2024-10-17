package io.github.startsmercury.simply_no_shading.impl.client;

import com.mojang.blaze3d.platform.InputConstants;

public class KeyMapping extends net.minecraft.client.KeyMapping {
    protected boolean pressed;
    protected boolean released;

    public KeyMapping(final String name, final int keyCode, final String category) {
        super(name, InputConstants.Type.KEYSYM, keyCode, category);
    }

    public boolean consumeAction() {
        final var action = this.pressed || this.released;
        this.pressed = this.released = false;
        return action;
    }

    public boolean consumePressed() {
        final var pressed = this.pressed;
        this.pressed = false;
        return pressed;
    }

    public boolean consumeReleased() {
        final var released = this.released;
        this.released = false;
        return released;
    }

    @Override
    public void setDown(final boolean down) {
        if (this.isDown()) {
            if (!down) {
                this.released = true;
                super.setDown(false);
            }
        } else {
            if (down) {
                this.pressed = true;
                super.setDown(true);
            }
        }
    }
}
