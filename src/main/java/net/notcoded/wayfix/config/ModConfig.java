package net.notcoded.wayfix.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;

@Config(name = "wayfix")
public class ModConfig implements ConfigData {
    @Tooltip
    public boolean autoScaleGUI = true;

    @Tooltip
    public boolean injectIcon = true;

    @Tooltip
    public boolean keyModifiersFix = true;

    @CollapsibleObject
    @Tooltip
    public Fullscreen fullscreen = new Fullscreen();

    public static class Fullscreen {
        @Tooltip
        public boolean usePrimaryMonitor = false;
    }
}
