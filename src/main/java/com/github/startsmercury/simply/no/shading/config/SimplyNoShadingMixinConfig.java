package com.github.startsmercury.simply.no.shading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

import com.github.startsmercury.simply.no.shading.mixin.SimplyNoShadingMixinPlugin;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.fabricmc.api.Environment;

/**
 * Simply No Shading mixin config.
 *
 * @see SimplyNoShadingMixinPlugin
 * @since 5.0.0
 */
@Environment(CLIENT)
public final class SimplyNoShadingMixinConfig implements Serializable {
	/**
	 * Serial version UID.
	 */
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Exclusion set.
	 */
	private final ObjectOpenHashSet<String> excluded = new ObjectOpenHashSet<>();

	/**
	 * @return the exclusion set.
	 * @since 5.0.0
	 */
	public ObjectOpenHashSet<String> getExcluded() {
		return this.excluded;
	}

	/**
	 * Copies the state of the other {@code SimplyNoShadingMixinConfig}.
	 *
	 * @param other the other config
	 * @return itself
	 * @since 5.0.0
	 */
	public SimplyNoShadingMixinConfig set(final SimplyNoShadingMixinConfig other) {
		setExcluded(other.excluded);

		return this;
	}

	/**
	 * Copies the exclusion set from a different collection.
	 *
	 * @param other the other collection
	 * @return itself
	 * @since 5.0.0
	 */
	public SimplyNoShadingMixinConfig setExcluded(final Collection<String> excluded) {
		this.excluded.clear();
		this.excluded.addAll(excluded);

		return this;
	}
}
