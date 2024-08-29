package net.notcoded.wayfix.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.*;
import net.minecraft.client.util.Monitor;
import net.notcoded.wayfix.WayFix;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

@Config(name = "wayfix")
public class ModConfig implements ConfigData {
    @Tooltip
    public boolean autoScaleGUI = true;

    @Tooltip
    @RequiresRestart
    public boolean injectIcon = true;

    @Tooltip
    public boolean keyModifiersFix = true;

    @CollapsibleObject(startExpanded = true)
    @Tooltip
    public Fullscreen fullscreen = new Fullscreen();

    public static class Fullscreen {
        @Tooltip
        public boolean useMonitorID = false;

        @Tooltip
        public long monitorID = 0;

        @CollapsibleObject
        public MonitorSelector monitorSelector = new MonitorSelector(new ArrayList<>());
    }

    public static class Monitors {
        public String name;
        public long id;
        public boolean primary;

        public Monitors() {
            this("Cannot find any monitors!", 0, false);
        }

        public Monitors(String name, long id, boolean primary) {
            this.name = name;
            this.id = id;
            this.primary = primary;
            if(id <= 0) return;

            if(WayFix.config.fullscreen.monitorID <= 0 && primary) {
                WayFix.config.fullscreen.monitorID = id;
            }
        }
    }

    public static class MonitorSelector {
        public ArrayList<Monitors> monitors = new ArrayList<>();

        public MonitorSelector(ArrayList<Monitor> monitors) {
            for(Monitor monitor : monitors) {
                this.monitors.add(new Monitors(monitor.getCurrentVideoMode().toString(), monitor.getHandle(), GLFW.glfwGetPrimaryMonitor() == monitor.getHandle()));
            }
        }
    }
}
