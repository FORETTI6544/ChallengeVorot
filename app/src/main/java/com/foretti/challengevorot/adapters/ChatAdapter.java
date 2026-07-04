package com.foretti.challengevorot.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foretti.challengevorot.R;
import com.foretti.challengevorot.converters.Converter;
import com.foretti.challengevorot.models.ChatMessage;
import com.google.android.material.imageview.ShapeableImageView;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_TEXT_OTHER = 0;
    private static final int VIEW_TYPE_TEXT_SELF = 1;
    private static final int VIEW_TYPE_IMAGE_OTHER = 2;
    private static final int VIEW_TYPE_IMAGE_SELF = 3;
    private static final int VIEW_TYPE_DATE = 4;

    private List<ChatMessage> messages = new ArrayList<>();
    private Map<String, String> userNames = new HashMap<>();
    private Map<String, String> userAvatars = new HashMap<>();
    private String currentUserId;

    private OnScrollListener scrollListener;

    public interface OnScrollListener {
        void onScrollToTop();
        void onScrollToBottom();
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.scrollListener = listener;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void setUserInfo(Map<String, String> names, Map<String, String> avatars) {
        this.userNames = names;
        this.userAvatars = avatars;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messages.get(position);

        if (message == null || "DATE".equals(message.type)) {
            return VIEW_TYPE_DATE;
        }

        boolean isSelf = currentUserId != null && currentUserId.equals(message.userId);

        if ("IMAGE".equals(message.type)) {
            return isSelf ? VIEW_TYPE_IMAGE_SELF : VIEW_TYPE_IMAGE_OTHER;
        } else {
            return isSelf ? VIEW_TYPE_TEXT_SELF : VIEW_TYPE_TEXT_OTHER;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW_TYPE_TEXT_SELF:
                return new MessageViewHolder(inflater.inflate(R.layout.item_chat_message_self, parent, false), viewType);
            case VIEW_TYPE_TEXT_OTHER:
                return new MessageViewHolder(inflater.inflate(R.layout.item_chat_message, parent, false), viewType);
            case VIEW_TYPE_IMAGE_SELF:
                return new MessageViewHolder(inflater.inflate(R.layout.item_attach_message_self, parent, false), viewType);
            case VIEW_TYPE_IMAGE_OTHER:
                return new MessageViewHolder(inflater.inflate(R.layout.item_attach_message, parent, false), viewType);
            default:
                return new DateViewHolder(inflater.inflate(R.layout.item_chat_date, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);

        if (holder instanceof MessageViewHolder) {
            MessageViewHolder msgHolder = (MessageViewHolder) holder;
            bindMessage(msgHolder, message, holder.getItemViewType());
        } else if (holder instanceof DateViewHolder) {
            DateViewHolder dateHolder = (DateViewHolder) holder;
            bindDate(dateHolder, message);
        }
    }

    private void bindMessage(MessageViewHolder holder, ChatMessage message, int viewType) {
        // Устанавливаем время
        if (holder.tvTime != null) {
            String time = formatTime(message.created_at);
            holder.tvTime.setText(time);
        }

        // Определяем, является ли сообщение с картинкой
        boolean isImageMessage = (viewType == VIEW_TYPE_IMAGE_SELF || viewType == VIEW_TYPE_IMAGE_OTHER);

        // 1. Обработка текстового содержимого (только для текстовых сообщений)
        if (!isImageMessage) {
            // Это текстовое сообщение - показываем текст
            if (holder.tvMessage != null) {
                holder.tvMessage.setVisibility(View.VISIBLE);
                if (message.content != null && !message.content.isEmpty()) {
                    holder.tvMessage.setText(message.content);
                } else {
                    holder.tvMessage.setText("");
                }
            }
        } else {
            // Это сообщение с картинкой - скрываем TextView с текстом (если он есть)
            if (holder.tvMessage != null) {
                holder.tvMessage.setVisibility(View.GONE);
            }
        }

        // 2. Обработка картинок
        if (isImageMessage) {
            if (holder.ivAttachedImage != null) {
                String imageBase64 = message.attachment_base64;
                // Проверяем, что это действительно base64 изображение
                if (imageBase64 != null && !imageBase64.isEmpty() &&
                        !"IMAGE".equals(imageBase64) && imageBase64.length() > 50) {
                    Bitmap bitmap = Converter.base64ToBitmap(imageBase64);
                    if (bitmap != null) {
                        holder.ivAttachedImage.setImageBitmap(bitmap);
                        holder.ivAttachedImage.setVisibility(View.VISIBLE);
                    } else {
                        // Если не удалось декодировать, показываем placeholder
                        holder.ivAttachedImage.setImageResource(R.drawable.send_message);
                        holder.ivAttachedImage.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Если нет изображения, показываем placeholder
                    holder.ivAttachedImage.setImageResource(R.drawable.send_message);
                    holder.ivAttachedImage.setVisibility(View.VISIBLE);
                }
            }
        }

        // 3. Обработка профиля чужого пользователя
        if (viewType == VIEW_TYPE_TEXT_OTHER || viewType == VIEW_TYPE_IMAGE_OTHER) {
            // Устанавливаем имя пользователя
            if (holder.tvName != null) {
                String userName = userNames.get(message.userId);
                holder.tvName.setText(userName != null ? userName : "Пользователь");
                holder.tvName.setVisibility(View.VISIBLE);
            }

            // Устанавливаем аватар
            if (holder.avatar != null) {
                String userAvatar = userAvatars.getOrDefault(message.userId, "");
                if (!userAvatar.isEmpty()) {
                    Bitmap bitmap = Converter.base64ToBitmap(userAvatar);
                    if (bitmap != null) {
                        holder.avatar.setImageBitmap(bitmap);
                    } else {
                        holder.avatar.setImageResource(R.drawable.ic_rotation);
                    }
                } else {
                    holder.avatar.setImageResource(R.drawable.ic_rotation);
                }
                holder.avatar.setVisibility(View.VISIBLE);
            }
        } else {
            // Для своих сообщений скрываем аватар и имя (если они есть)
            if (holder.avatar != null) {
                holder.avatar.setVisibility(View.GONE);
            }
            if (holder.tvName != null) {
                holder.tvName.setVisibility(View.GONE);
            }
        }
    }

    private void bindDate(DateViewHolder holder, ChatMessage message) {
        String date = formatDate(message.created_at);
        holder.tvDate.setText(date);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void updateMessages(List<ChatMessage> newMessages) {
        List<ChatMessage> messagesWithDates = new ArrayList<>();
        String lastDate = null;

        for (ChatMessage msg : newMessages) {
            String currentDate = formatDate(msg.created_at);

            if (lastDate == null || !lastDate.equals(currentDate)) {
                ChatMessage dateMessage = new ChatMessage();
                dateMessage.created_at = msg.created_at;
                dateMessage.type = "DATE";
                messagesWithDates.add(dateMessage);
                lastDate = currentDate;
            }

            messagesWithDates.add(msg);
        }

        this.messages = messagesWithDates;
        notifyDataSetChanged();
    }

    public void addMessage(ChatMessage message) {
        if (!messages.isEmpty()) {
            ChatMessage lastMessage = messages.get(messages.size() - 1);
            String lastDate = formatDate(lastMessage.created_at);
            String currentDate = formatDate(message.created_at);

            if (!lastDate.equals(currentDate)) {
                ChatMessage dateMessage = new ChatMessage();
                dateMessage.created_at = message.created_at;
                dateMessage.type = "DATE";
                this.messages.add(dateMessage);
                notifyItemInserted(messages.size() - 1);
            }
        } else {
            ChatMessage dateMessage = new ChatMessage();
            dateMessage.created_at = message.created_at;
            dateMessage.type = "DATE";
            this.messages.add(dateMessage);
            notifyItemInserted(0);
        }

        this.messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    private String formatTime(Instant created_at) {
        if (created_at == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm")
                .withZone(ZoneId.systemDefault());
        return formatter.format(created_at);
    }

    private String formatDate(Instant created_at) {
        if (created_at == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                .withZone(ZoneId.systemDefault());
        return formatter.format(created_at);
    }

    public int getLastPosition() {
        return messages.size() - 1;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView avatar;
        ImageView ivAttachedImage;
        TextView tvName;
        TextView tvMessage;
        TextView tvTime;
        View container;

        public MessageViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            // Общие элементы для всех типов
            tvTime = itemView.findViewById(R.id.chatMessageTime);
            container = itemView.findViewById(R.id.messageContainer);

            // tvMessage может отсутствовать в layout для картинок
            tvMessage = itemView.findViewById(R.id.chatMessageText);

            // Элементы для чужих сообщений
            if (viewType == VIEW_TYPE_TEXT_OTHER || viewType == VIEW_TYPE_IMAGE_OTHER) {
                avatar = itemView.findViewById(R.id.chatAvatar);
                tvName = itemView.findViewById(R.id.chatUserName);
            }

            // ImageView для картинок (есть только в layout для картинок)
            if (viewType == VIEW_TYPE_IMAGE_SELF || viewType == VIEW_TYPE_IMAGE_OTHER) {
                ivAttachedImage = itemView.findViewById(R.id.chatAttachedImage);
            }
        }
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.chatDate);
        }
    }
}