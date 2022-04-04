package com.github.startsmercury.simplynoshading.util;

import java.util.function.Supplier;

import org.slf4j.Logger;

/**
 * A custom {@link DefferedLogger} that is not closable and logs a message when
 * initialized.
 */
public final class SimplyNoShadingDefferedLogger extends DefferedLogger {
	/**
	 * Creates a {@code SimplyNoShadingDefferedLogger} with a class as a name.
	 *
	 * @param clazz the class
	 */
	public SimplyNoShadingDefferedLogger(final Class<?> clazz) {
		super(clazz);
	}

	/**
	 * Creates a {@code SimplyNoShadingDefferedLogger} with a name.
	 *
	 * @param name the name
	 */
	public SimplyNoShadingDefferedLogger(final String name) {
		super(name);
	}

	/**
	 * Creates a {@code SimplyNoShadingDefferedLogger} with a supplier.
	 *
	 * @param supplier the supplier
	 */
	public SimplyNoShadingDefferedLogger(final Supplier<? extends Logger> supplier) {
		super(supplier);
	}

	/**
	 * Performs no operations.
	 */
	@Deprecated
	@Override
	public void close() {
		warn("Closing this logger is not suppported.");
	}

	/**
	 * Logs messages after the delegate logger was first created.
	 */
	@Override
	protected void initialize(final Logger delegate) {
		debug("Initializing logger...");
		info("Logger initialized.");
	}
}