package com.foretti.challengevorot.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import com.google.android.material.imageview.ShapeableImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.foretti.challengevorot.R;
import com.foretti.challengevorot.converters.Converter;
import com.foretti.challengevorot.models.User;

import java.util.List;

public class ChainUsersAdapter extends RecyclerView.Adapter<ChainUsersAdapter.ChainViewHolder> {
    private List<User> users;

    public ChainUsersAdapter(List<User> users) {
        this.users = users != null ? users : new java.util.ArrayList<>();
    }

    @NonNull
    @Override
    public ChainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_chain, parent, false);
        return new ChainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChainViewHolder holder, int position) {
        User user = users.get(position);

        if (user != null && user.avatar != null && !user.avatar.isEmpty()) {
            Bitmap bitmap = Converter.base64ToBitmap(user.avatar);
            if (bitmap != null) {
                // Обрезаем до квадрата перед отображением
                Bitmap squareBitmap = Converter.cropToSquare(bitmap);
                holder.avatar.setImageBitmap(squareBitmap);
            }
        }
        holder.arrowIcon.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void updateUsers(List<User> newUsers) {
        this.users = newUsers != null ? newUsers : new java.util.ArrayList<>();
        notifyDataSetChanged();
    }

    public static class ChainViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView avatar;
        ImageView arrowIcon;

        public ChainViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.userAvatar);
            arrowIcon = itemView.findViewById(R.id.arrowIcon);
        }
    }
}
