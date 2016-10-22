package com.codepath.nytimes.listeners;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.codepath.nytimes.R;
import com.codepath.nytimes.Utils.Helper;
import com.codepath.nytimes.adapters.ArticleAdapter;
import com.codepath.nytimes.models.Article;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mdathrika on 10/21/16.
 */
public class NYSearchResponseHandler extends JsonHttpResponseHandler {

    private ArticleAdapter articleAdapter;
    private View view;
    private List<Article> articles;
    private boolean clear;

    public NYSearchResponseHandler(ArticleAdapter articleAdapter, View view, List<Article> articles, boolean clear) {
        this.articleAdapter = articleAdapter;
        this.view = view;
        this.articles = articles;
        this.clear = clear;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            JSONArray docs;
            if(response != null) {
                // Get the docs json array
                docs = response.getJSONObject("response").getJSONArray("docs");
                // Parse json array into array of model objects
                final ArrayList<Article> Articles = Article.fromJson(docs);

                if(clear) {
                    articles.clear();
                }

                articles.addAll(Articles);
                articleAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            // Invalid JSON format, show appropriate error.
            System.out.println("****OnSuccessException****");
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response ) {

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

