package net.notcoded.wayfix.forge;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
//? if >=1.19 {
import net.minecraftforge.client.ConfigScreenHandler;
//?} elif <1.19 {
/*import net.minecraftforge.fml.ExtensionPoint;
 *///?}
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.notcoded.wayfix.common.config.ModConfig;

@Mod(value = "wayfix")
@OnlyIn(Dist.CLIENT)
public class WayFixForge {
    public WayFixForge() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> WayFixForge::setupConfigScreen);
    }

    private static void setupConfigScreen() {
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
    }
}
