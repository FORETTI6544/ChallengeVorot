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
import com.example.abchihba.databinding.DialogGameBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class dialog_game extends DialogFragment {
    private DialogGameBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogGameBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle genre = getArguments();
        if (genre != null){
            String genreView = "Жанр: " + genre.getString("genre");
            binding.genre.setText(genreView);
        }else{
            Log.d("BUNDLE", "bundle is null");
        }

        binding.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newgame = binding.editgame.getText().toString();
                String preview = binding.preview.getText().toString();
                Bundle result = new Bundle();
                result.putString("new_game", newgame);
                result.putString("preview", preview);
                getParentFragmentManager().setFragmentResult("edit_game_result", result);
                dismiss();
            }
        });


        AutoCompleteTextView autoComplete = binding.editgame;
        var appList = SteamGameSearcher.getInstance(getContext()).getAppList();
        List<String> csvDataName = new ArrayList<>();
        Log.d("LHILUHUILUILBHHIULBILBJUIKYHBUKBHUBKHKVHJ", String.valueOf(appList.size()));
        for (Pair<Integer, String> app : appList) {
            csvDataName.add(app.second);
        }
        Log.d("AutoComplete", "Data size: " + csvDataName.size());
        ArrayAdapter<String> adapter = new ArrayAdapter (requireContext(), R.layout.adapter, csvDataName);
        autoComplete.setAdapter(adapter);
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int id = csvDataName.indexOf((String) adapterView.getItemAtPosition(i));
                List<String> csvDataAppId = new ArrayList<>();
                for (Pair<Integer, String> app : appList) {
                    csvDataAppId.add(app.first.toString());
                }
                String appid = csvDataAppId.get(id);;
                String url = "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/" + appid + "/header.jpg";
                binding.preview.setText(url);
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