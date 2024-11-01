package net.notcoded.wayfix.forge;

import me.shedaniel.autoconfig.AutoConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.notcoded.wayfix.common.config.ModConfig;

@Mod(value = "wayfix")
@OnlyIn(Dist.CLIENT)
public class WayFixNeoForge {
    public WayFixNeoForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(WayFixNeoForge::onClientSetup);
    }

    private static void onClientSetup(final FMLClientSetupEvent event) {
        // TODO: add config support for >1.20.6 (IConfigScreenFactory)
        event.enqueueWork(() -> {
            ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () ->
                    (
                            (client, parent) -> AutoConfig.getConfigScreen(ModConfig.class, parent).get()
                    )
            );
        });
    }
}
