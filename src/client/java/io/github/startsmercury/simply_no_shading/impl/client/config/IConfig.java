package io.github.startsmercury.simply_no_shading.impl.client.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.Optional;

public interface IConfig {
    int MIN_VERSION = 0;

    /**
     * The configuration format version.
     *
     * @return The config version.
     */
    int version();

    Codec<IConfig> LENIENT_CODEC = Codec.INT.partialDispatch(
        "version",
        config -> DataResult.success(config.version()),
        version -> switch (version) {
            case 0 -> DataResult.success(io.github.startsmercury.simply_no_shading.impl.client.config.v0.Config.LENIENT_CODEC);
            case 1 -> DataResult.success(io.github.startsmercury.simply_no_shading.impl.client.config.v1.Config.LENIENT_CODEC);
            default -> DataResult.error(() -> "Unrecognized config version " + version);
        }
    );

    Codec<IConfig> CODEC = Codec.INT.partialDispatch(
        "version",
        config -> DataResult.success(config.version()),
        version -> switch (version) {
            case 0 -> DataResult.success(io.github.startsmercury.simply_no_shading.impl.client.config.v0.Config.CODEC);
            case 1 -> DataResult.success(io.github.startsmercury.simply_no_shading.impl.client.config.v1.Config.CODEC);
            default -> DataResult.error(() -> "Unrecognized config version " + version);
        }
    );

    /**
     * Upgrades the given config to the latest format.
     *
     * @return The result of config upgrading.
     */
    default DataResult<io.github.startsmercury.simply_no_shading.impl.client.config.v1.Config> upgrade() {
        var self = this;

        // Iterative conversion. Best receives the first and last points.
        while (true) {
            switch (self) {
                case final io.github.startsmercury.simply_no_shading.impl.client.config.v0.Config v0 ->
                    self = new io.github.startsmercury.simply_no_shading.impl.client.config.v1.Config(
                        true,
                        io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigPreset.CUSTOM,
                        Optional.of(new io.github.startsmercury.simply_no_shading.impl.client.config.v1.ConfigData(
                            v0.blockShadingEnabled(),
                            v0.cloudShadingEnabled(),
                            v0.entityShadingEnabled()
                        ))
                    );

                case final io.github.startsmercury.simply_no_shading.impl.client.config.v1.Config v1 -> {
                    return DataResult.success(v1);
                }

                default -> {
                    final var end = self;
                    return DataResult.error(() -> {
                        //noinspection ConstantValue
                        if (this == end) {
                            return "Cant convert from " + end.getClass().getName();
                        } else {
                            return "Cant convert " + end.getClass().getName() + " from initial " + this.getClass().getName();
                        }
                    });
                }
            }
        }

        // unreachable
    }
}
