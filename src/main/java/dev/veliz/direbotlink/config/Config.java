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

    public static int serverId;
    public static String apiKey;
    public static String chatWebhook;
    public static String statusWebhook;


    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == Config.SERVER_SPEC) {
            System.out.println("\n\n\nRELOAD CONFIG\n\n\n");
            bakeConfig();
        }
    }

    public static void bakeConfig() {
        serverId = SERVER.serverId.get();
        apiKey = SERVER.apiKey.get();
        chatWebhook = SERVER.chatWebhook.get();
        statusWebhook = SERVER.statusWebhook.get();
    }

    public static class ServerConfig {

        public final ForgeConfigSpec.ConfigValue<Integer> serverId;
        public final ForgeConfigSpec.ConfigValue<String> apiKey;
        public final ForgeConfigSpec.ConfigValue<String> chatWebhook;
        public final ForgeConfigSpec.ConfigValue<String> statusWebhook;


        public ServerConfig(ForgeConfigSpec.Builder builder) {
            serverId = builder
                    .comment("ID of the server to sync with")
                    .define("serverId", 0);
            apiKey = builder
                    .comment("API Key to authenticate with the endpoint")
                    .define("apiKey", "");

            builder.push("Chat");
            chatWebhook = builder
                    .comment("Endpoint for the chat requests to go to")
                    .define("webhook", "");
            builder.pop();

            builder.push("Status");
            statusWebhook = builder
                    .comment("Endpoint for the status updates to go to")
                    .define("webhook", "");
            builder.pop();
        }
    }

}
