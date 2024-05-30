package com.github.startsmercury.simply.no.shading.util.storage;

import java.util.concurrent.CompletableFuture;

/**
 * A {@code Storage} allows {@link #load() load} and {@link #save(Object) save}
 * operations on objects. The mode of achieving this such as where the states
 * are stored, null-safety, stored format and others will depend on the
 * implementation.
 *
 * @param <T> the supported type for storing
 * @since 6.0.0
 * @deprecated No replacement
 */
@Deprecated(since = "7.0.0", forRemoval = true)
@SuppressWarnings({ "all", "removal" })
public interface Storage<T> {
	/**
	 * Loads the stored state as a new object.
	 *
	 * @return a new object loaded with the stored state
	 * @throws Exception            thrown when an implementation encountered a
	 *                              checked exception; the actual type may depend on
	 *                              the implementation
	 * @throws NullPointerException thrown when an implementation does not recognize
	 *                              {@code null} as a valid destination; usually it
	 *                              isn't
	 */
	T load() throws Exception;

	/**
	 * Loads the stored state asynchronously.
	 *
	 * @return the load future
	 * @see #load()
	 */
	default CompletableFuture<? extends T> loadAsync() {
		return CompletableFuture.supplyAsync(() -> {
			try {
				return load();
			} catch (final Exception e) {
				throw new RuntimeException("Caught unchecked, checked exception", e);
			}
		});
	}

	/**
	 * Stores the state of a given object.
	 *
	 * @param obj the object to store the state of
	 * @throws Exception            thrown when an implementation encountered a
	 *                              checked exception; the actual type may depend on
	 *                              the implementation
	 * @throws NullPointerException thrown when an implementation does not recognize
	 *                              {@code null} as a valid state to store
	 */
	void save(T obj) throws Exception;

	/**
	 * Stores the state of a given object asynchronously.
	 *
	 * @param obj the object to store the state of
	 * @return the save future
	 * @see #save(Object)
	 */
	default CompletableFuture<Void> saveAsync(final T obj) {
		return CompletableFuture.runAsync(() -> {
			try {
				save(obj);
			} catch (final Exception e) {
				throw new RuntimeException("Caught unchecked, checked exception", e);
			}
		});
	}
}
