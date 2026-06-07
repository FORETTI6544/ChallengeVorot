package com.foretti.challengevorot.network;

import android.util.JsonReader;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.foretti.challengevorot.models.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketManager {
    private static final String TAG = "WebSocketManager";
    private static final String wsUrl = "ws://192.168.191.226:3500";
    private static WebSocketManager instance;
    private WebSocket webSocket;
    private final OkHttpClient client;
    private RoomsCallback roomsCallback;
    private boolean isConnected = false;


    public static synchronized WebSocketManager getInstance() {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }
    private WebSocketManager() {
        client = new OkHttpClient();
    }
    public interface ConnectCallback {
        void onConnected() throws JSONException;
        void onError(String error);
    }
    public void connect(String userID, ConnectCallback callback) {
        Request request = new Request.Builder()
                .url(wsUrl)
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                String authMessage = "{\"type\":\"auth\",\"userId\":\"" + userID + "\"}";
                webSocket.send(authMessage);
                if (callback != null) {
                    try {
                        callback.onConnected();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                Log.d("WSMessage", text);
                try {
                    parseAndNotify(text);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                // Ошибка соединения
                Log.e("WS", "Connection failed", t);
            }
        });
    }
    public void parseAndNotify(String text) throws JSONException {
        JSONObject json = new JSONObject(text);
        String type = json.getString("type");
        switch (type) {
            case "rooms_list":
                if (roomsCallback != null) {
                    JSONArray roomsArray = json.getJSONArray("rooms");
                    List<Room> rooms = new ArrayList<>();

                    for (int i = 0; i < roomsArray.length(); i++) {
                        JSONObject roomObj = roomsArray.getJSONObject(i);
                        Room room = new Room();
                        room.name = roomObj.getString("name");
                        room.usersCount = roomObj.getInt("users_count");
                        room.rotationStatus = roomObj.getBoolean("rotation_status");
                        rooms.add(room);

                    }
                    roomsCallback.onRoomsUpdated(rooms);
                }
        }
    }

    public interface RoomsCallback {
        void onRoomsUpdated(List<Room> rooms);
    }
    // Установка callback (не подписка, а установка)
    public void setRoomsCallback(RoomsCallback callback) {
        this.roomsCallback = callback;
    }
    // Удаление callback
    public void clearRoomsCallback() {
        this.roomsCallback = null;
    }


    public void send(String message) {
        if (webSocket != null) {
            Log.d("WSMessage", message);
            webSocket.send(message);
        }
    }
    public void disconnect() {
        if (webSocket != null) {
            webSocket.close(1000, "Normal closure");
            webSocket = null;
        }
        isConnected = false;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
