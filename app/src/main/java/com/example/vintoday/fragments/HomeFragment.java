package com.example.vintoday.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.vintoday.R;
import com.example.vintoday.api.ApiService;
import com.example.vintoday.api.NewsResponse;
import com.example.vintoday.api.RetrofitClient;
import com.example.vintoday.models.News;
import com.example.vintoday.recyclerview.NewsAdapter;
import com.example.vintoday.recyclerview.RecomendationsAdapter;
import com.example.vintoday.recyclerview.TopPicksAdapter;
import com.example.vintoday.utils.Debouncer;
import com.example.vintoday.utils.LanguageUtils;
import com.example.vintoday.utils.Strings;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    ApiService apiService;
    RecyclerView tprecyclerView, rcrecyclerView, screcyclerView;
    RecomendationsAdapter recomendationsAdapter;
    TopPicksAdapter topPicksAdapter;
    NewsAdapter newsAdapter;
    ProgressBar pBR, pBT, pBS;
    Button trybtn;
    MaterialCardView searchcard;
    List<News> topPicksList = new ArrayList<>();
    List<News> recomendationsList = new ArrayList<>();
    List<News> searchNews = new ArrayList<>();
    LinearLayout llt, llr, lls, llnohome;
    TextView sr;


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
        screcyclerView = view.findViewById(R.id.rv_search);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        tprecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rcrecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        screcyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        topPicksAdapter = new TopPicksAdapter(topPicksList);
        recomendationsAdapter = new RecomendationsAdapter(recomendationsList);
        tprecyclerView.setAdapter(topPicksAdapter);
        rcrecyclerView.setAdapter(recomendationsAdapter);

        loadTopPicksData("all");
        loadRecomendationsData("all");

        llt = view.findViewById(R.id.llt);
        llr = view.findViewById(R.id.llr);
        lls = view.findViewById(R.id.lls);
        llnohome = view.findViewById(R.id.llnohome);
        trybtn = view.findViewById(R.id.btn_retry);
        searchcard = view.findViewById(R.id.card_search);

        trybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetConnection();
            }
        });

        checkInternetConnection();

        androidx.appcompat.widget.SearchView searchView = view.findViewById(R.id.search_view);
        Debouncer debouncer = new Debouncer(1000);
        sr = view.findViewById(R.id.tv_search_result);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    llt.setVisibility(View.GONE);
                    llr.setVisibility(View.GONE);
                    lls.setVisibility(View.VISIBLE);
                    newsAdapter = new NewsAdapter(searchNews);
                    screcyclerView.setAdapter(newsAdapter);
                    loadSearchData(query);
                    String n = getString(R.string.search_result);
                } else {
                    llt.setVisibility(View.VISIBLE);
                    llr.setVisibility(View.VISIBLE);
                    lls.setVisibility(View.GONE);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    llt.setVisibility(View.GONE);
                    llr.setVisibility(View.GONE);
                    lls.setVisibility(View.VISIBLE);
                    newsAdapter = new NewsAdapter(searchNews);
                    screcyclerView.setAdapter(newsAdapter);
                    debouncer.debounce(() -> loadSearchData(newText));
                } else {
                    llt.setVisibility(View.VISIBLE);
                    llr.setVisibility(View.VISIBLE);
                    lls.setVisibility(View.GONE);
                }
                return false;
            }
        });

    }

    private void loadTopPicksData(String search) {
        topPicksList.clear();
        pBT = getView().findViewById(R.id.pb_t);
        pBT.setVisibility(View.VISIBLE);

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
                pBT.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                t.printStackTrace();
                pBT.setVisibility(View.GONE);
            }
        });
    }

    private void loadRecomendationsData(String search) {
        recomendationsList.clear();
        pBR = getView().findViewById(R.id.pb_r);
        pBR.setVisibility(View.VISIBLE);

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
                pBR.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                t.printStackTrace();
                pBR.setVisibility(View.GONE);
            }
        });
    }

    private void loadSearchData(String category) {
        pBS = getView().findViewById(R.id.pb_search);
        pBS.setVisibility(View.VISIBLE);
        searchNews.clear();
        Call<NewsResponse> call = apiService.getAllNews(category, Strings.API_KEY);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    final List<News> news = response.body().getData();
                    searchNews.addAll(news);
                    newsAdapter.notifyDataSetChanged();
                    String n = getString(R.string.search_result);
                    if (searchNews.isEmpty()) {
                        String m = getString(R.string.search_message);
                        sr.setText(n + " " + m);
                    } else {
                        sr.setText(n);
                    }
                }
                pBS.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                t.printStackTrace();
                pBS.setVisibility(View.GONE);
            }
        });
    }

    private void checkInternetConnection() {
        if (!isConnectedToInternet(getContext())) {
            llnohome.setVisibility(View.VISIBLE);
            searchcard.setVisibility(View.GONE);
            llt.setVisibility(View.GONE);
            llr.setVisibility(View.GONE);
            lls.setVisibility(View.GONE);
            Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        } else {
            llnohome.setVisibility(View.GONE);
            searchcard.setVisibility(View.VISIBLE);
            llt.setVisibility(View.VISIBLE);
            llr.setVisibility(View.VISIBLE);
            lls.setVisibility(View.GONE);
            loadTopPicksData("all");
            loadRecomendationsData("all");
        }
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}