package net.notcoded.wayfix.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.util.Monitor;
import net.minecraft.client.util.VideoMode;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Monitor.class)
public class MonitorMixin {

    @Shadow
    @Final
    private long handle;

    @Shadow
    @Final
    private List<VideoMode> videoModes;

    @Inject(method = "populateVideoModes", at = @At("HEAD"))
    private void test(CallbackInfo ci) {
    }
}
