package com.foretti.challengevorot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.foretti.challengevorot.R;
import com.foretti.challengevorot.models.Room;
import com.foretti.challengevorot.network.WebSocketManager;
import com.foretti.challengevorot.profile.JoinRoomDialog;

import java.util.ArrayList;
import java.util.List;

// RoomsAdapter.java
public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.RoomViewHolder> {
    private List<Room> rooms = new ArrayList<>();
    Context context;

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvUsersCount, tvRotationStatus;
        Button joinRoomBtn;
        public RoomViewHolder(View view) {
            super(view);
            tvRoomName = view.findViewById(R.id.tvRoomName);
            tvUsersCount = view.findViewById(R.id.tvUsersCount);
            tvRotationStatus = view.findViewById(R.id.tvRotationStatus);
            joinRoomBtn = view.findViewById(R.id.joinRoomBtn);
        }
    }

    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room, parent, false);
        context = parent.getContext();
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.tvRoomName.setText(room.name);
        holder.tvUsersCount.setText("Игроков: " + room.usersCount);
        holder.tvRotationStatus.setText(room.rotationStatus ? "Идёт ротация..." : "Ожидание ротации...");
        holder.joinRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJoinRoomDialog(room.name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public void updateRooms(List<Room> newRooms) {
        this.rooms = newRooms;
        notifyDataSetChanged();
    }
    private void showJoinRoomDialog(String name) {
        JoinRoomDialog dialog = new JoinRoomDialog(context);
        dialog.show(name);
    }
}
