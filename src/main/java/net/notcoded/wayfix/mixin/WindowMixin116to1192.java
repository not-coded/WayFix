package net.notcoded.wayfix.mixin;

import net.minecraft.client.util.Window;
import net.notcoded.codelib.common.mixinhelper.annotation.MinecraftVersion;
import net.notcoded.wayfix.util.DesktopFileInjector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.InputStream;

import static net.notcoded.wayfix.WayFix.isWayland;

@MinecraftVersion(minecraft = {">=1.16", "<=1.19.2"})
@Mixin(Window.class)
public class WindowMixin116to1192 {

    // Ignore error
    @Inject(method = "setIcon", at = @At("HEAD"), cancellable = true)
    private void injectIcon(InputStream icon16, InputStream icon32, CallbackInfo ci) {
        if (isWayland()) {
            DesktopFileInjector.setIcon(icon16, icon32);
            ci.cancel();
        }
    }
}
