package dev.veliz.direbotlink.status;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.veliz.direbotlink.config.Config;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ChatType;
import net.minecraftforge.common.ForgeHooks;
import org.asynchttpclient.*;

import java.util.TimerTask;
import java.util.UUID;

public class PendingTask extends TimerTask {

    private MinecraftServer server;
    private AsyncHttpClient client;

    public PendingTask(MinecraftServer server) {
        this.server = server;
        this.client = Dsl.asyncHttpClient();
    }

    @Override
    public void run() {
        Request request = Dsl
                .get(Config.tasksWebhook + "?server=" + Config.serverId)
                .setHeader("Authorization", "Bearer " + Config.apiKey)
                .setHeader("Content-Type", "application/json")
                .build();
        this.client.executeRequest(request, new AsyncCompletionHandler<Object>() {
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
                                player.sendMessage(ForgeHooks.newChatWithLinks(message), player.getUUID());
                            }
                            break;
                        }
                        case "command": {
                            String command = task.get("command").getAsString();
                            try{
                                server.getCommands().getDispatcher().execute(command, server.createCommandSourceStack());
                            }
                            catch(Exception e) {
                                if(e instanceof CommandSyntaxException) {
                                }
                                else {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        }
                        case "broadcast": {
                            String message = task.get("message").getAsString();
                            server.getPlayerList().broadcastMessage(ForgeHooks.newChatWithLinks(message), ChatType.SYSTEM, UUID.randomUUID());
                            break;
                        }
                        default:
                            break;
                    }
                }

                return response;
            }
        });

    }
}
