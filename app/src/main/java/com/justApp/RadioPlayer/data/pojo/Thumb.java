package com.justApp.RadioPlayer.data.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "url"
})
public final class Thumb {

    @JsonProperty("url")
    private String url;
    private int mIdImage;

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    public int getIdImage() {
        return mIdImage;
    }

    public void setIdImage(int idImage) {
        mIdImage = idImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Thumb thumb = (Thumb) o;
        return mIdImage == thumb.mIdImage &&
                Objects.equal(url, thumb.url);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(url, mIdImage);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("url", url)
                .add("mIdImage", mIdImage)
                .toString();
    }
}
