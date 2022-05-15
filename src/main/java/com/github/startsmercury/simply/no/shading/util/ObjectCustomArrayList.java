package com.github.startsmercury.simply.no.shading.util;

import java.io.Serial;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.IntFunction;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectList;

public class ObjectCustomArrayList<K> extends ObjectArrayList<K> {
	@Serial
	private static final long serialVersionUID = 7697969750570842172L;

	public ObjectCustomArrayList() {
	}

	public ObjectCustomArrayList(final Collection<? extends K> c) {
		super(c);
	}

	public ObjectCustomArrayList(final int capacity) {
		super(capacity);
	}

	public ObjectCustomArrayList(final Iterator<? extends K> i) {
		super(i);
	}

	public ObjectCustomArrayList(final K[] a) {
		super(a);
	}

	public ObjectCustomArrayList(final K[] a, final boolean wrapped) {
		super(a, wrapped);
	}

	public ObjectCustomArrayList(final K[] a, final int offset, final int length) {
		super(a, offset, length);
	}

	public ObjectCustomArrayList(final ObjectCollection<? extends K> c) {
		super(c);
	}

	public ObjectCustomArrayList(final ObjectIterator<? extends K> i) {
		super(i);
	}

	public ObjectCustomArrayList(final ObjectList<? extends K> l) {
		super(l);
	}

	@Override
	public <T> T[] toArray(final IntFunction<T[]> generator) {
		return toArray(generator.apply(this.size));
	}
}
