package com.foretti.challengevorot.rotation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foretti.challengevorot.ActivityViewmodel;
import com.foretti.challengevorot.R;
import com.foretti.challengevorot.adapters.ChainUsersAdapter;
import com.foretti.challengevorot.adapters.ListUsersAdapter;
import com.foretti.challengevorot.models.User;
import com.foretti.challengevorot.network.WebSocketManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RotationFragment extends Fragment {
    private ActivityViewmodel activityViewModel;
    private RotationViewModel viewModel;
    private ChainUsersAdapter chainAdapter;
    private ListUsersAdapter usersAdapter;
    private Button btnChat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rotation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewmodel.class);
        viewModel = new ViewModelProvider(this).get(RotationViewModel.class);


        chainAdapter = new ChainUsersAdapter(null);
        usersAdapter = new ListUsersAdapter(null, requireContext());
        usersAdapter.setOnUserClickListener(user -> {
            AskGameDialog dialog = new AskGameDialog(requireContext(), user, (game, gamePreview) -> {
                // Отправляем данные через WebSocket
                WebSocketManager.getInstance().send(
                        "{\"type\":\"ask_game\",\"ask_to\":\"" + activityViewModel.getAskTo().getValue() + "\",\"game\":\"" + game + "\",\"gamePreview\":\"" + gamePreview + "\"}"
                );
            });
            dialog.show();
        });

        RecyclerView chainRecyclerView = view.findViewById(R.id.chainRecyclerView);
        RecyclerView usersRecyclerView = view.findViewById(R.id.usersRecyclerView);

        LinearLayoutManager chainLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        chainRecyclerView.setLayoutManager(chainLayoutManager);
        chainRecyclerView.setAdapter(chainAdapter);

        LinearLayoutManager usersLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        usersRecyclerView.setLayoutManager(usersLayoutManager);
        usersRecyclerView.setAdapter(usersAdapter);

        viewModel.getUsers().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users != null && !users.isEmpty()) {
                    List<User> chain = buildChain(users);
                    chainAdapter.updateUsers(chain);
                    usersAdapter.updateUsers(users);
                }
            }
        });

        btnChat = view.findViewById(R.id.btnChat);
        btnChat.setVisibility(View.VISIBLE);
        btnChat.setOnClickListener(v -> {
            // Открываем чат
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new ChatFragment())
                    .addToBackStack(null)
                    .commit();
        });
        Button btnReady = view.findViewById(R.id.btnReady);
        activityViewModel.getReadiness().observe(getViewLifecycleOwner(), isReady -> {
            btnReady.setText(isReady ? "Отменить" : "Готов");
            btnReady.setBackgroundResource(isReady ? R.drawable.frame_drop : R.drawable.frame_done);
            btnReady.setOnClickListener(v -> {
                WebSocketManager.getInstance().send("{\"type\":\"set_readiness\",\"readiness\":" + !isReady + "}");
            });
        });
        viewModel.getRotationStatus().observe(getViewLifecycleOwner(), rotationStatus -> {
            if (rotationStatus) {
                btnReady.setVisibility(View.GONE);
            } else {
                btnReady.setVisibility(View.VISIBLE);
            }
        });

        activityViewModel.getAskTo().observe(getViewLifecycleOwner(), askTo -> {
            usersAdapter.setAskTo(askTo);
        });

        WebSocketManager.getInstance().setUsersListCallback(users -> {
                viewModel.setUsers(users);
        });
        WebSocketManager.getInstance().send("{\"type\":\"get_users\"}");
        WebSocketManager.getInstance().setRotationStatusCallback(rotationStatus -> {
            viewModel.setRotationStatus(rotationStatus);
        });
        WebSocketManager.getInstance().send("{\"type\":\"get_rotation_status\"}");
        WebSocketManager.getInstance().setRoomUserUpdateCallback(user -> {
            List<User> users = viewModel.getUsers().getValue();
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    if (Objects.equals(users.get(i).id, user.id)) {
                        users.set(i, user);
                        break;
                    }
                }
                viewModel.setUsers(users);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().clearUsersListCallback();
    }

    public static List<User> buildChain(List<User> allUsers) {
        List<User> chain = new ArrayList<>();

        if (allUsers == null || allUsers.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, User> userMap = new HashMap<>();
        for (User user : allUsers) {
                userMap.put(user.id, user);
        }

        String firstUser = "";
        firstUser = allUsers.get(0).id;
        String currentUser = firstUser;
        String nextUser = userMap.get(currentUser).askTo;
        for (int i = 0; i < userMap.size()+1; i++) {
            chain.add(userMap.get(currentUser));
            Log.d( "buildChain", currentUser);
            currentUser = nextUser;
            nextUser = userMap.get(currentUser).askTo;
        }
        Log.d( "buildChain", String.valueOf(chain.size()));
        return chain;
    }
}
