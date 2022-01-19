package com.github.startsmercury.simplynoshading.client.option;

public interface SimplyNoShadingGameOptions {
	boolean isShading();

	void setShading(boolean shading);

	default void toggleShading() {
		setShading(!isShading());
	}
}
