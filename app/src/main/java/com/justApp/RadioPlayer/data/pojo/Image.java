package com.justApp.RadioPlayer.data.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "url",
        "thumb"
})
public final class Image {

    @JsonProperty("url")
    private String url;
    @JsonProperty("thumb")
    private Thumb thumb;
    private int mIdStation;

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("thumb")
    public Thumb getThumb() {
        return thumb;
    }

    @JsonProperty("thumb")
    public void setThumb(Thumb thumb) {
        this.thumb = thumb;
    }

    public int getIdStation() {
        return mIdStation;
    }

    public void setIdStation(int idStation) {
        mIdStation = idStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Image image = (Image) o;
        return mIdStation == image.mIdStation &&
                Objects.equal(url, image.url) &&
                Objects.equal(thumb, image.thumb);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(url, thumb, mIdStation);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("url", url)
                .add("thumb", thumb)
                .add("mIdStation", mIdStation)
                .toString();
    }
}
