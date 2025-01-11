package net.notcoded.wayfix;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.notcoded.wayfix.config.ModConfig;
import net.notcoded.wayfix.util.WindowHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class WayFix implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(WayFix.class);
    public static ModConfig config;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        WayFix.config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        WindowHelper.checkIfCanUseWindowHelper();
        if(WindowHelper.canUseWindowHelper) {
            WayFix.config.fullscreen.monitorName = "";
            WayFix.config.fullscreen.useMonitorName = false;
            WayFix.config.fullscreen.monitorSelector.monitors.clear();
            WayFix.config.fullscreen.monitorSelector.monitors.add(new ModConfig.Monitors("You do not need to use this as it is automatically fixed.", 0)); // most users probably won't understand this, but I'll include it anyway
        }
    }

    public static boolean isWayland() {
        try {
            return GLFW.glfwGetPlatform() == GLFW.GLFW_PLATFORM_WAYLAND;
        } catch (NoSuchMethodError ignored) { // <3.3.0
            return false;
        }
    }

    public static boolean supportsWayland() {
        try {
            return GLFW.glfwPlatformSupported(GLFW.GLFW_PLATFORM_WAYLAND);
        } catch (NoSuchMethodError ignored) { // <3.3.0
            LOGGER.warn("WayFix is disabling itself due to the LWJGL Version being too low.");
            LOGGER.warn("Please update to a LWJGL version such as '3.3.1' or higher.");
            return false;
        }
    }
}