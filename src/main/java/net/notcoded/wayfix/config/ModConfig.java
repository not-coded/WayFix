package net.notcoded.wayfix.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.*;
import net.minecraft.client.util.Monitor;
import net.minecraft.client.util.VideoMode;
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
        public boolean useMonitorName = false;

        @Tooltip
        public String monitorName = "";

        @CollapsibleObject
        public MonitorSelector monitorSelector = new MonitorSelector(new ArrayList<>());
    }

    public static class Monitors {
        public String monitorInfo;
        public String monitorName;
        @Excluded public long monitorID;
        @PrefixText
        public boolean primary;

        public Monitors() {
            this("Cannot find any monitors!", 0);
        }

        public Monitors(String info, long handle) {
            this.monitorInfo = info;
            if(handle == 0) return;
            this.monitorName = GLFW.glfwGetMonitorName(handle);
            this.monitorID = handle;
            this.primary = GLFW.glfwGetPrimaryMonitor() == handle;

            if(WayFix.config.fullscreen.monitorName.isBlank() && this.primary) {
                WayFix.config.fullscreen.monitorName = this.monitorName;
            }
        }

        public static long getMonitor(String name) {
            for(Monitors monitor : WayFix.config.fullscreen.monitorSelector.monitors) {
                if(name.equals(monitor.monitorName)) return monitor.monitorID;
            }
            return 0;
        }
    }

    public static class MonitorSelector {
        @PrefixText
        public ArrayList<Monitors> monitors = new ArrayList<>();

        public MonitorSelector(ArrayList<Monitor> monitors) {
            for(Monitor monitor : monitors) {
                VideoMode mode = monitor.getCurrentVideoMode();
                this.monitors.add(new Monitors(String.format("%sx%s@%s", mode.getWidth(), mode.getHeight(), mode.getRefreshRate()), monitor.getHandle()));
            }
        }
    }
}
