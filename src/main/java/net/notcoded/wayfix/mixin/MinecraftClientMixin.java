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
    //?}
}