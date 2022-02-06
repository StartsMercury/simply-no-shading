package com.github.startsmercury.simplynoshading.client;

import net.minecraft.client.Option;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.components.Button.OnTooltip;
import net.minecraft.client.gui.components.OptionsList;

/**
 * A button that can be added in {@link OptionsList}.
 */
public class ButtonOption extends Option {
	/**
	 * Called when this button is pressed.
	 */
	private final OnPress onPress;

	/**
	 * Called when this button is hovered, maybe showing a tooltip.
	 */
	private final OnTooltip onTooltip;

	/**
	 * Creates a new {@code ButtonOption} with a name key and an on press action.
	 *
	 * @param string  the name key
	 * @param onPress action on press
	 * @see #ButtonOption(String, OnPress, OnTooltip)
	 */
	public ButtonOption(final String string, final OnPress onPress) {
		this(string, onPress, Button.NO_TOOLTIP);
	}

	/**
	 * Creates a new {@code ButtonOption} with a name key, on press action, and an
	 * on tooltip action.
	 *
	 * @param string    the name key
	 * @param onPress   action on press
	 * @param onTooltip action on tooltip
	 */
	public ButtonOption(final String string, final OnPress onPress, final OnTooltip onTooltip) {
		super(string);

		this.onPress = onPress;
		this.onTooltip = onTooltip;
	}

	/**
	 * Creates the button representing this option.
	 *
	 * @see Button
	 */
	@Override
	public AbstractWidget createButton(final Options options, final int i, final int j, final int k) {
		return new Button(i, j, k, 20, getCaption(), this.onPress, this.onTooltip);
	}
}
