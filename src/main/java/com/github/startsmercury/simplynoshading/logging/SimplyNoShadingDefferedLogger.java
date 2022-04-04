package com.github.startsmercury.simplynoshading.logging;

import java.util.function.Supplier;

import org.slf4j.Logger;

public final class SimplyNoShadingDefferedLogger extends DefferedLogger {
	public SimplyNoShadingDefferedLogger(final Class<?> clazz) {
		super(clazz);
	}

	public SimplyNoShadingDefferedLogger(final String name) {
		super(name);
	}

	public SimplyNoShadingDefferedLogger(final Supplier<? extends Logger> supplier) {
		super(supplier);
	}

	@Override
	public void close() {
		warn("Closing this logger is not suppported.");
	}

	@Override
	protected void initialize(final Logger delegate) {
		debug("Initializing logger...");
		info("Logger initialized.");
	}
}