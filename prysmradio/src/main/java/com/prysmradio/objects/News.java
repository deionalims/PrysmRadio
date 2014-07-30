package com.prysmradio.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fxoxe_000 on 29/04/2014.
 */
public class News implements Serializable {

    private int id;
    private String lang;
    private String slug;
    private String url;
    private String title;
    private String content;
    private String excerpt;
    private String date;
    private String modified;
    private String author;
    private int countView;
    private String thumbnail;
    private String imageHeader;
    private ArrayList<Link> links;
    private ArrayList<Link> iframe;
    private ArrayList<NewsAttachment> attachment;

    @SerializedName("pseudoHTML")
    private String pseudoHTML;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCountView() {
        return countView;
    }

    public void setCountView(int countView) {
        this.countView = countView;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public String getImageHeader() {
        return imageHeader;
    }

    public void setImageHeader(String imageHeader) {
        this.imageHeader = imageHeader;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

    public ArrayList<Link> getIframe() {
        return iframe;
    }

    public void setIframe(ArrayList<Link> iframe) {
        this.iframe = iframe;
    }

    public ArrayList<NewsAttachment> getAttachment() {
        return attachment;
    }

    public void setAttachment(ArrayList<NewsAttachment> attachment) {
        this.attachment = attachment;
    }

    public String getPseudoHTML() {
        return pseudoHTML;
    }

    public void setPseudoHTML(String pseudoHTML) {
        this.pseudoHTML = pseudoHTML;
    }

    @SuppressWarnings("serial")
    public static class List extends ArrayList<News>{}
}
