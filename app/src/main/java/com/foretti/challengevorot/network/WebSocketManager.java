package com.foretti.challengevorot.network;

import android.util.JsonReader;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.foretti.challengevorot.models.ChatMessage;
import com.foretti.challengevorot.models.ChatUser;
import com.foretti.challengevorot.models.Game;
import com.foretti.challengevorot.models.MarketItem;
import com.foretti.challengevorot.models.Review;
import com.foretti.challengevorot.models.Room;
import com.foretti.challengevorot.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
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
    private static final String wsUrl = "ws://185.254.158.107:8888";
    private static WebSocketManager instance;
    private WebSocket webSocket;
    private final OkHttpClient client;
    private ConnectCallback connectCallback;
    private RoomsCallback roomsCallback;
    private UserCallback userCallback;
    private UsersListCallback usersListCallback;
    private RotationStatusCallback rotationStatusCallback;
    private GamesCallback gamesCallback;
    private GenresCallback genresCallback;
    private SpinningResultCallback spinningResultCallback;
    private ReviewsCallback reviewsCallback;
    private MarketCallback marketCallback;
    private ChatCallback chatCallback;
    private NewMessageCallback newMessageCallback;
    private boolean isConnected = false;
    private boolean isAuthenticated = false;


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
        this.connectCallback = callback;
        this.isAuthenticated = false;

        Request request = new Request.Builder()
                .url(wsUrl)
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                String authMessage = "{\"type\":\"auth\",\"user_id\":\"" + userID + "\"}";
                webSocket.send(authMessage);
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                Log.d("WSMessage", text);
                try {
                    JSONObject json = new JSONObject(text);
                    String type = json.getString("type");

                    // ✅ ИСПРАВЛЕННАЯ ПРОВЕРКА
                    String status = json.optString("status");
                    if ("auth".equals(type) && "auth_success".equals(status)) {
                        isAuthenticated = true;
                        Log.d(TAG, "✅ Authentication successful!");
                        if (connectCallback != null) {
                            connectCallback.onConnected();
                        }
                        return;
                    }

                    // Если есть ошибка
                    if (json.has("error")) {
                        String error = json.getString("error");
                        if (connectCallback != null) {
                            connectCallback.onError(error);
                        }
                        return;
                    }

                    // Только если аутентифицированы - обрабатываем сообщения
                    if (isAuthenticated) {
                        parseAndNotify(text);
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "JSON parsing error", e);
                }
            }

            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                Log.d(TAG, "WebSocket closed: " + reason);
                isAuthenticated = false;
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                Log.e("WS", "Connection failed", t);
                isAuthenticated = false;
                if (connectCallback != null) {
                    connectCallback.onError(t.getMessage());
                }
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
                            json.getString("user_id"),
                            json.getString("username"),
                            json.getString("avatar"),
                            json.getInt("balance"),
                            json.getString("ask_to"),
                            json.getBoolean("readiness"),
                            json.getString("genre"),
                            json.getString("game"),
                            json.getString("game_cover"),
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
                        user.name = userObj.getString("username");
                        user.avatar = userObj.optString("avatar", "");
                        user.genre = userObj.optString("genre", "");
                        user.game = userObj.optString("game", "");
                        user.gameStatus = userObj.optString("game_status", "");
                        user.id = userObj.getString("user_id");
                        user.askTo = userObj.optString("ask_to", "");
                        user.readiness = userObj.optBoolean("readiness", false);
                        users.add(user);
                    }
                    usersListCallback.onUsersUpdated(users);
                }
                break;
            case "room_update":
                Boolean roomData = json.getBoolean("rotation_status");


                rotationStatusCallback.onRotationStatusUpdated(roomData);
                break;
            case "game_search_result":
                if (gamesCallback != null) {
                    JSONArray gamesArray = json.getJSONArray("games");
                    List<Game> games = new ArrayList<>();
                    for (int i = 0; i < gamesArray.length(); i++) {
                        JSONObject gameObj = gamesArray.getJSONObject(i);
                        Game game = new Game();
                        game.game_name = gameObj.getString("name");
                        game.appid = gameObj.getString("app_id");
                        game.preview_image = gameObj.optString("preview_image", "");
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
                        genresList.add(genre.getString("genre"));
                    }
                    genresCallback.onGenresRecieved(genresList);
                }
                break;
            case "spinning_result":
                if (spinningResultCallback != null) {
                    String genre = json.getString("genre");
                    spinningResultCallback.onSpinningResult(genre);
                }
                break;
            case "reviews_list":
                if (reviewsCallback != null) {
                    JSONArray reviewsArray = json.getJSONArray("reviews");
                    List<Review> reviews = new ArrayList<>();
                    for (int i = 0; i < reviewsArray.length(); i++) {
                        try {
                            JSONObject reviewObj = reviewsArray.getJSONObject(i);
                            Review review = new Review();
                            review.userName = reviewObj.getString("user_name");
                            review.userAvatar = reviewObj.getString("user_avatar");
                            review.gameName = reviewObj.getString("game_name");
                            review.gamePreview = reviewObj.optString("game_preview", "");
                            review.rating = reviewObj.getInt("rating");
                            review.text = reviewObj.getString("review_text");
                            reviews.add(review);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    reviewsCallback.onReviewsReceived(reviews);
                }
                break;
            case "market_list":
                if (marketCallback != null) {
                    JSONArray itemsArray = json.getJSONArray("items");
                    List<MarketItem> items = new ArrayList<>();
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemObj = itemsArray.getJSONObject(i);
                        MarketItem item = new MarketItem();
                        item.name = itemObj.getString("name");
                        item.description = itemObj.optString("desc", "");
                        item.price = itemObj.getInt("price");

                        items.add(item);
                    }
                    marketCallback.onMarketReceived(items);
                }
                break;
            case "chat_history":
                if (chatCallback != null) {
                    JSONArray usersArray = json.getJSONArray("users");
                    List<ChatUser> users = new ArrayList<>();
                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject userObj = usersArray.getJSONObject(i);
                        ChatUser user = new ChatUser();
                        user.id = userObj.getString("user_id");
                        user.name = userObj.getString("username");
                        user.avatar = userObj.optString("avatar", "");
                        users.add(user);
                    }

                    JSONArray messagesArray = json.getJSONArray("messages");
                    List<ChatMessage> messages = new ArrayList<>();
                    for (int i = 0; i < messagesArray.length(); i++) {
                        JSONObject msgObj = messagesArray.getJSONObject(i);
                        ChatMessage message = new ChatMessage();
                        message.id = msgObj.getLong("id");
                        message.userId = msgObj.optString("user_id");
                        message.type = msgObj.optString("type");
                        message.content = msgObj.optString("content");
                        message.attachment_base64 = msgObj.optString("attachment_base64");
                        message.created_at = Instant.parse(msgObj.optString("created_at"));
                        messages.add(message);
                    }
                    chatCallback.onChatReceived(messages, users);
                }
                break;
            case "new_message":
                if (newMessageCallback != null) {
                    JSONObject msgObj = json.getJSONObject("message");
                    ChatMessage message = new ChatMessage();
                    message.id = msgObj.getLong("id");
                    message.userId = msgObj.optString("user_id");
                    message.type = msgObj.optString("type");
                    message.content = msgObj.optString("content");
                    message.attachment_base64 = msgObj.optString("attachment_base64");
                    message.created_at = Instant.parse(msgObj.optString("created_at"));
                    newMessageCallback.onNewMessageRecieved(message);
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
        void onUserUpdated(String userID, String name, String avatar, Integer balance, String askto, Boolean readiness,
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

    public interface ReviewsCallback {
        void onReviewsReceived(List<Review> reviews);
    }

    public void setReviewsCallback(ReviewsCallback callback) {
        this.reviewsCallback = callback;
    }

    public void clearReviewsCallback() {
        this.reviewsCallback = null;
    }

    public interface MarketCallback {
        void onMarketReceived(List<MarketItem> items);
    }

    public void setMarketCallback(MarketCallback callback) {
        this.marketCallback = callback;
    }

    public void clearMarketCallback() {
        this.marketCallback = null;
    }

    public interface ChatCallback {
        void onChatReceived(List<ChatMessage> messages, List<ChatUser> users);
    }
    public void setChatCallback(ChatCallback callback) {
        this.chatCallback = callback;
    }
    public void clearChatCallback() {
        this.chatCallback = null;
    }
    public interface NewMessageCallback {
        void onNewMessageRecieved(ChatMessage message);
    }
    public void setNewMessageCallback(NewMessageCallback callback) {
        this.newMessageCallback = callback;
    }
    public void clearNewMessageCallback() {
        this.newMessageCallback = null;
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
