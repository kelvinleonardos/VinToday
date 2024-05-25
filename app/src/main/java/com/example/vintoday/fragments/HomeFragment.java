package com.example.vintoday.fragments;

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

import com.example.vintoday.R;
import com.example.vintoday.api.ApiService;
import com.example.vintoday.api.NewsResponse;
import com.example.vintoday.api.RetrofitClient;
import com.example.vintoday.models.News;
import com.example.vintoday.recyclerview.RecomendationsAdapter;
import com.example.vintoday.recyclerview.TopPicksAdapter;
import com.example.vintoday.utils.LanguageUtils;
import com.example.vintoday.utils.Strings;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    ApiService apiService;
    RecyclerView tprecyclerView, rcrecyclerView;
    RecomendationsAdapter recomendationsAdapter;
    TopPicksAdapter topPicksAdapter;

    List<News> topPicksList = new ArrayList<>();
    List<News> recomendationsList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tprecyclerView = view.findViewById(R.id.rv_top_picks);
        rcrecyclerView = view.findViewById(R.id.rv_recomendations);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        tprecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rcrecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        topPicksAdapter = new TopPicksAdapter(topPicksList);
        recomendationsAdapter = new RecomendationsAdapter(recomendationsList);
        tprecyclerView.setAdapter(topPicksAdapter);
        rcrecyclerView.setAdapter(recomendationsAdapter);

        loadTopPicksData("all");
        loadRecomendationsData("all");

    }

    private void loadTopPicksData(String search) {
        topPicksList.clear();
        Call<NewsResponse> call = apiService.getTopPicks(search, "10", "1", Strings.API_KEY);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    final List<News> news = response.body().getData();
                    topPicksList.addAll(news);
                    topPicksAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadRecomendationsData(String search) {
        recomendationsList.clear();
        Call<NewsResponse> call = apiService.getTopPicks(search, "10", "1", Strings.API_KEY);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    final List<News> news = response.body().getData();
                    recomendationsList.addAll(news);
                    recomendationsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}