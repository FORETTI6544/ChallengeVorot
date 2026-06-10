package com.foretti.challengevorot.rotation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.foretti.challengevorot.models.ChatMessage;
import com.foretti.challengevorot.models.ChatUser;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {
    private MutableLiveData<List<ChatMessage>> messages;
    private MutableLiveData<List<ChatUser>> chatUsers;

    public ChatViewModel() {
        messages = new MutableLiveData<>();
        messages.setValue(new ArrayList<>());
        chatUsers = new MutableLiveData<>();
        chatUsers.setValue(new ArrayList<>());
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages.postValue(messages);
    }

    public LiveData<List<ChatMessage>> getMessages() {
        return messages;
    }

    public void setChatUsers(List<ChatUser> users) {
        this.chatUsers.postValue(users);
    }

    public LiveData<List<ChatUser>> getChatUsers() {
        return chatUsers;
    }
}
