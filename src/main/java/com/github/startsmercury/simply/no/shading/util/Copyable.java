package com.github.startsmercury.simply.no.shading.util;

public interface Copyable<T extends Copyable<T>> {
	T copy();

	void copyFrom(T other);

	void copyTo(T other);
}
