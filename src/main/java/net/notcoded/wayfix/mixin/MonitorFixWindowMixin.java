package net.notcoded.wayfix.mixin;

import net.minecraft.client.util.Monitor;
import net.minecraft.client.util.MonitorTracker;
import net.minecraft.client.util.Window;
import net.notcoded.wayfix.WayFix;
import net.notcoded.wayfix.config.ModClothConfig;
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

@Mixin(Window.class)
public abstract class MonitorFixWindowMixin {

    @Shadow protected abstract void onWindowPosChanged(long window, int x, int y);

    @Shadow @Final private long handle;

    @Redirect(method = "updateWindowRegion", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/MonitorTracker;getMonitor(Lnet/minecraft/client/util/Window;)Lnet/minecraft/client/util/Monitor;"))
    private Monitor fixWrongMonitor(MonitorTracker instance, Window window) {
        return WindowHelper.canUseWindowHelper ? instance.getMonitor(window) : wayfix$getMonitor(instance);
    }

    @Unique
    private Monitor wayfix$getMonitor(MonitorTracker instance) {
        String monitorName = WayFix.config.monitorName;
        long monitorID = GLFW.glfwGetPrimaryMonitor();
        if(!monitorName.trim().isEmpty()) {
            monitorID = ModClothConfig.monitors.getOrDefault(monitorName, 0L);
            if(monitorID == 0L &&
                    monitorName.toLowerCase().startsWith("dp-") &&
                    Character.isDigit(monitorName.charAt(monitorName.length() - 1))
            ) {
                ArrayList<Long> values = new ArrayList<>(ModClothConfig.monitors.values());
                values.sort(Collections.reverseOrder());

                try {
                    monitorID = values.get(Integer.parseInt(monitorName.substring(monitorName.length() - 1)) - 1);
                } catch (Exception ignored) { }
            }
        }



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
}
