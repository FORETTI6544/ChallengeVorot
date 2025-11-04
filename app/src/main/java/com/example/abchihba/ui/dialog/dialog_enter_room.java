package com.example.abchihba.ui.dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;
import com.example.abchihba.R;
import com.example.abchihba.SteamGameSearcher;
import com.example.abchihba.databinding.DialogEnterRoomBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class dialog_enter_room extends DialogFragment {
    private DialogEnterRoomBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogEnterRoomBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle bundle = getArguments();



        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}