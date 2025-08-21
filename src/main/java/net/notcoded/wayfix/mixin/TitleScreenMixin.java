package net.notcoded.wayfix.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import net.notcoded.wayfix.WayFix;
import net.notcoded.wayfix.util.DesktopFileInjector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if neoforge {
/*import net.notcoded.wayfix.platforms.neoforge.WayFixNeoForge;
*///?}

//? if forge {
/*import net.notcoded.wayfix.platforms.forge.WayFixForge;
*///?}

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    public boolean firstRun = false;

    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo ci) {
        if (firstRun) return;

        if(DesktopFileInjector.flatpakInjectFailed) {
            MinecraftClient.getInstance().getToastManager().add(new SystemToast(SystemToast.Type.WORLD_BACKUP,
                    Text.of("Failed to inject files"),
                    Text.of("Read the 'Flatpak' instructions in the modrinth page"))
            );

            WayFix.LOGGER.error("Failed to inject files: Read the 'Flatpak' instructions in the modrinth page");
        }


        //? if neoforge {
        /*WayFixNeoForge.checkEarlyWindow();
        *///?}

        //? if forge {
        /*WayFixForge.checkEarlyWindow();
        *///?}

        firstRun = true;
    }
}
