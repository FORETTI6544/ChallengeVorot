package com.example.abchihba.ui;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.abchihba.Chat;
import com.example.abchihba.converters.Converter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ViewModel extends androidx.lifecycle.ViewModel {

    private final MutableLiveData<String> name;
    private final MutableLiveData<String> avatar;
    private final MutableLiveData<String> balance;
    private final MutableLiveData<String> genre;
    private final MutableLiveData<String> rerolls;
    private final MutableLiveData<String> allow;
    private final MutableLiveData<String> to;
    private final MutableLiveData<String> game;
    private final MutableLiveData<String> status;
    private final MutableLiveData<String> preview;
    private final MutableLiveData<String> tag;
    private final MutableLiveData<String> room;
    private final MutableLiveData<Boolean> readiness;
    private final MutableLiveData<String> nextRotationType;
    private final MutableLiveData<String> nextGame;
    private final MutableLiveData<String> nextGamePreview;
    private final MutableLiveData<List<String>> genres;
    private final MutableLiveData<List<Users>> allUsersList;
    private final MutableLiveData<List<Users>> roomUsersList;
    private final MutableLiveData<List<Reviews>> reviewsList;
    private final MutableLiveData<List<Rooms>> roomsList;
    public final MutableLiveData<Boolean> rotationStarted;
    public final MutableLiveData<Boolean> nobodySpecifiedGame;
    public final MutableLiveData<Boolean> everybodySpecifiedGame;
    public final MutableLiveData<Boolean> targetUserGameIsEmpty;
    public final MutableLiveData<Boolean> iDontHaveGenre;
    public final MutableLiveData<Boolean> myStatusIsPlaying;
    public final MutableLiveData<List<Chat>> chatList;

    public ViewModel() {
        //WHEEL===========================================================
        genres = new MutableLiveData<>();
        genres.setValue(getGenres().getValue());
        //PROFILE=========================================================
        avatar = new MutableLiveData<>();
        avatar.setValue("0");
        name = new MutableLiveData<>();
        name.setValue("Загрузка");
        balance = new MutableLiveData<>();
        balance.setValue(String.valueOf(1488));
        genre = new MutableLiveData<>();
        genre.setValue("Жанр");
        rerolls = new MutableLiveData<>();
        rerolls.setValue("0");
        allow = new MutableLiveData<>();
        allow.setValue("no");
        to = new MutableLiveData<>();
        game = new MutableLiveData<>();
        game.setValue("Игра отсутствует");
        status = new MutableLiveData<>();
        status.setValue("done");
        preview = new MutableLiveData<>();
        preview.setValue("0");
        room = new MutableLiveData<>();
        room.setValue("0");
        readiness = new MutableLiveData<>();
        readiness.setValue(true);
        //TAG=============================================================
        tag = new MutableLiveData<>();
        //ROTATION========================================================

        nextRotationType = new MutableLiveData<>();
        nextRotationType.setValue("Отсутствует");
        nextGame = new MutableLiveData<>();
        nextGame.setValue("Игра отсутствует");
        nextGamePreview = new MutableLiveData<>();
        nextGamePreview.setValue("0");

        roomUsersList = new MutableLiveData<>();

        nobodySpecifiedGame = new MutableLiveData<>();
        nobodySpecifiedGame.setValue(true);
        everybodySpecifiedGame = new MutableLiveData<>();
        everybodySpecifiedGame.setValue(false);
        targetUserGameIsEmpty = new MutableLiveData<>();
        targetUserGameIsEmpty.setValue(false);
        iDontHaveGenre = new MutableLiveData<>();
        iDontHaveGenre.setValue(false);
        myStatusIsPlaying = new MutableLiveData<>();
        myStatusIsPlaying.setValue(false);
        rotationStarted = new MutableLiveData<>();
        //REVIEWS=========================================================
        reviewsList = new MutableLiveData<>();
        getReviews();
        //================================================================
        allUsersList = new MutableLiveData<>();
        loadAllUsers();
        //ROOMS===========================================================
        roomsList = new MutableLiveData<>();
        getRooms();
        //CHAT============================================================
        chatList = new MutableLiveData<>(new ArrayList<>());
    }


    public LiveData<String> getName() {
        return name;
    }

    public void setName(String newname) {
        String tagValue = tag.getValue();
        if (tagValue == null || tagValue.isEmpty()) {
            Log.e("ViewModel", "Tag is null or empty. Cannot update Firestore document.");
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(tagValue)
                .update("name", newname)
                .addOnSuccessListener(aVoid -> Log.d("ViewModel", "Name updated successfully in Firestore."))
                .addOnFailureListener(e -> Log.e("ViewModel", "Failed to update name in Firestore.", e));
    }

    public void setAvatar(String newavatar) {
        String tagValue = tag.getValue();
        if (tagValue == null || tagValue.isEmpty()) {
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(tagValue)
                .update("avatar", newavatar);
    }

    public LiveData<String> getAvatar() {
        return avatar;
    }

    public LiveData<String> getPreview() {
        return preview;
    }

    public void setPreview(String newpreview) {
        String tagValue = tag.getValue();
        if (tagValue == null || tagValue.isEmpty()) {
            Log.e("ViewModel", "Tag is null or empty. Cannot update Firestore document.");
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(tagValue)
                .update("preview", newpreview);
    }

    public LiveData<String> getBalance() {
        return balance;
    }

    public void setBalance(String newbalance) {
        String tagValue = tag.getValue();
        if (tagValue == null || tagValue.isEmpty()) {
            Log.e("ViewModel", "Tag is null or empty. Cannot update Firestore document.");
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(tagValue)
                .update("balance", newbalance);
    }

    public LiveData<String> getRoom() {
        return room;
    }

    public void setRoom(String newRoom) {
        String tagValue = tag.getValue();
        if (tagValue == null || tagValue.isEmpty()) {
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(tagValue)
                .update("room", newRoom);
        db.collection("rooms")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                List<String> users = (List<String>) document.get("users");
                                users.add(tagValue);
                                db.collection("rooms").document(newRoom)
                                        .update("users", users);
                            }
                        } else {
                        }
                    }
                });
    }

    public void loadUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("users").document(tag.getValue());

        doc.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.e("ViewModel", "Firestore listener error: ", e);
                return;
            }
            if (snapshot != null && snapshot.exists()) {
                if (snapshot.getString("name") != null) {
                    name.setValue(snapshot.getString("name"));
                }
                if (snapshot.getString("balance") != null) {
                    balance.setValue(snapshot.getString("balance"));
                }
                if (snapshot.getString("avatar") != null) {
                    avatar.setValue(snapshot.getString("avatar"));
                }
                if (snapshot.getString("genre") != null) {
                    genre.setValue(snapshot.getString("genre"));
                }
                if (snapshot.getString("rerolls") != null) {
                    rerolls.setValue(snapshot.getString("rerolls"));
                }
                if (snapshot.getString("allow") != null) {
                    allow.setValue(snapshot.getString("allow"));
                }
                if (snapshot.getString("to") != null) {
                    to.setValue(snapshot.getString("to"));
                }
                if (snapshot.getString("game") != null) {
                    game.setValue(snapshot.getString("game"));
                }
                if (snapshot.getString("status") != null) {
                    status.setValue(snapshot.getString("status"));
                }
                if (snapshot.getString("preview") != null) {
                    preview.setValue(snapshot.getString("preview"));
                }
                if (snapshot.getString("room") != null) {
                    room.setValue(snapshot.getString("room"));
                    if (!Objects.equals(room.getValue(), "0")) {
                        loadChat();
                        loadRoomUsers();
                        loadNextRotationType();
                    }
                }
                if (snapshot.get("readiness") != null) {
                    readiness.setValue(snapshot.getBoolean("readiness"));
                }
                Log.d("ViewModel", "Snapshot data: " + snapshot.getData());
            } else {
                Log.d("ViewModel", "Snapshot is null or does not exist");
            }
        });
    }

    public void setTag(String tag) {
        this.tag.setValue(tag);
        Log.d("ViewModel", "Tag set: " + tag);
        loadUser();
    }

    public LiveData<String> getTag() {
        return tag;
    }

    public void loadNextRotationType() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("rooms").document(room.getValue())
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        return;
                    }
                    if (querySnapshot != null) {
                        nextRotationType.setValue(querySnapshot.getString("next_rotation_type"));
                        nextGame.setValue(querySnapshot.getString("next_game"));
                        nextGamePreview.setValue(querySnapshot.getString("next_game_preview"));
                    }
                });
    }

    public LiveData<String> getNextRotationType() {
        return nextRotationType;
    }

    public void setNextRotationType(String type) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("rooms").document(room.getValue())
                .update("next_rotation_type", type);
    }

    public void setNextGame(String game, String preview) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("rooms").document(room.getValue())
                .update("next_game", game);
        db.collection("rooms").document(room.getValue())
                .update("next_game_preview", preview);
    }

    public LiveData<List<String>> getGenres() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("wheel").document("game_genres")
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Log.d("Genres", "e - null");
                        return;
                    }
                    if (querySnapshot != null) {
                        Log.d("Genres", "querySnapshot - !null");
                        Map<String, Object> fields = querySnapshot.getData();
                        int size = fields.size();
                        List<String> genreList = new ArrayList<>();
                        for (int i = 1; i <= size; i++) {
                            String genre = querySnapshot.getString("genre_" + i);
                            if (genre != null) {
                                Log.d("Genres", "genre - !null");
                                genreList.add(genre);
                            }
                        }
                        genres.setValue(genreList);
                        Log.d("Genres", "Жанр добавлен в лист");
                    }
                });
        return genres;
    }

    public LiveData<String> getGenre() {
        return genre;
    }

    public void setGenre(String newgenre) {
        String tagValue = tag.getValue();
        if (tagValue == null || tagValue.isEmpty()) {
            Log.e("ViewModel", "Tag is null or empty. Cannot update Firestore document.");
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(tagValue)
                .update("genre", newgenre)
                .addOnSuccessListener(aVoid -> Log.d("ViewModel", "Name updated successfully in Firestore."))
                .addOnFailureListener(e -> Log.e("ViewModel", "Failed to update name in Firestore.", e));
    }

    public void setRerolls(String newrerolls) {
        String tagValue = tag.getValue();
        if (tagValue == null || tagValue.isEmpty()) {
            Log.e("ViewModel", "Tag is null or empty. Cannot update Firestore document.");
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(tagValue)
                .update("rerolls", newrerolls);
    }

    public LiveData<String> getRerolls() {
        return rerolls;
    }

    public void setAllow(String newallow) {
        String tagValue = tag.getValue();
        if (tagValue == null || tagValue.isEmpty()) {
            Log.e("ViewModel", "Tag is null or empty. Cannot update Firestore document.");
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(tagValue)
                .update("allow", newallow)
                .addOnSuccessListener(aVoid -> Log.d("ViewModel", "Name updated successfully in Firestore."))
                .addOnFailureListener(e -> Log.e("ViewModel", "Failed to update name in Firestore.", e));
    }

    public LiveData<String> getAllow() {
        return allow;
    }

    public void loadAllUsers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        return;
                    }
                    List<Users> users = new ArrayList<>();
                    if (querySnapshot != null) {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String name = document.getString("name");
                            String avatar = document.getString("avatar");
                            String genre = document.getString("genre");
                            String game = document.getString("game");
                            String preview = document.getString("preview");
                            String status = document.getString("status");
                            String tag = document.getId();
                            String to = document.getString("to");
                            Boolean readiness = document.getBoolean("readiness");
                            Long time = document.getLong("started");

                            Users user = new Users(name, avatar, genre, game, preview, status, tag, to, readiness, time);

                            users.add(user);
                        }
                    }
                    allUsersList.setValue(users);
                });

    }
    public LiveData<List<Users>> getAllUsers() {
        return allUsersList;
    }

    public void loadRoomUsers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        return;
                    }
                    List<Users> users = new ArrayList<>();
                    if (querySnapshot != null) {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            if (Objects.equals(document.getString("room"), getRoom().getValue())) {
                                String name = document.getString("name");
                                String avatar = document.getString("avatar");
                                String genre = document.getString("genre");
                                String game = document.getString("game");
                                String preview = document.getString("preview");
                                String status = document.getString("status");
                                String tag = document.getId();
                                String to = document.getString("to");
                                Boolean readiness = document.getBoolean("readiness");
                                Long time = document.getLong("started");

                                Users user = new Users(name, avatar, genre, game, preview, status, tag, to, readiness, time);
                                users.add(user);
                            }
                        }
                    }
                    roomUsersList.setValue(users);
                });
    }
    public LiveData<List<Users>> getRoomUsers() {
        return roomUsersList;
    }
    public LiveData<String> getTo() {
        return to;
    }

    public LiveData<String> getGame() {
        return game;
    }

    public void setGame(String userTag, String game, String preview) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        long time = System.currentTimeMillis() / 1000;
        WriteBatch batch = db.batch();
        batch.update(db.collection("users").document(userTag), "status", "playing");
        batch.update(db.collection("users").document(userTag), "game", game);
        batch.update(db.collection("users").document(userTag), "preview", preview);
        batch.update(db.collection("users").document(userTag), "started", time);
        batch.commit();
    }

    public void setTo(String tag, String newto) {
        if (tag == null || tag.isEmpty()) {
            Log.e("ViewModel", "Tag is null or empty. Cannot update Firestore document.");
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(tag)
                .update("to", newto)
                .addOnSuccessListener(aVoid -> Log.d("ViewModel", "Name updated successfully in Firestore."))
                .addOnFailureListener(e -> Log.e("ViewModel", "Failed to update name in Firestore.", e));
    }

    public void setStatus(String newstatus) {
        String tagValue = tag.getValue();
        if (tagValue == null || tagValue.isEmpty()) {
            Log.e("ViewModel", "Tag is null or empty. Cannot update Firestore document.");
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(tagValue)
                .update("status", newstatus);
    }

    public LiveData<String> getStatus() {
        return status;
    }

    public LiveData<List<Reviews>> getReviews() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reviews")
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        return;
                    }
                    List<Reviews> reviews = new ArrayList<>();

                    if (querySnapshot != null) {
                        int counter = 1;
                        for (QueryDocumentSnapshot i : querySnapshot) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                if (Objects.equals(String.valueOf(counter), document.getId())) {
                                    String tag = document.getString("tag");
                                    String game = document.getString("game");
                                    String review1 = document.getString("review");
                                    String rating = document.getString("rating");
                                    String preview = document.getString("preview");
                                    String date = document.getString("date");

                                    Reviews review = new Reviews(tag, game, review1, rating, preview, date);

                                    reviews.add(review);
                                }
                            }
                            counter += 1;
                        }
                    }

                    reviewsList.setValue(reviews);
                });
        return reviewsList;
    }

    public void doneAndReview(String review, String rating) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        long time = System.currentTimeMillis() / 1000;

        String queue = String.valueOf(reviewsList.getValue().size() + 1);
        Map<String, Object> reviewMap = new HashMap<>();
        reviewMap.put("tag", this.tag.getValue());
        reviewMap.put("game", this.game.getValue());
        reviewMap.put("preview", this.preview.getValue());
        reviewMap.put("review", review);
        reviewMap.put("rating", rating);
        reviewMap.put("date", Converter.dateFormat(time));

        WriteBatch batch = db.batch();
        batch.set(db.collection("reviews").document(queue), reviewMap);
        batch.update(db.collection("users").document(this.tag.getValue()), "status", "done");
        if ("1".equals(this.rerolls.getValue())) {
            batch.update(db.collection("users").document(this.tag.getValue()), "rerolls", "2");
        } else if ("0".equals(this.rerolls.getValue())) {
            batch.update(db.collection("users").document(this.tag.getValue()), "rerolls", "1");
        }
        String newBalance = "0";
        boolean flag = true;
        if (this.roomUsersList.getValue() != null) {
            for (Users user : this.roomUsersList.getValue()) {
                if (Objects.equals(user.getStatus(), "done")){
                    flag = false;
                    break;
                }
            }
        }

        if (this.balance.getValue() != null) {
            if (flag) {
                newBalance = String.valueOf(Integer.parseInt(this.balance.getValue()) + 30);
            } else {
                newBalance = String.valueOf(Integer.parseInt(this.balance.getValue()) + 20);
            }
        }
        batch.update(db.collection("users").document(this.tag.getValue()), "balance", newBalance);
        batch.update(db.collection("users").document(this.tag.getValue()), "started", 0);
        batch.commit();
    }

    public LiveData<List<Rooms>> getRooms() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("rooms")
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        return;
                    }
                    List<Rooms> rooms = new ArrayList<>();

                    if (querySnapshot != null) {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String name = document.getId();
                            String password = document.getString("password");
                            Boolean status = document.getBoolean("rotation_status");
                            rotationStarted.setValue(status);
                            List<String> users = (List<String>) document.get("users");


                            Rooms room = new Rooms(name, password, status, users);
                            rooms.add(room);
                        }
                    }

                    roomsList.setValue(rooms);
                });
        return roomsList;
    }

    public void setRotationStarted(Boolean newStatus) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("rooms").document(room.getValue())
                .update("rotation_status", newStatus);
    }

    public LiveData<Boolean> getRotationStarted() {
        return rotationStarted;
    }

    public void newRotation() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Users> users = getRoomUsers().getValue();
        WriteBatch batch = db.batch();
        for (Users user : users) {
            batch.update(db.collection("users").document(user.getTag()), "game", nextGame.getValue());
            batch.update(db.collection("users").document(user.getTag()), "preview", nextGamePreview.getValue());
            batch.update(db.collection("users").document(user.getTag()), "genre", nextRotationType.getValue());
            batch.update(db.collection("users").document(user.getTag()), "to", "0");
            batch.update(db.collection("users").document(user.getTag()), "readiness", false);
            if (!Objects.equals(nextRotationType.getValue(), "Отсутствует")) {
                batch.update(db.collection("users").document(user.getTag()), "allow", "no");
            } else {
                batch.update(db.collection("users").document(user.getTag()), "allow", "yes");
            }
            if (!Objects.equals(nextGame.getValue(), "Игра отсутствует")) {
                long time = System.currentTimeMillis() / 1000;
                batch.update(db.collection("users").document(user.getTag()), "status", "playing");
                batch.update(db.collection("users").document(user.getTag()), "started", time);
            } else {
                batch.update(db.collection("users").document(user.getTag()), "status", "Новая ротация");
            }
        }
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                List<Localusers> localusers = new ArrayList<>();

                for (int j = 0; j < users.size(); j++) {
                    localusers.add(new Localusers(users.get(j).getTag(), "0"));
                }

                List<Integer> indices = new ArrayList<>();
                for (int i = 0; i < users.size(); i++) {
                    indices.add(i);
                }
                Collections.shuffle(indices);


                for (int i = 0; i < users.size(); i++) {
                    int currentIndex = indices.get(i);
                    int nextIndex = indices.get((i + 1) % users.size());

                    String currentTag = users.get(currentIndex).getTag();
                    String nextTag = users.get(nextIndex).getTag();

                    localusers.get(currentIndex).setTo(nextTag);
                }

                WriteBatch batch2 = db.batch();
                for (Localusers localuser : localusers) {
                    batch2.update(db.collection("users").document(localuser.getTag()), "to", localuser.getTo());
                }
                batch2.update(db.collection("rooms").document(room.getValue()), "rotation_status", true);
                batch2.update(db.collection("rooms").document(room.getValue()), "next_rotation_type", "Отсутствует");
                batch2.update(db.collection("rooms").document(room.getValue()), "next_game", "Игра отсутствует");
                batch2.update(db.collection("rooms").document(room.getValue()), "next_game_preview", "0");
                batch2.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });


    }

    public void leaveRoom() {
        String toMe = "";
        for (Users user : roomUsersList.getValue()) {
            if (Objects.equals(user.getTo(), tag.getValue())) {
                toMe = user.getTag();
                break;
            }
        }
        setTo(toMe, to.getValue());
        if (!Objects.equals(status.getValue(), "done") || !Objects.equals(status.getValue(), "drop")) {
            setStatus("drop");
            setBalance("0");
        }
        setRoom("0");
        setTo(tag.getValue(), "0");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("rooms").document(getRoom().getValue())
                .update("users", FieldValue.arrayRemove(getTag().getValue()));
    }

    public LiveData<List<Chat>> loadChat() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("rooms").document(room.getValue())
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        return;
                    }

                    List<Chat> chat = new ArrayList<>();
                    if (querySnapshot != null) {
                        List<String> rawChat = (List<String>) querySnapshot.get("chat");
                        for (String rawMsg : rawChat) {
                            String tag = rawMsg.substring(0, 20);
                            long time = Long.parseLong(rawMsg.substring(21, 30));
                            String msg = rawMsg.substring(30);
                            chat.add(new Chat(tag, time, msg));
                        }
                        chatList.setValue(chat);
                    }
                });
        return chatList;
    }

    public void sendMessage(String rawMessage) {
        long currentTime = System.currentTimeMillis() / 1000;
        String tagValue = tag.getValue();
        String message = tagValue + currentTime + rawMessage;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("rooms").document(room.getValue());

// Добавление элемента в массив
        docRef.update("chat", FieldValue.arrayUnion(message))
                .addOnSuccessListener(aVoid -> {
                    // Успешно добавлено
                })
                .addOnFailureListener(e -> {
                    // Ошибка
                });
    }

    public LiveData<Boolean> getReadiness() {
        return readiness;
    }

    public void setReadiness() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(tag.getValue())
                .update("readiness", true);
    }
    public void dropGame(String userTag) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();
        batch.update(db.collection("users").document(userTag), "status", "drop");
        batch.update(db.collection("users").document(userTag), "balance", "0");
        batch.update(db.collection("users").document(userTag), "started", 0);
        batch.commit();
    }
}
