package com.github.startsmercury.simply.no.shading.incubating.config;

import static com.google.gson.stream.JsonToken.NAME;
import static it.unimi.dsi.fastutil.objects.ReferenceLists.unmodifiable;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;

import org.jetbrains.annotations.ApiStatus;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mojang.blaze3d.platform.InputConstants;

import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceList;
import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.resources.ResourceLocation;

@ApiStatus.Experimental
public class ShadingRule {
	public static class Builder {
		protected static String asKey(final ResourceLocation location) {
			return location.getNamespace() + ".key." + location.getPath().replace('/', '.') + "Shading";
		}

		private ShadingRule ancestor;

		private boolean defaultShade;

		private int keyCode;

		private Function<? super ResourceLocation, ? extends String> keyProvider;

		private final ResourceLocation location;

		protected Builder(final ResourceLocation location) {
			this.defaultShade = true;
			this.keyCode = InputConstants.UNKNOWN.getValue();
			this.keyProvider = Builder::asKey;
			this.location = location;
		}

		public Builder asRoot() {
			return inherits(null);
		}

		public ShadingRule build() {
			return new ShadingRule(this.ancestor, this.defaultShade, this::createKeyMapping, this.location);
		}

		private final ToggleKeyMapping createKeyMapping(final ShadingRule rule) {
			return new ToggleKeyMapping(this.keyProvider.apply(this.location), this.keyCode,
			    "simply-no-shading.key.categories.simply-no-shading", rule::shouldShade);
		}

		public Builder defaultsTo(final boolean defaultShade) {
			this.defaultShade = defaultShade;

			return this;
		}

		public Builder defaultsToFalse() {
			return defaultsTo(false);
		}

		public Builder defaultsToTrue() {
			return defaultsTo(true);
		}

		public Builder inherits(final ShadingRule ancestor) {
			this.ancestor = ancestor;

			return this;
		}

		public Builder inheritsAll() {
			return inherits(ALL);
		}

		public Builder keyCode(final int keyCode) {
			this.keyCode = keyCode;

			return this;
		}

		public Builder keyProvider(final Function<? super ResourceLocation, ? extends String> keyProvider) {
			this.keyProvider = keyProvider == null ? Builder::asKey : keyProvider;

			return this;
		}

		protected void setHardRoot() {
			this.ancestor = ALL;
		}
	}

	public static final ShadingRule ALL;

	public static final ShadingRule BLOCK;

	public static final ShadingRule ENHANCED_BLOCK_ENTITY;

	private static final ReferenceList<ShadingRule> ROOTS;

	private static final ReferenceList<ShadingRule> ROOTS_VIEW;

	private static final ReferenceList<ShadingRule> VALUES;

	private static final ReferenceList<ShadingRule> VALUES_VIEW;

	static {
		ROOTS = new ReferenceArrayList<>(4);
		ROOTS_VIEW = unmodifiable(ROOTS);
		VALUES = new ReferenceArrayList<>();
		VALUES_VIEW = unmodifiable(VALUES);

		ALL = createAll();
		BLOCK = createBuiltIn("block");
		ENHANCED_BLOCK_ENTITY = createBuiltIn("enhancedBlockEntity");
	}

	private static final ShadingRule createAll() {
		return new Builder(new ResourceLocation("simply-no-shading", "all")).build();
	}

	protected static ShadingRule createBuiltIn(final String id) {
		return of(new ResourceLocation("simply-no-shading", id)).build();
	}

	public static Builder of(final ResourceLocation location) {
		return new Builder(location).inheritsAll();
	}

	public static ReferenceList<ShadingRule> roots() {
		return ROOTS_VIEW;
	}

	public static ReferenceList<ShadingRule> values() {
		return VALUES_VIEW;
	}

	private final ShadingRule ancestor;

	private final ReferenceList<ShadingRule> children;

	private final ReferenceList<ShadingRule> childrenView;

	private final boolean defaultShade;

	private final ToggleKeyMapping keyMapping;

	private final ResourceLocation location;

	private boolean shade;

	protected ShadingRule(final ShadingRule ancestor, final boolean defaultShade,
	    final Function<? super ShadingRule, ? extends ToggleKeyMapping> keyMappingProvider,
	    final ResourceLocation location) {
		Objects.requireNonNull(keyMappingProvider);

		this.ancestor = ancestor;
		this.children = new ReferenceArrayList<>(0);
		this.childrenView = unmodifiable(this.children);
		this.defaultShade = defaultShade;
		this.keyMapping = keyMappingProvider.apply(this);
		this.location = location;
		this.shade = defaultShade;

		if (ancestor != null) {
			ancestor.children.add(this);
		} else {
			ROOTS.add(this);
		}

		VALUES.add(this);
	}

	public ShadingRule getAncestor() {
		return this.ancestor;
	}

	public ReferenceList<ShadingRule> getChildren() {
		return this.childrenView;
	}

	public boolean getDefaultShade() {
		return this.defaultShade;
	}

	public ToggleKeyMapping getKeyMapping() {
		return this.keyMapping;
	}

	public ResourceLocation getLocation() {
		return this.location;
	}

	public boolean hasAncestor() {
		return getAncestor() != null;
	}

	public void read(final JsonReader in) throws IOException {
		if (in.peek() == NAME && !this.location.toString().equals(in.nextName())) {
			throw new IOException("For name " + in);
		}

		setShade(in.nextBoolean());
	}

	public ShadingRule setShade(final boolean shade) {
		this.shade = shade;

		return this;
	}

	public ShadingRule setShadeFalse() {
		return setShade(false);
	}

	public ShadingRule setShadeTrue() {
		return setShade(true);
	}

	public boolean shouldAncestorShade() {
		final var ancestor = getAncestor();

		return ancestor != null && ancestor.shouldShade();
	}

	public boolean shouldChildrenShade() {
		final var children = getChildren();

		if (children.isEmpty()) {
			return getDefaultShade();
		}

		for (final var child : children) {
			if (!child.shouldShade() && !child.shouldChildrenShade()) {
				return false;
			}
		}

		return true;
	}

	public boolean shouldShade() {
		return this.shade;
	}

	public boolean toggleShade() {
		final var wouldHaveShade = wouldShade();

		setShade(!shouldShade());

		return wouldHaveShade != wouldShade();
	}

	public boolean wouldShade() {
		return shouldAncestorShade() || shouldShade();
	}

	public boolean wouldShade(final boolean shaded) {
		return wouldShade() && shaded;
	}

	public void write(final JsonWriter out) throws IOException {
		out.name(this.location.toString());
		out.value(shouldShade());
	}
}
