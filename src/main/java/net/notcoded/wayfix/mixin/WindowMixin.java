package net.notcoded.wayfix.mixin;

import net.minecraft.client.util.Monitor;
import net.minecraft.client.util.MonitorTracker;
import net.minecraft.client.util.Window;
import net.notcoded.wayfix.WayFix;
import net.notcoded.wayfix.config.ModClothConfig;
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
import java.util.ArrayList;
import java.util.Collections;

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
    // forge only allows injecting into constructors on return, this is a jank fix, but it works
    //? if forge || (neoforge && 1.20.4) {
    /*@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwDefaultWindowHints()V", remap = false))
    *///?} else {
    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwDefaultWindowHints()V", shift = At.Shift.AFTER, remap = false))
    private void onWindowHints(CallbackInfo ci) {
    //?}
    //? if forge || (neoforge && 1.20.4) {
    /*private void onWindowHints() {
        GLFW.glfwDefaultWindowHints();
    *///?}
        if (isWayland()) {
            GLFW.glfwWindowHint(GLFW.GLFW_FOCUS_ON_SHOW, GLFW.GLFW_FALSE);

            if(!WayFix.config.injectIcon) return;
            DesktopFileInjector.inject();
            GLFW.glfwWindowHintString(GLFW.GLFW_WAYLAND_APP_ID, DesktopFileInjector.APP_ID);
        }
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