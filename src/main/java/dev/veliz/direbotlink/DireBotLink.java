package dev.veliz.direbotlink;

import dev.veliz.direbotlink.config.Config;
import dev.veliz.direbotlink.events.ChatHandler;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(DireBotLink.MOD_ID)
public class DireBotLink {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "direbotlink";

    public DireBotLink() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);

        MinecraftForge.EVENT_BUS.register(ChatHandler.class);
    }

}
