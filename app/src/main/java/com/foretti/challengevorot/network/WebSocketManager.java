package com.foretti.challengevorot.network;

import android.util.JsonReader;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.foretti.challengevorot.models.Game;
import com.foretti.challengevorot.models.Room;
import com.foretti.challengevorot.models.User;

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
    private UserCallback userCallback;
    private UsersListCallback usersListCallback;
    private RotationStatusCallback rotationStatusCallback;
    private GamesCallback gamesCallback;
    private GenresCallback genresCallback;
    private SpinningResultCallback spinningResultCallback;
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
                break;
            case "user_update":
                if (userCallback != null) {
                    userCallback.onUserUpdated(
                            json.getString("name"),
                            json.getString("avatar"),
                            json.getInt("balance"),
                            json.getString("ask_to"),
                            json.getBoolean("readiness"),
                            json.getString("genre"),
                            json.getString("game"),
                            json.getString("game_preview"),
                            json.getString("game_status"),
                            json.getInt("game_started_date"),
                            json.getBoolean("allow_wheel_spinning"),
                            json.getInt("rerolls_count"));
                }
                break;
            case "users_list":
                if (usersListCallback != null) {
                    JSONArray usersArray = json.getJSONArray("users");
                    List<User> users = new ArrayList<>();
                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject userObj = usersArray.getJSONObject(i);
                        User user = new User();
                        user.name = userObj.getString("name");
                        user.avatar = userObj.optString("avatar", "");
                        user.genre = userObj.optString("genre", "");
                        user.game = userObj.optString("game", "");
                        user.gameStatus = userObj.optString("game_status", "");
                        user.id = userObj.getString("id");
                        user.askTo = userObj.optString("ask_to", "");
                        user.readiness = userObj.optBoolean("readiness", false);
                        users.add(user);
                    }
                    usersListCallback.onUsersUpdated(users);
                }
                break;
            case "rotation_status_update":
                rotationStatusCallback.onRotationStatusUpdated(json.getBoolean("rotation_status"));
                break;
            case "game_search":
                if (gamesCallback != null) {
                    JSONArray gamesArray = json.getJSONArray("games");
                    List<Game> games = new ArrayList<>();
                    for (int i = 0; i < gamesArray.length(); i++) {
                        JSONObject gameObj = gamesArray.getJSONObject(i);
                        Game game = new Game();
                        game.game_name = gameObj.getString("game_name");
                        game.appid = gameObj.getString("appid");
                        games.add(game);
                    }
                    gamesCallback.onGamesFound(games);
                }
                break;
            case "genres_list":
                if (genresCallback != null) {
                    JSONArray genresArray = json.getJSONArray("genres");
                    List<String> genresList = new ArrayList<>();
                    for (int i = 0; i < genresArray.length(); i++) {
                        JSONObject genre = genresArray.getJSONObject(i);
                        genresList.add(genre.getString("name"));
                    }
                    genresCallback.onGenresRecieved(genresList);
                }
                break;
            case "spinning_result":
                if (spinningResultCallback != null) {
                    JSONObject result = json.getJSONObject("genre");
                    String genre = result.getString("name");
                    spinningResultCallback.onSpinningResult(genre);
                }
                break;
        }
    }
    public interface GenresCallback {
        void onGenresRecieved(List<String> genres);
    }
    public void setGenresCallback(GenresCallback callback) {
        this.genresCallback = callback;
    }
    public void clearGenresCallback() {
        this.genresCallback = null;
    }
    public interface GamesCallback {
        void onGamesFound(List<Game> games);
    }
    public void setGamesCallback(GamesCallback callback) {
        this.gamesCallback = callback;
    }
    public void clearGamesCallback() {
        this.gamesCallback = null;
    }
    public interface RotationStatusCallback {
        void onRotationStatusUpdated(Boolean rotationStatus);
    }
    public void setRotationStatusCallback(RotationStatusCallback callback) {
        this.rotationStatusCallback = callback;
    }
    public void clearRotationStatusCallback() {
        this.rotationStatusCallback = null;
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

    public interface UserCallback {
        void onUserUpdated(String name, String avatar, Integer balance, String askto, Boolean readiness,
                           String genre, String game, String gamepreview, String gamestatus,
                           Integer gamestarteddate, Boolean allowwheelspinning, Integer rerollscount);
    }
    public void setUserCallback(UserCallback callback) {
        this.userCallback = callback;
    }
    public void clearUserCallback() {
        this.userCallback = null;
    }

    public interface UsersListCallback {
        void onUsersUpdated(List<User> users);
    }


    public void setUsersListCallback(UsersListCallback callback) {
        this.usersListCallback = callback;
    }
    public void clearUsersListCallback() {
        this.usersListCallback = null;
    }

    public interface SpinningResultCallback {
        void onSpinningResult(String genre);
    }

    public void setSpinningResultCallback(SpinningResultCallback callback) {
        this.spinningResultCallback = callback;
    }

    public void clearSpinningResultCallback() {
        this.spinningResultCallback = null;
    }

    public void send(String message) {
        if (webSocket != null) {
            Log.d("WSMessageSend", message);
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
