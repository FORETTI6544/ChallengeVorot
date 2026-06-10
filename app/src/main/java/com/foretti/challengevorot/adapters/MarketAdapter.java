package com.foretti.challengevorot.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foretti.challengevorot.R;
import com.foretti.challengevorot.models.MarketItem;

import java.util.ArrayList;
import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.MarketViewHolder> {
    private List<MarketItem> items = new ArrayList<>();
    private int expandedPosition = -1;

    public static class MarketViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvItemDescription, tvItemPrice;

        public MarketViewHolder(View view) {
            super(view);
            tvItemName = view.findViewById(R.id.tvItemName);
            tvItemDescription = view.findViewById(R.id.tvItemDescription);
            tvItemPrice = view.findViewById(R.id.tvItemPrice);
        }
    }

    @NonNull
    @Override
    public MarketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_market, parent, false);
        return new MarketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketViewHolder holder, int position) {
        MarketItem item = items.get(position);
        holder.tvItemName.setText(item.name);
        holder.tvItemDescription.setText(item.description);
        holder.tvItemPrice.setText("Цена: " + item.price);

        // Показываем описание только для открытого элемента
        holder.tvItemDescription.setVisibility(position == expandedPosition ? View.VISIBLE : View.GONE);

        // Клик по элементу - переключаем expanded state
        holder.itemView.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) return;

            int previousExpanded = expandedPosition;
            expandedPosition = currentPos;
            notifyItemChanged(previousExpanded);
            notifyItemChanged(currentPos);
        });

        // Длинный клик (покупка)
        holder.itemView.setOnLongClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) return true;
            Toast.makeText(v.getContext(), "Купить: " + items.get(currentPos).name, Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<MarketItem> newItems) {
        this.items = newItems;
        expandedPosition = -1;
        notifyDataSetChanged();
    }
}
