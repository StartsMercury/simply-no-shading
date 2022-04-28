package com.github.startsmercury.simply.no.shading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import java.io.Serial;
import java.io.Serializable;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.api.Environment;

@Environment(CLIENT)
public final class SimplyNoShadingMixinConfig implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private final ObjectArrayList<String> excluded = new ObjectArrayList<>();

	public ObjectArrayList<String> getExcluded() {
		return this.excluded;
	}
}
