package com.github.startsmercury.simply.no.shading.config;

import static com.google.gson.stream.JsonToken.END_OBJECT;
import static java.lang.reflect.Modifier.isStatic;
import static net.fabricmc.api.EnvType.CLIENT;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.github.startsmercury.simply.no.shading.impl.CloudRenderer;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(CLIENT)
@JsonAdapter(SimplyNoShadingClientConfig.JsonAdapter.class)
public class SimplyNoShadingClientConfig {
	public static class JsonAdapter extends TypeAdapter<SimplyNoShadingClientConfig> {
		protected SimplyNoShadingClientConfig newInstance() {
			return new SimplyNoShadingClientConfig();
		}

		@Override
		public SimplyNoShadingClientConfig read(final JsonReader in) throws IOException {
			final var config = newInstance();
			final var ruleProviders = getRuleProviders(config);

			in.beginObject();

			while (in.peek() != END_OBJECT) {
				ruleProviders.get(in.nextName()).apply(config).setShade(in.nextBoolean());
			}

			in.endObject();

			return config;
		}

		@Override
		public void write(final JsonWriter out, final SimplyNoShadingClientConfig config) throws IOException {
			out.beginObject();

			for (final var ruleProviderEntry : getRuleProviders(config).entrySet()) {
				out.name(ruleProviderEntry.getKey());
				out.value(ruleProviderEntry.getValue().apply(config).shouldShade());
			}

			out.endObject();
		}
	}

	public class Observation<T extends SimplyNoShadingClientConfig> {
		public final T past;

		@SuppressWarnings("unchecked")
		public Observation() {
			this.past = (T) copy();
		}

		public void consume(final Minecraft client) {
			if (rebuildChunks()) {
				client.levelRenderer.allChanged();
			}

			if (rebuildClouds()) {
				((CloudRenderer) client.levelRenderer).generateClouds();
			}
		}

		public boolean rebuildChunks() {
			return this.past.blockShading.wouldEquals(SimplyNoShadingClientConfig.this.blockShading)
			    || this.past.liquidShading.wouldEquals(SimplyNoShadingClientConfig.this.liquidShading);
		}

		public boolean rebuildClouds() {
			return this.past.cloudShading.wouldEquals(SimplyNoShadingClientConfig.this.cloudShading);
		}
	}

	public static class ShadingRule {
		public static class Child extends ShadingRule {
			private final ShadingRule parent;

			public Child(final ShadingRule parent, final boolean defaultShade) {
				super(defaultShade);

				this.parent = parent;
			}

			@Override
			public boolean wouldShade() {
				return this.parent.shouldShade() || shouldShade();
			}
		}

		private final boolean defaultShade;

		private boolean shade;

		public ShadingRule(final boolean defaultShade) {
			this.defaultShade = defaultShade;
			this.shade = defaultShade;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			} else if (obj instanceof final ShadingRule other) {
				return shouldShade() == other.shouldShade();
			} else {
				return false;
			}
		}

		public boolean getDefaultShade() {
			return this.defaultShade;
		}

		public void setShade(final boolean shade) {
			this.shade = shade;
		}

		public boolean shouldShade() {
			return this.shade;
		}

		public void toggleShade() {
			setShade(!shouldShade());
		}

		public boolean wouldEquals(final ShadingRule other) {
			return wouldShade() == wouldShade();
		}

		public boolean wouldShade() {
			return shouldShade();
		}

