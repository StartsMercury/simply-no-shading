package com.github.startsmercury.simply.no.shading;

import com.github.startsmercury.simply.no.shading.api.CommonMod;

public interface SimplyNoShadingCommonMod extends CommonMod {
	@Override
	default String getIdentifier() {
		return "simply-no-shading";
	}
}
