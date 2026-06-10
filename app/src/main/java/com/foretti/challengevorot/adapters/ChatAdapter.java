package com.foretti.challengevorot.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foretti.challengevorot.R;
import com.foretti.challengevorot.converters.Converter;
import com.foretti.challengevorot.models.ChatMessage;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_MESSAGE = 0;
    private static final int VIEW_TYPE_DATE = 1;

    private List<ChatMessage> messages = new ArrayList<>();
    private Map<String, String> userNames = new HashMap<>();
    private Map<String, String> userAvatars = new HashMap<>();
    private String currentUserId;

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
        // Если message равен null или пустой строке - это дата
        if (message == null || message.message == null || message.message.isEmpty()) {
            return VIEW_TYPE_DATE;
        }
        return VIEW_TYPE_MESSAGE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MESSAGE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_message, parent, false);
            return new MessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_date, parent, false);
            return new DateViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);

        if (holder.getItemViewType() == VIEW_TYPE_MESSAGE) {
            MessageViewHolder msgHolder = (MessageViewHolder) holder;
            bindMessage(msgHolder, message);
        } else {
            DateViewHolder dateHolder = (DateViewHolder) holder;
            bindDate(dateHolder, message);
        }
    }

    private void bindMessage(MessageViewHolder holder, ChatMessage message) {
        String userName = userNames.getOrDefault(message.userId, message.userName != null ? message.userName : "Unknown");
        String userAvatar = userAvatars.getOrDefault(message.userId, "");

        holder.tvName.setText(userName);
        holder.tvMessage.setText(message.message);

        // Формат времени
        String time = formatTime(message.timestamp);
        holder.tvTime.setText(time);

        // Аватарка
        if (userAvatar != null && !userAvatar.isEmpty()) {
            Bitmap bitmap = Converter.base64ToBitmap(userAvatar);
            if (bitmap != null) {
                holder.avatar.setImageBitmap(bitmap);
            }
        }

        // Определяем, правое ли сообщение (отправлено текущим пользователем)
        if (currentUserId != null && currentUserId.equals(message.userId)) {
            holder.container.setBackgroundResource(R.drawable.frame_done);
            holder.tvName.setTextColor(holder.itemView.getContext().getColor(R.color.textPrimary));
            holder.tvMessage.setTextColor(holder.itemView.getContext().getColor(R.color.textPrimary));
            holder.tvTime.setTextColor(holder.itemView.getContext().getColor(R.color.textSecondary));
        } else {
            holder.container.setBackgroundResource(R.drawable.frame_secondary);
            holder.tvName.setTextColor(holder.itemView.getContext().getColor(R.color.textPrimary));
            holder.tvMessage.setTextColor(holder.itemView.getContext().getColor(R.color.textSecondary));
            holder.tvTime.setTextColor(holder.itemView.getContext().getColor(R.color.textSecondary));
        }
    }

    private void bindDate(DateViewHolder holder, ChatMessage message) {
        String date = formatDate(message.timestamp);
        holder.tvDate.setText(date);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    /**
     * Обновляет сообщения и автоматически добавляет даты между сообщениями из разных дней
     */
    public void updateMessages(List<ChatMessage> newMessages) {
        List<ChatMessage> messagesWithDates = new ArrayList<>();
        String lastDate = null;

        for (ChatMessage msg : newMessages) {
            String currentDate = formatDate(msg.timestamp);

            // Если это первое сообщение или дата изменилась - добавляем дату
            if (lastDate == null || !lastDate.equals(currentDate)) {
                ChatMessage dateMessage = new ChatMessage();
                dateMessage.timestamp = msg.timestamp;
                dateMessage.message = null; // Помечаем как дату
                messagesWithDates.add(dateMessage);
                lastDate = currentDate;
            }

            messagesWithDates.add(msg);
        }

        this.messages = messagesWithDates;
        notifyDataSetChanged();
    }

    /**
     * Добавляет сообщение и проверяет, нужно ли добавить дату
     */
    public void addMessage(ChatMessage message) {
        // Проверяем, нужно ли добавить дату перед новым сообщением
        if (!messages.isEmpty()) {
            ChatMessage lastMessage = messages.get(messages.size() - 1);
            // Если последний элемент - это сообщение (не дата)
            if (lastMessage.message != null) {
                String lastDate = formatDate(lastMessage.timestamp);
                String currentDate = formatDate(message.timestamp);

                if (!lastDate.equals(currentDate)) {
                    // Добавляем дату
                    ChatMessage dateMessage = new ChatMessage();
                    dateMessage.timestamp = message.timestamp;
                    dateMessage.message = null;
                    this.messages.add(dateMessage);
                    notifyItemInserted(messages.size() - 1);
                }
            }
        } else {
            // Первое сообщение - добавляем дату
            ChatMessage dateMessage = new ChatMessage();
            dateMessage.timestamp = message.timestamp;
            dateMessage.message = null;
            this.messages.add(dateMessage);
            notifyItemInserted(0);
        }

        this.messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    private String formatTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp * 1000L));
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp * 1000L));
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView avatar;
        TextView tvName;
        TextView tvMessage;
        TextView tvTime;
        android.widget.LinearLayout container;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.chatAvatar);
            tvName = itemView.findViewById(R.id.chatUserName);
            tvMessage = itemView.findViewById(R.id.chatMessageText);
            tvTime = itemView.findViewById(R.id.chatMessageTime);
            container = itemView.findViewById(R.id.messageContainer);
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
