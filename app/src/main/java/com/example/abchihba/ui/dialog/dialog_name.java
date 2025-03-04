package com.example.abchihba.ui.dialog;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.abchihba.databinding.DialogEditNameBinding;
import com.example.abchihba.ui.ViewModel;

public class dialog_name extends DialogFragment {
    private DialogEditNameBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogEditNameBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ViewModel viewModel = new ViewModelProvider(this).get(ViewModel.class);

        binding.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newname = binding.editname.getText().toString();
                if (newname.length()>15) {
                    Toast.makeText(getContext(), "Имя не должно превышать 15 символов!", Toast.LENGTH_SHORT).show();
                } else if (newname.isEmpty()) {
                    Toast.makeText(getContext(), "Имя не должно быть пустым!", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle result = new Bundle();
                    result.putString("new_name", newname);
                    getParentFragmentManager().setFragmentResult("edit_name_result", result);
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
