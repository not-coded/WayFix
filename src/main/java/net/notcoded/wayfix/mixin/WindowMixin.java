package net.notcoded.wayfix.mixin;

import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.WindowSettings;
import net.minecraft.client.util.Monitor;
import net.minecraft.client.util.MonitorTracker;
import net.minecraft.client.util.Window;
import net.notcoded.wayfix.WayFix;
import net.notcoded.wayfix.config.ModConfig;
import net.notcoded.wayfix.util.DesktopFileInjector;
import net.notcoded.wayfix.util.WindowHelper;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.notcoded.wayfix.WayFix.isWayland;

//? if >=1.20 {

import net.minecraft.client.util.Icons;
import net.minecraft.resource.ResourcePack;
import java.io.IOException;
//?} elif 1.19.3 {
/*import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.resource.InputSupplier;
*///?}

//? if <1.20 {
/*import java.io.InputStream;
*///?}

/*
- Credits to moehreag for most of the code
- https://github.com/moehreag/wayland-fixes
*/

@Mixin(Window.class)
public abstract class WindowMixin {
    @Shadow protected abstract void onWindowPosChanged(long window, int x, int y);

    @Shadow @Final private long handle;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwDefaultWindowHints()V", shift = At.Shift.AFTER, remap = false))
    private void onWindowHints(WindowEventHandler windowEventHandler, MonitorTracker monitorTracker, WindowSettings windowSettings, String string, String string2, CallbackInfo ci) {
        if (isWayland()) {
            GLFW.glfwWindowHint(GLFW.GLFW_FOCUS_ON_SHOW, GLFW.GLFW_FALSE);

            if(WayFix.config.injectIcon) DesktopFileInjector.inject();
            GLFW.glfwWindowHintString(GLFW.GLFW_WAYLAND_APP_ID, DesktopFileInjector.APP_ID);
        }
    }

    @Redirect(method = "updateWindowRegion", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/MonitorTracker;getMonitor(Lnet/minecraft/client/util/Window;)Lnet/minecraft/client/util/Monitor;"))
    private Monitor fixWrongMonitor(MonitorTracker instance, Window window) {
        return WayFix.config.fullscreen.useMonitorName && !WindowHelper.canUseWindowHelper ? getMonitor(instance) : instance.getMonitor(window);
    }

    @Unique
    private Monitor getMonitor(MonitorTracker instance) {
        String monitorName = WayFix.config.fullscreen.monitorName;
        long monitorID = monitorName.trim().isEmpty() ? GLFW.glfwGetPrimaryMonitor() : ModConfig.Monitors.getMonitor(monitorName);

        if(monitorID <= 0 || instance.getMonitor(monitorID) == null) {
            WayFix.LOGGER.warn("Error occurred while trying to set monitor.");
            WayFix.LOGGER.warn("Using primary monitor instead.");
            monitorID = GLFW.glfwGetPrimaryMonitor();
        }

        return instance.getMonitor(monitorID);
    }


    // KDE Plasma ONLY
    @Inject(method = "updateWindowRegion", at = @At("HEAD"))
    private void fixWrongMonitor(CallbackInfo ci) {
        if(!WindowHelper.canUseWindowHelper) return;

        int[] pos = WindowHelper.getWindowPos();
        if(pos == null) return;

        onWindowPosChanged(this.handle, pos[0], pos[1]);
    }
    
    @Inject(method = "setIcon", at = @At("HEAD"), cancellable = true)
    //? if >=1.20 {
    
    private void injectIcon(ResourcePack resourcePack, Icons icons, CallbackInfo ci) {
    //?} elif 1.19.3 {
    /*private void injectIcon(InputSupplier<InputStream> smallIconSupplier, InputSupplier<InputStream> bigIconSupplier, CallbackInfo ci) {
    *///?} elif <1.19.3 {
    
    /*private void injectIcon(InputStream icon16, InputStream icon32, CallbackInfo ci) {
    *///?}
        if (isWayland()) {
            //? if >=1.20 {
            try {
                DesktopFileInjector.setIcon(icons.getIcons(resourcePack));
            } catch (IOException ignored) { }
            //?} elif 1.19.3 {
            /*DesktopFileInjector.setIcon(new ArrayList<>(Arrays.asList(smallIconSupplier, bigIconSupplier)));
            *///?} elif <1.19.3 {
            
            /*DesktopFileInjector.setIcon(icon16, icon32);
            *///?}

            ci.cancel();
        }
    }
}