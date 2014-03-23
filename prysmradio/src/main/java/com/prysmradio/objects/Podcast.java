package com.prysmradio.objects;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fxoxe_000 on 23/03/2014.
 */
public class Podcast implements Serializable {

    private String title;
    private int id;
    private String lang;
    private PodcastInfo infos;
    private int episodeNum;
    private List<PodcastEpisode> episodes;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public PodcastInfo getInfos() {
        return infos;
    }

    public void setInfos(PodcastInfo infos) {
        this.infos = infos;
    }

    public int getEpisodeNum() {
        return episodeNum;
    }

    public void setEpisodeNum(int episodeNum) {
        this.episodeNum = episodeNum;
    }

    public List<PodcastEpisode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<PodcastEpisode> episodes) {
        this.episodes = episodes;
    }
}
