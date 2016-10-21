package com.codepath.nytimes.serviceclient;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by mdathrika on 10/19/16.
 */
public class NYSearchClient {

    private String sortOrder;
    private String beginDate;
    private List<String> filters;

    private AsyncHttpClient client = new AsyncHttpClient();

    private String API_KEY = "a64b1a0b931941008b2b0a29f47b4313";
    private String API_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";



    public NYSearchClient() {

    }

    public NYSearchClient(String sortOrder, String beginDate, List<String> filters) {
        this.sortOrder = sortOrder;
        this.beginDate = beginDate;
        this.filters = filters;
    }

    public NYSearchClient criteria(String sortOrder, String beginDate, List<String> filters) {
        this.sortOrder = sortOrder;
        this.beginDate = beginDate;
        this.filters = filters;

        return this;
    }

    public void searchArticle(String query, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.add("api-key", API_KEY);
        params.add("q", query);
        if(filters != null && filters.size() > 0) {
            String filter = "news_desk:(";
            for(String filt : filters) {
                filter += "\"" + filt + "\" ";
            }
            filter += ")";
            params.add("fq", filter);
        }

        if(beginDate != null)
            params.add("begin_date", beginDate);

        if(sortOrder != null)
            params.add("sort", sortOrder);

        client.get(API_URL, params, handler);
    }
}
