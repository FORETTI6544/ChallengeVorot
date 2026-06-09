package com.foretti.challengevorot.wheel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.foretti.challengevorot.ActivityViewmodel;
import com.foretti.challengevorot.R;
import com.foretti.challengevorot.network.WebSocketManager;

import java.util.List;

public class WheelFragment extends Fragment {

    private WheelViewModel wheelViewModel;
    private ActivityViewmodel activityViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewmodel.class);
        wheelViewModel = new ViewModelProvider(this).get(WheelViewModel.class);
        View root = inflater.inflate(R.layout.fragment_wheel, container, false);

        WheelView wheelView = root.findViewById(R.id.wheelView);
        TextView selectedGenre = root.findViewById(R.id.selectedGenre);
        Button spinBtn = root.findViewById(R.id.spinBtn);

        // Подписываемся на обновление жанров из базы данных
        wheelViewModel.getGenres().observe(getViewLifecycleOwner(), genres -> {
            wheelView.setGenres(genres);
        });

        // Запрашиваем жанры при создании фрагмента
        WebSocketManager.getInstance().setGenresCallback(new WebSocketManager.GenresCallback() {
            @Override
            public void onGenresRecieved(List<String> genres) {
                wheelViewModel.setGenres(genres);
            }
        });
        WebSocketManager.getInstance().send("{\"type\":\"get_genres\"}");

        // Подписываемся на результат вращения от сервера через WebSocket
        WebSocketManager.getInstance().setSpinningResultCallback(new WebSocketManager.SpinningResultCallback() {
            @Override
            public void onSpinningResult(String genre) {
                if (!wheelView.isSpinning()) {
                    wheelView.spinToGenre(genre);
                    wheelViewModel.setIsSpinning(true);
                }
            }
        });

        // Обновляем отображение выбранного жанра
        wheelView.getGenre().observe(getViewLifecycleOwner(), genre -> {
            selectedGenre.setText(genre);
        });

        // Обработка клика по кнопке вращения
        spinBtn.setOnClickListener(v -> {
            if (!wheelView.isSpinning()) {
                // Запрашиваем результат вращения у сервера
                WebSocketManager.getInstance().send("{\"type\":\"spin_wheel\"}");
            }
        });

        return root;
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
