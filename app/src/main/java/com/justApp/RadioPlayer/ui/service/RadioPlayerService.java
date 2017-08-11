package com.justApp.RadioPlayer.ui.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.google.common.base.Strings;
import com.justApp.RadioPlayer.data.pojo.Station;
import com.justApp.RadioPlayer.data.pojo.Stream;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * @author Sergey Rodionov
 */
public class RadioPlayerService extends Service {

    public static final String TAG = RadioPlayerService.class.getSimpleName();
    public static final String ACTION_PREV = "com.justApp.RadioPlayer.prev";
    public static final String ACTION_NEXT = "com.justApp.RadioPlayer.next";
    public static final String ACTION_PLAY = "com.justApp.RadioPlayer.play";
    public static final String ACTION_PAUSE = "com.justApp.RadioPlayer.pause";
    public static final String ACTION_STOP = "com.justApp.RadioPlayer.stopAction";
    private final IBinder mRadioServiceBinder = new RadioPlayerServiceBinder();
    private PlaybackManager mPlayback;
    private PhoneManager mPhoneManager;
    private List<Station> mStations = new ArrayList<>();
    private int mCurrentPosition = -1;
    private Station mCurrentStation;
    private NotificationManager mNotificationManager;
    private MediaSessionCompat mMediaSession;
    private MediaControllerCompat mMediaController;
    private BroadcastReceiver mBecomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                mMediaController.getTransportControls().pause();
                mPlayback.pause();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaSession = new MediaSessionCompat(this, TAG);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setCallback(new MediaSessionCallback());

        mPlayback = new PlaybackManager(this, mMediaSession);

        mNotificationManager = new NotificationManager(this, mPlayback);
        mPhoneManager = new PhoneManager(this, mPlayback);
        mPhoneManager.callStateListener();
        try {
            mMediaController = new MediaControllerCompat(this, mMediaSession.getSessionToken());
        } catch (RemoteException e) {
            Timber.e("Error instantiating Media Controller: %s", e.getMessage());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (!mPlayback.requestAudioFocus()) {
                stopSelf();
            }
            checkActions(intent);
        }
        MediaButtonReceiver.handleIntent(mMediaSession, intent);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mRadioServiceBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayback.stop();
        mPhoneManager.removePhoneStateListener();
        mNotificationManager.cancelNotify();
        Timber.i("Releasing resources");
    }

    public MediaSessionCompat.Token getMediaSessionToken() {
        return mMediaSession.getSessionToken();
    }

    public void setStationList(List<Station> stations) {
        mStations.clear();
        mStations.addAll(stations);
    }

    public void playSpecifiedStation(int position) {
        if (mCurrentPosition != position) {
            mCurrentPosition = position;
            mCurrentStation = mStations.get(position);
            mMediaController.getTransportControls().play();
        }
    }

    private boolean checkConditionsPlayback() {
        int state = mPlayback.getPlaybackState();
        return !mStations.isEmpty()
                && mCurrentPosition >= 0
                && mCurrentPosition < mStations.size()
                && (state == PlaybackStateCompat.STATE_NONE
                || state == PlaybackStateCompat.STATE_STOPPED
                || state == PlaybackStateCompat.STATE_PAUSED);
    }

    public void updateCurrentStation(Station currentStation) {
        mCurrentStation = currentStation;
        mCurrentPosition = mStations.indexOf(mCurrentStation);
    }

    public boolean isPlaying() {
        return mPlayback.isPlaying();
    }

    private void updateMetadata(Station station) {
        String name = Strings.nullToEmpty(station.getName());
        String slug = Strings.nullToEmpty(station.getSlug());
        String country = Strings.nullToEmpty(station.getCountry());
        String imageUrl = Strings.nullToEmpty(station.getImage().getUrl());
        String thumbUrl = Strings.nullToEmpty(station.getImage().getThumb().getUrl());

        mMediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, name)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, slug)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, country)
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, imageUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, thumbUrl)
                .build());
    }

    public void updateSession() {
        mMediaSession.setActive(false);
        unregisterReceiver(mBecomingNoisyReceiver);
    }

    public void createNotification() {
        MediaMetadataCompat metadata = mMediaController.getMetadata();
        mNotificationManager.createNotification(metadata);
    }

    public void registerBecomingNoisyReceiver() {
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(mBecomingNoisyReceiver, intentFilter);
    }

    private void checkActions(Intent intent) {
        switch (intent.getAction()) {
            case ACTION_PREV:
                Timber.i("Calling prev");
                mMediaController.getTransportControls().skipToPrevious();
                break;
            case ACTION_PLAY:
                Timber.i("Calling play");
                mMediaController.getTransportControls().play();
                break;
            case ACTION_PAUSE:
                Timber.i("Calling pause");
                mMediaController.getTransportControls().pause();
                break;
            case ACTION_NEXT:
                Timber.i("Calling next");
                mMediaController.getTransportControls().skipToNext();
                break;
            case ACTION_STOP:
                Timber.i("Calling stop");
                mMediaController.getTransportControls().stop();
                stopSelf();
                break;
            default:
                break;
        }
    }

    @Nullable
    private String getStream(Station station) {
        String url = null;
        List<Stream> streams = station.getStreams();
        int status;
        for (Stream stream : streams) {
            status = stream.getStatus();
            if (status >= 0) {
                url = stream.getStream();
                if (url != null && !url.isEmpty()) {
                    break;
                }
            }
        }
        return url;
    }

    public class RadioPlayerServiceBinder extends Binder {
        public RadioPlayerService getService() {
            return RadioPlayerService.this;
        }
    }

    private final class MediaSessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            if (checkConditionsPlayback()) {
                Uri stationUri = Uri.parse(getStream(mCurrentStation));
                if (stationUri != null) {
                    updateMetadata(mCurrentStation);
                    mPlayback.play(stationUri);
                    createNotification();
                } else {
                    mPlayback.onErrorPlayback();
                }
            }
        }

        @Override
        public void onStop() {
            int state = mPlayback.getPlaybackState();
            if (state == PlaybackStateCompat.STATE_PLAYING ||
                    state == PlaybackStateCompat.STATE_BUFFERING) {

                mPlayback.stop();
                createNotification();

            }
        }

        @Override
        public void onPause() {
            mPlayback.pause();
            createNotification();
        }

        @Override
        public void onSkipToNext() {
            if (++mCurrentPosition >= mStations.size()) {
                mCurrentPosition = 0;
            }
            onPlay();
        }

        @Override
        public void onSkipToPrevious() {
            if (--mCurrentPosition < 0 && !mStations.isEmpty()) {
                mCurrentPosition = mStations.size() - 1;
            }
            onPlay();
        }
    }
}
