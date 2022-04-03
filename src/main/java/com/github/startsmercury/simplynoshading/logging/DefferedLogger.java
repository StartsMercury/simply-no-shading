package com.github.startsmercury.simplynoshading.logging;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class DefferedLogger implements AutoCloseable, Logger {
	private static Logger getRootLogger() {
		return LoggerFactory.getLogger(ROOT_LOGGER_NAME);
	}

	private Object value;

	public DefferedLogger(final Class<?> clazz) {
		this(() -> LoggerFactory.getLogger(clazz));
	}

	public DefferedLogger(final String name) {
		this(() -> LoggerFactory.getLogger(name));
	}

	public DefferedLogger(final Supplier<? extends Logger> supplier) {
		this.value = supplier;
	}

	@Override
	public void close() {
		this.value = getRootLogger();
	}

	private final Logger createDelegate() {
		final Logger delegate = this.value instanceof final Supplier<?> supplier &&
				supplier.get() instanceof final Logger logger ? logger : getRootLogger();
		this.value = delegate;

		initialize(delegate);

		return delegate;
	}

	@Override
	public void debug(final Marker arg0, final String arg1) {
		getDelegate().debug(arg0, arg1);
	}

	@Override
	public void debug(final Marker arg0, final String arg1, final Object... arg2) {
		getDelegate().debug(arg0, arg1, arg2);
	}

	@Override
	public void debug(final Marker arg0, final String arg1, final Object arg2) {
		getDelegate().debug(arg0, arg1, arg2);
	}

	@Override
	public void debug(final Marker arg0, final String arg1, final Object arg2, final Object arg3) {
		getDelegate().debug(arg0, arg1, arg2, arg3);
	}

	@Override
	public void debug(final Marker arg0, final String arg1, final Throwable arg2) {
		getDelegate().debug(arg0, arg1, arg2);
	}

	@Override
	public void debug(final String arg0) {
		getDelegate().debug(arg0);
	}

	@Override
	public void debug(final String arg0, final Object... arg1) {
		getDelegate().debug(arg0, arg1);
	}

	@Override
	public void debug(final String arg0, final Object arg1) {
		getDelegate().debug(arg0, arg1);
	}

	@Override
	public void debug(final String arg0, final Object arg1, final Object arg2) {
		getDelegate().debug(arg0, arg1, arg2);
	}

	@Override
	public void debug(final String arg0, final Throwable arg1) {
		getDelegate().debug(arg0, arg1);
	}

	@Override
	public void error(final Marker arg0, final String arg1) {
		getDelegate().error(arg0, arg1);
	}

	@Override
	public void error(final Marker arg0, final String arg1, final Object... arg2) {
		getDelegate().error(arg0, arg1, arg2);
	}

	@Override
	public void error(final Marker arg0, final String arg1, final Object arg2) {
		getDelegate().error(arg0, arg1, arg2);
	}

	@Override
	public void error(final Marker arg0, final String arg1, final Object arg2, final Object arg3) {
		getDelegate().error(arg0, arg1, arg2, arg3);
	}

	@Override
	public void error(final Marker arg0, final String arg1, final Throwable arg2) {
		getDelegate().error(arg0, arg1, arg2);
	}

	@Override
	public void error(final String arg0) {
		getDelegate().error(arg0);
	}

	@Override
	public void error(final String arg0, final Object... arg1) {
		getDelegate().error(arg0, arg1);
	}

	@Override
	public void error(final String arg0, final Object arg1) {
		getDelegate().error(arg0, arg1);
	}

	@Override
	public void error(final String arg0, final Object arg1, final Object arg2) {
		getDelegate().error(arg0, arg1, arg2);
	}

	@Override
	public void error(final String arg0, final Throwable arg1) {
		getDelegate().error(arg0, arg1);
	}

	private final Logger getDelegate() {
		return this.value instanceof final Logger delegate ? delegate : createDelegate();
	}

	@Override
	public String getName() {
		return getDelegate().getName();
	}

	@Override
	public void info(final Marker arg0, final String arg1) {
		getDelegate().info(arg0, arg1);
	}

	@Override
	public void info(final Marker arg0, final String arg1, final Object... arg2) {
		getDelegate().info(arg0, arg1, arg2);
	}

	@Override
	public void info(final Marker arg0, final String arg1, final Object arg2) {
		getDelegate().info(arg0, arg1, arg2);
	}

	@Override
	public void info(final Marker arg0, final String arg1, final Object arg2, final Object arg3) {
		getDelegate().info(arg0, arg1, arg2, arg3);
	}

	@Override
	public void info(final Marker arg0, final String arg1, final Throwable arg2) {
		getDelegate().info(arg0, arg1, arg2);
	}

	@Override
	public void info(final String arg0) {
		getDelegate().info(arg0);
	}

	@Override
	public void info(final String arg0, final Object... arg1) {
		getDelegate().info(arg0, arg1);
	}

	@Override
	public void info(final String arg0, final Object arg1) {
		getDelegate().info(arg0, arg1);
	}

	@Override
	public void info(final String arg0, final Object arg1, final Object arg2) {
		getDelegate().info(arg0, arg1, arg2);
	}

	@Override
	public void info(final String arg0, final Throwable arg1) {
		getDelegate().info(arg0, arg1);
	}

	protected void initialize(final Logger delegate) {
	}

	@Override
	public boolean isDebugEnabled() {
		return getDelegate().isDebugEnabled();
	}

	@Override
	public boolean isDebugEnabled(final Marker arg0) {
		return getDelegate().isDebugEnabled(arg0);
	}

	@Override
	public boolean isErrorEnabled() {
		return getDelegate().isErrorEnabled();
	}

	@Override
	public boolean isErrorEnabled(final Marker arg0) {
		return getDelegate().isErrorEnabled(arg0);
	}

	@Override
	public boolean isInfoEnabled() {
		return getDelegate().isInfoEnabled();
	}

	@Override
	public boolean isInfoEnabled(final Marker arg0) {
		return getDelegate().isInfoEnabled(arg0);
	}

	@Override
	public boolean isTraceEnabled() {
		return getDelegate().isTraceEnabled();
	}

	@Override
	public boolean isTraceEnabled(final Marker arg0) {
		return getDelegate().isTraceEnabled(arg0);
	}

	@Override
	public boolean isWarnEnabled() {
		return getDelegate().isWarnEnabled();
	}

	@Override
	public boolean isWarnEnabled(final Marker arg0) {
		return getDelegate().isWarnEnabled(arg0);
	}

	@Override
	public void trace(final Marker arg0, final String arg1) {
		getDelegate().trace(arg0, arg1);
	}

	@Override
	public void trace(final Marker arg0, final String arg1, final Object... arg2) {
		getDelegate().trace(arg0, arg1, arg2);
	}

	@Override
	public void trace(final Marker arg0, final String arg1, final Object arg2) {
		getDelegate().trace(arg0, arg1, arg2);
	}

	@Override
	public void trace(final Marker arg0, final String arg1, final Object arg2, final Object arg3) {
		getDelegate().trace(arg0, arg1, arg2, arg3);
	}

	@Override
	public void trace(final Marker arg0, final String arg1, final Throwable arg2) {
		getDelegate().trace(arg0, arg1, arg2);
	}

	@Override
	public void trace(final String arg0) {
		getDelegate().trace(arg0);
	}

	@Override
	public void trace(final String arg0, final Object... arg1) {
		getDelegate().trace(arg0, arg1);
	}

	@Override
	public void trace(final String arg0, final Object arg1) {
		getDelegate().trace(arg0, arg1);
	}

	@Override
	public void trace(final String arg0, final Object arg1, final Object arg2) {
		getDelegate().trace(arg0, arg1, arg2);
	}

	@Override
	public void trace(final String arg0, final Throwable arg1) {
		getDelegate().trace(arg0, arg1);
	}

	@Override
	public void warn(final Marker arg0, final String arg1) {
		getDelegate().warn(arg0, arg1);
	}

	@Override
	public void warn(final Marker arg0, final String arg1, final Object... arg2) {
		getDelegate().warn(arg0, arg1, arg2);
	}

	@Override
	public void warn(final Marker arg0, final String arg1, final Object arg2) {
		getDelegate().warn(arg0, arg1, arg2);
	}

	@Override
	public void warn(final Marker arg0, final String arg1, final Object arg2, final Object arg3) {
		getDelegate().warn(arg0, arg1, arg2, arg3);
	}

	@Override
	public void warn(final Marker arg0, final String arg1, final Throwable arg2) {
		getDelegate().warn(arg0, arg1, arg2);
	}

	@Override
	public void warn(final String arg0) {
		getDelegate().warn(arg0);
	}

	@Override
	public void warn(final String arg0, final Object... arg1) {
		getDelegate().warn(arg0, arg1);
	}

	@Override
	public void warn(final String arg0, final Object arg1) {
		getDelegate().warn(arg0, arg1);
	}

	@Override
	public void warn(final String arg0, final Object arg1, final Object arg2) {
		getDelegate().warn(arg0, arg1, arg2);
	}

	@Override
	public void warn(final String arg0, final Throwable arg1) {
		getDelegate().warn(arg0, arg1);
	}
}
