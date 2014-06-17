package com.prysmradio.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by fxoxe_000 on 12/04/2014.
 */
public class TrackCovers implements Serializable {

    @SerializedName("30") private String cover30x30;
    @SerializedName("60") private String cover60x60;
    @SerializedName("100") private String cover100x100;
    @SerializedName("200") private String cover200x200;
    @SerializedName("400") private String cover400x400;
    @SerializedName("600") private String cover600x600;
    @SerializedName("1400") private String cover1400x1400;


    public String getCover30x30() {
        return cover30x30;
    }

    public void setCover30x30(String cover30x30) {
        this.cover30x30 = cover30x30;
    }

    public String getCover60x60() {
        return cover60x60;
    }

    public void setCover60x60(String cover60x60) {
        this.cover60x60 = cover60x60;
    }

    public String getCover100x100() {
        return cover100x100;
    }

    public void setCover100x100(String cover100x100) {
        this.cover100x100 = cover100x100;
    }

    public String getCover200x200() {
        return cover200x200;
    }

    public void setCover200x200(String cover200x200) {
        this.cover200x200 = cover200x200;
    }

    public String getCover400x400() {
        return cover400x400;
    }

    public void setCover400x400(String cover400x400) {
        this.cover400x400 = cover400x400;
    }

    public String getCover600x600() {
        return cover600x600;
    }

    public void setCover600x600(String cover600x600) {
        this.cover600x600 = cover600x600;
    }

    public String getCover1400x1400() {
        return cover1400x1400;
    }

    public void setCover1400x1400(String cover1400x1400) {
        this.cover1400x1400 = cover1400x1400;
    }
}
