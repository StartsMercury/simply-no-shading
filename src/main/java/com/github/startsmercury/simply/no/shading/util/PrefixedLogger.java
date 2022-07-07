package com.github.startsmercury.simply.no.shading.util;

import static java.util.Objects.requireNonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import com.mojang.logging.LogUtils;

/**
 * The {@code PrefixedLogger} represents a wrapped logger where messages passed
 * to the delegated logger are prefixed.
 *
 * @since 5.0.0
 */
public class PrefixedLogger implements Logger {
	/**
	 * Returns a named prefix logger.
	 *
	 * @param name   the name of the logger
	 * @param prefix the prefixes to the message
	 * @return a named prefixed logger
	 * @see LogUtils#getLogger()
	 */
	public static PrefixedLogger named(final String name, final String prefix) {
		return new PrefixedLogger(LoggerFactory.getLogger(name), prefix);
	}

	/**
	 * Returns a prefixed root logger.
	 *
	 * @param prefix the prefixes to the message
	 * @return a prefixed root logger
	 * @see Logger#ROOT_LOGGER_NAME
	 */
	public static PrefixedLogger root(final String prefix) {
		return new PrefixedLogger(prefix);
	}

	/**
	 * Returns a prefix logger.
	 *
	 * @param logger the logger
	 * @param prefix the prefixes to the message
	 * @return a prefixed logger
	 * @see LogUtils#getLogger()
	 */
	public static PrefixedLogger wrapped(final Logger logger, final String prefix) {
		requireNonNull(logger);

		return new PrefixedLogger(logger, prefix);
	}

	/**
	 * The wrapped or delegated logger.
	 */
	private final Logger delegate;

	/**
	 * The prefixes to the message.
	 */
	private final String prefix;

	/**
	 * Creates a new prefixed logger, wrapping a logger {@code delegate}.
	 *
	 * @param delegate the wrapped or delegated logger
	 * @param prefix   the prefixes to the message
	 */
	public PrefixedLogger(final Logger delegate, final String prefix) {
		this.delegate = delegate == null ? LoggerFactory.getLogger(ROOT_LOGGER_NAME) : delegate;
		this.prefix = prefix != null ? prefix : "";
	}

