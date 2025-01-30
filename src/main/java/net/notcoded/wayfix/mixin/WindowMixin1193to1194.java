package net.notcoded.wayfix.mixin;

import net.minecraft.client.util.Window;
import net.minecraft.resource.InputSupplier;
import net.notcoded.codelib.common.mixinhelper.annotation.MinecraftVersion;
import net.notcoded.wayfix.util.DesktopFileInjector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static net.notcoded.wayfix.WayFix.isWayland;

@MinecraftVersion(minecraft = {"1.19.3", "1.19.4"})
@Mixin(Window.class)
public class WindowMixin1193to1194 {

    // Ignore error
    @Inject(method = "setIcon", at = @At("HEAD"), cancellable = true)
    private void injectIcon(InputSupplier<InputStream> smallIconSupplier, InputSupplier<InputStream> bigIconSupplier, CallbackInfo ci) {
        if (isWayland()) {
            DesktopFileInjector.setIcon(new ArrayList<>(Arrays.asList(smallIconSupplier, bigIconSupplier)));
            ci.cancel();
        }
    }
}
