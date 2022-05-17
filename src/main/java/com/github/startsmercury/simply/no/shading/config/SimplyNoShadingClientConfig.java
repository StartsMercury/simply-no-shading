package com.github.startsmercury.simply.no.shading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import net.fabricmc.api.Environment;

@Environment(CLIENT)
public class SimplyNoShadingClientConfig {
	public abstract static class ShadingRule {
		public static class Impl extends ShadingRule {
			public Impl(final boolean defaultShade) {
				super(defaultShade);
			}

			@Override
			public boolean wouldShade() {
				return Root.ALL.shouldShade() || shouldShade();
			}
		}

		public static class Root extends ShadingRule {
			public static final Root ALL = new Root();

			public static final Root DUMMY = new Root();

			public Root() {
				super(false);
			}

			@Override
			public boolean wouldShade() {
				return shouldShade();
			}
		}

		public final boolean defaultShade;

		public boolean shade;

		protected ShadingRule(final boolean defaultShade) {
			this.defaultShade = defaultShade;
			this.shade = defaultShade;
		}

		public boolean getDefaultShade() {
			return this.defaultShade;
		}

		public void read(final JsonReader in) throws IOException {
			setShade(in.nextBoolean());
		}

		public void setShade(final boolean shade) {
			this.shade = shade;
		}

		public boolean shouldShade() {
			return this.shade;
		}

		public boolean toggleShade() {
			final var wouldHaveShade = wouldShade();

			setShade(!shouldShade());

			return wouldHaveShade != wouldShade();
		}

		public abstract boolean wouldShade();

		public boolean wouldShade(final boolean shaded) {
			return shaded && wouldShade();
		}

		public void write(final JsonWriter out) throws IOException {
			out.value(shouldShade());
		}
	}

	public final ShadingRule allShading = ShadingRule.Root.ALL;

	public final ShadingRule blockShading = new ShadingRule.Impl(false);

	public final ShadingRule cloudShading = new ShadingRule.Impl(true);

	public void read(final JsonReader in) throws IOException {
		do {
			final var shadingRule = switch (in.nextName()) {
			case "allShading" -> this.allShading;
			case "blockShading" -> this.blockShading;
			case "cloudShading" -> this.cloudShading;
			default -> ShadingRule.Root.DUMMY;
			};

			shadingRule.read(in);
		} while (in.peek() != JsonToken.END_OBJECT);
	}

	public void write(final JsonWriter out) throws IOException {
		// @formatter:off
		out.name("allShading"); this.allShading.write(out);
		out.name("blockShading"); this.blockShading.write(out);
		out.name("cloudShading"); this.cloudShading.write(out);
		// @formatter:on
	}
}
