package com.github.startsmercury.simply.no.shading.impl;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.startsmercury.simply.no.shading.api.CommonMod;

public abstract class AbstractCommonMod<C, I> implements CommonMod {
	private C config;

	private final Class<C> configClass;

	private final I initializer;

	private Logger logger;

	private AbstractCommonMod(final C config, final Class<C> configClass,
			final Function<? super CommonMod, ? extends I> initializerFunction, final boolean createLogger) {
		this.config = config;
		this.configClass = configClass;
		this.initializer = initializerFunction.apply(this);

		if (createLogger) {
			this.logger = createLogger();
		}
	}

	protected AbstractCommonMod(final C config, final Function<? super CommonMod, ? extends I> initializerFunction) {
		this(config, initializerFunction, false);
	}

	@SuppressWarnings("unchecked")
	protected AbstractCommonMod(final C config, final Function<? super CommonMod, ? extends I> initializerFunction,
			final boolean createLogger) {
		this(config, (Class<C>) config.getClass(), initializerFunction, createLogger);
	}

	protected AbstractCommonMod(final Class<C> configClass,
			final Function<? super CommonMod, ? extends I> initializerFunction) {
		this(configClass, initializerFunction, false);
	}

	protected AbstractCommonMod(final Class<C> configClass,
			final Function<? super CommonMod, ? extends I> initializerFunction, final boolean createLogger) {
		this(null, configClass, initializerFunction, createLogger);
	}

	protected AbstractCommonMod(final Function<? super CommonMod, ? extends I> initializerFunction) {
		this((C) null, initializerFunction, false);
	}

	protected AbstractCommonMod(final Function<? super CommonMod, ? extends I> initializerFunction,
			final boolean createLogger) {
		this((C) null, initializerFunction, createLogger);
	}

	protected Logger createLogger() {
		return LoggerFactory.getLogger(getName());
	}

	@Override
	public C getConfig() {
		return this.config;
	}

	protected Class<C> getConfigClass() {
		return this.configClass;
	}

	@Override
	public I getInitializer() {
		return this.initializer;
	}

	@Override
	public Logger getLogger() {
		return this.logger == null ? this.logger = createLogger() : this.logger;
	}

	@Override
	public void loadConfig() {
		setConfig(loadConfigInternally());
	}

	protected C loadConfigInternally() {
		return Utils.loadJsonConfig(getConfigClass(), getConfig(), GSON, this.logger, getConfigPath());
	}

	protected void setConfig(final C config) {
		this.config = config;
	}
}
