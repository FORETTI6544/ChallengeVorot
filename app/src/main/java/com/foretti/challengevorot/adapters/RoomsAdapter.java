package com.foretti.challengevorot.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.foretti.challengevorot.R;
import com.foretti.challengevorot.models.Room;

import java.util.ArrayList;
import java.util.List;

// RoomsAdapter.java
public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.RoomViewHolder> {
    private List<Room> rooms = new ArrayList<>();

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvUsersCount, tvRotationStatus;

        public RoomViewHolder(View view) {
            super(view);
            tvRoomName = view.findViewById(R.id.tvRoomName);
            tvUsersCount = view.findViewById(R.id.tvUsersCount);
            tvRotationStatus = view.findViewById(R.id.tvRotationStatus);
        }
    }

    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.tvRoomName.setText(room.name);
        holder.tvUsersCount.setText("Игроков: " + room.usersCount);
        holder.tvRotationStatus.setText(room.rotationStatus ? "Идёт ротация..." : "Ожидание ротации...");
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public void updateRooms(List<Room> newRooms) {
        this.rooms = newRooms;
        notifyDataSetChanged();
    }
}
