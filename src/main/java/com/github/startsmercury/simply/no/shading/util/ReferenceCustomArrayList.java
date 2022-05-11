package com.github.startsmercury.simply.no.shading.util;

import java.io.Serial;
import java.util.function.IntFunction;

import it.unimi.dsi.fastutil.objects.ReferenceArrayList;

public class ReferenceCustomArrayList<K> extends ReferenceArrayList<K> {
	@Serial
	private static final long serialVersionUID = 4936407012722790734L;

	@Override
	public <T> T[] toArray(final IntFunction<T[]> generator) {
		return toArray(generator.apply(this.size));
	}
}
