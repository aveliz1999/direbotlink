package dev.veliz.direbotlink.status;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.veliz.direbotlink.config.Config;
import net.minecraft.server.MinecraftServer;
import org.asynchttpclient.*;

import java.util.TimerTask;

public class StatusTask extends TimerTask {

    private MinecraftServer server;

    public StatusTask(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        JsonArray playerList = new JsonArray();
        server.getPlayerList().getPlayers().forEach(
                serverPlayerEntity -> {
                    JsonObject player = new JsonObject();
                    player.addProperty("minecraftUsername", serverPlayerEntity.getDisplayName().getString());
                    player.addProperty("minecraftUuid", serverPlayerEntity.getStringUUID());
                    playerList.add(player);
                }
        );

        JsonObject obj = new JsonObject();
        obj.addProperty("serverId", Config.serverId);
        obj.add("players", playerList);

        AsyncHttpClient client = Dsl.asyncHttpClient();
        Request request = Dsl
                .post(Config.statusWebhook)
                .setBody(obj.toString())
                .setHeader("Authorization", "Bearer " + Config.apiKey)
                .setHeader("Content-Type", "application/json")
                .build();
        client.executeRequest(request, new AsyncCompletionHandler<Object>() {
            @Override
            public Object onCompleted(Response response) throws Exception {
                return response;
            }
        });
    }
}
