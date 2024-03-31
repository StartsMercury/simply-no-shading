package io.github.startsmercury.simply_no_shading.client;

public enum ReloadType {
    None,
    Minor,
    Major;

    public static ReloadType max(final ReloadType... rts) {
        var lhs = ReloadType.None;
        for (final var rhs : rts) {
            lhs = lhs.max(rhs);
        }
        return lhs;
    }

    public void applyTo(final LevelRenderer levelRenderer) {
        switch (this) {
            case None -> {};
            case Minor -> levelRenderer.needsUpdate();
            case Major -> levelRenderer.allChanged();
        }
    }

    public ReloadType max(final ReloadType rhs) {
        if (this.compareTo(rhs) <= 0) {
            return this;
        } else {
            return rhs;
        }
    }
}
