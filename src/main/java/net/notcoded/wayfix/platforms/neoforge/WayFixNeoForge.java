//? if neoforge {
package net.notcoded.wayfix.platforms.neoforge;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import net.neoforged.fml.loading.FMLConfig;
import net.notcoded.wayfix.config.ModConfig;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
//? if <1.20.6 {
import net.neoforged.neoforge.client.ConfigScreenHandler;

//?} else {
/*import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
*///?}
@Mod("wayfix")
public class WayFixNeoForge {
	public WayFixNeoForge() {
        ModLoadingContext.get().registerExtensionPoint(
                //? if <1.20.6 {
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        ((client, parent) -> AutoConfig.getConfigScreen(ModConfig.class, parent).get())
                )
                //?} else {
                /*IConfigScreenFactory.class,
                () -> (client, parent) -> AutoConfig.getConfigScreen(ModConfig.class, parent).get()
                *///?}
        );
	}

    public static void checkEarlyWindow() {
        if(!FMLConfig.getBoolConfigValue(FMLConfig.ConfigValue.EARLY_WINDOW_CONTROL)) return;
        FMLConfig.updateConfig(FMLConfig.ConfigValue.EARLY_WINDOW_CONTROL, false);

        MinecraftClient.getInstance().getToastManager().add(new SystemToast(SystemToast.Type.WORLD_BACKUP,
                Text.translatable("wayfix.toast.restart-game.title"),
                Text.translatable("wayfix.toast.restart-game.description"))
        );
    }
}
//?}