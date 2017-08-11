package com.justApp.RadioPlayer.ui.service;

import android.support.v4.media.session.PlaybackStateCompat;

/**
 * @author Sergey Rodionov
 */
public enum PlaybackStatus {

    PLAYBACK_STOP(PlaybackStateCompat.STATE_STOPPED, "Остановлено"),
    PLAYBACK_START(PlaybackStateCompat.STATE_PLAYING, "Воспроизводится"),
    PLAYBACK_BUFFERING(PlaybackStateCompat.STATE_BUFFERING, "Буферизация"),
    PLAYBACK_ERROR(PlaybackStateCompat.STATE_ERROR, "Ошибка воспроизведения");

    private final int mId;
    private final String mKey;

    PlaybackStatus(int id, String value) {
        mId = id;
        mKey = value;
    }

    public static String getStatusValue(int id) {
        for (PlaybackStatus statusStation : PlaybackStatus.values()) {
            if (statusStation.getId() == id) {
                return statusStation.getKey();
            }
        }
        return PLAYBACK_ERROR.getKey();
    }

    private String getKey() {
        return mKey;
    }

    public int getId() {
        return mId;
    }
}
