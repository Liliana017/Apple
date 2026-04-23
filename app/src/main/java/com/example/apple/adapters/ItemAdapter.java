package com.example.apple.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apple.R;
import com.example.apple.models.ItemModel;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private Context context;
    private List<ItemModel> itemList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ItemModel item);
        void onFavoriteClick(ItemModel item, boolean isFavorite);
    }

    public ItemAdapter(Context context, List<ItemModel> itemList, OnItemClickListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemModel item = itemList.get(position);

        holder.tvTitle.setText(item.getTitle());
        holder.tvDescription.setText(item.getDescription());
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("ru", "RU"));
        holder.tvPrice.setText(format.format(item.getPrice()));

        holder.btnFavorite.setText(item.isFavorite() ? "★" : "☆");
        holder.btnFavorite.setOnClickListener(v -> {
            listener.onFavoriteClick(item, !item.isFavorite());
        });

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvPrice;
        Button btnFavorite;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }
    }
}