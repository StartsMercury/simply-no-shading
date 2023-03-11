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
	@SuppressWarnings("javadoc")
	public static class Builder {
		private boolean blockShadingEnabled = true;

		private boolean cloudShadingEnabled = true;

		/**
		 * Returns a newly build config.
		 *
		 * @return a newly build config
		 */
		public Config build() {
			return new Config(this.blockShadingEnabled, this.cloudShadingEnabled);
		}

		public boolean isBlockShadingEnabled() {
			return this.blockShadingEnabled;
		}

		public boolean isCloudShadingEnabled() {
			return this.cloudShadingEnabled;
		}

		public Builder setBlockShadingEnabled(final boolean blockShadingEnabled) {
			this.blockShadingEnabled = blockShadingEnabled;

			return this;
		}

		public Builder setCloudShadingEnabled(final boolean cloudShadingEnabled) {
			this.cloudShadingEnabled = cloudShadingEnabled;

			return this;
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
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getClass().getCanonicalName() + "[blockShadingEnabled=" + this.blockShadingEnabled
		        + ", cloadShadingEnabled=" + this.cloudShadingEnabled + "]";
	}
}
