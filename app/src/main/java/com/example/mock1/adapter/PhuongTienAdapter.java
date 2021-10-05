package com.example.mock1.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mock1.R;

import com.example.mock1.entity.PhuongTien;

import java.util.List;

public class PhuongTienAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<PhuongTien> phuongTiens;

    private int position;

    public PhuongTienAdapter(Context context, List<PhuongTien> phuongTiens) {
        this.context = context;
        this.phuongTiens = phuongTiens;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private static class PhuongTienViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        private final TextView id, name, category, price;

        public PhuongTienViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.txt_id);
            name = itemView.findViewById(R.id.txt_name);
            category = itemView.findViewById(R.id.txt_category);
            price = itemView.findViewById(R.id.txt_price);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(Menu.NONE, R.id.action_delete,
                    Menu.NONE, "Xóa");
            contextMenu.add(Menu.NONE, R.id.action_update,
                    Menu.NONE, "Sửa");
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhuongTienViewHolder(LayoutInflater.from(context).inflate(R.layout.item_phuong_tien, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PhuongTien phuongTien = phuongTiens.get(position);
        PhuongTienViewHolder phuongTienViewHolder = (PhuongTienViewHolder) holder;

        String stringId = context.getString(R.string.id, phuongTien.getId());
        String stringName = context.getString(R.string.name, phuongTien.getName());
        String stringCategory = context.getString(R.string.category, phuongTien.getCategory());
        String stringPrice = context.getString(R.string.price, phuongTien.getPrice());

        phuongTienViewHolder.price.setText(stringPrice);
        phuongTienViewHolder.id.setText(stringId);
        phuongTienViewHolder.name.setText(stringName);
        phuongTienViewHolder.category.setText(stringCategory);

        holder.itemView.setOnLongClickListener(view -> {
            setPosition(holder.getAdapterPosition());
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return phuongTiens.size();
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }
}
