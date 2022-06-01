package com.github.startsmercury.simply.no.shading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import java.util.Objects;

import com.github.startsmercury.simply.no.shading.util.Copyable;

import net.fabricmc.api.Environment;

/**
 * A shading rule modifies the shade of the target shade.
 *
 * @see #wouldShade(boolean)
 * @since 5.0.0
 */
@Environment(CLIENT)
public class ShadingRule implements Copyable<ShadingRule> {
	/**
	 * A dummy instance of {@code ShadingRule}.
	 *
	 * @since 5.0.0
	 */
	public static final ShadingRule DUMMY = new ShadingRule(false);

	/**
	 * The default shade.
	 */
	private final boolean defaultShade;

	/**
	 * The parent.
	 */
	private final transient ShadingRule parent;

	/**
	 * The shade.
	 */
	private boolean shade;

	/**
	 * Creates an instance of {@code ShadingRule}.
	 *
	 * @param defaultShade the default shade
	 */
	public ShadingRule(final boolean defaultShade) {
		this(null, defaultShade);
	}

	/**
	 * Creates an instance of {@code ShadingRule} with a parent.
	 *
	 * @param parent       the parent
	 * @param defaultShade the default shade
	 */
	public ShadingRule(final ShadingRule parent, final boolean defaultShade) {
		this.defaultShade = defaultShade;
		this.parent = parent;
		this.shade = defaultShade;
	}

	/**
	 * <b>This functionality is not supported</b><br>
	 *
	 * @since 5.0.0
	 */
	@Deprecated
	@Override
	public ShadingRule copy() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Copies from another object.
	 *
	 * @param other the other object
	 * @since 5.0.0
	 */
	@Override
	public void copyFrom(final ShadingRule other) {
		setShade(other.shouldShade());
	}

	/**
	 * Copies into another object.
	 *
	 * @param other the other object
	 * @since 5.0.0
	 */
	@Override
	public void copyTo(final ShadingRule other) {
		other.setShade(shouldShade());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
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

	/**
	 * @param other the other shading rule
	 * @return a {@code boolean} value
	 */
	protected boolean equals(final ShadingRule other) {
		// @formatter:off
		return getParent() == other.getParent()
		    && getDefaultShade() == other.getDefaultShade()
		    && shouldShade() == other.shouldShade();
		// @formatter:on
	}

	/**
	 * @return the default shade
	 * @since 5.0.0
	 */
	public boolean getDefaultShade() {
		return this.defaultShade;
	}

	/**
	 * @return the parent
	 * @since 5.0.0
	 */
	public ShadingRule getParent() {
		return this.parent;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getParent(), shouldShade(), wouldShade());
	}

	/**
	 * Checks if this shading rule has a parent.
	 *
	 * @return a {@code boolean} value
	 * @since 5.0.0
	 */
	public boolean hasParent() {
		return getParent() != null;
	}

	/**
	 * Resets the shade to the default shade.
	 *
	 * @since 5.0.0
	 */
	public void resetShade() {
		setShade(getDefaultShade());
	}

	/**
	 * Sets the shade to a new value.
	 *
	 * @param shade the new shade
	 * @since 5.0.0
	 */
	public void setShade(final boolean shade) {
		this.shade = shade;
	}

	/**
	 * @return the shade
	 * @since 5.0.0
	 */
	public boolean shouldShade() {
		return this.shade;
	}

	/**
	 * Toggles the shade.
	 *
	 * @since 5.0.0
	 */
	public void toggleShade() {
		setShade(!shouldShade());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 5.0.0
	 */
	@Override
	public String toString() {
		// @formatter:off
		return "{parent:" + getParent()
		     + ",defaultShade:" + getDefaultShade()
		     + ",shouldShade:" + shouldShade()
		     + '}';
		// @formatter:on
	}

	/**
	 * @param other the other shading rule
	 * @return would shade
	 * @since 5.0.0
	 */
	public boolean wouldEquals(final ShadingRule other) {
		return wouldShade() == other.wouldShade();
	}

	/**
	 * @return would shade
	 * @since 5.0.0
	 */
	public boolean wouldShade() {
		final var parent = getParent();
		final var wouldParentShade = parent != null && parent.wouldShade();

		return wouldParentShade || shouldShade();
	}

	/**
	 * @param shaded the shaded state
	 * @return the modified shaded state
	 * @since 5.0.0
	 */
	public boolean wouldShade(final boolean shaded) {
		return shaded && wouldShade();
	}
}
