package com.justApp.RadioPlayer.ui.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.io.IOException;

/**
 * @author Sergey Rodionov
 */

public class PlaybackManager implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener {

    private static final String TAG = PlaybackManager.class.getSimpleName();

    private final RadioPlayerService mService;
    private final WifiManager.WifiLock mWifiLock;
    private final MediaSessionCompat mMediaSessionCompat;
    private PlaybackStateCompat mPlaybackStateCompat;
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    public PlaybackManager(RadioPlayerService service, MediaSessionCompat mediaSession) {
        Context context = service.getApplicationContext();
        mService = service;
        mWifiLock = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, TAG);
        mMediaSessionCompat = mediaSession;
        updatePlaybackState(PlaybackStateCompat.STATE_NONE);
    }

    public void stop() {
        updatePlaybackState(PlaybackStateCompat.STATE_STOPPED);
        releaseResources(true);
        mService.updateSession();
    }

    private void createMediaPlayerIfNeeded() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();

            mMediaPlayer.setWakeMode(mService.getApplicationContext(),
                    PowerManager.PARTIAL_WAKE_LOCK);

            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnErrorListener(this);
        } else {
            mMediaPlayer.reset();
        }
    }

    public void play(Uri uri) {
        try {
            updatePlaybackState(PlaybackStateCompat.STATE_BUFFERING);
            createMediaPlayerIfNeeded();

            mMediaPlayer.setDataSource(mService, uri);
            mMediaPlayer.prepareAsync();
            mWifiLock.acquire();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    public void resume() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            updatePlaybackState(PlaybackStateCompat.STATE_PLAYING);
        }
    }

    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
        updatePlaybackState(PlaybackStateCompat.STATE_PAUSED);
        releaseResources(false);
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                if (mMediaPlayer == null) {
                    createMediaPlayerIfNeeded();
                } else if (!mMediaPlayer.isPlaying()) {
                    resume();
                    updatePlaybackState(PlaybackStateCompat.STATE_PLAYING);
                    mService.createNotification();
                }
                mMediaPlayer.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if (mMediaPlayer.isPlaying()) {
                    stop();
                    mService.createNotification();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.setVolume(0.1f, 0.1f);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        Log.d(TAG, "MediaPlayer error " + what + " (" + extra + ")");
        onErrorPlayback();
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mService.registerBecomingNoisyReceiver();
        mMediaSessionCompat.setActive(true);
        mMediaPlayer.start();
        updatePlaybackState(PlaybackStateCompat.STATE_PLAYING);
        mService.createNotification();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stop();
    }

    private void releaseResources(boolean releaseMediaPlayer) {
        mService.stopForeground(true);

        if (releaseMediaPlayer && mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        if (mWifiLock.isHeld()) {
            mWifiLock.release();
        }

        removeAudioFocus();
    }

    public boolean requestAudioFocus() {
        mAudioManager = (AudioManager) mService.getSystemService(Context.AUDIO_SERVICE);
        int result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    private void removeAudioFocus() {
        mAudioManager.abandonAudioFocus(this);
    }

    public void updatePlaybackState(int playbackState) {
        mPlaybackStateCompat = new PlaybackStateCompat.Builder()
                .setState(playbackState, 0, 1.0f)
                .build();
        mMediaSessionCompat.setPlaybackState(mPlaybackStateCompat);
    }

    public int getPlaybackState() {
        return mPlaybackStateCompat.getState();
    }

    public void onErrorPlayback() {
        updatePlaybackState(PlaybackStateCompat.STATE_ERROR);
    }
}
