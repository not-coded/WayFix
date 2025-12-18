package net.notcoded.wayfix.mixin;

import io.netty.util.internal.PlatformDependent;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import net.notcoded.wayfix.WayFix;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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

    @Unique
    private static final int LEFT_CTRL = PlatformDependent.isOsx() ? GLFW.GLFW_KEY_LEFT_SUPER : GLFW.GLFW_KEY_LEFT_CONTROL;
    @Unique
    private static final int RIGHT_CTRL = PlatformDependent.isOsx() ? GLFW.GLFW_KEY_RIGHT_SUPER : GLFW.GLFW_KEY_RIGHT_CONTROL;

    @Dynamic
    @Inject(method = {"onChar", "method_1457"}, at = @At("HEAD"), cancellable = true, require = 1)
    private void charTyped(CallbackInfo ci) {
        if(!WayFix.config.keyModifiersFix || !WayFix.isWayland()) return;

        long window = MinecraftClient.getInstance().getWindow().getHandle();
        if(GLFW.glfwGetKey(window, LEFT_CTRL) == GLFW.GLFW_PRESS ||
                GLFW.glfwGetKey(window, RIGHT_CTRL) == GLFW.GLFW_PRESS ||
                GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS
        ) {
            ci.cancel();
        }
    }
}