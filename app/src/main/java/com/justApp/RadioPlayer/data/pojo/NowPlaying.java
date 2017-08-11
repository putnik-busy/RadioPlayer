package com.justApp.RadioPlayer.data.pojo;

import com.google.common.base.Objects;

/**
 * @author Sergey Rodionov
 */
public final class NowPlaying {

    private Integer mStationId;

    public Integer getStationId() {
        return mStationId;
    }

    public void setStationId(Integer stationId) {
        mStationId = stationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NowPlaying that = (NowPlaying) o;
        return Objects.equal(mStationId, that.mStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mStationId);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("mStationId", mStationId)
                .toString();
    }
}
