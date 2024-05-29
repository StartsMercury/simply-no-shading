package io.github.startsmercury.simply_no_shading.impl.client;

import com.mojang.blaze3d.platform.InputConstants;

/**
 * A {@link net.minecraft.client.KeyMapping Minecraft key mapping} with extended
 * functionality.
 * <p>
 * This extension supports differentiating and consuming instantaneous press and
 * release events.
 *
 * @since 7.0.0
 */
public class KeyMapping extends net.minecraft.client.KeyMapping {
    protected boolean pressed;
    protected boolean released;

    /**
     * Creates a new key mapping.
     */
    public KeyMapping(final String name, final int key, final String category) {
        super(name, InputConstants.Type.KEYSYM, key, category);
    }

    /**
     * Detects a press and clears this state.
     *
     * @return {@code true} if {@code this} was pressed
     */
    public boolean consumePressed() {
        final var pressed = this.pressed;
        this.pressed = false;
        return pressed;
    }

    /**
     * Detects a release and clears this state.
     *
     * @return {@code true} if {@code this} was released
     */
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
