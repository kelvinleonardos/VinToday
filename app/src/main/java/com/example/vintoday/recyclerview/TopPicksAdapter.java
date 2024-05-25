package com.example.vintoday.recyclerview;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vintoday.NewsActivity;
import com.example.vintoday.R;
import com.example.vintoday.models.News;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;
public class TopPicksAdapter extends RecyclerView.Adapter<TopPicksAdapter.ViewHolder> {
    private List<News> topPickList;

    public TopPicksAdapter(List<News> topPickList) {
        this.topPickList = topPickList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_pick_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News topPick = topPickList.get(position);
        holder.textView.setText(topPick.getTitle());
        if (topPick.getUrlToImage() != null) {
            Picasso.get().load(topPick.getUrlToImage()).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.no_image);
        }
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewsActivity.class);
                intent.putExtra("news", topPick);
                startActivity(v.getContext(), intent, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topPickList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;
        ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.top_pick_title);
            imageView = itemView.findViewById(R.id.top_pick_image);
            constraintLayout = itemView.findViewById(R.id.top_pick_card);
        }
    }
}
