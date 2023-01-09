package com.github.startsmercury.simply.no.shading.v6.util;

/**
 * An {@code Observable} represents an object who's changes are observed. An
 * Observation can be created using {@link #observe()} or
 * {@link #observe(Observable)} when a specific past is preferred as reference.
 * <blockquote>
 *
 * <pre>
 * // create an observation at this point in time as reference.
 * var observation = observable.observe();
 *
 * // do something with local variable 'observable'...
 *
 * // react to changes
 * // the context type is optional depending on the implementation
 * observation.react(context)
 * </pre>
 *
 * </blockquote>
 *
 * @since 5.0.0
 * @param <T> the observed type
 */
public interface Observable<T extends Observable<T>> {
	/**
	 * The {@code Observation} class represents the observed changes between the
	 * past and the present state of an object.
	 *
	 * @param <T> the observed type
	 * @param <C> the context type
	 * @since 5.0.0
	 */
	public abstract class Observation<T, C> {
		/**
		 * The past.
		 */
		public final T past;

		/**
		 * the present.
		 */
		public final T present;

		/**
		 * Creates a new observation using the "copy" of the present as the past.
		 * Providing a present state that is not an instance of {@link Copyable} will
		 * throw an {@link IllegalArgumentException}.
		 *
		 * @param present the present
		 * @throws IllegalArgumentException if construction is unable to copy the
		 *                                  present
		 */
		@SuppressWarnings("unchecked")
		public Observation(final T present) {
			if (present instanceof final Copyable<?> copyable)
				this.past = (T) copyable.copy();
			else
				throw new IllegalArgumentException("expected an instance of Copyable");

			this.present = present;
		}

		/**
		 * Creates a new observation with complete information.
		 *
		 * @param past    the past
		 * @param present the present
		 */
		public Observation(final T past, final T present) {
			this.past = past;
			this.present = present;
		}

		/**
		 * Reacts to the changes between the past and the present.
		 *
		 * @param context the context
		 * @since 5.0.0
		 */
		public abstract void react(final C context);
	}

	/**
	 * Creates an observation with a "copy" of this object at this point in time as
	 * reference.
	 *
	 * @return the observation
	 */
	Observation<? extends T, ?> observe();

	/**
	 * Creates an observation with a provided point of the past as reference.
	 *
	 * @param past the past
	 * @return the observation
	 */
	Observation<? extends T, ?> observe(T past);
}
