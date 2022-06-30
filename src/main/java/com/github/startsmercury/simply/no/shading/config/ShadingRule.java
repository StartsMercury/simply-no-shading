package com.github.startsmercury.simply.no.shading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import java.util.Objects;

import com.github.startsmercury.simply.no.shading.util.Copyable;

import net.fabricmc.api.Environment;

/**
 * The {@code ShadingRule} class represents a toggle for shading. The toggled
 * state can be accessed, changed, and cycled using {@link #shouldShade()},
 * {@link #setShade(boolean)}, and {@link #toggleShade()} respectively.
 * <p>
 * To implement the toggling behavior (example is by using mixins)
 * {@link #wouldShade()} or {@link #wouldShade(boolean)} would be sufficient.
 *
 * @since 5.0.0
 */
@Environment(CLIENT)
public class ShadingRule implements Copyable<ShadingRule> {
	/**
	 * A dummy instance of {@code ShadingRule}.
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
	 * Creates a new {@code ShadingRule}.
	 *
	 * @param defaultShade the default shade
	 */
	public ShadingRule(final boolean defaultShade) {
		this(null, defaultShade);
	}

	/**
	 * Creates a new {@code ShadingRule} with a parent.
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
	 * <b>Deprecated.</b> <i>This operation is not supported.</i>
	 * <p>
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public ShadingRule copy() {
		throw new UnsupportedOperationException("copy");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyFrom(final ShadingRule other) {
		setShade(other.shouldShade());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyTo(final ShadingRule other) {
		other.setShade(shouldShade());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		else if (obj instanceof final ShadingRule other)
			return equals(other);
		else
			return false;
	}

	/**
	 * Returns {@code true} if the other shading rule have the same parent, default
	 * shade, and shade.
	 *
	 * @param other the other shading rule
	 * @return {@code true} if the other shading rule have the same parent, default
	 *         shade, and shade
	 */
	protected boolean equals(final ShadingRule other) {
		// @formatter:off
		return getParent() == other.getParent()
		    && getDefaultShade() == other.getDefaultShade()
		    && shouldShade() == other.shouldShade();
		// @formatter:on
	}

	/**
	 * Returns the default shade.
	 *
	 * @return the default shade
	 * @since 5.0.0
	 */
	public boolean getDefaultShade() { return this.defaultShade; }

	/**
	 * Returns the parent.
	 *
	 * @return the parent
	 * @since 5.0.0
	 */
	public ShadingRule getParent() { return this.parent; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getParent(), shouldShade(), wouldShade());
	}

	/**
	 * Returns {@code true} if this shading rule has a parent.
	 *
	 * @return {@code true} if this shading rule has a parent
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
	public void setShade(final boolean shade) { this.shade = shade; }

	/**
	 * Returns the shade.
	 *
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
	 * Returns {@code true} if the value returned from the two objects'
	 * {@link #wouldShade()} are equal.
	 *
	 * @param other the other shading rule
	 * @return {@code true} if the value returned from the two objects'
	 *         {@link #wouldShade()} are equal
	 * @since 5.0.0
	 */
	public boolean wouldEquals(final ShadingRule other) {
		return wouldShade() == other.wouldShade();
	}

	/**
	 * Returns the same value when using {@link #wouldShade(boolean)
	 * wouldShade(true)}.
	 *
	 * @return the same value when using {@link #wouldShade(boolean)
	 *         wouldShade(true)}
	 * @since 5.0.0
	 */
	public boolean wouldShade() {
		final var parent = getParent();
		final var wouldParentShade = parent != null && parent.wouldShade();

		return wouldParentShade || shouldShade();
	}

	/**
	 * Returns the modified shade.
	 *
	 * @param shaded the raw shade
	 * @return the modified shade
	 * @since 5.0.0
	 */
	public boolean wouldShade(final boolean shaded) {
		return shaded && wouldShade();
	}
}
