package com.foretti.challengevorot.reviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.foretti.challengevorot.models.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewsViewModel extends ViewModel {
    private MutableLiveData<List<Review>> reviews;

    public ReviewsViewModel() {
        reviews = new MutableLiveData<>();
        reviews.setValue(new ArrayList<>());
    }

    public void setReviews(List<Review> reviews) {
        this.reviews.postValue(reviews);
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }
}
