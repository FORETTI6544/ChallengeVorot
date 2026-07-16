package com.foretti.challengevorot.reviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.foretti.challengevorot.models.Review;
import com.foretti.challengevorot.models.ReviewsUser;

import java.util.ArrayList;
import java.util.List;

public class ReviewsViewModel extends ViewModel {
    private MutableLiveData<List<ReviewsUser>> users;
    private MutableLiveData<List<Review>> reviews;

    public ReviewsViewModel() {
        reviews = new MutableLiveData<>();
        reviews.setValue(new ArrayList<>());
        users = new MutableLiveData<>();
        users.setValue(new ArrayList<>());
    }

    public void setReviews(List<ReviewsUser> users, List<Review> reviews) {
        this.reviews.postValue(reviews);
        this.users.postValue(users);
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }
    public LiveData<List<ReviewsUser>> getReviewsUsers() {
        return users;
    }
}
