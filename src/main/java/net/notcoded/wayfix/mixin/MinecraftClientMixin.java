package net.notcoded.wayfix.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.notcoded.wayfix.WayFix;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Final private Window window;

    @Redirect(method = "onResolutionChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;calculateScaleFactor(IZ)I"))
    private int fixHiDPIScaling(Window instance, int guiScale, boolean forceUnicodeFont) {
        int fixedGuiScale = guiScale;

        // "Auto" or Gui Scale 0 already auto-scales it
        if (guiScale != 0 && WayFix.config.autoScaleGUI) {
            fixedGuiScale = Math.round(guiScale * getScaleFactor(instance));
        }

        return instance.calculateScaleFactor(fixedGuiScale, forceUnicodeFont);
    }

    @Unique
    private float getScaleFactor(Window instance) {
        float[] pos = new float[1];
        GLFW.glfwGetWindowContentScale(instance.getHandle(), pos, pos);

        return pos[0]; // using x or y doesn't matter
    }
}