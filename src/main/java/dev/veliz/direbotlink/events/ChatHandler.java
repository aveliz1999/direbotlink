package dev.veliz.direbotlink.events;

import com.google.gson.JsonObject;
import dev.veliz.direbotlink.config.Config;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import org.asynchttpclient.*;

public class ChatHandler {

    @SubscribeEvent
    public static void chat(ServerChatEvent event) {
        AsyncHttpClient client = Dsl.asyncHttpClient();

        JsonObject obj = new JsonObject();
        obj.addProperty("player", event.getPlayer().getDisplayName().getString());
        obj.addProperty("id", event.getPlayer().getStringUUID());
        obj.addProperty("text", event.getMessage());

        Request request = Dsl
                .post(Config.chatWebhook)
                .setBody(obj.toString())
                .setHeader("Authorization", "Bearer " + Config.apiKey)
                .build();
        client.executeRequest(request, new AsyncCompletionHandler<Object>() {
            @Override
            public Object onCompleted(Response response) throws Exception {
                return response;
            }
        });
    }

}
