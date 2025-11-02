package io.github.startsmercury.simply_no_shading.mixin.client.extension;

import io.github.startsmercury.simply_no_shading.impl.client.extension.OptionsList$HeaderEntryAware;
import io.github.startsmercury.simply_no_shading.impl.client.gui.components.OptionsList$HeaderEntry;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(OptionsList.class)
public abstract class OptionsListMixin extends ContainerObjectSelectionList<OptionsList.Entry> implements OptionsList$HeaderEntryAware {
    @Final
    @Shadow
    private OptionsSubScreen screen;

    private OptionsListMixin(
        final Minecraft minecraft,
        final int width,
        final int height,
        final int y,
        final int defaultEntryHeight
    ) {
        super(minecraft, width, height, y, defaultEntryHeight);
    }

    @Override
    public void simply_no_shading$addHeader(final Component component) {
        Objects.requireNonNull(this.minecraft.font);
        final int i = 9;
        final int j = this.children().isEmpty() ? 0 : i * 2;
        this.addEntry(new OptionsList$HeaderEntry(this.screen, component, j), j + i + 4);
    }
}