		public boolean wouldShade(final boolean shaded) {
			return shaded && wouldShade();
		}
	}

	private static final Map<Class<? extends SimplyNoShadingClientConfig>, Map<String, Function<? super SimplyNoShadingClientConfig, ? extends ShadingRule>>> RULE_PROVIDERS_MAP = new WeakHashMap<>();

	private static final Map<String, Function<? super SimplyNoShadingClientConfig, ? extends ShadingRule>> getRuleProviders(
	    final Class<? extends SimplyNoShadingClientConfig> clazz) {
		return RULE_PROVIDERS_MAP.computeIfAbsent(clazz, key -> {
			final var ruleFields = key.getFields();
			final var ruleProviders = new HashMap<String, Function<? super SimplyNoShadingClientConfig, ? extends ShadingRule>>(
			    ruleFields.length);

			for (final var rule : ruleFields) {
				if (isStatic(rule.getModifiers()) || !ShadingRule.class.isAssignableFrom(rule.getType())) {
					continue;
				}

				rule.setAccessible(true);

				ruleProviders.put(rule.getName(), config -> {
					try {
						return (ShadingRule) rule.get(config);
					} catch (final IllegalAccessException iae) {
						throw new InternalError("setAccessible(true) is flawed or missing", iae);
					} catch (final IllegalArgumentException iae) {
						throw new InternalError("Static filter is flawed or missing", iae);
					}
				});
			}

			return ruleProviders;
		});
	}

	private static final Map<String, Function<? super SimplyNoShadingClientConfig, ? extends ShadingRule>> getRuleProviders(
	    final SimplyNoShadingClientConfig config) {
		return getRuleProviders(config.getClass());
	}

	public final ShadingRule allShading;

	public final ShadingRule blockShading;

	public final ShadingRule cloudShading;

	public final ShadingRule liquidShading;

	public SimplyNoShadingClientConfig() {
		this.allShading = new ShadingRule(false);
		this.blockShading = new ShadingRule.Child(this.allShading, false);
		this.cloudShading = new ShadingRule.Child(this.allShading, true);
		this.liquidShading = new ShadingRule.Child(this.allShading, false);
	}

	public SimplyNoShadingClientConfig(final SimplyNoShadingClientConfig other) {
		this();

		copyFrom(other);
	}

	public SimplyNoShadingClientConfig copy() {
		return new SimplyNoShadingClientConfig(this);
	}

	public void copyFrom(final SimplyNoShadingClientConfig other) {
		forEachShadingRules((name, shadingRule) -> {
			final var otherShadingRule = other.getShadingRule(name);

			if (otherShadingRule != null) {
				shadingRule.setShade(otherShadingRule.shouldShade());
			}
		});
	}

	public void copyTo(final SimplyNoShadingClientConfig other) {
		forEachShadingRules((name, shadingRule) -> {
			final var otherShadingRule = other.getShadingRule(name);

			if (otherShadingRule != null) {
				otherShadingRule.setShade(shadingRule.shouldShade());
			}
		});
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof final SimplyNoShadingClientConfig other) {
			return shadingRuleEquals(other);
		} else {
			return false;
		}
	}

	public void forEachShadingRules(final BiConsumer<? super String, ? super ShadingRule> action) {
		getRuleProviders(this).forEach((name, ruleProvider) -> action.accept(name, ruleProvider.apply(this)));
	}

	public ShadingRule getShadingRule(final String name) {
		return getRuleProviders(this).get(name).apply(this);
	}

	public HashMap<String, ShadingRule> getShadingRules() {
		return getShadingRules(new HashMap<>());
	}

	public <T extends Map<String, ShadingRule>> T getShadingRules(final T map) {
		for (final var ruleProviderEntry : getRuleProviders(this).entrySet()) {
			map.put(ruleProviderEntry.getKey(), ruleProviderEntry.getValue().apply(this));
		}

		return map;
	}

	public Observation<? extends SimplyNoShadingClientConfig> observe() {
		return new Observation<>();
	}

	protected boolean shadingRuleEquals(final SimplyNoShadingClientConfig other) {
		final var ruleProviders = new HashMap<>(getRuleProviders(this));
		final var otherRuleProviders = getRuleProviders(other);

		ruleProviders.keySet().retainAll(otherRuleProviders.keySet());

		for (final var ruleProviderEntry : ruleProviders.entrySet()) {
			if (!ruleProviderEntry.getValue().apply(this)
			    .equals(otherRuleProviders.get(ruleProviderEntry.getKey()).apply(other))) {
				return false;
			}
		}

		return true;
	}
}
