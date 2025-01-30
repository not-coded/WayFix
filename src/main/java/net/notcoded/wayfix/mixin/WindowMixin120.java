package net.notcoded.wayfix.mixin;

import net.minecraft.client.util.Icons;
import net.minecraft.client.util.Window;
import net.minecraft.resource.ResourcePack;
import net.notcoded.codelib.common.mixinhelper.annotation.MinecraftVersion;
import net.notcoded.wayfix.util.DesktopFileInjector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

import static net.notcoded.wayfix.WayFix.isWayland;

@MinecraftVersion(minecraft = ">=1.20")
@Mixin(Window.class)
public class WindowMixin120 {

    // Ignore error
    @Inject(method = "setIcon", at = @At("HEAD"), cancellable = true)
    private void injectIcon(ResourcePack resourcePack, Icons icons, CallbackInfo ci) {
        if (isWayland()) {
            try {
                DesktopFileInjector.setIcon(icons.getIcons(resourcePack));
            } catch (IOException ignored) { }
            ci.cancel();
        }
    }
}
