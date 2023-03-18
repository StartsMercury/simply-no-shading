package com.github.startsmercury.simply.no.shading.test.mixin.minecraft.notification;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.mojang.serialization.Codec;

import net.minecraft.client.PeriodicNotificationManager;
import net.minecraft.client.PeriodicNotificationManager.Notification;
import net.minecraft.resources.ResourceLocation;

/**
 * The {@code PeriodicNotificationManagerMixin} is a {@linkplain Mixin mixin}
 * class for the {@link PeriodicNotificationManager} class.
 *
 * @since 6.0.0
 */
@Mixin(PeriodicNotificationManager.class)
public abstract class PeriodicNotificationManagerMixin {
	/**
	 * The CODEC used when parsing.
	 */
	@Final
	@Shadow
	private static Codec<Map<String, List<Notification>>> CODEC;

	/**
	 * The logger.
	 */
	@Final
	@Shadow
	private static Logger LOGGER;

	/**
	 * The notifications.
	 */
	@Final
	@Shadow
	private ResourceLocation notifications;

	/**
	 * This is a {@linkplain Redirect redirector} that redirects all calls to the
	 * non-existent method {@code JsonParser.parseReader(Reader)} in
	 * {@code PeriodicNotificationManagerMixin.perpare(ResourceManager, ProfilerFiller)},
	 * returning the parsed json.
	 *
	 * @param reader the reader
	 * @return the parsed json
	 * @throws IOException if an IO operation went wrong
	 */
	@Redirect(method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Ljava/util/Map;",
	          at = @At(value = "INVOKE",
	                   target = "Lcom/google/gson/JsonParser;parseReader(Ljava/io/Reader;)Lcom/google/gson/JsonElement;"))
	private final JsonElement redirectParseReader(final Reader reader) throws IOException {
		try (final var jsonReader = new JsonReader(reader)) {
			return Streams.parse(jsonReader);
		}
	}
}