	/**
	 * Creates a new prefixed logger, wrapping the root logger.
	 *
	 * @param prefix the prefixes to the message
	 */
	public PrefixedLogger(final String prefix) {
		this(null, prefix);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker arg0, final String arg1) {
		this.delegate.debug(arg0, this.prefix + arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker arg0, final String arg1, final Object... arg2) {
		this.delegate.debug(arg0, this.prefix + arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker arg0, final String arg1, final Object arg2) {
		this.delegate.debug(arg0, this.prefix + arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker arg0, final String arg1, final Object arg2, final Object arg3) {
		this.delegate.debug(arg0, this.prefix + arg1, arg2, arg3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker arg0, final String arg1, final Throwable arg2) {
		this.delegate.debug(arg0, this.prefix + arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String arg0) {
		this.delegate.debug(this.prefix + arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String arg0, final Object... arg1) {
		this.delegate.debug(this.prefix + arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String arg0, final Object arg1) {
		this.delegate.debug(this.prefix + arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String arg0, final Object arg1, final Object arg2) {
		this.delegate.debug(this.prefix + arg0, arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String arg0, final Throwable arg1) {
		this.delegate.debug(this.prefix + arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker arg0, final String arg1) {
		this.delegate.error(arg0, this.prefix + arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker arg0, final String arg1, final Object... arg2) {
		this.delegate.error(arg0, this.prefix + arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker arg0, final String arg1, final Object arg2) {
		this.delegate.error(arg0, this.prefix + arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker arg0, final String arg1, final Object arg2, final Object arg3) {
		this.delegate.error(arg0, this.prefix + arg1, arg2, arg3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker arg0, final String arg1, final Throwable arg2) {
		this.delegate.error(arg0, this.prefix + arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String arg0) {
		this.delegate.error(this.prefix + arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String arg0, final Object... arg1) {
		this.delegate.error(this.prefix + arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String arg0, final Object arg1) {
		this.delegate.error(this.prefix + arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String arg0, final Object arg1, final Object arg2) {
		this.delegate.error(this.prefix + arg0, arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String arg0, final Throwable arg1) {
		this.delegate.error(this.prefix + arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() { return this.delegate.getName(); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker arg0, final String arg1) {
		this.delegate.info(arg0, this.prefix + arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker arg0, final String arg1, final Object... arg2) {
		this.delegate.info(arg0, this.prefix + arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker arg0, final String arg1, final Object arg2) {
		this.delegate.info(arg0, this.prefix + arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker arg0, final String arg1, final Object arg2, final Object arg3) {
		this.delegate.info(arg0, this.prefix + arg1, arg2, arg3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker arg0, final String arg1, final Throwable arg2) {
		this.delegate.info(arg0, this.prefix + arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String arg0) {
		this.delegate.info(this.prefix + arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String arg0, final Object... arg1) {
		this.delegate.info(this.prefix + arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String arg0, final Object arg1) {
		this.delegate.info(this.prefix + arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String arg0, final Object arg1, final Object arg2) {
		this.delegate.info(this.prefix + arg0, arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String arg0, final Throwable arg1) {
		this.delegate.info(this.prefix + arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDebugEnabled() { return this.delegate.isDebugEnabled(); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDebugEnabled(final Marker arg0) {
		return this.delegate.isDebugEnabled(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isErrorEnabled() { return this.delegate.isErrorEnabled(); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isErrorEnabled(final Marker arg0) {
		return this.delegate.isErrorEnabled(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInfoEnabled() { return this.delegate.isInfoEnabled(); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInfoEnabled(final Marker arg0) {
		return this.delegate.isInfoEnabled(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTraceEnabled() { return this.delegate.isTraceEnabled(); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTraceEnabled(final Marker arg0) {
		return this.delegate.isTraceEnabled(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWarnEnabled() { return this.delegate.isWarnEnabled(); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWarnEnabled(final Marker arg0) {
		return this.delegate.isWarnEnabled(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker arg0, final String arg1) {
		this.delegate.trace(arg0, this.prefix + arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker arg0, final String arg1, final Object... arg2) {
		this.delegate.trace(arg0, this.prefix + arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker arg0, final String arg1, final Object arg2) {
		this.delegate.trace(arg0, this.prefix + arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker arg0, final String arg1, final Object arg2, final Object arg3) {
		this.delegate.trace(arg0, this.prefix + arg1, arg2, arg3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker arg0, final String arg1, final Throwable arg2) {
		this.delegate.trace(arg0, this.prefix + arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final String arg0) {
		this.delegate.trace(this.prefix + arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final String arg0, final Object... arg1) {
		this.delegate.trace(this.prefix + arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final String arg0, final Object arg1) {
		this.delegate.trace(this.prefix + arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final String arg0, final Object arg1, final Object arg2) {
		this.delegate.trace(this.prefix + arg0, arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final String arg0, final Throwable arg1) {
		this.delegate.trace(this.prefix + arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker arg0, final String arg1) {
		this.delegate.warn(arg0, this.prefix + arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker arg0, final String arg1, final Object... arg2) {
		this.delegate.warn(arg0, this.prefix + arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker arg0, final String arg1, final Object arg2) {
		this.delegate.warn(arg0, this.prefix + arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker arg0, final String arg1, final Object arg2, final Object arg3) {
		this.delegate.warn(arg0, this.prefix + arg1, arg2, arg3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker arg0, final String arg1, final Throwable arg2) {
		this.delegate.warn(arg0, this.prefix + arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String arg0) {
		this.delegate.warn(this.prefix + arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String arg0, final Object... arg1) {
		this.delegate.warn(this.prefix + arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String arg0, final Object arg1) {
		this.delegate.warn(this.prefix + arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String arg0, final Object arg1, final Object arg2) {
		this.delegate.warn(this.prefix + arg0, arg1, arg2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String arg0, final Throwable arg1) {
		this.delegate.warn(this.prefix + arg0, arg1);
	}
}
