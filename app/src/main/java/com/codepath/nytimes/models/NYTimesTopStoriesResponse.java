package com.codepath.nytimes.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdathrika on 10/22/16.
 */
public class NYTimesTopStoriesResponse {

    List <Article> results;

    public NYTimesTopStoriesResponse() {
        results = new ArrayList<>();
    }

    public class Article {
        String title;
        String url;

        List<Multimedia> multimedia;

        public Article() {
            multimedia = new ArrayList<>();
        }
    }

    public class Multimedia {
        @SerializedName("url")
        String imageUrl;
    }
}
