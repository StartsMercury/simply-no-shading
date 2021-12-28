# Simply No Shading

**You might also want to check out the [FAQ](FAQ.md), or the [GALLERY](GALLERY.md).**

---

This mod, by default, will disable *shading*.

But what is shading? To this mod, shading is the difference in brightness to a cube's sides. Each side are assigned a different brightness if that cube applies shading.

This mod allows you to force disable the said shading.

## Configuration

**You might also want to check out the [FAQ](FAQ.md), or the [GALLERY](GALLERY.md).**

---

If you desire to be able toggle shading in-game then here is what you do:

 - Make sure [Fabric API](https://github.com/FabricMC/fabric) is installed.
 - Then you can now press `F6` to toggle shading in-game.
 - You can now also change the key binding in `Options -> Controls -> Key Bind`.

If you currently dont have Fabric API installed you can edit or add the file `.minecraft/config/simply-no-shading.json` with the contents:

```
{ "shading": true }
```

Alternatively setting it to false will re-enable shading.

## Compatibility

**You might also want to check out the [FAQ](FAQ.md), or the [GALLERY](GALLERY.md).**

---

Currently these are the test-proven compatible mods:

 - [Fabric API's Renderer - Indigo](https://github.com/FabricMC/fabric)

Mods that are currently not on the above list might be compatible.

### Incompatiblities

When a certain mod is incompatible with this mod common side-effects are:

 - force-disable shading won't work for some modded blocks or fluids.
 - In-game crash, when certain code is conflicting another.
 - Startup crash, when a certain code can't properly run due to conflicts.
