package com.github.startsmercury.simplynoshading.client;

import net.minecraft.client.Option;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.components.Button.OnTooltip;

public class ButtonOption extends Option {
	private final OnPress onPress;

	private final OnTooltip onTooltip;

	public ButtonOption(final String string, final OnPress onPress) {
		this(string, onPress, Button.NO_TOOLTIP);
	}

	public ButtonOption(final String string, final OnPress onPress, final OnTooltip onTooltip) {
		super(string);

		this.onPress = onPress;
		this.onTooltip = onTooltip;
	}

	@Override
	public AbstractWidget createButton(final Options options, final int i, final int j, final int k) {
		return new Button(i, j, k, 20, getCaption(), this.onPress, this.onTooltip);
	}
}
