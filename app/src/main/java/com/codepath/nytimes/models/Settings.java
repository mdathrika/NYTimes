package com.codepath.nytimes.models;

import java.util.Date;
import java.util.List;

/**
 * Created by mdathrika on 10/22/16.
 */
public class Settings {
    public Settings() {

    }

    public Settings(Date beginDate,
            String sortOrder,
            List<String> newsCategory) {
        this.beginDate = beginDate;
        this.sortOrder = sortOrder;
        this.newsCategory = newsCategory;
    }

    Date beginDate;
    String sortOrder;
    List<String> newsCategory;

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<String> getNewsCategory() {
        return newsCategory;
    }

    public void setNewsCategory(List<String> newsCategory) {
        this.newsCategory = newsCategory;
    }
}
