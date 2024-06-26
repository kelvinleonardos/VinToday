package com.example.vintoday.recyclerview;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vintoday.NewsActivity;
import com.example.vintoday.R;
import com.example.vintoday.models.News;
import com.squareup.picasso.Picasso;
import java.util.List;
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    public List<News> newsList;

    public NewsAdapter(List<News> newsList){

        this.newsList = newsList;
    }
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        return new NewsViewHolder(view);
    }
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.bind(news);
    }
    public int getItemCount() {
        return newsList.size();
    }
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        private ImageView newsImageView;
        private TextView titleTextView;
        private TextView subtitleTextView;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImageView = itemView.findViewById(R.id.news_image);
            titleTextView = itemView.findViewById(R.id.news_title);
            subtitleTextView = itemView.findViewById(R.id.news_subtitle);
        }
        public void bind(News news) {
            if (news.getUrlToImage() != null && !news.getUrlToImage().isEmpty()) {
                Picasso.get().load(news.getUrlToImage()).into(newsImageView);
            } else {
                newsImageView.setImageResource(R.drawable.no_image);
            }
            titleTextView.setText(news.getTitle());
            subtitleTextView.setText(news.getDescription());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), NewsActivity.class);
                    intent.putExtra("news", news);
                    startActivity(v.getContext(), intent, null);
                }
            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String text) {
        newsList.clear();
        if (!text.isEmpty()) {
            text = text.toLowerCase();
            for (News news : newsList) {
                if (news.getTitle().toLowerCase().contains(text) || news.getContent().toLowerCase().contains(text)) {
                    newsList.add(news);
                }
            }
        }
        notifyDataSetChanged();
    }


}