package com.foretti.challengevorot.market;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foretti.challengevorot.R;
import com.foretti.challengevorot.adapters.MarketAdapter;
import com.foretti.challengevorot.models.MarketItem;
import com.foretti.challengevorot.network.WebSocketManager;

import java.util.ArrayList;
import java.util.List;

public class MarketFragment extends Fragment {
    private MarketViewModel viewModel;
    private MarketAdapter marketAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_market, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(MarketViewModel.class);
        marketAdapter = new MarketAdapter();

        RecyclerView marketRecyclerView = view.findViewById(R.id.marketRecyclerView);
        marketRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        marketRecyclerView.setAdapter(marketAdapter);

        WebSocketManager.getInstance().setMarketCallback(items -> {
            viewModel.setItems(items);
        });
        WebSocketManager.getInstance().send("{\"type\":\"get_market\"}");

        viewModel.getItems().observe(getViewLifecycleOwner(), items -> {
            marketAdapter.updateItems(items);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().clearMarketCallback();
    }
}
