package net.notcoded.wayfix;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import net.notcoded.wayfix.config.ModConfig;
//? if fabric {
import net.fabricmc.api.ClientModInitializer;
//?} elif forge {
/*
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.ConfigScreenHandler;
*///?} elif neoforge {
/*
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
*///?}

//? if forge || neoforge
//@Mod(value = "wayfix"/*? if neoforge {*/, dist = Dist.CLIENT/*?}*/)
public class WayFix /*? if fabric {*/ implements ClientModInitializer /*?}*/ {
    public static final Logger LOGGER = LogManager.getLogger(WayFix.class);
    public static ModConfig config;

    //? if fabric {
    @Override
    public void onInitializeClient() {
        registerConfig();
    }
    //?}

    //? if forge || neoforge {
    /*public WayFix() {
        registerConfig();

        //? if neoforge
        ////ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (client, parent) -> AutoConfig.getConfigScreen(ModConfig.class, parent).get());
        //? if forge
		////ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
		//    new ConfigScreenHandler.ConfigScreenFactory(
		//        (mc, screen) -> AutoConfig.getConfigScreen(ModConfig.class, parent).get()
		//    )
		//);
    }
	*///?}

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

    private void registerConfig() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        WayFix.config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}