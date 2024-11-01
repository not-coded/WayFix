package net.notcoded.wayfix.forge;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
//? if >=1.19 {
import net.minecraftforge.client.ConfigScreenHandler;
//?} elif <1.19 {
/*import net.minecraftforge.fml.ExtensionPoint;
 *///?}
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.notcoded.wayfix.common.config.ModConfig;

@Mod(value = "wayfix")
@OnlyIn(Dist.CLIENT)
public class WayFixForge {
    public WayFixForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(WayFixForge::onClientSetup);
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
}
