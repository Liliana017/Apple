package com.example.apple;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apple.models.Item;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<Item> items;
    private OnItemClickListener listener;
    private OnFavoriteClickListener favoriteListener;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("ru", "RU"));

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Item item, int position);
    }

    public ItemAdapter(List<Item> items, OnItemClickListener listener, OnFavoriteClickListener favoriteListener) {
        this.items = items;
        this.listener = listener;
        this.favoriteListener = favoriteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvDescription.setText(item.getDescription());
        holder.tvPrice.setText(currencyFormat.format(item.getPrice()));
        holder.btnFavorite.setText(item.isFavorite() ? "F" : "N");

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        holder.btnFavorite.setOnClickListener(v -> favoriteListener.onFavoriteClick(item, position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateList(List<Item> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvPrice;
        Button btnFavorite;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvPrice = itemView.findViewById(R.id.tv_price);
            btnFavorite = itemView.findViewById(R.id.btn_favorite);
        }
    }
}