package com.justApp.RadioPlayer.data.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "stream",
        "bitrate",
        "content_type",
        "listeners",
        "status"
})
public final class Stream {

    @JsonProperty("stream")
    private String stream;
    @JsonProperty("bitrate")
    private Integer bitrate;
    @JsonProperty("content_type")
    private String contentType;
    @JsonProperty("listeners")
    private Integer listeners;
    @JsonProperty("status")
    private Integer status;
    private int mIdStation;

    @JsonProperty("stream")
    public String getStream() {
        return stream;
    }

    @JsonProperty("stream")
    public void setStream(String stream) {
        this.stream = stream;
    }

    @JsonProperty("bitrate")
    public Integer getBitrate() {
        return bitrate;
    }

    @JsonProperty("bitrate")
    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    @JsonProperty("content_type")
    public String getContentType() {
        return contentType;
    }

    @JsonProperty("content_type")
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @JsonProperty("listeners")
    public Integer getListeners() {
        return listeners;
    }

    @JsonProperty("listeners")
    public void setListeners(Integer listeners) {
        this.listeners = listeners;
    }

    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Integer status) {
        this.status = status;
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
        Stream stream1 = (Stream) o;
        return mIdStation == stream1.mIdStation &&
                Objects.equal(stream, stream1.stream) &&
                Objects.equal(bitrate, stream1.bitrate) &&
                Objects.equal(contentType, stream1.contentType) &&
                Objects.equal(listeners, stream1.listeners) &&
                Objects.equal(status, stream1.status);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(stream, bitrate, contentType, listeners, status, mIdStation);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("stream", stream)
                .add("bitrate", bitrate)
                .add("contentType", contentType)
                .add("listeners", listeners)
                .add("status", status)
                .add("mIdStation", mIdStation)
                .toString();
    }
}
