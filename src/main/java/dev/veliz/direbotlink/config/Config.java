package dev.veliz.direbotlink.config;

import dev.veliz.direbotlink.DireBotLink;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = DireBotLink.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;
    static {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static String chatWebhook;
    public static String apiKey;

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == Config.SERVER_SPEC) {
            System.out.println("\n\n\nRELOAD CONFIG\n\n\n");
            bakeConfig();
        }
    }

    public static void bakeConfig() {
        chatWebhook = SERVER.chatWebhook.get();
        apiKey = SERVER.apiKey.get();
    }

    public static class ServerConfig {

        public final ForgeConfigSpec.ConfigValue<String> chatWebhook;
        public final ForgeConfigSpec.ConfigValue<String> apiKey;

        public ServerConfig(ForgeConfigSpec.Builder builder) {
            chatWebhook = builder
                    .comment("Endpoint for the chat requests to go to")
                    .define("chatWebhook", "");

            apiKey = builder
                    .comment("API key to authenticate with the endpoint")
                    .define("apiKey", "");
        }
    }

}
