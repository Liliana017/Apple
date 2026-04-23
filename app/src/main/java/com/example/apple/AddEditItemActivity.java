package com.example.apple;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.apple.models.ItemModel;
import com.example.apple.utils.DatabaseHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditItemActivity extends AppCompatActivity {
    private EditText etTitle, etDescription, etPrice;
    private Button btnSave, btnDelete;
    private DatabaseHelper dbHelper;
    private int itemId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        dbHelper = new DatabaseHelper(this);

        if (getIntent().hasExtra("item_id")) {
            itemId = getIntent().getIntExtra("item_id", -1);
            isEditMode = true;
            loadItemData();
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(v -> saveItem());
        btnDelete.setOnClickListener(v -> deleteItem());
    }

    private void loadItemData() {
        ItemModel item = dbHelper.getItem(itemId);
        if (item != null) {
            etTitle.setText(item.getTitle());
            etDescription.setText(item.getDescription());
            etPrice.setText(String.valueOf(item.getPrice()));
        }
    }

    private void saveItem() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            etTitle.setError("Введите название");
            return;
        }
        if (TextUtils.isEmpty(description)) {
            etDescription.setError("Введите описание");
            return;
        }
        if (TextUtils.isEmpty(priceStr)) {
            etPrice.setError("Введите цену");
            return;
        }

        double price = Double.parseDouble(priceStr);
        String date = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(new Date());

        if (isEditMode) {
            dbHelper.updateItem(itemId, title, description, price);
            Toast.makeText(this, "Товар обновлён", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.addItem(title, description, price, date);
            Toast.makeText(this, "Товар добавлен", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void deleteItem() {
        dbHelper.deleteItem(itemId);
        Toast.makeText(this, "Товар удалён", Toast.LENGTH_SHORT).show();
        finish();
    }
}