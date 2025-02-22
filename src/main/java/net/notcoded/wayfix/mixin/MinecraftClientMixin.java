package net.notcoded.wayfix.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

import net.notcoded.wayfix.WayFix;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import org.spongepowered.asm.mixin.injection.Redirect;

//? if forge || neoforge {
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//?}

//? if forge {
/*import net.notcoded.wayfix.platforms.forge.WayFixForge;

//? if >1.16.5 <1.20.1 {
/^import com.mojang.blaze3d.systems.RenderSystem;
import static net.notcoded.wayfix.WayFix.supportsWayland;
^///?}

//? if >1.16.5 <1.19.2 {
/^import java.util.function.LongSupplier;
^///?}

//? if 1.19.2 || 1.19.3 {
/^import net.minecraft.util.TimeSupplier;
^///?}

*///?}

//? if neoforge {
import net.notcoded.wayfix.platforms.neoforge.WayFixNeoForge;
//?}

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Redirect(method = "onResolutionChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;calculateScaleFactor(IZ)I"))
    private int fixHiDPIScaling(Window instance, int guiScale, boolean forceUnicodeFont) {
        // "Auto" or Gui Scale 0 already auto-scales it
        if (guiScale != 0 && WayFix.config.autoScaleGUI) {
            guiScale = Math.round(guiScale * wayfix$getScaleFactor(instance));
        }

        return instance.calculateScaleFactor(guiScale, forceUnicodeFont);
    }

    //? if forge {

    /*//? if >1.16.5 <1.20.1 {
    /^//? if >1.16.5 <1.19.2 {
    /^¹@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;initBackendSystem()Ljava/util/function/LongSupplier;"))
    private LongSupplier preGLFWInit(){
    ¹^///?} elif 1.19.2 || 1.19.3 {
    /^¹@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;initBackendSystem()Lnet/minecraft/util/TimeSupplier$Nanoseconds;"))
    private TimeSupplier.Nanoseconds preGLFWInit(){
    ¹^///?}
        if (supportsWayland()) {
            GLFW.glfwInitHint(GLFW.GLFW_PLATFORM, GLFW.GLFW_PLATFORM_WAYLAND); // enable wayland backend if supported
        }
        return RenderSystem.initBackendSystem();
    }
    ^///?}

    *///?}

    @Unique
    private float wayfix$getScaleFactor(Window instance) {
        float[] pos = new float[1];
        GLFW.glfwGetWindowContentScale(instance.getHandle(), pos, pos);

        return pos[0]; // using x or y doesn't matter
    }

    //? if forge || neoforge {
    @Inject(method = "<clinit>", at = @At("HEAD"))
    private static void initMod(CallbackInfo ci) {
        WayFix.init();
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void checkEarlyWindow(CallbackInfo ci) {
        //? if neoforge {
        WayFixNeoForge.checkEarlyWindow();
        //?}

        //? if forge {
        /*WayFixForge.checkEarlyWindow();
        *///?}
    }
    //?}
}