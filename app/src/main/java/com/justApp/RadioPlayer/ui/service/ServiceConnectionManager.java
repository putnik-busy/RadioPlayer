package com.justApp.RadioPlayer.ui.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.media.session.MediaControllerCompat;

import com.justApp.RadioPlayer.App;
import com.justApp.RadioPlayer.data.pojo.Station;

import java.util.List;


/**
 * @author Sergey Rodionov
 */
public class ServiceConnectionManager {

    private final Context mContext;
    private final MediaControllerCompat.Callback mMediaControllerCallback;
    private final ServiceConnection mServiceConnection;
    private RadioPlayerService mRadioPlayerService;
    private boolean mServiceBound;
    private MediaControllerCompat mMediaController;

    public ServiceConnectionManager(MediaControllerCompat.Callback mediaControllerCallback) {
        mContext = App.getInstance();
        mMediaControllerCallback = mediaControllerCallback;
        mServiceConnection = new RadioPlayerServiceConnection();
    }

    public void bindService() {
        Intent intent = new Intent(mContext, RadioPlayerService.class);
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        mContext.startService(intent);
    }

    public void unbindService() {
        mContext.unbindService(mServiceConnection);
        mRadioPlayerService = null;
    }

    public void updateCurrentStation(Station station) {
        if (mServiceBound) {
            mRadioPlayerService.updateCurrentStation(station);
        }
    }

    public void updateStationList(List<Station> stations) {
        if (mServiceBound) {
            mRadioPlayerService.setStationList(stations);
        }
    }

    public boolean isServiceBound() {
        return mServiceBound;
    }

    public boolean isPlaying() {
        return mServiceBound && mRadioPlayerService.isPlaying();
    }

    public int getPlaybackState() {
        return mMediaController.getPlaybackState().getState();
    }

    public void playbackPause() {
        if (mServiceBound) {
            mMediaController.getTransportControls().pause();
        }
    }

    public void playbackPlay() {
        if (mServiceBound) {
            mMediaController.getTransportControls().play();
        }
    }

    public void playSpecifiedStation(int position) {
        if (mServiceBound) {
            mRadioPlayerService.playSpecifiedStation(position);
        }
    }

    public void playbackNext() {
        if (mServiceBound) {
            mMediaController.getTransportControls().skipToNext();
        }
    }

    public void playbackPrev() {
        if (mServiceBound) {
            mMediaController.getTransportControls().skipToPrevious();
        }
    }

    private class RadioPlayerServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                RadioPlayerService.RadioPlayerServiceBinder binder =
                        (RadioPlayerService.RadioPlayerServiceBinder) service;
                mRadioPlayerService = binder.getService();
                mServiceBound = true;

                mMediaController = new MediaControllerCompat(mContext,
                        mRadioPlayerService.getMediaSessionToken());
                mMediaController.registerCallback(mMediaControllerCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (mMediaController != null) {
                mMediaController.unregisterCallback(mMediaControllerCallback);
                mServiceBound = false;
            }
        }
    }
}
