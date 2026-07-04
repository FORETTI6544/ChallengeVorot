package com.foretti.challengevorot.rotation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foretti.challengevorot.ActivityViewmodel;
import com.foretti.challengevorot.MainActivity;
import com.foretti.challengevorot.R;
import com.foretti.challengevorot.adapters.ChatAdapter;
import com.foretti.challengevorot.converters.Converter;
import com.foretti.challengevorot.models.ChatMessage;
import com.foretti.challengevorot.models.ChatUser;
import com.foretti.challengevorot.network.WebSocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
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

    // UI компоненты
    private RecyclerView chatRecyclerView;
    private CardView btnScrollToBottom;
    private EditText messageInput;
    private ImageButton attachButton;
    private ImageButton sendButton;

    // Флаги для управления скроллом
    private boolean isUserScrolling = false;
    private boolean shouldAutoScroll = true;
    private boolean isAtBottom = true; // Флаг, находится ли пользователь внизу
    ActivityResultLauncher<String> pickImageLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null && getContext() != null) {
                            try {
                                // Вместо getContentResolver() используем requireActivity().getContentResolver()
                                InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                                byte[] bytes = Converter.bitmapToByteArray(Converter.cropToSquare(bitmap), 50, Bitmap.CompressFormat.JPEG);
                                String base64 = Base64.getEncoder().encodeToString(bytes);
                                // Ваша логика отправки через WebSocket
                                sendImage(base64);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewmodel.class);
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        adapter = new ChatAdapter();

        // Инициализация UI
        initViews(view);
        setupRecyclerView();
        setupObservers();
        setupListeners();

        // Получаем текущего пользователя
        activityViewModel.getUsername().observe(getViewLifecycleOwner(), username -> {
            currentUserId = activityViewModel.getUserID().getValue();
            adapter.setCurrentUserId(currentUserId);
        });

        // Устанавливаем callback для чата
        WebSocketManager.getInstance().setChatCallback((messages, users) -> {
            viewModel.setMessages(messages);
            viewModel.setChatUsers(users);
        });
        WebSocketManager.getInstance().setNewMessageCallback(message -> {
            viewModel.addMessage(message);
        });

        // Запрашиваем чат
        WebSocketManager.getInstance().send("{\"type\":\"get_chat\"}");

        // Запрашиваем фокус и открываем клавиатуру
        messageInput.requestFocus();
    }

    private void initViews(View view) {
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        btnScrollToBottom = view.findViewById(R.id.btnScrollToBottom);
        messageInput = view.findViewById(R.id.messageInput);
        attachButton = view.findViewById(R.id.attachButton);
        sendButton = view.findViewById(R.id.sendButton);

        btnScrollToBottom.setVisibility(View.GONE);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(adapter);

        chatRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isUserScrolling = true;
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // Проверяем, достигли ли дна
                    boolean canScrollDown = recyclerView.canScrollVertically(1);
                    isAtBottom = !canScrollDown;

                    if (isAtBottom) {
                        // Достигли дна
                        hideScrollButton();
                        isUserScrolling = false;
                        shouldAutoScroll = true;
                    } else if (isUserScrolling) {
                        // Не достигли дна и пользователь скроллит
                        showScrollButton();
                        shouldAutoScroll = false;
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Проверяем позицию скролла
                boolean canScrollDown = recyclerView.canScrollVertically(1);
                isAtBottom = !canScrollDown;

                // Если достигли дна
                if (isAtBottom) {
                    hideScrollButton();
                    if (isUserScrolling) {
                        isUserScrolling = false;
                        shouldAutoScroll = true;
                    }
                } else if (dy < 0 && isUserScrolling) {
                    // Скроллим вверх и не внизу
                    showScrollButton();
                    shouldAutoScroll = false;
                }
            }
        });

        // Добавляем слушатель для определения, находится ли пользователь внизу
        chatRecyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (!isUserScrolling && isAtBottom && adapter.getItemCount() > 0) {
                // Если мы внизу и не скроллим, делаем плавный скролл
                chatRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }

    private void setupObservers() {
        // Наблюдаем за сообщениями
        viewModel.getMessages().observe(getViewLifecycleOwner(), new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(List<ChatMessage> messages) {
                int oldSize = adapter.getItemCount();
                adapter.updateMessages(messages);

                // Автоматически скроллим только если пользователь внизу
                if (shouldAutoScroll && !messages.isEmpty() && isAtBottom) {
                    // Используем scrollToPosition вместо smoothScrollToPosition
                    chatRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
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
    }

    private void setupListeners() {
        sendButton.setOnClickListener(v -> sendMessage());
        attachButton.setOnClickListener(v -> pickImage());

        messageInput.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == android.view.KeyEvent.KEYCODE_ENTER &&
                    event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                sendMessage();
                return true;
            }
            return false;
        });


        btnScrollToBottom.setOnClickListener(v -> {
            // Анимированный скролл только при нажатии кнопки
            if (chatRecyclerView != null && adapter.getItemCount() > 0) {
                chatRecyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                hideScrollButton();
                isUserScrolling = false;
                shouldAutoScroll = true;
                isAtBottom = true;
            }
        });
    }

    private void sendImage(String base64) {
            JSONObject msg = new JSONObject();
            try {
                msg.put("type", "IMAGE");
                msg.put("user_id", currentUserId);
                msg.put("attachment_base64", base64);
                JSONObject msgSend = new JSONObject()
                        .put("type", "send_message")
                        .put("message", msg);
                WebSocketManager.getInstance().send(msgSend.toString());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
    }
    private void sendMessage() {
        String message = messageInput.getText().toString().trim();
        if (!message.isEmpty()) {
            JSONObject msg = new JSONObject();
            try {
                msg.put("type", "TEXT");
                msg.put("user_id", currentUserId);
                msg.put("content", message);
                JSONObject msgSend = new JSONObject()
                        .put("type", "send_message")
                        .put("message", msg);
                WebSocketManager.getInstance().send(msgSend.toString());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            messageInput.setText("");
        }
    }
    private void pickImage() {
        if (pickImageLauncher != null) {
            pickImageLauncher.launch("image/*"); // Открывает галерею с фильтром только по картинкам
        }
    }
    private void showScrollButton() {
        if (btnScrollToBottom.getVisibility() == View.GONE) {
            btnScrollToBottom.setVisibility(View.VISIBLE);
            btnScrollToBottom.setScaleX(0.5f);
            btnScrollToBottom.setScaleY(0.5f);
            btnScrollToBottom.setAlpha(0f);
            btnScrollToBottom.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(200)
                    .start();
        }
    }

    private void hideScrollButton() {
        if (btnScrollToBottom.getVisibility() == View.VISIBLE) {
            btnScrollToBottom.animate()
                    .alpha(0f)
                    .scaleX(0.5f)
                    .scaleY(0.5f)
                    .setDuration(200)
                    .withEndAction(() -> btnScrollToBottom.setVisibility(View.GONE))
                    .start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().clearChatCallback();
    }
}