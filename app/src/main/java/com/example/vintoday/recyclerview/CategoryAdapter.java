package com.example.vintoday.recyclerview;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vintoday.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<String> categoryList;
    private OnItemClickListener listener;
    private int selectedItem = 0;

    public CategoryAdapter(List<String> categoryList, OnItemClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String category = categoryList.get(position);
        holder.bind(category, position, listener);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String category);
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        MaterialCardView cardView;
        LinearLayout linearLayout;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.category_name);
            cardView = itemView.findViewById(R.id.category_card);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }

        @SuppressLint("ResourceAsColor")
        void bind(final String category, final int position, final OnItemClickListener listener) {
            textView.setText(category);

            if (selectedItem == position) {
                linearLayout.setBackgroundColor(com.google.android.material.R.color.mtrl_indicator_text_color);
            } else {
                linearLayout.setBackgroundColor(Color.TRANSPARENT);
            }

            itemView.setOnClickListener(v -> {
                notifyItemChanged(selectedItem);
                selectedItem = getAdapterPosition();
                notifyItemChanged(selectedItem);
                listener.onItemClick(category);
            });
        }
    }
}