package io.github.startsmercury.simply_no_shading.impl.client.gui.components;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class OptionsList$HeaderEntry extends OptionsList.Entry {
    private final int paddingTop;
    private final StringWidget widget;

    public OptionsList$HeaderEntry(
        final Screen screen,
        final Component component,
        final int paddingTop
    ) {
        this(screen, component, paddingTop, new StringWidget(component, screen.getFont()));
    }

    private OptionsList$HeaderEntry(
        final Screen screen,
        final Component component,
        final int paddingTop,
        final StringWidget widget
    ) {
        super(List.of(widget), screen);
        this.paddingTop = paddingTop;
        this.widget = new StringWidget(component, screen.getFont());
    }

    public void renderContent(
        final GuiGraphics guiGraphics,
        final int mouseX,
        final int mouseY,
        final boolean hovered,
        final float deltaTicks
    ) {
        this.widget.setPosition(
            (this.screen.width - OptionsList.BIG_BUTTON_WIDTH) / 2,
            this.getContentY() + this.paddingTop
        );
        this.widget.render(guiGraphics, mouseX, mouseY, deltaTicks);
    }
}
