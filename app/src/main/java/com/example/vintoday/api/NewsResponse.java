package com.example.vintoday.api;

import com.example.vintoday.models.News;

import java.util.List;
public class NewsResponse{
    private List<News> articles;
    public List<News> getData() {
        return articles;
    }
    public void setData(List<News> articles) {
        this.articles = articles;
    }
}