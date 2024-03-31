package io.github.startsmercury.simply_no_shading.client;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.Objects;

/**
 * Config I/O exception.
 *
 * @since 6.2.0
 */
public abstract sealed class ConfigIOException extends IOException {
    /**
     * Config not exist I/O exception.
     */
    public static final class NotExist extends ConfigIOException {
        /**
         * Creates a config not exist I/O exception.
         */
        public NotExist() {
            super("Config file is yet to exist");
        }

        /**
         * The message.
         * <p>
         * {@inheritDoc}
         */
        @Override
        public String getMessage() {
            final var message = super.getMessage();
            assert message != null;
            return message;
        }
    }

    /**
     * Malformed config I/O exception.
     */
    public static final class Malformed extends ConfigIOException {
        /**
         * Creates a malformed config I/O exception with the cause.
         */
        public Malformed(final JsonSyntaxException cause) {
            super("Config file content is malformed", cause);
        }

        /**
         * The cause of malformed config.
         *
         * @see #getCause()
         */
        public JsonSyntaxException cause() {
            return getCause();
        }

        /**
         * The cause of malformed config.
         * <p>
         * {@inheritDoc}
         *
         * @see #cause()
         */
        @Override
        public JsonSyntaxException getCause() {
            final var cause = super.getCause();
            assert cause instanceof JsonSyntaxException;
            return (JsonSyntaxException) cause;
        }
    }

    /**
     * Config system I/O exception.
     */
    public static final class System extends ConfigIOException {
        /**
         * Creates a config system I/O exception with the cause.
         */
        public System(final IOException cause) {
            super("Config I/O failure", cause);
        }

        /**
         * The I/O exception.
         *
         * @see #getCause()
         */
        public IOException cause() {
            return getCause();
        }

        /**
         * The I/O exception.
         * <p>
         * {@inheritDoc}
         *
         * @see #cause()
         */
        @Override
        public IOException getCause() {
            final var cause = super.getCause();
            assert cause instanceof IOException;
            return (IOException) cause;
        }
    }

    /**
     * Other uncategorized config I/O exception.
     */
    public static final class Other extends ConfigIOException {
        public Other(final Throwable cause) {
            super(cause);
        }

        /**
         * The cause.
         *
         * @see #getCause()
         */
        public Throwable cause() {
            return this.getCause();
        }

        /**
         * The cause.
         * <p>
         * {@inheritDoc}
         *
         * @see #cause()
         */
        @Override
        public Throwable getCause() {
            final var cause = super.getCause();
            assert cause != null;
            return cause;
        }
    }

    /**
     * Creates a new config I/O exception with a message.
     */
    protected ConfigIOException(final String message) {
        super(Objects.requireNonNull(message, "Parameter message is null"));
    }

    /**
     * Creates a new config I/O exception with a message and the cause.
     */
    protected ConfigIOException(final String message, final Throwable cause) {
        super(
            Objects.requireNonNull(message, "Parameter message is null"),
            Objects.requireNonNull(cause, "Parameter cause is null")
        );
    }

    /**
     * Creates a new config I/O exception with the cause.
     */
    protected ConfigIOException(final Throwable cause) {
        super(Objects.requireNonNull(cause, "Parameter cause is null"));
    }
}
