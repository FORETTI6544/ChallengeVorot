package com.example.abchihba.ui.wheel;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.abchihba.R;
import com.example.abchihba.WheelView;
import com.example.abchihba.databinding.FragmentWheelBinding;
import com.example.abchihba.ui.ViewModel;

import java.util.Collections;
import java.util.Map;
import java.util.Set;


public class wheel extends Fragment {

    private FragmentWheelBinding binding;
    private ViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);

        binding = FragmentWheelBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        WheelView wheelView = binding.wheelView;

        viewModel.getGenres().observe(getViewLifecycleOwner(), genres -> {
            wheelView.setGenres(genres);
        });
        viewModel.getRerolls().observe(getViewLifecycleOwner(), rerolls -> {
            String rerolls_count = rerolls;
            if ("2".equals(rerolls_count)) {
                binding.leftHeart.setBackgroundResource(R.drawable.heart_active);
                binding.rightHeart.setBackgroundResource(R.drawable.heart_active);
            } else if ("1".equals(rerolls_count)) {
                binding.leftHeart.setBackgroundResource(R.drawable.heart_active);
                binding.rightHeart.setBackgroundResource(R.drawable.heart_inactive);
            } else if ("0".equals(rerolls_count)) {
                binding.leftHeart.setBackgroundResource(R.drawable.heart_inactive);
                binding.rightHeart.setBackgroundResource(R.drawable.heart_inactive);
            } else {
                viewModel.setRerolls("2");
            }
            Button leftheart = binding.leftHeart;
            Button rightheart = binding.rightHeart;
            leftheart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ("2".equals(rerolls_count) && "no".equals(viewModel.getAllow().getValue()) && "Новая ротация".equals(viewModel.getStatus().getValue()) && !wheelView.isSpinning()) {
                        viewModel.setAllow("yes");
                        viewModel.setRerolls("1");
                    } else if ("1".equals(rerolls_count) && "no".equals(viewModel.getAllow().getValue()) && "Новая ротация".equals(viewModel.getStatus().getValue()) && !wheelView.isSpinning()) {
                        viewModel.setAllow("yes");
                        viewModel.setRerolls("0");
                    }
                }
            });
            rightheart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ("2".equals(rerolls_count) && "no".equals(viewModel.getAllow().getValue()) && "Новая ротация".equals(viewModel.getStatus().getValue()) && !wheelView.isSpinning()) {
                        viewModel.setAllow("yes");
                        viewModel.setRerolls("1");
                    }
                }
            });
        });

        viewModel.getAllow().observe(getViewLifecycleOwner(), allow -> {
            Button spinBtn = binding.spinBtn;
            if ("yes".equals(allow)) {
                spinBtn.setBackgroundResource(R.drawable.spin_btn_active);
            } else {
                spinBtn.setBackgroundResource(R.drawable.spin_btn_inactive);
            }
            spinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ("yes".equals(allow)) {
                        wheelView.spinToRandomGenre();
                        viewModel.setAllow("no");
                    }
                }
            });
        });


        wheelView.getGenre().observe(getViewLifecycleOwner(), genre -> {
            binding.selectedGenre.setText(genre);
            String newgenre = genre;
            viewModel.setGenre(newgenre);
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        WheelView wheelView = binding.wheelView;
        if (wheelView.isSpinning()) {
            viewModel.setAllow("yes");
        }
        binding = null;
    }
}