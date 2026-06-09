package com.foretti.challengevorot.wheel;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.foretti.challengevorot.ActivityViewmodel;
import com.foretti.challengevorot.R;
import com.foretti.challengevorot.network.WebSocketManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WheelFragment extends Fragment {

    private WheelViewModel wheelViewModel;
    private ActivityViewmodel activityViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_wheel, container, false);
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewmodel.class);
        wheelViewModel = new ViewModelProvider(this).get(WheelViewModel.class);


        WheelView wheelView = view.findViewById(R.id.wheelView);
        TextView selectedGenre = view.findViewById(R.id.selectedGenre);
        Button spinBtn = view.findViewById(R.id.spinBtn);
        Button leftHeartBtn = view.findViewById(R.id.left_heart);
        Button rightHeartBtn = view.findViewById(R.id.right_heart);


        activityViewModel.getAllowWheelSpinning().observe(getViewLifecycleOwner(), allow -> {
            if (allow) {
                leftHeartBtn.setClickable(false);
                rightHeartBtn.setClickable(false);
                spinBtn.setClickable(true);
                spinBtn.setBackgroundResource(R.drawable.spin_btn_active);
            } else {
                leftHeartBtn.setClickable(true);
                rightHeartBtn.setClickable(true);
                spinBtn.setClickable(false);
                spinBtn.setBackgroundResource(R.drawable.spin_btn_inactive);
            }
        });

        wheelViewModel.getGenres().observe(getViewLifecycleOwner(), genres -> {
            wheelView.setGenres(genres);
        });
        WebSocketManager.getInstance().setGenresCallback(new WebSocketManager.GenresCallback() {
            @Override
            public void onGenresRecieved(List<String> genres) {
                wheelViewModel.setGenres(genres);
            }
        });
        WebSocketManager.getInstance().send("{\"type\":\"get_genres\"}");

        WebSocketManager.getInstance().setSpinningResultCallback(new WebSocketManager.SpinningResultCallback() {
            @Override
            public void onSpinningResult(String genre) {
                WheelView wheelView = view.findViewById(R.id.wheelView);

                wheelView.post(() -> {
                    if (!isAdded() || getView() == null) return;
                    if (!wheelView.isSpinning()) {
                        wheelView.spinToGenre(genre);
                        wheelViewModel.setIsSpinning(true);
                    }
                });
            }
        });
        wheelView.getGenre().observe(getViewLifecycleOwner(), genre -> {
            selectedGenre.setText(genre);
        });

        spinBtn.setOnClickListener(v -> {
            if (!wheelView.isSpinning()&&activityViewModel.getAllowWheelSpinning().getValue()) {
                // Запрашиваем результат вращения у сервера
                WebSocketManager.getInstance().send("{\"type\":\"spin_wheel\"}");
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        WebSocketManager.getInstance().clearGenresCallback();
        WebSocketManager.getInstance().clearSpinningResultCallback();
        if (getView() != null && getView().findViewById(R.id.wheelView) != null) {
            WheelView wheelView = getView().findViewById(R.id.wheelView);
            if (wheelView.isSpinning()) {
                wheelViewModel.setIsSpinning(false);
            }
        }
    }
}
