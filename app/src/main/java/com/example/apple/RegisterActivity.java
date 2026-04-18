package com.example.apple;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apple.utils.DatabaseHelper;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private DatabaseHelper dbHelper;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        tvLogin = findViewById(R.id.tv_login);

        btnRegister.setOnClickListener(v -> register());
        tvLogin.setOnClickListener(v -> finish());
    }

    private void register() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Валидация email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Введите email");
            return;
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            etEmail.setError("Введите корректный email");
            return;
        }

        // Валидация пароля
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Введите пароль");
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Пароль должен быть минимум 6 символов");
            return;
        }

        // Проверка подтверждения
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Пароли не совпадают");
            return;
        }

        // Проверка существования email
        if (dbHelper.isEmailExists(email)) {
            etEmail.setError("Email уже зарегистрирован");
            return;
        }

        // Регистрация
        if (dbHelper.registerUser(email, password)) {
            Toast.makeText(this, "Регистрация успешна! Теперь войдите.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
        }
    }
}