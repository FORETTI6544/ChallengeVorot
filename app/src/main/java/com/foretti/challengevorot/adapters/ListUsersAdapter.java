package com.foretti.challengevorot.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import com.google.android.material.imageview.ShapeableImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.foretti.challengevorot.R;
import com.foretti.challengevorot.converters.Converter;
import com.foretti.challengevorot.models.User;

import java.util.List;

public class ListUsersAdapter extends RecyclerView.Adapter<ListUsersAdapter.UserViewHolder> {
    private List<User> users;
    private String askTo;
    private Context context;
    private OnUserClickListener clickListener;
    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    public void setOnUserClickListener(OnUserClickListener listener) {
        this.clickListener = listener;
    }

    public ListUsersAdapter(List<User> users, Context context) {
        this.users = users != null ? users : new java.util.ArrayList<>();
        this.askTo = "";
        this.context = context;
    }

    public void setAskTo(String askTo) {
        this.askTo = askTo != null ? askTo : "";
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_list, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);

        if (user.avatar != null && !user.avatar.isEmpty()) {
            Bitmap bitmap = Converter.base64ToBitmap(user.avatar);
            if (bitmap != null) {
                holder.avatar.setImageBitmap(bitmap);
            }
        }

        holder.userName.setText(user.name);
        holder.userGame.setText(user.game);

        switch (user.gameStatus) {
            case "new":
                if (!user.genre.equals("Отсутствует") && user.id.equals(askTo)) {
                    holder.container.setBackgroundResource(R.drawable.frame_tertiary);
                    holder.statusCircle.setBackgroundResource(R.drawable.frame_tertiary);
                    holder.statusCircleCutter.setBackgroundResource(R.drawable.frame_tertiary);
                    holder.itemView.setOnClickListener(v -> {
                        if (clickListener != null) {
                            clickListener.onUserClick(user);
                        }
                    });
                } else {
                    holder.container.setBackgroundResource(R.drawable.frame_secondary);
                    holder.statusCircle.setBackgroundResource(R.drawable.frame_secondary);
                    holder.statusCircleCutter.setBackgroundResource(R.drawable.frame_secondary);

                }
                break;
            case "playing":
                holder.container.setBackgroundResource(R.drawable.frame_secondary);
                holder.statusCircle.setVisibility(View.VISIBLE);
                holder.statusCircle.setBackgroundResource(R.drawable.frame_playing);
                break;
            case "done":
                holder.container.setBackgroundResource(R.drawable.frame_secondary);
                holder.statusCircle.setVisibility(View.VISIBLE);
                holder.statusCircle.setBackgroundResource(R.drawable.frame_done);
                break;
            case "drop":
                holder.container.setBackgroundResource(R.drawable.frame_secondary);
                holder.statusCircle.setVisibility(View.VISIBLE);
                holder.statusCircle.setBackgroundResource(R.drawable.frame_drop);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void updateUsers(List<User> newUsers) {
        this.users = newUsers != null ? newUsers : new java.util.ArrayList<>();
        notifyDataSetChanged();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView avatar;
        TextView userName;
        TextView userGame;
        ConstraintLayout statusCircle;
        ConstraintLayout statusCircleCutter;
        ConstraintLayout container;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            avatar = itemView.findViewById(R.id.userAvatar);
            userName = itemView.findViewById(R.id.userName);
            userGame = itemView.findViewById(R.id.userGame);
            statusCircle = itemView.findViewById(R.id.statusCircle);
            statusCircleCutter = itemView.findViewById(R.id.statusCircleCutter);
        }

    }
}
