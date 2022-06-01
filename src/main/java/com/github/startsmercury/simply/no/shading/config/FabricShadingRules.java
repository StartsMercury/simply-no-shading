package com.github.startsmercury.simply.no.shading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import net.coderbot.iris.Iris;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(CLIENT)
public class FabricShadingRules extends ShadingRules {
	public static class Observation<T extends FabricShadingRules> extends ShadingRules.Observation<T> {
		public Observation(final T point) {
			super(point);
		}

		public Observation(final T past, final T present) {
			super(past, present);
		}

		@Override
		protected boolean shouldRebuild() {
			return super.shouldRebuild()
			    && (!FabricLoader.getInstance().isModLoaded("iris") || !Iris.getIrisConfig().areShadersEnabled());
		}

		@Override
		public boolean shouldRebuildBlocks() {
			return super.shouldRebuildBlocks()
			    || !this.past.enhancedBlockEntities.wouldEquals(this.present.enhancedBlockEntities);
		}
	}

	public final ShadingRule enhancedBlockEntities;

	public FabricShadingRules() {
		this.enhancedBlockEntities = register("enhancedBlockEntities", new ShadingRule(this.all, true));
	}

	public FabricShadingRules(final ShadingRules other) {
		this();

		copyFrom(other);
	}

	@Override
	public FabricShadingRules copy() {
		return new FabricShadingRules(this);
	}

	@Override
	public Observation<? extends FabricShadingRules> observe() {
		return new Observation<>(this);
	}

	public Observation<? extends FabricShadingRules> observe(final FabricShadingRules past) {
		return new Observation<>(past, this);
	}

	@Deprecated
	@Override
	public ShadingRules.Observation<? extends ShadingRules> observe(final ShadingRules past) {
		return past instanceof final FabricShadingRules fabricPast ? observe(fabricPast) : super.observe(past);
	}
}
