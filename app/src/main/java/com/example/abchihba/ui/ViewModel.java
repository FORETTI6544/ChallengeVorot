package com.example.abchihba.ui;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
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
    private final MutableLiveData<String> rotationType;
    private final MutableLiveData<List<String>> genres;
    private final MutableLiveData<List<Users>> usersList;
    private final MutableLiveData<List<Reviews>> reviewsList;
    public final MutableLiveData<Boolean> rotationStarted;

    public final MutableLiveData<Boolean> nobodySpecifiedGame;
    public final MutableLiveData<Boolean> everybodySpecifiedGame;
    public final MutableLiveData<Boolean> targetUserGameIsEmpty;
    public final MutableLiveData<Boolean> iDontHaveGenre;
    public final MutableLiveData<Boolean> myStatusIsPlaying;

    public ViewModel() {
        //WHEEL===========================================================
        rotationType = new MutableLiveData<>();
        rotationType.setValue("game");
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

        //TAG=============================================================
        tag = new MutableLiveData<>();
        //ROTATION========================================================
        usersList = new MutableLiveData<>();
        getUsers();
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
        setRotationStarted();
        //REVIEWS=========================================================
        reviewsList = new MutableLiveData<>();
        getReviews();
        //================================================================
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
    }
    public void setTag(String tag) {
        this.tag.setValue(tag);
        Log.d("ViewModel", "Tag set: " + tag);

        // Загружаем данные с Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("users").document(tag);

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
                }
                Log.d("ViewModel", "Snapshot data: " + snapshot.getData());
            } else {
                Log.d("ViewModel", "Snapshot is null or does not exist");
            }
        });
    }
    public LiveData<String> getTag() {
        return tag;
    }
    public LiveData<String> getRotationType() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("wheel").document("rotation")
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        return;
                    }
                    if (querySnapshot != null) {
                        rotationType.setValue(querySnapshot.getString("current_rotation"));
                    }
                });
        return rotationType;
    }
    public LiveData<List<String>> getGenres() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        getRotationType();
        Log.d("Genres", "Тип ротации загрузился");
        if ("game".equals(rotationType.getValue())) {
            Log.d("Genres", "запустилось файербэйз");
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
        }
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
    public LiveData<List<Users>> getUsers() {
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

                            Users user = new Users(name, avatar, genre, game, preview, status, tag, to);

                            users.add(user);
                        }
                    }
                    usersList.setValue(users);
                    int usersWithGame = 0;
                    for (Users user : usersList.getValue()) {
                        if (!user.getGame().equals("Игра отсутствует")) {
                            nobodySpecifiedGame.setValue(false);
                            usersWithGame++;
                        }
                    }
                    if (usersWithGame == usersList.getValue().size()) {
                        everybodySpecifiedGame.setValue(true);
                    }
                    for (Users user : usersList.getValue()) {
                        if (Objects.equals(user.getTag(), to.getValue())) {
                            if (user.getGame().equals("Игра отсутствует")) {
                                targetUserGameIsEmpty.setValue(true);
                            }
                        }
                    }
                    if (Objects.equals(genre.getValue(), "Отсутствует")) {
                        iDontHaveGenre.setValue(true);
                    }
                    if (Objects.equals(status.getValue(), "playing")) {
                        myStatusIsPlaying.setValue(true);
                    }
                });
        return usersList;
    }
    public LiveData<String> getTo() {
        return to;
    }
    public LiveData<String> getGame() {
        return game;
    }
    public void setGame(String newgame) {
        String tagValue = tag.getValue();
        if (tagValue == null || tagValue.isEmpty()) {
            Log.e("ViewModel", "Tag is null or empty. Cannot update Firestore document.");
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(tagValue)
                .update("game", newgame);
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
    public void updateRotationStarted(Boolean newstatus) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("rotation").document("rotation_status")
                .update("started", newstatus);
    }
    public void setRotationStarted() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("rotation").document("rotation_status")
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        return;
                    }
                    if (querySnapshot != null) {
                        rotationStarted.setValue(querySnapshot.getBoolean("started"));
                    }
                });
    }
    public LiveData<Boolean> getRotationStarted() {
        return rotationStarted;
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
    public void addReview(String tag, String game, String preview, String review, String rating, String queue, String date) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> reviewMap = new HashMap<>();
        reviewMap.put("tag", tag);
        reviewMap.put("game", game);
        reviewMap.put("preview", preview);
        reviewMap.put("review", review);
        reviewMap.put("rating", rating);
        reviewMap.put("date", date);

        db.collection("reviews").document(queue)
                .set(reviewMap);
    }
}
