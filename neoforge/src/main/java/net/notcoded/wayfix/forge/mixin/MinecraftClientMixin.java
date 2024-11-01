package net.notcoded.wayfix.forge.mixin;

import net.minecraft.client.MinecraftClient;
import net.notcoded.wayfix.common.WayFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    // there's probably a better way to do this, but atm I don't care
    @Inject(method = "<clinit>", at = @At("HEAD"))
    private static void initConfig(CallbackInfo ci) {
        WayFix.init();
    }
}
