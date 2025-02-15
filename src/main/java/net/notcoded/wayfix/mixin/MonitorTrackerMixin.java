package net.notcoded.wayfix.mixin;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.client.util.Monitor;
import net.minecraft.client.util.MonitorTracker;
import net.notcoded.wayfix.WayFix;
import net.notcoded.wayfix.config.ModConfig;
import net.notcoded.wayfix.util.WindowHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(MonitorTracker.class)
public class MonitorTrackerMixin {
    @Shadow @Final private Long2ObjectMap<Monitor> pointerToMonitorMap;

    @Inject(method = {"handleMonitorEvent", "<init>"}, at = @At("RETURN"))
    private void handleConfigAdditions(CallbackInfo ci) {
        if(!WindowHelper.canUseWindowHelper) this.wayfix$refreshMonitors();
    }

    @Unique
    private void wayfix$refreshMonitors() {
        ArrayList<Monitor> monitors = new ArrayList<>();
        this.pointerToMonitorMap.forEach((aLong, monitor1) -> monitors.add(monitor1));

        WayFix.config.fullscreen.monitorSelector = new ModConfig.MonitorSelector(monitors);
    }
}
