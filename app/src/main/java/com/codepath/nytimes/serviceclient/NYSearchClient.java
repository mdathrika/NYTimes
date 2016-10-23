package com.codepath.nytimes.serviceclient;

import com.codepath.nytimes.models.Settings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by mdathrika on 10/19/16.
 */
public class NYSearchClient {

    private String sortOrder;
    private String beginDate;
    private List<String> filters;

    private String searchQuery;
    private int pageNumber = 0;

    private AsyncHttpClient client = new AsyncHttpClient();

    private String API_KEY = "a64b1a0b931941008b2b0a29f47b4313";
    private String API_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

    String months[] = {"01","02","03","04","05","06","07","08","09","10","11","12"};


    public NYSearchClient() {

    }

    public NYSearchClient(Settings settings) {
        this.sortOrder = settings.getSortOrder();
        this.beginDate = null;

        if(settings.getBeginDate() != null) {
            Calendar date = new GregorianCalendar();
            date.setTime(settings.getBeginDate());
            int year = date.get(Calendar.YEAR);  // 2012
            String month = months[date.get(Calendar.MONTH)];  // 9 - October!!!
            int day = date.get(Calendar.DAY_OF_MONTH);  // 5

            this.beginDate = ""+year+""+month+""+day;
        }

        this.filters = settings.getNewsCategory();
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

    public void searchArticle(String query, TextHttpResponseHandler handler) {
        searchQuery = query;
        RequestParams params = new RequestParams();
        params.add("api-key", API_KEY);
        params.add("q", query);
        params.add("page", Integer.toString(pageNumber));
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

        System.out.println("********Params::: "+params.toString());
        client.get(API_URL, params, handler);
    }

    public void getNextPage(int pageNumber, TextHttpResponseHandler handler) {
        this.pageNumber = pageNumber;
        searchArticle(searchQuery, handler);
    }

    public void topStories(TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.add("api-key", API_KEY);

        client.get("https://api.nytimes.com/svc/topstories/v2/home.json", params, handler);
    }
}
