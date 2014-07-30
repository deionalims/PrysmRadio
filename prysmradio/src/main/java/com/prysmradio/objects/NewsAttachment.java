package com.prysmradio.objects;

import java.io.Serializable;

/**
 * Created by fxoxe_000 on 27/07/2014.
 */
public class NewsAttachment implements Serializable {

    private String thumbnail;
    private String medium;
    private String postThumbnail;
    private String postThumb;
    private String mediumThumb;
    private String squareThumb;
    private String smallThumb;


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getPostThumbnail() {
        return postThumbnail;
    }

    public void setPostThumbnail(String postThumbnail) {
        this.postThumbnail = postThumbnail;
    }

    public String getPostThumb() {
        return postThumb;
    }

    public void setPostThumb(String postThumb) {
        this.postThumb = postThumb;
    }

    public String getMediumThumb() {
        return mediumThumb;
    }

    public void setMediumThumb(String mediumThumb) {
        this.mediumThumb = mediumThumb;
    }

    public String getSquareThumb() {
        return squareThumb;
    }

    public void setSquareThumb(String squareThumb) {
        this.squareThumb = squareThumb;
    }

    public String getSmallThumb() {
        return smallThumb;
    }

    public void setSmallThumb(String smallThumb) {
        this.smallThumb = smallThumb;
    }
}
