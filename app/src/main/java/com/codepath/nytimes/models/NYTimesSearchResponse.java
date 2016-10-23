package com.codepath.nytimes.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdathrika on 10/22/16.
 */
public class NYTimesSearchResponse {

    public Response response;

    public Response getResponse() {
        return response;
    }

    public class Response {
        public List<Docs> docs;
        public Response() {
            docs = new ArrayList<>();
        }

        public List<Docs> getDocs() {
            return docs;
        }
    }

    public class Docs {
        @SerializedName("web_url")
        public String webUrl;

        @SerializedName("headline")
        public Headline headline;

        public List<ArticleImage> multimedia;

        public Docs() {
            multimedia = new ArrayList<>();
        }

        public String getWebUrl() {
            return webUrl;
        }

        public Headline getHeadline() {
            return headline;
        }

        public List<ArticleImage> getMultimedia() {
            return multimedia;
        }
    }

    public class Headline {
        public String main;

        public String getMain() {
            return main;
        }
    }

    public class ArticleImage {
        public String getUrl() {
            return url;
        }

        public String url;
    }
}
