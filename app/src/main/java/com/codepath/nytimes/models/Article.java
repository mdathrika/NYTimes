package com.codepath.nytimes.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdathrika on 10/19/16.
 */
public class Article implements Parcelable {

    private String headline;

    private String webUrl;

    private String thumbnailUrl;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getThumbnailUrl() {
        if(thumbnailUrl != null && thumbnailUrl.indexOf("http") != 0) {
            return "http://www.nytimes.com/" + thumbnailUrl;
        } else {
            return thumbnailUrl;
        }

    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.headline);
        dest.writeString(this.webUrl);
        dest.writeString(this.thumbnailUrl);
    }

    public Article() {
    }

    protected Article(Parcel in) {
        this.headline = in.readString();
        this.webUrl = in.readString();
        this.thumbnailUrl = in.readString();
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    /*public static Article fromJson(JSONObject jsonObject) {
        Article article = new Article();
        try {
            // Deserialize json into object fields
            // Check if a cover edition is available
            article.thumbnailUrl = getThumbnail(jsonObject);
            article.webUrl = jsonObject.getString("web_url");
            article.headline = jsonObject.getJSONObject("headline").getString("main");

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return article;
    }*/

    public static List<Article> fromResponse(NYTimesSearchResponse response) {
        List<Article> articles = new ArrayList<>();
        for(NYTimesSearchResponse.Docs doc : response.getResponse().getDocs()) {
            Article article = new Article();

            if(doc.getMultimedia() != null && doc.getMultimedia().size() > 0)
                article.thumbnailUrl = doc.getMultimedia().get(0).url;

            article.webUrl = doc.getWebUrl();
            article.headline = doc.headline.main;
            articles.add(article);
        }
        return articles;
    }

    public static List<Article> fromResponse(NYTimesTopStoriesResponse response) {
        List<Article> articles = new ArrayList<>();
        for(NYTimesTopStoriesResponse.Article art : response.results) {
            Article article = new Article();

            if(art.multimedia != null && art.multimedia.size() > 0)
                article.thumbnailUrl = art.multimedia.get(0).imageUrl;

            article.webUrl = art.url;
            article.headline = art.title;
            articles.add(article);
        }
        return articles;
    }

    // Return comma separated author list when there is more than one author
    private static String getThumbnail(final JSONObject jsonObject) {
        try {
            final JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            for(int i=0; i<multimedia.length(); i++) {
                JSONObject obj = multimedia.getJSONObject(i);
                if("thumbnail".equals(obj.getString("subtype")))
                    return obj.getString("url");
            }
        } catch (JSONException e) {
            return null;
        }

        return null;
    }

    // Decodes array of book json results into business model objects
    /*public static ArrayList<Article> fromJson(JSONArray jsonArray) {
        ArrayList<Article> articles = new ArrayList<>(jsonArray.length());
        // Process each result in json array, decode and convert to business
        // object
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject bookJson;
            try {
                bookJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Article article = Article.fromJson(bookJson);
            if (article != null) {
                articles.add(article);
            }
        }
        return articles;
    }*/

}
