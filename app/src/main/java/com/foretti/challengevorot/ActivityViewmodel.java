package com.foretti.challengevorot;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ActivityViewmodel extends androidx.lifecycle.ViewModel {
    MutableLiveData<String> userID;
    MutableLiveData<String> username;
    MutableLiveData<String> avatar;
    MutableLiveData<Integer> balance;
    MutableLiveData<String> askTo;
    MutableLiveData<Boolean> readiness;
    MutableLiveData<String> genre;
    MutableLiveData<String> game;
    MutableLiveData<String> gamePreview;
    MutableLiveData<String> gameStatus;
    MutableLiveData<Integer> gameStartedDate;
    MutableLiveData<Boolean> allowWheelSpinning;
    MutableLiveData<Integer> rerollsCount;





    public ActivityViewmodel() {
        userID = new MutableLiveData<>();
        userID.setValue("");
        username = new MutableLiveData<>();
        username.setValue("");
        avatar = new MutableLiveData<>();
        avatar.setValue("");
        balance = new MutableLiveData<>();
        balance.setValue(0);
        askTo = new MutableLiveData<>();
        askTo.setValue("");
        readiness = new MutableLiveData<>();
        readiness.setValue(false);
        genre = new MutableLiveData<>();
        genre.setValue("");
        game = new MutableLiveData<>();
        game.setValue("");
        gamePreview = new MutableLiveData<>();
        gamePreview.setValue("0");
        gameStatus = new MutableLiveData<>();
        gameStatus.setValue("");
        gameStartedDate = new MutableLiveData<>();
        gameStartedDate.setValue(0);
        allowWheelSpinning = new MutableLiveData<>();
        allowWheelSpinning.setValue(false);
        rerollsCount = new MutableLiveData<>();
        rerollsCount.setValue(0);

    }

    public LiveData<String> getUserID() {
        return userID;
    }

    public LiveData<String> getUsername() {
        return username;
    }
    public LiveData<String> getAvatar() {
        return avatar;
    }
    public LiveData<Integer> getBalance() {
        return balance;
    }
    public LiveData<String> getAskTo() {
        return askTo;
    }
    public LiveData<Boolean> getReadiness() {
        return readiness;
    }
    public LiveData<String> getGenre() {
        return genre;
    }
    public LiveData<String> getGame() {
        return game;
    }
    public MutableLiveData<String> getGamePreview() {
        return gamePreview;
    }
    public LiveData<String> getGameStatus() {
        return gameStatus;
    }
    public LiveData<Integer> getGameStartedDate() {
        return gameStartedDate;
    }
    public LiveData<Boolean> getAllowWheelSpinning() {
        return allowWheelSpinning;
    }

    public LiveData<Integer> getRerollsCount() {
        return rerollsCount;
    }

    public void setAvatar(MutableLiveData<String> avatar) {
        this.avatar = avatar;
    }
    public void setUser(String userID, String name, String avatar, Integer balance, String askto, Boolean readiness,
                        String genre, String game, String gamepreview, String gamestatus,
                        Integer gamestarteddate, Boolean allowwheelspinning, Integer rerollscount) {
        this.userID.postValue(userID);
        this.username.postValue(name);
        this.avatar.postValue(avatar);
        this.balance.postValue(balance);
        this.askTo.postValue(askto);
        this.readiness.postValue(readiness);
        this.genre.postValue(genre);
        this.game.postValue(game);
        this.gamePreview.postValue(gamepreview);
        this.gameStatus.postValue(gamestatus);
        this.gameStartedDate.postValue(gamestarteddate);
        this.allowWheelSpinning.postValue(allowwheelspinning);
        this.rerollsCount.postValue(rerollscount);
    }
}
