package com.github.startsmercury.simply.no.shading.util;

/**
 * Represents an object that can create {@link Observation observations} to
 * detect changes.
 *
 * @since 5.0.0
 *
 * @param <T>
 */
public interface Observable<T extends Observable<T>> {
	/**
	 * Represents an observed changed between the past and the present state of an
	 * object.
	 *
	 * @param <T> the observed type
	 * @param <C> the context type
	 * @since 5.0.0
	 */
	public abstract class Observation<T, C> {
		/**
		 * The past.
		 *
		 * @since 5.0.0
		 */
		public final T past;

		/**
		 * the present.
		 *
		 * @since 5.0.0
		 */
		public final T present;

		/**
		 * Creates a new instance of {@link Observation} with the a reference point.
		 *
		 * @param past the past
		 * @since 5.0.0
		 */
		@SuppressWarnings("unchecked")
		public Observation(final T point) {
			if (point instanceof final Copyable<?> copyable) {
				this.past = (T) copyable.copy();
			} else {
				throw new IllegalArgumentException("expected an instance of Copyable");
			}

			this.present = point;
		}

		/**
		 * Creates a new instance of {@link Observation} with the past and present.
		 *
		 * @param past    the past
		 * @param present the present
		 * @since 5.0.0
		 */
		public Observation(final T past, final T present) {
			this.past = past;
			this.present = present;
		}

		/**
		 * Reacts to the changes.
		 *
		 * @param context the context
		 * @since 5.0.0
		 */
		public abstract void react(final C context);
	}

	/**
	 * Creates an observation with a "copy" of this object.
	 *
	 * @return the observation
	 * @since 5.0.0
	 */
	Observation<? extends T, ?> observe();

	/**
	 * Creates an observation with a distinct past.
	 *
	 * @param past the past
	 * @return the observation
	 * @since 5.0.0
	 */
	Observation<? extends T, ?> observe(T past);
}
