package io.github.startsmercury.simply_no_shading.client;

import com.mojang.blaze3d.platform.InputConstants;

public class KeyMapping extends net.minecraft.client.KeyMapping {
    protected boolean pressed;
    protected boolean released;

    public KeyMapping(final String name, final int key, final String category) {
        super(name, InputConstants.Type.KEYSYM, key, category);
    }

    @Override
    public void setDown(final boolean down) {
        if (this.isDown()) {
            if (down) {
                // Do nothing
            } else {
                this.released = true;
                super.setDown(false);
            }
        } else {
            if (down) {
                this.pressed = true;
                super.setDown(true);
            } else {
                // Do nothing
            }
        }
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
}
