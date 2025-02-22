package net.notcoded.wayfix.mixin;

import com.mojang.blaze3d.platform.GLX;
import org.spongepowered.asm.mixin.Mixin;
//? if ((forge || neoforge) && (1.16.5 || >=1.20.1)) || fabric {
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.LongSupplier;

import static net.notcoded.wayfix.WayFix.supportsWayland;
//?}

/*
- Credits to moehreag
- https://github.com/moehreag/wayland-fixes
*/

@Mixin(GLX.class)
public abstract class GLXMixin {
    //? if ((forge || neoforge) && (1.16.5 || >=1.20.1)) || fabric {

    @Inject(method = "_initGlfw", at = @At("HEAD"), remap = false)
    private static void preGLFWInit(CallbackInfoReturnable<LongSupplier> cir) {
        if (supportsWayland()) {
            GLFW.glfwInitHint(GLFW.GLFW_PLATFORM, GLFW.GLFW_PLATFORM_WAYLAND); // enable wayland backend if supported
        }
    }
    //?}
}