package com.example.abchihba.ui.profile;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.abchihba.R;
import com.example.abchihba.databinding.FragmentProfileBinding;
import com.example.abchihba.ui.Reviews;
import com.example.abchihba.ui.Users;
import com.example.abchihba.ui.ViewModel;
import com.example.abchihba.ui.dialog.dialog_drop;
import com.example.abchihba.ui.dialog.dialog_done;
import com.example.abchihba.ui.dialog.dialog_info;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import com.squareup.picasso.Picasso;


public class profile extends Fragment {

    private FragmentProfileBinding binding;
    private ViewModel viewModel;
    private Button done;
    private Button drop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Инициализация ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);

        // Инициализация binding
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        viewModel.getGenre().observe(getViewLifecycleOwner(), genre -> binding.genre.setText("Жанр: " + genre));
        viewModel.getGame().observe(getViewLifecycleOwner(), game -> {
            binding.game.setText(game);
        });
        viewModel.getPreview().observe(getViewLifecycleOwner(), preview -> {
            ImageView gamePreview = binding.gamePreview;
            Picasso.with(getContext())
                    .load(preview)
                    .placeholder(R.drawable.avatar_add)
                    .error(R.drawable.avatar_add)
                    .into(gamePreview);
        });
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if ("playing".equals(status)) {
                done = new Button(getContext());
                done.setId(View.generateViewId());
                done.setText("Прошел");
                done.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondaryText));
                done.setTextSize(20);
                done.setTypeface(null, Typeface.BOLD);
                done.setBackgroundResource(R.drawable.proshol);
                ConstraintLayout.LayoutParams doneLayout = new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        150);
                doneLayout.topToBottom = binding.gamePanel.getId();
                doneLayout.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                doneLayout.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                doneLayout.setMargins(0,20, 0, 10);
                done.setLayoutParams(doneLayout);
                binding.container.addView(done);

                done.setOnClickListener(view -> {
                    DialogFragment dialog_done = new dialog_done();
                    dialog_done.show(getParentFragmentManager(), "dialog_done");
                    Bundle game = new Bundle();
                    game.putString("game", viewModel.getGame().getValue());
                    dialog_done.setArguments(game);

                });

                getParentFragmentManager().setFragmentResultListener("done_result", this, (requestKey, result) -> {
                    String review = result.getString("review");
                    String rating = result.getString("rating");
                    viewModel.doneAndReview(review, rating);
                });


                drop = new Button(getContext());
                drop.setId(View.generateViewId());
                drop.setText("ДРОПНУТЬ");
                drop.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondaryText));
                drop.setTextSize(20);
                drop.setTypeface(null, Typeface.BOLD);
                drop.setBackgroundResource(R.drawable.dropnul);
                ConstraintLayout.LayoutParams dropLayout = new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        150);
                dropLayout.topToBottom = done.getId();
                dropLayout.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                dropLayout.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                dropLayout.setMargins(0,10, 0, 10);
                drop.setLayoutParams(dropLayout);
                binding.container.addView(drop);

                drop.setOnClickListener(view -> {
                    DialogFragment dialog_drop = new dialog_drop();
                    dialog_drop.show(getParentFragmentManager(), "dialog_drop");
                });

            } else {
                binding.container.removeView(done);
                done = null;
                binding.container.removeView(drop);
                drop = null;
            }

            getParentFragmentManager().setFragmentResultListener("drop_result", this, (requestKey, result) -> {
                String yesOrNo = result.getString("result");
                if (yesOrNo != null && yesOrNo.equals("yes")) {
                    viewModel.dropGame(viewModel.getTag().getValue());
                }
            });

        });


        ShapeableImageView info = binding.info;
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog_info = new dialog_info();
                dialog_info.show(getParentFragmentManager(), "dialog_info");
            }
        });


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
