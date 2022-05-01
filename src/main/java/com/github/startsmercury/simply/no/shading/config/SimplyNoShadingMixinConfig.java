package com.github.startsmercury.simply.no.shading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.fabricmc.api.Environment;

@Environment(CLIENT)
public final class SimplyNoShadingMixinConfig implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private final ObjectOpenHashSet<String> excluded = new ObjectOpenHashSet<>();

	public ObjectOpenHashSet<String> getExcluded() {
		return this.excluded;
	}

	public SimplyNoShadingMixinConfig set(final SimplyNoShadingMixinConfig other) {
		setExcluded(other.excluded);

		return this;
	}

	public SimplyNoShadingMixinConfig setExcluded(final Collection<String> excluded) {
		this.excluded.clear();
		this.excluded.addAll(excluded);

		return this;
	}
}
