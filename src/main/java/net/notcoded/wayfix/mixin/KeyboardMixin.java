package net.notcoded.wayfix.mixin;

import net.minecraft.client.Keyboard;

//? if >=1.21.9 {
/*import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.SystemKeycodes;
*///?} else if <1.21.9 {
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
//?}
import org.lwjgl.glfw.GLFW;
import net.notcoded.wayfix.WayFix;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
- Credits to ishland
- https://github.com/ishland/fix-keyboard-on-linux
- Licensed under MIT
*/

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Inject(method = "onChar", at = @At("HEAD"), cancellable = true)
    //? if <1.21.9 {
    private void charTyped(long window, int codePoint, int modifiers, CallbackInfo ci) {
    //?}
    //? if >=1.21.9 {
    /*private void charTyped(long window, CharInput input, CallbackInfo ci) {
    *///?}
        if(!WayFix.config.keyModifiersFix || !WayFix.isWayland()) return;

        //? if >=1.21.9 {
        /*if(GLFW.glfwGetKey(window, SystemKeycodes.LEFT_CTRL) == GLFW.GLFW_PRESS ||
                GLFW.glfwGetKey(window, SystemKeycodes.RIGHT_CTRL) == GLFW.GLFW_PRESS ||
                GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS
        *///?}
        //? if <1.21.9 {
        if(
                Screen.hasControlDown() ||
                        InputUtil.isKeyPressed(window, GLFW.GLFW_KEY_LEFT_ALT)
        //?}
        ) {
            ci.cancel();
        }
    }
}