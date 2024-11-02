package net.notcoded.wayfix.common.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.Window;
import net.notcoded.wayfix.common.WayFix;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    // broken in forge
    @Shadow @Final private Window window;

    @Shadow @Final public GameOptions options;

    // broken in forge
    @Shadow public abstract boolean forcesUnicodeFont();

    @ModifyArg(method = "onResolutionChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;setScaleFactor(D)V"))
    private double fixHiDPIScaling(double d) {
        int guiScale;
        //? if >=1.19 {
        guiScale = this.options.getGuiScale().getValue();
        //?} elif <1.19 {
        /*guiScale = this.options.guiScale;
        *///?}


        // "Auto" or Gui Scale 0 already auto-scales it

        // this.forcesUnicodeFont() doesn't work on forge & neoforge
        // TODO: this.options.getForceUnicodeFont().getValue();

        return guiScale != 0 && WayFix.config.autoScaleGUI ? window.calculateScaleFactor(Math.round(guiScale * wayFix$getScaleFactor()), this.forcesUnicodeFont()) : d;
    }

    @Unique
    private float wayFix$getScaleFactor() {
        float[] pos = new float[1];
         GLFW.glfwGetWindowContentScale(this.window.getHandle(), pos, pos);

        return pos[0]; // using x or y doesn't matter
    }
}