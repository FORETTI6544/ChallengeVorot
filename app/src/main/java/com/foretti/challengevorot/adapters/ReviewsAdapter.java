package com.foretti.challengevorot.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foretti.challengevorot.R;
import com.foretti.challengevorot.converters.Converter;
import com.foretti.challengevorot.models.Review;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    private List<Review> reviews = new ArrayList<>();
    private Map<String, String> userNames = new HashMap<>();
    private Map<String, String> userAvatars = new HashMap<>();

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvGameName, tvReviewText, tvRating, tvGamePreviewText;
        ImageView ivGamePreview;
        ShapeableImageView tvUserAvatar;

        public ReviewViewHolder(View view) {
            super(view);
            tvUserName = view.findViewById(R.id.tvUserName);
            tvUserAvatar = view.findViewById(R.id.tvUserAvatar);
            tvGameName = view.findViewById(R.id.tvGameName);
            tvReviewText = view.findViewById(R.id.tvReviewText);
            tvRating = view.findViewById(R.id.tvRating);
            tvGamePreviewText = view.findViewById(R.id.tvGamePreviewText);
            ivGamePreview = view.findViewById(R.id.ivGamePreview);
        }
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.tvUserName.setText(userNames.get(review.userId));
        holder.tvUserAvatar.setImageBitmap(Converter.base64ToBitmap(userAvatars.get(review.userId)));
        holder.tvGameName.setText(review.gameName);
        holder.tvReviewText.setText(review.text);
        holder.tvRating.setText("Оценка: " + review.rating + "/10");

        if (review.gamePreview != null && !review.gamePreview.isEmpty()) {
            Picasso.get()
                    .load(review.gamePreview)
                    .placeholder(R.drawable.heart_inactive)
                    .error(R.drawable.heart_inactive)
                    .into(holder.ivGamePreview);
            holder.tvGamePreviewText.setVisibility(View.GONE);
            holder.ivGamePreview.setVisibility(View.VISIBLE);
        } else {
            holder.ivGamePreview.setVisibility(View.GONE);
            holder.tvGamePreviewText.setVisibility(View.VISIBLE);
            holder.tvGamePreviewText.setText(review.gameName);
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void updateReviews(List<Review> newReviews) {
        this.reviews = newReviews;
        notifyDataSetChanged();
    }
    public void setUsersInfo(Map<String, String> names, Map<String, String> avatars) {
        this.userNames = names;
        this.userAvatars = avatars;
    }
}
