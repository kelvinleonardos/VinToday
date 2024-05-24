package com.example.vintoday.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vintoday.R;
import com.example.vintoday.models.News;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;
public class RecomendationsAdapter extends RecyclerView.Adapter<RecomendationsAdapter.ViewHolder> {
    private List<News> recomendationList;

    public RecomendationsAdapter(List<News> recomendationList) {
        this.recomendationList = recomendationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recomendation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News recomendation = recomendationList.get(position);
        holder.title.setText(recomendation.getTitle());
        holder.caption.setText(recomendation.getDescription());
        Picasso.get().load(recomendation.getUrlToImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return recomendationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView caption;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recomendation_title);
            caption = itemView.findViewById(R.id.recomendation_description);
            imageView = itemView.findViewById(R.id.recomendation_image);
        }
    }
}
