package net.notcoded.wayfix.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.notcoded.wayfix.common.WayFix;

public class WayFixFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WayFix.init();
    }
}
