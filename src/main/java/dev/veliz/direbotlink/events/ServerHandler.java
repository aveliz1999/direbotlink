package dev.veliz.direbotlink.events;

import dev.veliz.direbotlink.status.StatusTask;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

import java.util.Timer;

public class ServerHandler {

    @SubscribeEvent
    public static void onServerStarted(FMLServerStartedEvent e) {
        new Timer().scheduleAtFixedRate(new StatusTask(e.getServer()), 0, 15000);
    }

}
