package net.notcoded.wayfix.config;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class ModConfig {

    @Comment("Auto-scales the GUI scale depending on your display's scaling. (e.g. 2 GUI Scale on 1920x1080 at 100% → 4 GUI Scale on 3840x2160 at 200%)")
    public boolean autoScaleGUI = true;

    @Comment("Injects the Minecraft Icon at Startup instead of defaulting to the normal Wayland icon.")
    public boolean injectIcon = true;

    @Comment("Fixes issues where keyboard combinations like 'CTRL + A' or 'CTRL + C' are sent as characters in chat instead of being recognized as key combinations.")
    public boolean keyModifiersFix = true;

    @Comment("Full-screens the minecraft window in the selected monitor.\n[!] If you use KDE Plasma with kdotool installed then you can ignore this setting.\n\nPut the name of the monitor (that is shown in your OS monitor settings) in the quotes.\nIf you don't know the monitor of your name then you can put 'DP-x' (x being a number) e.g. DP-1\n\nLeave empty to use primary monitor.")
    public String monitorName = "";
}
