package com.example.abchihba.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.example.abchihba.ui.ViewModel;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.abchihba.R;
import com.example.abchihba.databinding.DialogGenreBinding;

import java.util.List;
import java.util.Objects;


public class dialog_genre extends DialogFragment {
    private DialogGenreBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogGenreBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ViewModel viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);


        List<String> genres = viewModel.getGenres().getValue();

        final String[] newgenre = {"Жанр"};

        Spinner spinner = binding.editgenre;
        ArrayAdapter<String> adapter = new ArrayAdapter (requireContext(), R.layout.adapter, genres);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                newgenre[0] = (String)adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(newgenre[0], "Жанр")) {
                    Toast.makeText(requireContext(), "Выберите жанр", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle result = new Bundle();
                    result.putString("new_genre", newgenre[0]);
                    getParentFragmentManager().setFragmentResult("edit_genre_result", result);
                    dismiss();
                }
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