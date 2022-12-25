<div align=center>

# Simply No Shading

[![MIT License](https://img.shields.io/github/license/StartsMercury/simply-no-shading)](LICENSE)
[![Build Workflow](https://github.com/StartsMercury/simply-no-shading/actions/workflows/build.yml/badge.svg?branch=5.x%2F1.18.2)](https://github.com/StartsMercury/simply-no-shading/actions/workflows/build.yml)
[![FabricMC](https://img.shields.io/badge/mod%20loader-fabric-1976d2)](https://fabricmc.net)
[![FabricMC Tutorial Wiki: Side](https://img.shields.io/badge/environment-client-1976d2)](https://fabricmc.net/wiki/tutorial:side)

A mod mimicking OptiFine's Internal Shader though with less performance impact.

[![CurseForge](https://img.shields.io/badge/curseforge-simply--no--shading-e96a41?logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/simply-no-shading)
[![GitHub](https://img.shields.io/badge/github-simply--no--shading-1976d2?logo=github)](https://github.com/StartsMercury/simply-no-shading)
[![Modrinth](https://img.shields.io/badge/modrinth-simply--no--shading-5da545?logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxMSAxMSIgd2lkdGg9IjE0LjY2NyIgaGVpZ2h0PSIxNC42NjciICB4bWxuczp2PSJodHRwczovL3ZlY3RhLmlvL25hbm8iPjxkZWZzPjxjbGlwUGF0aCBpZD0iQSI+PHBhdGggZD0iTTAgMGgxMXYxMUgweiIvPjwvY2xpcFBhdGg+PC9kZWZzPjxnIGNsaXAtcGF0aD0idXJsKCNBKSI+PHBhdGggZD0iTTEuMzA5IDcuODU3YTQuNjQgNC42NCAwIDAgMS0uNDYxLTEuMDYzSDBDLjU5MSA5LjIwNiAyLjc5NiAxMSA1LjQyMiAxMWMxLjk4MSAwIDMuNzIyLTEuMDIgNC43MTEtMi41NTZoMGwtLjc1LS4zNDVjLS44NTQgMS4yNjEtMi4zMSAyLjA5Mi0zLjk2MSAyLjA5MmE0Ljc4IDQuNzggMCAwIDEtMy4wMDUtMS4wNTVsMS44MDktMS40NzQuOTg0Ljg0NyAxLjkwNS0xLjAwM0w4LjE3NCA1LjgybC0uMzg0LS43ODYtMS4xMTYuNjM1LS41MTYuNjk0LS42MjYuMjM2LS44NzMtLjM4N2gwbC0uMjEzLS45MS4zNTUtLjU2Ljc4Ny0uMzcuODQ1LS45NTktLjcwMi0uNTEtMS44NzQuNzEzLTEuMzYyIDEuNjUxLjY0NSAxLjA5OC0xLjgzMSAxLjQ5MnptOS42MTQtMS40NEE1LjQ0IDUuNDQgMCAwIDAgMTEgNS41QzExIDIuNDY0IDguNTAxIDAgNS40MjIgMCAyLjc5NiAwIC41OTEgMS43OTQgMCA0LjIwNmguODQ4QzEuNDE5IDIuMjQ1IDMuMjUyLjgwOSA1LjQyMi44MDljMi42MjYgMCA0Ljc1OCAyLjEwMiA0Ljc1OCA0LjY5MSAwIC4xOS0uMDEyLjM3Ni0uMDM0LjU2bC43NzcuMzU3aDB6IiBmaWxsLXJ1bGU9ImV2ZW5vZGQiIGZpbGw9IiM1ZGE0MjYiLz48L2c+PC9zdmc+)](https://modrinth.com/mod/simply-no-shading)

## Comparisions (TODO)

### Implementations

| Vanilla | OptiFine's Internal Shader | Simply No Shading |
| :-----: | :------------------------: | :---------------: |
| ![]()   | ![]()                      | ![]()             |

### Options

| All Options OFF | Defaults | All Options ON |
| :-------------: | :------: | :------------: |
| ![]()           | ![]()    | ![]()          |

## Settings

<div align=left>

### Settings Screen

The settings screen for this mod can be accessed via ModMenu by following these steps:

 0. Pause the game or go to the main menu.
 1. Open the Mod Menu
 2. Find Simply No Shading
 3. Finally, on the top right of the screen, click 'Configure...'

Those without ModMenu but with Fabric API:

 0. Pause the game or go to the main menu.
 1. Open settings
 2. Open the control settings
 3. Open the key settings
 4. Scroll down and look for the Simply No Shading category.
 5. 'Open Settings' should be bound to a key.
 6. Finally, in-game, use the bound key to access the settings screen for this mod.

For those without Fabric API:

 0. Open the Minecraft folder.
 1. Navigate to the configs folder.
 2. Open 'simply-no-shading+client.json'. Create it when it's unavailable.
 3. Default contents of <tt>simply-no-shading+client.json</tt>:

```json
{
  "shadingRules": {
    "blocks": false,
    "clouds": false,
    "all": false,
    "liquids": false,
    "enhancedBlockEntities": true
  },
  "smartReload": true
  "smartReloadMessage": true
}
```

### Shading Settings

| Name                          | Type   | Defaults | Description                                                                         |
| :---------------------------: | :----: | :------: | :---------------------------------------------------------------------------------- |
| All Shading                   | Toggle | OFF      | Controls all available shading options<br>ON is equivalent to all options set to ON |
| Block Shading                 | Toggle | OFF      | Controls block shading, excluding block entities                                    |
| Cloud Shading                 | Toggle | ON       | Controls cloud shading                                                              |
| Enhanced Block Entity Shading | Toggle | ON       | Controls enhanced block entity shading                                              |
| Liquid Shading                | Toggle | OFF      | Controls liquid shading                                                             |

### Advance Settings

| Name                 | Type   | Defaults | Description                                             |
| :------------------: | :----: | :------: | :------------------------------------------------------ |
| Smart Reload         | Toggle | OFF      | Reduces unnecessary chunk reloads                       |
| Smart Reload Message | Toggle | OFF      | Notify whenever an unnecessary chunk reload was avoided |

</div>

</div>
