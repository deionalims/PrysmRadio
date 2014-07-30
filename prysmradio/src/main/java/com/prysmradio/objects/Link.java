package com.prysmradio.objects;

import java.io.Serializable;

/**
 * Created by fxoxe_000 on 27/07/2014.
 */
public class Link implements Serializable {

    private String url;
    private String label;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
