package dev.veliz.direbotlink.status;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.veliz.direbotlink.config.Config;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import org.asynchttpclient.*;

import java.util.TimerTask;

public class PendingTask extends TimerTask {

    private MinecraftServer server;

    public PendingTask(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        AsyncHttpClient client = Dsl.asyncHttpClient();
        Request request = Dsl
                .get(Config.tasksWebhook + "?server=" + Config.serverId)
                .setHeader("Authorization", "Bearer " + Config.apiKey)
                .setHeader("Content-Type", "application/json")
                .build();
        client.executeRequest(request, new AsyncCompletionHandler<Object>() {
            @Override
            public Object onCompleted(Response response) throws Exception {
                JsonParser parser = new JsonParser();
                JsonArray responseBody = parser.parse(response.getResponseBody()).getAsJsonArray();

                for(int i = 0; i < responseBody.size(); i++) {
                    JsonObject task = parser.parse(responseBody.get(i).getAsString()).getAsJsonObject();

                    String type = task.get("type").getAsString();
                    switch(type) {
                        case "whisper": {
                            String target = task.get("target").getAsString();
                            String message = task.get("message").getAsString();
                            ServerPlayerEntity player = server.getPlayerList().getPlayerByName(target);
                            if(player != null) {
                                player.sendMessage(new StringTextComponent(message), player.getUUID());
                            }
                            break;
                        }
                        // TODO add command task
                        default:
                            break;
                    }
                }

                return response;
            }
        });

    }
}
