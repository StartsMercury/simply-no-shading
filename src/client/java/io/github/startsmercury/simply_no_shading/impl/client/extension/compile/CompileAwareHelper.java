package io.github.startsmercury.simply_no_shading.impl.client.extension.compile;

final class CompileAwareHelper {
    private static Error unimplemented0() {
        final var err = new InternalError("Unimplemented interface injected method");
        //noinspection CallToPrintStackTrace
        err.printStackTrace();

        System.exit(1);

        final var fatal = new Error("Delayed force exit");
        fatal.addSuppressed(err);
        throw fatal;
    }

    public static <T> T unimplemented() {
        throw unimplemented0();
    }

    private CompileAwareHelper() {}
}
