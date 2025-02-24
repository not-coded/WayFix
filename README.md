<div align="center">

<img src="https://cdn.modrinth.com/data/hxIWsdEF/11a8bb4badc214361593ec6d33e29998bcc6ef46.png" height=128 alt="WayFix Logo"/>
<br>

# WayFix

[![Supports minecraft versions from 1.16](https://notcoded.needs.rest/r/badge_minecraft_1.16plus.svg)](https://minecraft.net) [![Cloth Config API](https://raw.githubusercontent.com/intergrav/devins-badges/v3/assets/cozy/requires/cloth-config-api_vector.svg)](https://www.modrinth.com/mod/cloth-config)

Fixes multiple issues regarding Wayland compatibility for Minecraft.

**Recommended to be used with [glfw-wayland](https://github.com/BoyOrigin/glfw-wayland).**
<br>
**With the [Cursor Fix](https://www.reddit.com/r/kde/comments/13ddktm/mouse_cursor_changing_when_over_some_apps_when/).**
<br>
**And [kdotool](https://github.com/jinliu/kdotool)** ***(only if on KDE Plasma)***.

</div>

## Features
- Auto Scale GUI
  - Auto-scales the GUI scale depending on your display's scaling. (e.g. 2 GUI Scale on 1920x1080 at 100% → 4 GUI Scale on 3840x2160 at 200%)

- Inject Minecraft Icon at Startup
  - Injects the Minecraft Icon at Startup instead of defaulting to the normal Wayland icon.

- Key Modifiers Fix
  - Fixes issues where keyboard combinations like 'CTRL + A' or 'CTRL + C' are sent as characters in chat instead of being recognized as key combinations.

- **Fullscreen**
  - Select Monitor
    - Select which monitor you want to fullscreen Minecraft to. (primary monitor by default) (must turn on in seperate config option)

> [!NOTE]
> By default Minecraft sometimes full-screens on the wrong monitor due to Wayland window limitations (unable to get X and Y position).
> This is automatically **fixed only on KDE Plasma** without specifying the monitor by installing **[kdotool](https://github.com/jinliu/kdotool)**.

## Building
- Clone the repository
  - `git clone https://github.com/not-coded/WayFix`
- Run `./gradlew chiseledBuild`