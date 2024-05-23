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
import com.example.vintoday.recyclerview.NewsAdapter;
import com.example.vintoday.utils.Network;
import com.example.vintoday.utils.Strings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {

    private List<News> newsList = new ArrayList<>();
    NewsAdapter newsAdapter;
    ApiService apiService;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsAdapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(newsAdapter);

        loadData();

    }

    private void loadData() {
        Call<NewsResponse> call = apiService.getAllNews("all", Strings.API_KEY);
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

}