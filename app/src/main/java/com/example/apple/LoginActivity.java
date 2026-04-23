package com.example.apple;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apple.utils.DatabaseHelper;
import com.example.apple.utils.SessionManager;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private TextView tvRegister;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);

        btnLogin.setOnClickListener(v -> login());
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Введите email");
            return;
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            etEmail.setError("Введите корректный email");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Введите пароль");
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Пароль должен быть минимум 6 символов");
            return;
        }

        int userId = dbHelper.loginUser(email, password);
        if (userId != -1) {
            sessionManager.createSession(userId,
                    email);
            Toast.makeText(this, "Добро пожаловать!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Неверный email или пароль", Toast.LENGTH_SHORT).show();
        }
    }
}
