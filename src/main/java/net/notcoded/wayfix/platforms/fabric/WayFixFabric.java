//? if fabric {
package net.notcoded.wayfix.platforms.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.notcoded.wayfix.WayFix;
import net.notcoded.wayfix.platforms.ModPlatform;

public class WayFixFabric implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		WayFix.init(new FabricPlatform());
	}

    public static class FabricPlatform implements ModPlatform {

        @Override
        public String getModLoader() {
            return "Fabric";
        }

        @Override
        public boolean isModLoaded(String modId) {
            return FabricLoader.getInstance().isModLoaded(modId);
        }

        @Override
        public boolean isDevelopmentEnvironment() {
            return FabricLoader.getInstance().isDevelopmentEnvironment();
        }
    }
}
//?}