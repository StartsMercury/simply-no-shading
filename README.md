# Simply No Shading

**You might also want to check out the [FAQ](FAQ.md), or the [GALLERY](GALLERY.md).**

---

This mod, by default, will disable *shading*.

But what is shading? To this mod, shading is the difference in brightness to a cube's sides. Each side are assigned a different brightness if that cube applies shading.

This mod allows you to force disable the said shading.

## Configuration

**You might also want to check out the [FAQ](FAQ.md), or the [GALLERY](GALLERY.md).**

---

If you desire to toggle shading in the settings here is what you do:

 - Open Video Settings.
 - When you have [Sodium](https://github.com/CaffeineMC/sodium-fabric) installed, click on the quality tab.
 - Lastly you should see a button with text 'Shading'.

If you are unable to find the said button:

 - Open your file explorer
 - Go to your `.minecraft` folder
 - Open `options.txt`
 - Find the line containing "`shading`"
 - Then set the text after the colon (`:`) with either `true` or `false`.

---

If you desire to be able toggle shading in-game then here is what you do:

 - Make sure [Fabric API](https://github.com/FabricMC/fabric) is installed.
 - Then you can now press `F6` to toggle shading in-game.
 - You can now also change the key binding in `Options -> Controls -> Key Bind`.

## Compatibility

**You might also want to check out the [FAQ](FAQ.md), or the [GALLERY](GALLERY.md).**

---

Currently these are the test-proven compatible mods:

 - [Fabric API's Renderer - Indigo](https://github.com/FabricMC/fabric)
 - [Sodium](https://github.com/CaffeineMC/sodium-fabric)

Mods that are currently not on the above list may be compatible, but is not assured.

### Incompatiblities

When a certain mod is incompatible with this mod common side-effects are:

 - force-disable shading won't work for some modded blocks or fluids.
 - In-game crash, when certain code is conflicting another.
 - Startup crash, when a certain code can't properly run due to conflicts.
