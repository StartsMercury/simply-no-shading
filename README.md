<h1 align=center>Simply No Shading</h1>

<p align=center>
	<a alt="License: MIT" href="LICENSE">
		<img src="https://img.shields.io/badge/license-MIT-yellow.svg"/></a>
	<a alt="build" href="https://github.com/StartsMercury/simply-no-shading/actions/workflows/build.yml">
		<img src="https://github.com/StartsMercury/simply-no-shading/actions/workflows/build.yml/badge.svg?branch=1.18.x"/>
	<a alt="Mod loader: Fabric (External link)" href="https://fabricmc.net">
		<img src="https://img.shields.io/badge/mod%20loader-fabric-d64541"/></a>
	<a alt="client" href="https://fabricmc.net/wiki/tutorial:side">
		<img src="https://img.shields.io/badge/environment-client-1976d2"/></a>
	<a alt="channel" href="https://semver.org">
		<img src="https://img.shields.io/badge/dynamic/json?label=version%20&query=$[0]['version_type']&url=https://api.modrinth.com/v2/project/9gx5Xvc5/version"/></a>
	<a alt="version" href="https://semver.org">
		<img src="https://img.shields.io/badge/dynamic/json?label=version%20&query=$[0]['version_number']&url=https://api.modrinth.com/v2/project/9gx5Xvc5/version"/></a>
	<a alt="minecraft version" href="https://www.minecraft.net">
		<img src="https://img.shields.io/badge/dynamic/json?label=minecraft&query=$[0]['game_versions']&url=https://api.modrinth.com/v2/project/9gx5Xvc5/version"/></a></a></p>

This mod, by default, mimics OptiFine's Internal Shader with Old Lighting OFF. The benefit is the absence of lag that is present to any shader, including Internal Shader. You also have the freedom to select which and which not this mod affects and may bind keys to toggle them in-game, though it hasn't yet supported every possible feature.

---

 - [Configuration](#Configuration)
 - [Compatibility](#Compatibility)
    - [Incompatiblities](#Incompatiblities)
 - [Images](#Images)
 - [Performance](#Performance)

## Configuration

The screen for configuring this mod can be found `Options > Video Settings > Shading Settings`. However, as you notice, in sodium it is `Options > Video Settings`, Shading tab. Alternatively, if you have modmenu installed, go to `Mods > Simply No Shading > Configure`.

---

The screen for configuring keys can be found in `Options > Controls > Key Binds`, then scroll down and you'll see the Shading category.

## Compatibility

All resource packs should be compatible, and the mods that are currently known to be compatible are:

 - [Indigo](https://github.com/FabricMC/fabric) - Fabric API's Renderer
 - [Iris](https://github.com/IrisShaders/Iris) with shaders **disabled**
 - LambdaBetterGrass
 - [Sodium](https://github.com/CaffeineMC/sodium-fabric)

Mods that aren't listed may or may not be compatible, for signs and/or effects of incompatibilities proceed on reading.

### Incompatiblities

When a certain mod is incompatible with this mod common side-effects are:

 - Visual features are buggy or doesn't work at all.
 - Crashes, these occur when there are serious incompatibilities needed to be addressed.

Any incompatibilities should be reported to the [issue tracker](https://github.com/StartsMercury/simply-no-shading/issues).

## Images

<table>
<tr><th>OFF - Vanilla</th>
    <th>ON - Default</th>
    <th>ON - No Shading</th></tr>
<tr><td><img alt="OFF - Vanilla"
             height=144
             src="./images/frozen_peeks_vanilla.png"
             width=256/></td>
    <td><img alt="ON - Default"
             height=144
             src="./images/frozen_peeks_default.png"
             width=256/></td>
    <td><img alt="ON - No Shading"
             height=144
             src="./images/frozen_peeks_shadeless.png"
             width=256/></td></tr>
<tr><td><img alt="OFF - Vanilla"
             height=144
             src="./images/lush_caves_vanilla.png"
             width=256/></td>
    <td><img alt="ON"
             height=144
             src="./images/lush_caves_shadeless.png"
             width=256/></td>
    <td><img alt="ON"
             height=144
             src="./images/lush_caves_shadeless.png"
             width=256/></td>
<tr><td><img alt="OFF - Vanilla"
             height=144
             src="./images/many_biomes_vanilla.png"
             width=256/></td>
    <td><img alt="ON - Default"
             height=144
             src="./images/many_biomes_default.png"
             width=256/></td>
    <td><img alt="ON - No Shading"
             height=144
             src="./images/many_biomes_shadeless.png"
             width=256/></td></tr>
</table>

## Performance

Performance shouldn't be affected and if it did, it is negligible.
