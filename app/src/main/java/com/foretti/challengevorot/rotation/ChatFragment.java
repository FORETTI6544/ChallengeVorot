package com.foretti.challengevorot.rotation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foretti.challengevorot.ActivityViewmodel;
import com.foretti.challengevorot.R;
import com.foretti.challengevorot.adapters.ChatAdapter;
import com.foretti.challengevorot.models.ChatMessage;
import com.foretti.challengevorot.models.ChatUser;
import com.foretti.challengevorot.network.WebSocketManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatFragment extends Fragment {
    private ChatViewModel viewModel;
    private ChatAdapter adapter;
    private ActivityViewmodel activityViewModel;
    private String currentUserId;
    private Map<String, String> userNames = new HashMap<>();
    private Map<String, String> userAvatars = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewmodel.class);
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        adapter = new ChatAdapter();

        // Получаем текущего пользователя
        activityViewModel.getUsername().observe(getViewLifecycleOwner(), username -> {
            currentUserId = activityViewModel.getUsername().getValue();
        });

        RecyclerView chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(adapter);

        // Наблюдаем за сообщениями
        viewModel.getMessages().observe(getViewLifecycleOwner(), new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(List<ChatMessage> messages) {
                adapter.updateMessages(messages);
                if (!messages.isEmpty()) {
                    chatRecyclerView.scrollToPosition(messages.size() - 1);
                }
            }
        });

        // Наблюдаем за пользователями
        viewModel.getChatUsers().observe(getViewLifecycleOwner(), new Observer<List<ChatUser>>() {
            @Override
            public void onChanged(List<ChatUser> users) {
                userNames.clear();
                userAvatars.clear();
                for (ChatUser user : users) {
                    userNames.put(user.id, user.name);
                    userAvatars.put(user.id, user.avatar);
                }
                adapter.setUserInfo(userNames, userAvatars);
                adapter.notifyDataSetChanged();
            }
        });

        // Устанавливаем callback для чата
        WebSocketManager.getInstance().setChatCallback((messages, users) -> {
            viewModel.setMessages(messages);
            viewModel.setChatUsers(users);
        });

        // Запрашиваем чат
        WebSocketManager.getInstance().send("{\"type\":\"get_chat\"}");

        // Обработка отправки сообщения
        Button sendButton = view.findViewById(R.id.sendButton);
        EditText messageInput = view.findViewById(R.id.messageInput);

        // Запрашиваем фокус и открываем клавиатуру
        messageInput.requestFocus();

        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                WebSocketManager.getInstance().send(
                        "{\"type\":\"send_message\",\"message\":\"" + message + "\"}"
                );
                messageInput.setText("");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().clearChatCallback();
    }
}
