package com.github.startsmercury.simply.no.shading;

import static com.github.startsmercury.simply.no.shading.util.Value.mutable;

import com.github.startsmercury.simply.no.shading.api.ClientMod;
import com.github.startsmercury.simply.no.shading.config.SimplyNoShadingClientConfig;
import com.github.startsmercury.simply.no.shading.impl.SimplyNoShadingFabricClientMod;
import com.github.startsmercury.simply.no.shading.util.Value;

public interface SimplyNoShadingClientMod extends ClientMod, SimplyNoShadingCommonMod {
	Value<SimplyNoShadingClientMod> INSTANCE = Value.constant(() -> {
		final Value.Mutable<SimplyNoShadingClientMod> value = mutable();

		value.whenNull(() -> SimplyNoShadingFabricClientMod.INSTANCE.use(value::set));
		value.whenNull(() -> {
			throw new IllegalStateException("Unable to load any instance of SimplyNoShadingClientMod");
		});

		return value.get();
	});

	static SimplyNoShadingClientMod getInstance() {
		return INSTANCE.get();
	}

	@Override
	SimplyNoShadingClientConfig getConfig();
}
