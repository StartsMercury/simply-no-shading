package com.github.startsmercury.simply.no.shading.client;

import java.util.Objects;

/**
 * The {@code Config} class is extendable but immutable collection of data that
 * plays a role in the bahavior of Simply No Shading, primarily in toggling
 * shading.
 *
 * @since 6.0.0
 */
public class Config {
	/**
	 * The {@code Config.Builder} class is the builder for {@code Config}. For
	 * further details, refer to the {@code Config} class as documenting the builder
	 * would potentially be redundant and may be overlooked or be outdated.
	 */
	public static class Builder {
		/**
		 * Controls block shading, excluding block entities.
		 */
		private boolean blockShadingEnabled;

		/**
		 * Controls cloud shading.
		 */
		private boolean cloudShadingEnabled;

		/**
		 * Returns a newly build config.
		 *
		 * @return a newly build config
		 */
		public Config build() {
			return new Config(this.blockShadingEnabled, this.cloudShadingEnabled);
		}

		/**
		 * Returns {@code true} if block shading is enabled; {@code false} otherwise.
		 *
		 * @return {@code true} if block shading is enabled; {@code false} otherwise
		 */
		public boolean isBlockShadingEnabled() {
			return this.blockShadingEnabled;
		}

		/**
		 * Returns {@code true} if cloud shading is enabled; {@code false} otherwise.
		 *
		 * @return {@code true} if cloud shading is enabled; {@code false} otherwise
		 */
		public boolean isCloudShadingEnabled() {
			return this.cloudShadingEnabled;
		}

		/**
		 * Sets block shading enabled or disabled, excluding block entities.
		 *
		 * @param blockShadingEnabled block shading flag
		 * @return {@code this} builder
		 */
		public Builder setBlockShadingEnabled(final boolean blockShadingEnabled) {
			this.blockShadingEnabled = blockShadingEnabled;

			return this;
		}

		/**
		 * Sets cloud shading enabled or disabled.
		 *
		 * @param cloudShadingEnabled cloud shading flag
		 * @return {@code this} builder
		 */
		public Builder setCloudShadingEnabled(final boolean cloudShadingEnabled) {
			this.cloudShadingEnabled = cloudShadingEnabled;

			return this;
		}

		/**
		 * Toggles block shading enabled or disabled.
		 *
		 * @return {@code this} builder
		 */
		public Builder toggleBlockShading() {
			return setBlockShadingEnabled(!isBlockShadingEnabled());
		}

		/**
		 * Toggles cloud shading enabled or disabled.
		 *
		 * @return {@code this} builder
		 */
		public Builder toggleCloudShading() {
			return setCloudShadingEnabled(!isCloudShadingEnabled());
		}
	}

	/**
	 * The config preset where all shading options were disabled.
	 */
	public static final Config ALL_OFF = builder().setBlockShadingEnabled(false).setCloudShadingEnabled(false).build();

	/**
	 * The config preset that mimics OptiFine's Internal Shaders (with Old Lighting
	 * enabled).
	 */
	public static final Config INTERNAL_SHADERS = builder().setBlockShadingEnabled(false).build();

	/**
	 * The config preset that aligns with the vanilla game's default. Essentially
	 * like when Simply No Shading is not present.
	 */
	public static final Config VANILLA = builder().build();

	/**
	 * Creates a new builder.
	 *
	 * @return a new builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Creates a new builder with fields set to a pre-existing config.
	 *
	 * @param config the config to base on
	 * @return a new builder
	 */
	public static Builder builder(final Config config) {
		return builder().setBlockShadingEnabled(config.blockShadingEnabled)
		                .setCloudShadingEnabled(config.cloudShadingEnabled);
	}

	/**
	 * Controls block shading, excluding block entities.
	 */
	public final boolean blockShadingEnabled;

	/**
	 * Controls cloud shading.
	 */
	public final boolean cloudShadingEnabled;

	/**
	 * Creates a new config with all the fields set.
	 *
	 * @param blockShadingEnabled controls block shading, excluding block entities
	 * @param cloudShadingEnabled controls cloud shading
	 */
	public Config(final boolean blockShadingEnabled, final boolean cloudShadingEnabled) {
		this.blockShadingEnabled = blockShadingEnabled;
		this.cloudShadingEnabled = cloudShadingEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof final Config other)
			return this.blockShadingEnabled == other.blockShadingEnabled
			        && this.cloudShadingEnabled == other.cloudShadingEnabled;
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.blockShadingEnabled, this.cloudShadingEnabled);
	}

	/**
	 * Returns {@code true} if block shading is enabled; {@code false} otherwise.
	 *
	 * @return {@code true} if block shading is enabled; {@code false} otherwise
	 */
	public boolean isBlockShadingEnabled() {
		return this.blockShadingEnabled;
	}

	/**
	 * Returns {@code true} if cloud shading is enabled; {@code false} otherwise.
	 *
	 * @return {@code true} if cloud shading is enabled; {@code false} otherwise
	 */
	public boolean isCloudShadingEnabled() {
		return this.cloudShadingEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getClass().getCanonicalName() + "[blockShadingEnabled=" + this.blockShadingEnabled
		        + ", cloadShadingEnabled=" + this.cloudShadingEnabled + "]";
	}
}
