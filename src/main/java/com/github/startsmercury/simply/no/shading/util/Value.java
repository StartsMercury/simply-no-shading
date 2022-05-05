package com.github.startsmercury.simply.no.shading.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Value<T> extends Supplier<T> {
	final class Mutable<T> implements Value<T> {
		private T value;

		public Mutable() {
			this.value = null;
		}

		public Mutable(final T value) {
			this.value = value;
		}

		@Override
		public T get() {
			return this.value;
		}

		public void set(final T value) {
			this.value = value;
		}
	}

	final class Unified {
		private boolean unlocked;

		public <T> Value<T> constant(final Supplier<? extends T> resolver) {
			return new Value<>() {
				private Object reference = false;

				@Override
				@SuppressWarnings("unchecked")
				public T get() {
					if (!Unified.this.unlocked) {
						throw new IllegalStateException("Accessed too early");
					}

					if (this.reference instanceof Boolean) {
						this.reference = resolver.get();
					}

					return (T) this.reference;
				}
			};
		}

		public boolean unlock() {
			if (!this.unlocked) {
				return this.unlocked = true;
			} else {
				return false;
			}
		}
	}

	static <T> Value<T> constant(final Supplier<? extends T> resolver) {
		return new Value<>() {
			private Object reference = false;

			@Override
			@SuppressWarnings("unchecked")
			public T get() {
				if (this.reference instanceof Boolean) {
					this.reference = resolver.get();
				}

				return (T) this.reference;
			}
		};
	}

	static <T> Mutable<T> mutable() {
		return new Mutable<>();
	}

	static <T> Mutable<T> mutable(final T value) {
		return new Mutable<>(value);
	}

	static Unified unified() {
		return new Unified();
	}

	default boolean isNull() {
		return get() == null;
	}

	default void whenNull(final Runnable action) {
		if (isNull()) {
			action.run();
		}
	}

	default void use(final Consumer<? super T> action) {
		try {
			action.accept(get());
		} catch (final RuntimeException re) {
		}
	}
}
