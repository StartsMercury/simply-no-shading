package com.github.startsmercury.simply.no.shading.util;

import static java.util.Objects.requireNonNull;
import static org.apache.logging.log4j.LogManager.ROOT_LOGGER_NAME;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.EntryMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.util.Supplier;

/**
 * The {@code PrefixedLogger} represents a wrapped logger where messages passed
 * to the delegated logger are prefixed.
 *
 * @since 5.0.0
 */
@Deprecated
public class PrefixedLogger implements Logger {
	/**
	 * Returns a named prefix logger.
	 *
	 * @param name   the name of the logger
	 * @param prefix the prefixes to the message
	 * @return a named prefixed logger
	 * @see LoggerFactory#getLogger(String)
	 */
	public static PrefixedLogger named(final String name, final String prefix) {
		return new PrefixedLogger(LogManager.getLogger(name), prefix);
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
	 * @see PrefixedLogger#PrefixedLogger(Logger, String) new PrefixedLogger(Logger,
	 *      String)
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
	@SuppressWarnings("unused")
	private final String prefix;

	/**
	 * Creates a new prefixed logger, wrapping a logger {@code delegate}.
	 *
	 * @param delegate the wrapped or delegated logger
	 * @param prefix   the prefixes to the message
	 */
	public PrefixedLogger(final Logger delegate, final String prefix) {
		this.delegate = delegate == null ? LogManager.getLogger(ROOT_LOGGER_NAME) : delegate;
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
	public void catching(final Level level, final Throwable t) {
		delegate.catching(level, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void catching(final Throwable t) {
		delegate.catching(t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final Message msg) {
		delegate.debug(marker, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final Message msg, final Throwable t) {
		delegate.debug(marker, msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final MessageSupplier msgSupplier) {
		delegate.debug(marker, msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final MessageSupplier msgSupplier, final Throwable t) {
		delegate.debug(marker, msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final CharSequence message) {
		delegate.debug(marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final CharSequence message, final Throwable t) {
		delegate.debug(marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final Object message) {
		delegate.debug(marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final Object message, final Throwable t) {
		delegate.debug(marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final String message) {
		delegate.debug(marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final String message, final Object... params) {
		delegate.debug(marker, message, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final String message, final Supplier<?>... paramSuppliers) {
		delegate.debug(marker, message, paramSuppliers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final String message, final Throwable t) {
		delegate.debug(marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final Supplier<?> msgSupplier) {
		delegate.debug(marker, msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final Supplier<?> msgSupplier, final Throwable t) {
		delegate.debug(marker, msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Message msg) {
		delegate.debug(msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Message msg, final Throwable t) {
		delegate.debug(msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final MessageSupplier msgSupplier) {
		delegate.debug(msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final MessageSupplier msgSupplier, final Throwable t) {
		delegate.debug(msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final CharSequence message) {
		delegate.debug(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final CharSequence message, final Throwable t) {
		delegate.debug(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Object message) {
		delegate.debug(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Object message, final Throwable t) {
		delegate.debug(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String message) {
		delegate.debug(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String message, final Object... params) {
		delegate.debug(message, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String message, final Supplier<?>... paramSuppliers) {
		delegate.debug(message, paramSuppliers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String message, final Throwable t) {
		delegate.debug(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Supplier<?> msgSupplier) {
		delegate.debug(msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Supplier<?> msgSupplier, final Throwable t) {
		delegate.debug(msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final String message, final Object p0) {
		delegate.debug(marker, message, p0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final String message, final Object p0, final Object p1) {
		delegate.debug(marker, message, p0, p1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
		delegate.debug(marker, message, p0, p1, p2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
		delegate.debug(marker, message, p0, p1, p2, p3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
		delegate.debug(marker, message, p0, p1, p2, p3, p4);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
		delegate.debug(marker, message, p0, p1, p2, p3, p4, p5);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6) {
		delegate.debug(marker, message, p0, p1, p2, p3, p4, p5, p6);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7) {
		delegate.debug(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7, final Object p8) {
		delegate.debug(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7, final Object p8, final Object p9) {
		delegate.debug(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String message, final Object p0) {
		delegate.debug(message, p0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String message, final Object p0, final Object p1) {
		delegate.debug(message, p0, p1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String message, final Object p0, final Object p1, final Object p2) {
		delegate.debug(message, p0, p1, p2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
		delegate.debug(message, p0, p1, p2, p3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
		delegate.debug(message, p0, p1, p2, p3, p4);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
		delegate.debug(message, p0, p1, p2, p3, p4, p5);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
		delegate.debug(message, p0, p1, p2, p3, p4, p5, p6);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6,
			final Object p7) {
		delegate.debug(message, p0, p1, p2, p3, p4, p5, p6, p7);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6,
			final Object p7, final Object p8) {
		delegate.debug(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6,
			final Object p7, final Object p8, final Object p9) {
		delegate.debug(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}

	@Deprecated
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void entry() {
		delegate.entry();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void entry(final Object... params) {
		delegate.entry(params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final Message msg) {
		delegate.error(marker, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final Message msg, final Throwable t) {
		delegate.error(marker, msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final MessageSupplier msgSupplier) {
		delegate.error(marker, msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final MessageSupplier msgSupplier, final Throwable t) {
		delegate.error(marker, msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final CharSequence message) {
		delegate.error(marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final CharSequence message, final Throwable t) {
		delegate.error(marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final Object message) {
		delegate.error(marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final Object message, final Throwable t) {
		delegate.error(marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final String message) {
		delegate.error(marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final String message, final Object... params) {
		delegate.error(marker, message, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final String message, final Supplier<?>... paramSuppliers) {
		delegate.error(marker, message, paramSuppliers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final String message, final Throwable t) {
		delegate.error(marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final Supplier<?> msgSupplier) {
		delegate.error(marker, msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final Supplier<?> msgSupplier, final Throwable t) {
		delegate.error(marker, msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Message msg) {
		delegate.error(msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Message msg, final Throwable t) {
		delegate.error(msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final MessageSupplier msgSupplier) {
		delegate.error(msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final MessageSupplier msgSupplier, final Throwable t) {
		delegate.error(msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final CharSequence message) {
		delegate.error(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final CharSequence message, final Throwable t) {
		delegate.error(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Object message) {
		delegate.error(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Object message, final Throwable t) {
		delegate.error(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message) {
		delegate.error(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message, final Object... params) {
		delegate.error(message, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message, final Supplier<?>... paramSuppliers) {
		delegate.error(message, paramSuppliers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message, final Throwable t) {
		delegate.error(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Supplier<?> msgSupplier) {
		delegate.error(msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Supplier<?> msgSupplier, final Throwable t) {
		delegate.error(msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final String message, final Object p0) {
		delegate.error(marker, message, p0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final String message, final Object p0, final Object p1) {
		delegate.error(marker, message, p0, p1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
		delegate.error(marker, message, p0, p1, p2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
		delegate.error(marker, message, p0, p1, p2, p3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
		delegate.error(marker, message, p0, p1, p2, p3, p4);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
		delegate.error(marker, message, p0, p1, p2, p3, p4, p5);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6) {
		delegate.error(marker, message, p0, p1, p2, p3, p4, p5, p6);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7) {
		delegate.error(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7, final Object p8) {
		delegate.error(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7, final Object p8, final Object p9) {
		delegate.error(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message, final Object p0) {
		delegate.error(message, p0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message, final Object p0, final Object p1) {
		delegate.error(message, p0, p1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message, final Object p0, final Object p1, final Object p2) {
		delegate.error(message, p0, p1, p2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
		delegate.error(message, p0, p1, p2, p3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
		delegate.error(message, p0, p1, p2, p3, p4);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
		delegate.error(message, p0, p1, p2, p3, p4, p5);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
		delegate.error(message, p0, p1, p2, p3, p4, p5, p6);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6,
			final Object p7) {
		delegate.error(message, p0, p1, p2, p3, p4, p5, p6, p7);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6,
			final Object p7, final Object p8) {
		delegate.error(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6,
			final Object p7, final Object p8, final Object p9) {
		delegate.error(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public void exit() {
		delegate.exit();
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public <R> R exit(final R result) {
		return delegate.exit(result);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final Message msg) {
		delegate.fatal(marker, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final Message msg, final Throwable t) {
		delegate.fatal(marker, msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final MessageSupplier msgSupplier) {
		delegate.fatal(marker, msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final MessageSupplier msgSupplier, final Throwable t) {
		delegate.fatal(marker, msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final CharSequence message) {
		delegate.fatal(marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final CharSequence message, final Throwable t) {
		delegate.fatal(marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final Object message) {
		delegate.fatal(marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final Object message, final Throwable t) {
		delegate.fatal(marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final String message) {
		delegate.fatal(marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final String message, final Object... params) {
		delegate.fatal(marker, message, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final String message, final Supplier<?>... paramSuppliers) {
		delegate.fatal(marker, message, paramSuppliers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final String message, final Throwable t) {
		delegate.fatal(marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final Supplier<?> msgSupplier) {
		delegate.fatal(marker, msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final Supplier<?> msgSupplier, final Throwable t) {
		delegate.fatal(marker, msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Message msg) {
		delegate.fatal(msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Message msg, final Throwable t) {
		delegate.fatal(msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final MessageSupplier msgSupplier) {
		delegate.fatal(msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final MessageSupplier msgSupplier, final Throwable t) {
		delegate.fatal(msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final CharSequence message) {
		delegate.fatal(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final CharSequence message, final Throwable t) {
		delegate.fatal(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Object message) {
		delegate.fatal(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Object message, final Throwable t) {
		delegate.fatal(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final String message) {
		delegate.fatal(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final String message, final Object... params) {
		delegate.fatal(message, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final String message, final Supplier<?>... paramSuppliers) {
		delegate.fatal(message, paramSuppliers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final String message, final Throwable t) {
		delegate.fatal(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Supplier<?> msgSupplier) {
		delegate.fatal(msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Supplier<?> msgSupplier, final Throwable t) {
		delegate.fatal(msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final String message, final Object p0) {
		delegate.fatal(marker, message, p0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final String message, final Object p0, final Object p1) {
		delegate.fatal(marker, message, p0, p1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
		delegate.fatal(marker, message, p0, p1, p2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
		delegate.fatal(marker, message, p0, p1, p2, p3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
		delegate.fatal(marker, message, p0, p1, p2, p3, p4);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
		delegate.fatal(marker, message, p0, p1, p2, p3, p4, p5);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6) {
		delegate.fatal(marker, message, p0, p1, p2, p3, p4, p5, p6);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7) {
		delegate.fatal(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7, final Object p8) {
		delegate.fatal(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7, final Object p8, final Object p9) {
		delegate.fatal(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final String message, final Object p0) {
		delegate.fatal(message, p0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final String message, final Object p0, final Object p1) {
		delegate.fatal(message, p0, p1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final String message, final Object p0, final Object p1, final Object p2) {
		delegate.fatal(message, p0, p1, p2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
		delegate.fatal(message, p0, p1, p2, p3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
		delegate.fatal(message, p0, p1, p2, p3, p4);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
		delegate.fatal(message, p0, p1, p2, p3, p4, p5);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
		delegate.fatal(message, p0, p1, p2, p3, p4, p5, p6);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6,
			final Object p7) {
		delegate.fatal(message, p0, p1, p2, p3, p4, p5, p6, p7);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6,
			final Object p7, final Object p8) {
		delegate.fatal(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6,
			final Object p7, final Object p8, final Object p9) {
		delegate.fatal(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Level getLevel() {
		return delegate.getLevel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <MF extends MessageFactory> MF getMessageFactory() {
		return delegate.getMessageFactory();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return delegate.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final Message msg) {
		delegate.info(marker, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final Message msg, final Throwable t) {
		delegate.info(marker, msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final MessageSupplier msgSupplier) {
		delegate.info(marker, msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final MessageSupplier msgSupplier, final Throwable t) {
		delegate.info(marker, msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final CharSequence message) {
		delegate.info(marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final CharSequence message, final Throwable t) {
		delegate.info(marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final Object message) {
		delegate.info(marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final Object message, final Throwable t) {
		delegate.info(marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final String message) {
		delegate.info(marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final String message, final Object... params) {
		delegate.info(marker, message, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final String message, final Supplier<?>... paramSuppliers) {
		delegate.info(marker, message, paramSuppliers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final String message, final Throwable t) {
		delegate.info(marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final Supplier<?> msgSupplier) {
		delegate.info(marker, msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final Supplier<?> msgSupplier, final Throwable t) {
		delegate.info(marker, msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Message msg) {
		delegate.info(msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Message msg, final Throwable t) {
		delegate.info(msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final MessageSupplier msgSupplier) {
		delegate.info(msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final MessageSupplier msgSupplier, final Throwable t) {
		delegate.info(msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final CharSequence message) {
		delegate.info(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final CharSequence message, final Throwable t) {
		delegate.info(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Object message) {
		delegate.info(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Object message, final Throwable t) {
		delegate.info(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message) {
		delegate.info(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message, final Object... params) {
		delegate.info(message, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message, final Supplier<?>... paramSuppliers) {
		delegate.info(message, paramSuppliers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message, final Throwable t) {
		delegate.info(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Supplier<?> msgSupplier) {
		delegate.info(msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Supplier<?> msgSupplier, final Throwable t) {
		delegate.info(msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final String message, final Object p0) {
		delegate.info(marker, message, p0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final String message, final Object p0, final Object p1) {
		delegate.info(marker, message, p0, p1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
		delegate.info(marker, message, p0, p1, p2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
		delegate.info(marker, message, p0, p1, p2, p3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
		delegate.info(marker, message, p0, p1, p2, p3, p4);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
		delegate.info(marker, message, p0, p1, p2, p3, p4, p5);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6) {
		delegate.info(marker, message, p0, p1, p2, p3, p4, p5, p6);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7) {
		delegate.info(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7, final Object p8) {
		delegate.info(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7, final Object p8, final Object p9) {
		delegate.info(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message, final Object p0) {
		delegate.info(message, p0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message, final Object p0, final Object p1) {
		delegate.info(message, p0, p1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message, final Object p0, final Object p1, final Object p2) {
		delegate.info(message, p0, p1, p2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
		delegate.info(message, p0, p1, p2, p3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
		delegate.info(message, p0, p1, p2, p3, p4);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
		delegate.info(message, p0, p1, p2, p3, p4, p5);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
		delegate.info(message, p0, p1, p2, p3, p4, p5, p6);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6,
			final Object p7) {
		delegate.info(message, p0, p1, p2, p3, p4, p5, p6, p7);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6,
			final Object p7, final Object p8) {
		delegate.info(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6,
			final Object p7, final Object p8, final Object p9) {
		delegate.info(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDebugEnabled() {
		return delegate.isDebugEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDebugEnabled(final Marker marker) {
		return delegate.isDebugEnabled(marker);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnabled(final Level level) {
		return delegate.isEnabled(level);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnabled(final Level level, final Marker marker) {
		return delegate.isEnabled(level, marker);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isErrorEnabled() {
		return delegate.isErrorEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isErrorEnabled(final Marker marker) {
		return delegate.isErrorEnabled(marker);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFatalEnabled() {
		return delegate.isFatalEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFatalEnabled(final Marker marker) {
		return delegate.isFatalEnabled(marker);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInfoEnabled() {
		return delegate.isInfoEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInfoEnabled(final Marker marker) {
		return delegate.isInfoEnabled(marker);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTraceEnabled() {
		return delegate.isTraceEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTraceEnabled(final Marker marker) {
		return delegate.isTraceEnabled(marker);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWarnEnabled() {
		return delegate.isWarnEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWarnEnabled(final Marker marker) {
		return delegate.isWarnEnabled(marker);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final Message msg) {
		delegate.log(level, marker, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final Message msg, final Throwable t) {
		delegate.log(level, marker, msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final MessageSupplier msgSupplier) {
		delegate.log(level, marker, msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final MessageSupplier msgSupplier, final Throwable t) {
		delegate.log(level, marker, msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final CharSequence message) {
		delegate.log(level, marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final CharSequence message, final Throwable t) {
		delegate.log(level, marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final Object message) {
		delegate.log(level, marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final Object message, final Throwable t) {
		delegate.log(level, marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final String message) {
		delegate.log(level, marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final String message, final Object... params) {
		delegate.log(level, marker, message, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final String message, final Supplier<?>... paramSuppliers) {
		delegate.log(level, marker, message, paramSuppliers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final String message, final Throwable t) {
		delegate.log(level, marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final Supplier<?> msgSupplier) {
		delegate.log(level, marker, msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final Supplier<?> msgSupplier, final Throwable t) {
		delegate.log(level, marker, msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Message msg) {
		delegate.log(level, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Message msg, final Throwable t) {
		delegate.log(level, msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final MessageSupplier msgSupplier) {
		delegate.log(level, msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final MessageSupplier msgSupplier, final Throwable t) {
		delegate.log(level, msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final CharSequence message) {
		delegate.log(level, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final CharSequence message, final Throwable t) {
		delegate.log(level, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Object message) {
		delegate.log(level, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Object message, final Throwable t) {
		delegate.log(level, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final String message) {
		delegate.log(level, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final String message, final Object... params) {
		delegate.log(level, message, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final String message, final Supplier<?>... paramSuppliers) {
		delegate.log(level, message, paramSuppliers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final String message, final Throwable t) {
		delegate.log(level, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Supplier<?> msgSupplier) {
		delegate.log(level, msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Supplier<?> msgSupplier, final Throwable t) {
		delegate.log(level, msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final String message, final Object p0) {
		delegate.log(level, marker, message, p0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1) {
		delegate.log(level, marker, message, p0, p1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
		delegate.log(level, marker, message, p0, p1, p2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
		delegate.log(level, marker, message, p0, p1, p2, p3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
		delegate.log(level, marker, message, p0, p1, p2, p3, p4);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4,
			final Object p5) {
		delegate.log(level, marker, message, p0, p1, p2, p3, p4, p5);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4,
			final Object p5, final Object p6) {
		delegate.log(level, marker, message, p0, p1, p2, p3, p4, p5, p6);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4,
			final Object p5, final Object p6, final Object p7) {
		delegate.log(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4,
			final Object p5, final Object p6, final Object p7, final Object p8) {
		delegate.log(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4,
			final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
		delegate.log(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final String message, final Object p0) {
		delegate.log(level, message, p0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final String message, final Object p0, final Object p1) {
		delegate.log(level, message, p0, p1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final String message, final Object p0, final Object p1, final Object p2) {
		delegate.log(level, message, p0, p1, p2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
		delegate.log(level, message, p0, p1, p2, p3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
		delegate.log(level, message, p0, p1, p2, p3, p4);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
		delegate.log(level, message, p0, p1, p2, p3, p4, p5);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6) {
		delegate.log(level, message, p0, p1, p2, p3, p4, p5, p6);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7) {
		delegate.log(level, message, p0, p1, p2, p3, p4, p5, p6, p7);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7, final Object p8) {
		delegate.log(level, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7, final Object p8, final Object p9) {
		delegate.log(level, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void printf(final Level level, final Marker marker, final String format, final Object... params) {
		delegate.printf(level, marker, format, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void printf(final Level level, final String format, final Object... params) {
		delegate.printf(level, format, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Throwable> T throwing(final Level level, final T t) {
		return delegate.throwing(level, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Throwable> T throwing(final T t) {
		return delegate.throwing(t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final Message msg) {
		delegate.trace(marker, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final Message msg, final Throwable t) {
		delegate.trace(marker, msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final MessageSupplier msgSupplier) {
		delegate.trace(marker, msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final MessageSupplier msgSupplier, final Throwable t) {
		delegate.trace(marker, msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final CharSequence message) {
		delegate.trace(marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final CharSequence message, final Throwable t) {
		delegate.trace(marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final Object message) {
		delegate.trace(marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final Object message, final Throwable t) {
		delegate.trace(marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final String message) {
		delegate.trace(marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final String message, final Object... params) {
		delegate.trace(marker, message, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final String message, final Supplier<?>... paramSuppliers) {
		delegate.trace(marker, message, paramSuppliers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final String message, final Throwable t) {
		delegate.trace(marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final Supplier<?> msgSupplier) {
		delegate.trace(marker, msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final Supplier<?> msgSupplier, final Throwable t) {
		delegate.trace(marker, msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Message msg) {
		delegate.trace(msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Message msg, final Throwable t) {
		delegate.trace(msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final MessageSupplier msgSupplier) {
		delegate.trace(msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final MessageSupplier msgSupplier, final Throwable t) {
		delegate.trace(msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final CharSequence message) {
		delegate.trace(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final CharSequence message, final Throwable t) {
		delegate.trace(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Object message) {
		delegate.trace(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Object message, final Throwable t) {
		delegate.trace(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final String message) {
		delegate.trace(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final String message, final Object... params) {
		delegate.trace(message, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final String message, final Supplier<?>... paramSuppliers) {
		delegate.trace(message, paramSuppliers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final String message, final Throwable t) {
		delegate.trace(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Supplier<?> msgSupplier) {
		delegate.trace(msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Supplier<?> msgSupplier, final Throwable t) {
		delegate.trace(msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final String message, final Object p0) {
		delegate.trace(marker, message, p0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final String message, final Object p0, final Object p1) {
		delegate.trace(marker, message, p0, p1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
		delegate.trace(marker, message, p0, p1, p2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
		delegate.trace(marker, message, p0, p1, p2, p3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
		delegate.trace(marker, message, p0, p1, p2, p3, p4);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
		delegate.trace(marker, message, p0, p1, p2, p3, p4, p5);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6) {
		delegate.trace(marker, message, p0, p1, p2, p3, p4, p5, p6);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7) {
		delegate.trace(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7, final Object p8) {
		delegate.trace(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7, final Object p8, final Object p9) {
		delegate.trace(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final String message, final Object p0) {
		delegate.trace(message, p0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final String message, final Object p0, final Object p1) {
		delegate.trace(message, p0, p1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final String message, final Object p0, final Object p1, final Object p2) {
		delegate.trace(message, p0, p1, p2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
		delegate.trace(message, p0, p1, p2, p3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
		delegate.trace(message, p0, p1, p2, p3, p4);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
		delegate.trace(message, p0, p1, p2, p3, p4, p5);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
		delegate.trace(message, p0, p1, p2, p3, p4, p5, p6);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6,
			final Object p7) {
		delegate.trace(message, p0, p1, p2, p3, p4, p5, p6, p7);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6,
			final Object p7, final Object p8) {
		delegate.trace(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6,
			final Object p7, final Object p8, final Object p9) {
		delegate.trace(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntryMessage traceEntry() {
		return delegate.traceEntry();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntryMessage traceEntry(final String format, final Object... params) {
		return delegate.traceEntry(format, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntryMessage traceEntry(final Supplier<?>... paramSuppliers) {
		return delegate.traceEntry(paramSuppliers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntryMessage traceEntry(final String format, final Supplier<?>... paramSuppliers) {
		return delegate.traceEntry(format, paramSuppliers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntryMessage traceEntry(final Message message) {
		return delegate.traceEntry(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void traceExit() {
		delegate.traceExit();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R> R traceExit(final R result) {
		return delegate.traceExit(result);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R> R traceExit(final String format, final R result) {
		return delegate.traceExit(format, result);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void traceExit(final EntryMessage message) {
		delegate.traceExit(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R> R traceExit(final EntryMessage message, final R result) {
		return delegate.traceExit(message, result);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R> R traceExit(final Message message, final R result) {
		return delegate.traceExit(message, result);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final Message msg) {
		delegate.warn(marker, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final Message msg, final Throwable t) {
		delegate.warn(marker, msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final MessageSupplier msgSupplier) {
		delegate.warn(marker, msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final MessageSupplier msgSupplier, final Throwable t) {
		delegate.warn(marker, msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final CharSequence message) {
		delegate.warn(marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final CharSequence message, final Throwable t) {
		delegate.warn(marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final Object message) {
		delegate.warn(marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final Object message, final Throwable t) {
		delegate.warn(marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final String message) {
		delegate.warn(marker, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final String message, final Object... params) {
		delegate.warn(marker, message, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final String message, final Supplier<?>... paramSuppliers) {
		delegate.warn(marker, message, paramSuppliers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final String message, final Throwable t) {
		delegate.warn(marker, message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final Supplier<?> msgSupplier) {
		delegate.warn(marker, msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final Supplier<?> msgSupplier, final Throwable t) {
		delegate.warn(marker, msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Message msg) {
		delegate.warn(msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Message msg, final Throwable t) {
		delegate.warn(msg, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final MessageSupplier msgSupplier) {
		delegate.warn(msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final MessageSupplier msgSupplier, final Throwable t) {
		delegate.warn(msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final CharSequence message) {
		delegate.warn(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final CharSequence message, final Throwable t) {
		delegate.warn(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Object message) {
		delegate.warn(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Object message, final Throwable t) {
		delegate.warn(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String message) {
		delegate.warn(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String message, final Object... params) {
		delegate.warn(message, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String message, final Supplier<?>... paramSuppliers) {
		delegate.warn(message, paramSuppliers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String message, final Throwable t) {
		delegate.warn(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Supplier<?> msgSupplier) {
		delegate.warn(msgSupplier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Supplier<?> msgSupplier, final Throwable t) {
		delegate.warn(msgSupplier, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final String message, final Object p0) {
		delegate.warn(marker, message, p0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final String message, final Object p0, final Object p1) {
		delegate.warn(marker, message, p0, p1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
		delegate.warn(marker, message, p0, p1, p2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
		delegate.warn(marker, message, p0, p1, p2, p3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
		delegate.warn(marker, message, p0, p1, p2, p3, p4);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
		delegate.warn(marker, message, p0, p1, p2, p3, p4, p5);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6) {
		delegate.warn(marker, message, p0, p1, p2, p3, p4, p5, p6);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7) {
		delegate.warn(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7, final Object p8) {
		delegate.warn(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
			final Object p6, final Object p7, final Object p8, final Object p9) {
		delegate.warn(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String message, final Object p0) {
		delegate.warn(message, p0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String message, final Object p0, final Object p1) {
		delegate.warn(message, p0, p1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String message, final Object p0, final Object p1, final Object p2) {
		delegate.warn(message, p0, p1, p2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
		delegate.warn(message, p0, p1, p2, p3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
		delegate.warn(message, p0, p1, p2, p3, p4);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
		delegate.warn(message, p0, p1, p2, p3, p4, p5);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
		delegate.warn(message, p0, p1, p2, p3, p4, p5, p6);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6,
			final Object p7) {
		delegate.warn(message, p0, p1, p2, p3, p4, p5, p6, p7);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6,
			final Object p7, final Object p8) {
		delegate.warn(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6,
			final Object p7, final Object p8, final Object p9) {
		delegate.warn(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}
}
