package com.github.startsmercury.simply.no.shading.api;

public interface ServerMod extends CommonMod {
	@Override
	default String getName() {
		return getIdentifier() + "+server";
	}

	@Override
	default boolean isClientSided() {
		return false;
	}

	@Override
	default boolean isServerSided() {
		return true;
	}
}
