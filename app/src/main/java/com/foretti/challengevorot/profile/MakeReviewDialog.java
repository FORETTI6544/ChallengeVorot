package com.foretti.challengevorot.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.foretti.challengevorot.R;
import com.foretti.challengevorot.network.WebSocketManager;

public class MakeReviewDialog {
    private Context context;
    private String gameName;
    private OnReviewSubmittedListener listener;
    private AlertDialog dialog;

    public interface OnReviewSubmittedListener {
        void onReviewSubmitted(int rating, String text);
    }

    public MakeReviewDialog(Context context, String gameName, OnReviewSubmittedListener listener) {
        this.context = context;
        this.gameName = gameName != null ? gameName : "Неизвестная игра";
        this.listener = listener;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogStyle);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_make_review, null);

        TextView textGameName = dialogView.findViewById(R.id.textGameName);
        SeekBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        TextView textRating = dialogView.findViewById(R.id.textRating);
        EditText editTextReview = dialogView.findViewById(R.id.editTextReview);
        View btnCancel = dialogView.findViewById(R.id.btnCancelReview);
        View btnSubmit = dialogView.findViewById(R.id.btnSubmitReview);

        textGameName.setText(gameName);

        ratingBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textRating.setText("Оценка: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSubmit.setOnClickListener(v -> {
            int rating = ratingBar.getProgress();
            String text = editTextReview.getText().toString().trim();

            if (listener != null) {
                listener.onReviewSubmitted(rating, text);
            }

            String message = "{\"type\":\"make_review\",\"rating\":" + rating + ",\"text\":\"" + text + "\"}";
            WebSocketManager.getInstance().send(message);

            dialog.dismiss();
        });

        builder.setView(dialogView);
        dialog = builder.create();
        dialog.show();
    }
}
