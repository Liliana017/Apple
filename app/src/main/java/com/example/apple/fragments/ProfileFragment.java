package com.example.apple.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.apple.LoginActivity;
import com.example.apple.R;
import com.example.apple.utils.SessionManager;

public class ProfileFragment extends Fragment {
    private TextView tvEmail, tvUserName;
    private Button btnLogout;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvEmail = view.findViewById(R.id.tv_email);
        tvUserName = view.findViewById(R.id.tvDate);
        btnLogout = view.findViewById(R.id.btn_logout);

        sessionManager = new SessionManager(getContext());

        String email = sessionManager.getUserEmail();

        if (email != null && !email.isEmpty()) {
            tvEmail.setText(email);
            int atIndex = email.indexOf("@");
            if (atIndex > 0) {
                tvUserName.setText(email.substring(0, atIndex));
            } else {
                tvUserName.setText(email);
            }
        } else {
            tvEmail.setText("email@example.com");
            tvUserName.setText("Пользователь");
        }

        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Toast.makeText(getContext(), "Вы вышли из системы", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}