package com.example.abchihba.ui.profile;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.abchihba.ui.dialog.dialog_name;
import com.example.abchihba.ui.dialog.dialog_avatar;
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


        viewModel.getName().observe(getViewLifecycleOwner(), name -> {
            binding.name.setText(name);
        });
        viewModel.getAvatar().observe(getViewLifecycleOwner(), avatar -> {

            if ("1".equals(avatar)) {
                binding.avatar.setImageResource(R.mipmap.avatar_1);
            } else if ("2".equals(avatar)) {
                binding.avatar.setImageResource(R.mipmap.avatar_2);
            } else if ("3".equals(avatar)) {
                binding.avatar.setImageResource(R.mipmap.avatar_3);
            } else if ("4".equals(avatar)) {
                binding.avatar.setImageResource(R.mipmap.avatar_4);
            } else if ("5".equals(avatar)) {
                binding.avatar.setImageResource(R.mipmap.avatar_5);
            } else if ("6".equals(avatar)) {
                binding.avatar.setImageResource(R.mipmap.avatar_6);
            } else if ("7".equals(avatar)) {
                binding.avatar.setImageResource(R.mipmap.avatar_7);
            } else if ("8".equals(avatar)) {
                binding.avatar.setImageResource(R.mipmap.avatar_8);
            } else if ("9".equals(avatar)) {
                binding.avatar.setImageResource(R.mipmap.avatar_9);
            } else {
                binding.avatar.setImageResource(R.drawable.avatar_add);
            }

        });
        viewModel.getBalance().observe(getViewLifecycleOwner(), balance -> binding.balance.setText(balance));
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
                done.setTextColor(ContextCompat.getColor(requireContext(), R.color.buy_text));
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
                    Calendar calendar = Calendar.getInstance();
                    String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                    if (day.length() == 1) {
                        day = "0" + day;
                    }
                    String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
                    if (month.length() == 1) {
                        month = "0" + month;
                    }
                    String year = String.valueOf(calendar.get(Calendar.YEAR));
                    String date = day + "." + month + "." + year;
                    if (review != null && !review.isEmpty() && rating != null && !rating.isEmpty()) {
                        List<Reviews> reviewsList = viewModel.getReviews().getValue();
                        String queue = "1";
                        if (reviewsList != null) {
                            queue = String.valueOf(reviewsList.size() + 1);
                        }


                        viewModel.addReview(viewModel.getTag().getValue()
                                , viewModel.getGame().getValue(), viewModel.getPreview().getValue(),
                                review, rating, queue, date);

                        viewModel.setStatus("done");
                        if ("1".equals(viewModel.getRerolls().getValue())) {
                            viewModel.setRerolls("2");
                        } else if ("0".equals(viewModel.getRerolls().getValue())) {
                            viewModel.setRerolls("1");
                        }
                        String newbalance = "0";
                        boolean flag = true;
                        if (viewModel.getRoomUsers().getValue() != null) {
                            List<Users> users = viewModel.getRoomUsers().getValue();
                            for (Users user : users) {
                                if (Objects.equals(user.getStatus(), "done")){
                                    flag = false;
                                }
                            }
                        }

                        if (viewModel.getBalance().getValue() != null) {
                            if (flag) {
                                newbalance = String.valueOf(Integer.parseInt(viewModel.getBalance().getValue()) + 30);
                            } else {
                                newbalance = String.valueOf(Integer.parseInt(viewModel.getBalance().getValue()) + 20);
                            }
                        }
                        viewModel.setBalance(newbalance);
                    }
                });


                drop = new Button(getContext());
                drop.setId(View.generateViewId());
                drop.setText("ДРОПНУТЬ");
                drop.setTextColor(ContextCompat.getColor(requireContext(), R.color.buy_text));
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
                if (yesOrNo != null && !yesOrNo.isEmpty()) {

                    viewModel.setStatus("drop");
                    viewModel.setBalance("0");
                }
            });

        });


        ShapeableImageView avatar = binding.avatar;
        avatar.setOnClickListener(view -> {
            DialogFragment dialog_avatar = new dialog_avatar();
            dialog_avatar.show(getParentFragmentManager(), "dialog_edit_avatar");
        });

        getParentFragmentManager().setFragmentResultListener("edit_avatar_result", this, (requestKey, result) -> {
            String newAvatar = result.getString("new_avatar");
            if (newAvatar != null) {
                viewModel.setAvatar(newAvatar);
                Toast.makeText(getContext(), "Аватар изменён!", Toast.LENGTH_SHORT).show();
            }
        });
        TextView edit = binding.name;
        edit.setOnClickListener(view -> {
            DialogFragment dialog_name = new dialog_name();
            dialog_name.show(getParentFragmentManager(), "dialog_edit_name");
        });

        getParentFragmentManager().setFragmentResultListener("edit_name_result", this, (requestKey, result) -> {
            String newName = result.getString("new_name");
            if (newName != null) {
                viewModel.setName(newName);
                Toast.makeText(getContext(), "Имя изменено на: " + newName, Toast.LENGTH_SHORT).show();
            }
        });

        ShapeableImageView info = binding.info;
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog_info = new dialog_info();
                dialog_info.show(getParentFragmentManager(), "dialog_info");
            }
        });

        String hui = "";
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
