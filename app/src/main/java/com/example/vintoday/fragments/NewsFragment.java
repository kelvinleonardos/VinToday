package com.example.vintoday.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.vintoday.R;
import com.example.vintoday.api.ApiService;
import com.example.vintoday.api.NewsResponse;
import com.example.vintoday.api.RetrofitClient;
import com.example.vintoday.models.News;
import com.example.vintoday.recyclerview.CategoryAdapter;
import com.example.vintoday.recyclerview.NewsAdapter;
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
    ProgressBar progressBar;
    Button trybtnnews;
    LinearLayout llnonews, content;


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

        content = view.findViewById(R.id.content_view);

        llnonews = view.findViewById(R.id.llnonews);
        trybtnnews = view.findViewById(R.id.btn_retry_news);

        trybtnnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetConnection();
            }
        });

        checkInternetConnection();

        loadData("all");

    }

    private void loadData(String category) {
        progressBar = getView().findViewById(R.id.pb_n);
        progressBar.setVisibility(View.VISIBLE);
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
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {

                t.printStackTrace();
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onItemClick(String category) {
        loadData(category);
    }

    private void checkInternetConnection() {
        if (!isConnectedToInternet(getContext())) {
            Log.d("halo", "halo");
            llnonews.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
            Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        } else {
            Log.d("halo", "hai");
            llnonews.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
            loadData("all");
        }
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}