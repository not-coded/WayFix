package net.notcoded.wayfix.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.notcoded.wayfix.WayFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/*
- Credits to wired-tomato
- https://github.com/wired-tomato/WayGL
*/

@Mixin(TextFieldWidget.class)
public class TextFieldWidgetMixin {
    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (WayFix.config.keyModifiersFix && WayFix.isWayland() && isSpecialChar(chr) && Screen.hasControlDown()) cir.setReturnValue(false);
    }

    @Unique
    private boolean isSpecialChar(char chr) {
        return chr == 'a' // CTRL + A (select all)
                || chr == 'v' // CTRL + V (paste)
                || chr == 'c' // CTRL + C (copy)
                || chr == 'x'; // CTRL + X (cut)
    }
}
