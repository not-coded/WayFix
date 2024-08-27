package net.notcoded.wayfix;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class WayFix implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(WayFix.class);

    @Override
    public void onInitializeClient() {
        // TODO:

        // fix fullscreen in wrong location
        // add config option to let user specify fullscreen location (screen) (default option: primary monitor)
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