package io.github.startsmercury.simply_no_shading.client;

import net.minecraft.client.renderer.LevelRenderer;

// TODO: might be unstable API
/**
 * Game reload type.
 * <p>
 * This enumeration represents different ways or degree the game to reload such
 * that changes can be observed in-game.
 *
 * @since 6.2.0
 */
public enum ReloadType {
    /**
     * A major reload.
     * <p>
     * This would rebuild all chunks and stuff.
     * 
     * @since 6.2.0
     */
    Major,
    /**
     * A minor reload.
     * <p>
     * This would rebuild clouds and a few stuff.
     * 
     * @since 6.2.0
     */
    Minor,
    /**
     * No reload.
     * 
     * @since 6.2.0
     */
    None;

    /**
     * Takes the greater reload type.
     * <p>
     * Usually used to avoid reapplying reloading supplied by some array of it.
     * {@link None} when supplied with no reload types.
     *
     * @return the greatest reload type
     * @see #max(ReloadType)
     * @since 6.2.0
     */
    public static ReloadType max(final ReloadType... rts) {
        var lhs = ReloadType.None;
        for (final var rhs : rts) {
            lhs = lhs.max(rhs);
        }
        return lhs;
    }

    /**
     * Applies this reload type.
     *
     * @param levelRenderer the level renderer
     * @since 6.2.0
     */
    public void applyTo(final LevelRenderer levelRenderer) {
        switch (this) {
            case None -> {}
            case Minor -> levelRenderer.needsUpdate();
            case Major -> levelRenderer.allChanged();
        }
    }

    /**
     * Returns the greater of two reload types.
     *
     * @since 6.2.0
     * @see ReloadType#max(ReloadType[]) max(ReloadType...)
     */
    public ReloadType max(final ReloadType rhs) {
        if (this.compareTo(rhs) <= 0) {
            return this;
        } else {
            return rhs;
        }
    }
}
