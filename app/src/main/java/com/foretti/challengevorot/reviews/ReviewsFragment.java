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
import com.foretti.challengevorot.models.Review;
import com.foretti.challengevorot.network.WebSocketManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReviewsFragment extends Fragment {
    private ReviewsViewModel viewModel;
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
        WebSocketManager.getInstance().setReviewsCallback(reviews -> {
            viewModel.setReviews(reviews);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().clearReviewsCallback();
    }
}
