package com.github.startsmercury.simply.no.shading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import java.util.Objects;

import com.github.startsmercury.simply.no.shading.util.Copyable;

import net.fabricmc.api.Environment;

@Environment(CLIENT)
public class ShadingRule implements Copyable<ShadingRule> {
	public static final ShadingRule DUMMY = new ShadingRule(false);

	private final boolean defaultShade;

	private final ShadingRule parent;

	private boolean shade;

	public ShadingRule(final boolean defaultShade) {
		this(null, defaultShade);
	}

	public ShadingRule(final ShadingRule parent, final boolean defaultShade) {
		this.defaultShade = defaultShade;
		this.parent = parent;
		this.shade = defaultShade;
	}

	@Deprecated
	@Override
	public ShadingRule copy() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void copyFrom(final ShadingRule other) {
		setShade(other.shouldShade());
	}

	@Override
	public void copyTo(final ShadingRule other) {
		other.setShade(shouldShade());
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof final ShadingRule other) {
			return equals(other);
		} else {
			return false;
		}
	}

	protected boolean equals(final ShadingRule other) {
		// @formatter:off
		return getParent() == other.getParent()
		    && getDefaultShade() == other.getDefaultShade()
		    && shouldShade() == other.shouldShade();
		// @formatter:on
	}

	public boolean getDefaultShade() {
		return this.defaultShade;
	}

	public ShadingRule getParent() {
		return this.parent;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getParent(), shouldShade(), wouldShade());
	}

	public boolean hasParent() {
		return getParent() != null;
	}

	public void setShade(final boolean shade) {
		this.shade = shade;
	}

	public boolean shouldShade() {
		return this.shade;
	}

	public void toggleShade() {
		setShade(!shouldShade());
	}

	@Override
	public String toString() {
		// @formatter:off
		return "{parent:" + getParent()
		     + ",defaultShade:" + getDefaultShade()
		     + ",shouldShade:" + shouldShade()
		     + '}';
		// @formatter:on
	}

	public boolean wouldEquals(final ShadingRule other) {
		return wouldShade() == other.wouldShade();
	}

	public boolean wouldShade() {
		final var parent = getParent();
		final var wouldParentShade = parent != null && parent.wouldShade();

		return wouldParentShade || shouldShade();
	}

	public boolean wouldShade(final boolean shaded) {
		return shaded && wouldShade();
	}
}
