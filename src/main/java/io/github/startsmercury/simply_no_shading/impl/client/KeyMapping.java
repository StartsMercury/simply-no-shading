package io.github.startsmercury.simply_no_shading.impl.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.minecraft.resources.ResourceLocation;

public class KeyMapping extends FabricKeyBinding {
    protected boolean pressed;
    protected boolean released;

    public KeyMapping(final ResourceLocation resourceLocation, final int keyCode, final String category) {
        super(resourceLocation, InputConstants.Type.KEYSYM, keyCode, category);
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
