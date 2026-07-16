package com.foretti.challengevorot.reviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foretti.challengevorot.R;
import com.foretti.challengevorot.adapters.ReviewsAdapter;
import com.foretti.challengevorot.models.ChatMessage;
import com.foretti.challengevorot.models.ChatUser;
import com.foretti.challengevorot.models.Review;
import com.foretti.challengevorot.models.ReviewsUser;
import com.foretti.challengevorot.network.WebSocketManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewsFragment extends Fragment {
    private ReviewsViewModel viewModel;
    private Map<String, String> userNames = new HashMap<>();
    private Map<String, String> userAvatars = new HashMap<>();
    private ReviewsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reviews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ReviewsViewModel.class);
        setupObservers();
        adapter = new ReviewsAdapter();

        RecyclerView reviewsRecyclerView = view.findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        reviewsRecyclerView.setAdapter(adapter);

        viewModel.getReviews().observe(getViewLifecycleOwner(), new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                adapter.updateReviews(reviews);
            }
        });

        WebSocketManager.getInstance().send("{\"type\":\"get_reviews\"}");
        WebSocketManager.getInstance().setReviewsCallback((users, reviews) -> {
            viewModel.setReviews(users, reviews);
        });
    }

    private void setupObservers() {
        // Наблюдаем за сообщениями
        viewModel.getReviews().observe(getViewLifecycleOwner(), new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                adapter.updateReviews(reviews);
            }
        });

        // Наблюдаем за пользователями
        viewModel.getReviewsUsers().observe(getViewLifecycleOwner(), new Observer<List<ReviewsUser>>() {
            @Override
            public void onChanged(List<ReviewsUser> users) {
                userNames.clear();
                userAvatars.clear();
                for (ReviewsUser user : users) {
                    userNames.put(user.userId, user.userName);
                    userAvatars.put(user.userId, user.userAvatar);
                }
                adapter.setUsersInfo(userNames, userAvatars);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().clearReviewsCallback();
    }
}
