package com.github.startsmercury.simply.no.shading.api;

public interface ClientMod extends CommonMod {
	@Override
	default String getName() {
		return getIdentifier() + "+client";
	}

	@Override
	default boolean isClientSided() {
		return true;
	}

	@Override
	default boolean isServerSided() {
		return false;
	}
}
