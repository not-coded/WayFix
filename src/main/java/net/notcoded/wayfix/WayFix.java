package net.notcoded.wayfix;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.notcoded.wayfix.config.ModClothConfig;
import net.notcoded.wayfix.config.ModConfig;
import net.notcoded.wayfix.platforms.ModPlatform;
import net.notcoded.wayfix.util.WindowHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class WayFix {
    public static final Logger LOGGER = LogManager.getLogger(WayFix.class);
    public static ModConfig config;
    public static ModPlatform platform;

    public static void init(ModPlatform platform) {
        AutoConfig.register(ModClothConfig.class, JanksonConfigSerializer::new);
        WayFix.config = AutoConfig.getConfigHolder(ModClothConfig.class).getConfig();
        WayFix.platform = platform;

        if(platform.isDevelopmentEnvironment()) return;
        WindowHelper.checkIfCanUseWindowHelper();
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