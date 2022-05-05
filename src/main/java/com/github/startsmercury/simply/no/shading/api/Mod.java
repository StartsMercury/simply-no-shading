package com.github.startsmercury.simply.no.shading.api;

public interface Mod extends CommonMod {
	@Override
	default String getName() {
		return getIdentifier();
	}
}
