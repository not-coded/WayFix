package net.notcoded.wayfix;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import net.notcoded.wayfix.config.ModConfig;
//? if fabric {
/*import net.fabricmc.api.ClientModInitializer;
*///?} elif forge {
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.common.Mod;
//? if >=1.19 {
import net.minecraftforge.client.ConfigScreenHandler;
//?} elif <1.19 {
/*import net.minecraftforge.fml.ExtensionPoint;
*///?}
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
//?}

//? if forge {
@Mod(value = "wayfix")
@OnlyIn(Dist.CLIENT)
//?}
public class WayFix /*? if fabric {*/ /*implements ClientModInitializer *//*?}*/ {
    public static final Logger LOGGER = LogManager.getLogger(WayFix.class);
    public static ModConfig config;

    //? if fabric {
    /*@Override
    public void onInitializeClient() {
        registerConfig();
    }
    *///?}

    //? if forge {
    public WayFix() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(WayFix::onClientSetup);
    }

	private static void onClientSetup(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
            //? if >=1.19 {
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                    new ConfigScreenHandler.ConfigScreenFactory(
                            (client, parent) -> AutoConfig.getConfigScreen(ModConfig.class, parent).get()
                    )
            );
            //?} elif <1.19 {
            /*ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () ->
                    (client, parent) -> AutoConfig.getConfigScreen(ModConfig.class, parent).get()
            );*///?}
        });
	}
	//?}

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

    public static void registerConfig() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        WayFix.config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}