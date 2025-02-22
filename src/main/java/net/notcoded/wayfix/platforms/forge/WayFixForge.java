//? if forge {
/*package net.notcoded.wayfix.platforms.forge;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.notcoded.wayfix.config.ModConfig;

//? if >=1.20.1 {
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraftforge.fml.loading.FMLConfig;
//?}
//? if >=1.19 {
import net.minecraftforge.client.ConfigScreenHandler;
//?} elif 1.18.2 {
/^import net.minecraftforge.client.ConfigGuiHandler;
^///?} elif 1.17.1 {
/^import net.minecraftforge.fmlclient.ConfigGuiHandler;
^///?} elif 1.16.5 {
/^import net.minecraftforge.fml.ExtensionPoint;
^///?}

@Mod("wayfix")
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
        //?} elif >1.16.5 <1.19 {
        /^ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () ->
                new ConfigGuiHandler.ConfigGuiFactory((client, parent) -> AutoConfig.getConfigScreen(ModConfig.class, parent).get()
        ));
        ^///?} elif 1.16.5 {
        /^ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () ->
                (client, parent) -> AutoConfig.getConfigScreen(ModConfig.class, parent).get()
        );
        ^///?}
    }


    public static void checkEarlyWindow() {

        //? if >=1.20.1 {
        if(!FMLConfig.getBoolConfigValue(FMLConfig.ConfigValue.EARLY_WINDOW_CONTROL)) return;
        FMLConfig.updateConfig(FMLConfig.ConfigValue.EARLY_WINDOW_CONTROL, false);

        MinecraftClient.getInstance().getToastManager().add(new SystemToast(SystemToast.Type.WORLD_BACKUP,
                Text.translatable("wayfix.toast.restart-game.title"),
                Text.translatable("wayfix.toast.restart-game.description"))
        );
        //?}
    }
}
*///?}