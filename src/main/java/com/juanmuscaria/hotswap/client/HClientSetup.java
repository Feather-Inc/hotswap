package com.juanmuscaria.hotswap.client;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class HClientSetup {
    public static void register(IEventBus bus) {
        bus.addListener(HClientSetup::clientSetup);
    }

    private static void clientSetup(FMLClientSetupEvent event) {
    }
}
