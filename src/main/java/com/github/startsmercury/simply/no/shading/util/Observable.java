package com.github.startsmercury.simply.no.shading.util;

public interface Observable<T extends Observable<T>> {
	public abstract class Observation<T, C> {
		public final T past;

		public final T present;

		@SuppressWarnings("unchecked")
		public Observation(final T point) {
			if (point instanceof final Copyable<?> copyable) {
				this.past = (T) copyable.copy();
			} else {
				throw new IllegalArgumentException("expected an instance of Copyable");
			}

			this.present = point;
		}

		public Observation(final T past, final T present) {
			this.past = past;
			this.present = present;
		}

		public abstract void react(final C context);
	}

	Observation<? extends T, ?> observe();

	Observation<? extends T, ?> observe(T past);
}
