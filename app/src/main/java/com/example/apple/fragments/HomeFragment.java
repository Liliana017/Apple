package com.example.apple.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apple.AddEditItemActivity;
import com.example.apple.MainActivity;
import com.example.apple.R;
import com.example.apple.adapters.ItemAdapter;
import com.example.apple.models.ItemModel;
import com.example.apple.utils.DatabaseHelper;
import com.example.apple.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private TextView tvEmpty;
    private Button btnAdd;
    private List<ItemModel> itemList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        btnAdd = view.findViewById(R.id.btnAdd);

        dbHelper = DatabaseHelper.getInstance(getContext());
        sessionManager = new SessionManager(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddEditItemActivity.class);
            startActivity(intent);
        });

        loadItems();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadItems();
    }

    private void loadItems() {
        int userId = sessionManager.getUserId();
        if (userId == -1) {
            return;
        }

        itemList = dbHelper.getAllItems();

        if (itemList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new ItemAdapter(getContext(), itemList, new ItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ItemModel item) {
                    Intent intent = new Intent(getContext(), AddEditItemActivity.class);
                    intent.putExtra("item_id", item.getId());
                    startActivity(intent);
                }

                @Override
                public void onFavoriteClick(ItemModel item, boolean isFavorite) {
                    dbHelper.setFavoriteStatus(item.getId(), isFavorite);
                    loadItems();
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }
}