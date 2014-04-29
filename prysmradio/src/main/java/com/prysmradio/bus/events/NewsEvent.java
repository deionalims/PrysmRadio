package com.prysmradio.bus.events;

import com.prysmradio.objects.News;

import java.util.List;

/**
 * Created by fxoxe_000 on 29/04/2014.
 */
public class NewsEvent {

    private List<News> newsList;

    public NewsEvent(List<News> news){
        newsList = news;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }
}
