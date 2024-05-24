package com.example.vintoday.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vintoday.R;
import com.example.vintoday.api.ApiService;
import com.example.vintoday.api.NewsResponse;
import com.example.vintoday.api.RetrofitClient;
import com.example.vintoday.models.News;
import com.example.vintoday.recyclerview.CategoryAdapter;
import com.example.vintoday.recyclerview.NewsAdapter;
import com.example.vintoday.utils.Network;
import com.example.vintoday.utils.Strings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment implements CategoryAdapter.OnItemClickListener{

    private List<News> newsList = new ArrayList<>();
    private List<String> categoryList = new ArrayList<>();
    NewsAdapter newsAdapter;
    CategoryAdapter categoryAdapter;
    ApiService apiService;
    RecyclerView newsrecyclerView, categoryRecyclerView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryList.add("all");
        categoryList.add("business");
        categoryList.add("entertainment");
        categoryList.add("general");
        categoryList.add("health");
        categoryList.add("science");
        categoryList.add("sports");
        categoryList.add("technology");

        apiService = RetrofitClient.getClient().create(ApiService.class);
        newsrecyclerView = view.findViewById(R.id.news_recycler_view);
        newsrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsAdapter = new NewsAdapter(newsList);
        newsrecyclerView.setAdapter(newsAdapter);

        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(categoryList, this);
        categoryRecyclerView.setAdapter(categoryAdapter);

        loadData("all");

    }

    private void loadData(String category) {
        newsList.clear();
        Call<NewsResponse> call = apiService.getAllNews(category, Strings.API_KEY);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    final List<News> news = response.body().getData();
                    newsList.addAll(news);
                    newsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onItemClick(String category) {
        Log.d("TAG", "onItemClick: " + category);
        loadData(category);
    }

}