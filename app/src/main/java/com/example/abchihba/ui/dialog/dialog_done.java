package com.example.abchihba.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.abchihba.databinding.DialogDoneBinding;


public class dialog_done extends DialogFragment {
    private DialogDoneBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogDoneBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle game = getArguments();
        if (game != null) {
            String QUESTION = "Оставьте отзыв на игру: " + game.getString("game");
            binding.question.setText(QUESTION);
        }






        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle result = new Bundle();
                result.putString("review", binding.review.getText().toString());
                result.putString("rating", binding.rating.getText().toString());
                getParentFragmentManager().setFragmentResult("done_result", result);
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
