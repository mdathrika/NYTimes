package com.codepath.nytimes.listeners;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.codepath.nytimes.R;
import com.codepath.nytimes.Utils.Helper;
import com.codepath.nytimes.adapters.ArticleAdapter;
import com.codepath.nytimes.models.Article;
import com.codepath.nytimes.models.NYTimesSearchResponse;
import com.codepath.nytimes.models.NYTimesTopStoriesResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mdathrika on 10/21/16.
 */
public class NYTopStoriesResponseHandler extends TextHttpResponseHandler {

    private ArticleAdapter articleAdapter;
    private View view;
    private List<Article> articles;
    private boolean clear;

    public NYTopStoriesResponseHandler(ArticleAdapter articleAdapter, View view, List<Article> articles, boolean clear) {
        this.articleAdapter = articleAdapter;
        this.view = view;
        this.articles = articles;
        this.clear = clear;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String response) {
        Gson gson = new GsonBuilder().create();
        NYTimesTopStoriesResponse ny_response = gson.fromJson(response, NYTimesTopStoriesResponse.class);

        final List<Article> Articles = Article.fromResponse(ny_response);

        if(clear) {
            articles.clear();
        }

        articles.addAll(Articles);
        articleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

        articleAdapter.notifyDataSetChanged();

        if(!Helper.isOnline()) {
            Snackbar.make(view, R.string.snackbar_no_internet, Snackbar.LENGTH_LONG)
                    .setAction(R.string.snackbar_action, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //search(query);
                        }
                    })  // action text on the right side
                    .setActionTextColor(Color.WHITE)
                    .setDuration(3000).show();
        } else {
            Snackbar.make(view, R.string.snackbar_service_down, Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.WHITE)
                    .setDuration(3000).show();
        }

        System.out.println("******Service Error****** " + statusCode + response);
    }
}

