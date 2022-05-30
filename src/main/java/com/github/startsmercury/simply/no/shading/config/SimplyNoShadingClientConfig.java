package com.github.startsmercury.simply.no.shading.config;

import static net.fabricmc.api.EnvType.CLIENT;

import com.github.startsmercury.simply.no.shading.config.ShadingRules.Observation.Context;
import com.github.startsmercury.simply.no.shading.util.Copyable;
import com.github.startsmercury.simply.no.shading.util.Observable;

import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(CLIENT)
public class SimplyNoShadingClientConfig<R extends ShadingRules>
    implements Copyable<SimplyNoShadingClientConfig<?>>, Observable<SimplyNoShadingClientConfig<R>> {
	public static class Observation<T extends SimplyNoShadingClientConfig<?>>
	    extends Observable.Observation<T, Minecraft> {
		public Observation(final T point) {
			super(point);
		}

		public Observation(final T past, final T present) {
			super(past, present);
		}

		@Override
		public void react(final Minecraft context) {
			this.present.shadingRules.observe(this.past.shadingRules)
			    .react(new Context(context, this.present.smartReload));
		}
	}

	public final R shadingRules;

	public boolean smartReload;

	public SimplyNoShadingClientConfig(final R shadingRules) {
		this.shadingRules = shadingRules;
	}

	@SuppressWarnings("unchecked")
	public SimplyNoShadingClientConfig(final SimplyNoShadingClientConfig<R> other) {
		this((R) other.shadingRules.copy());
	}

	@Override
	public SimplyNoShadingClientConfig<R> copy() {
		return new SimplyNoShadingClientConfig<>(this);
	}

	@Override
	public void copyFrom(final SimplyNoShadingClientConfig<?> other) {
		this.shadingRules.copyFrom(other.shadingRules);
	}

	@Override
	public void copyTo(final SimplyNoShadingClientConfig<?> other) {
		this.shadingRules.copyTo(other.shadingRules);
	}

	@Override
	public Observation<? extends SimplyNoShadingClientConfig<R>> observe() {
		return new Observation<>(this);
	}

	@Override
	public Observation<? extends SimplyNoShadingClientConfig<R>> observe(final SimplyNoShadingClientConfig<R> past) {
		return new Observation<>(past, this);
	}
}
