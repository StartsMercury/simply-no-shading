package com.github.startsmercury.simply.no.shading.impl;

import it.unimi.dsi.fastutil.booleans.BooleanUnaryOperator;

public interface ShadableBakedQuad {
	BooleanUnaryOperator getShadeModifier();

	boolean isShade();
	
	void setShadeModifier(BooleanUnaryOperator shadeModifier);
}
