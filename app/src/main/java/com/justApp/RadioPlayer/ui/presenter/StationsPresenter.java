package com.justApp.RadioPlayer.ui.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.justApp.RadioPlayer.data.RepositoryProvider;
import com.justApp.RadioPlayer.data.pojo.NowPlaying;
import com.justApp.RadioPlayer.data.pojo.Station;
import com.justApp.RadioPlayer.data.repository.StationRepository;
import com.justApp.RadioPlayer.ui.contract.StationsContract;
import com.justApp.RadioPlayer.ui.service.PlaybackStatus;
import com.justApp.RadioPlayer.ui.service.ServiceConnectionManager;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * @author Sergey Rodionov
 */
public class StationsPresenter implements StationsContract.Presenter {

    @NonNull
    private final StationRepository mStationRepository;
    @Nullable
    private StationsContract.View mStationsView;
    @NonNull
    private CompositeSubscription mSubscriptions;
    @NonNull
    private NowPlaying mNowPlaying;
    @NonNull
    private ServiceConnectionManager mConnectionManager;

    public StationsPresenter() {
        mStationRepository = RepositoryProvider.provideStationRepository();
        mSubscriptions = new CompositeSubscription();
        mNowPlaying = new NowPlaying();
        mConnectionManager = new ServiceConnectionManager(new MediaControllerCallback());
    }

    @Override
    public void subscribe(@NonNull StationsContract.View view) {
        mStationsView = view;
        mConnectionManager.bindService();
        loadStations(false);
        loadLastPlayedStation();
    }

    @Override
    public void unSubscribe() {
        if (mSubscriptions.hasSubscriptions()) {
            mSubscriptions.clear();
        }
        StationRepository.destroyInstance();
        mConnectionManager.unbindService();
        mStationsView = null;
    }

    @Override
    public void loadStations(boolean forceUpdate) {
        loadStationsList(forceUpdate);
    }

    @Override
    public void saveNowPlayingStation(@NonNull Station station) {
        checkNotNull(station);
        mNowPlaying.setStationId(station.getId());
        mStationRepository.saveNowPlaying(mNowPlaying);
    }

    @Override
    public void playPauseStation() {
        int state = mConnectionManager.getPlaybackState();
        if (mConnectionManager.isServiceBound()) {
            if (isPlaybackStateInit(state)) {
                mConnectionManager.playbackPause();
                Timber.i("Clicked pause");
            } else if (!isPlaybackStateInit(state)) {
                mConnectionManager.playbackPlay();
                Timber.i("Clicked play");
            }
        }
    }

    @Override
    public void playSpecifiedStation(int position, Integer stationId) {
        mConnectionManager.playSpecifiedStation(position);
        loadStationById(stationId);
    }

    @Override
    public void playNext() {
        int state = mConnectionManager.getPlaybackState();
        if (isPlaybackStateInit(state)) {
            mConnectionManager.playbackNext();
        }
    }

    @Override
    public void playPrev() {
        int state = mConnectionManager.getPlaybackState();
        if (isPlaybackStateInit(state)) {
            mConnectionManager.playbackPrev();
        }
    }

    private void loadStationsList(boolean forceUpdate) {
        if (mStationsView != null) {
            if (forceUpdate) {
                mStationRepository.setExpiredData(true);
            }

            Subscription subscription = mStationRepository.getStations()
                    .doOnSubscribe(() -> mStationsView.setLoading(true))
                    .doOnTerminate(() -> mStationsView.setLoading(false))
                    .subscribe(stations -> {
                        if (!stations.isEmpty()) {
                            mStationsView.showStations(stations);
                            mConnectionManager.updateStationList(stations);
                        } else {
                            mStationsView.showNoStations();
                        }
                    }, throwable -> mStationsView.showLoadingStationsError());
            mSubscriptions.add(subscription);
        }
    }

    private void loadLastPlayedStation() {
        Subscription subscription = mStationRepository.getNowPlayingStation()
                .doOnCompleted(() -> {
                    if (mStationsView != null) {
                        mStationsView.hideBottomView();
                    }
                })
                .subscribe(nowPlaying -> loadStationById(nowPlaying.getStationId()));

        mSubscriptions.add(subscription);
    }

    private void loadStationById(@NonNull Integer stationId) {
        if (mStationsView != null) {
            Subscription subscription = mStationRepository.getStation(stationId)
                    .subscribe(station -> {
                        if (station != null) {
                            mStationsView.showPlayingStation(station);
                            int state = mConnectionManager.getPlaybackState();
                            updateBottomView(state, mConnectionManager.isPlaying());
                            mConnectionManager.updateCurrentStation(station);
                        }
                    }, throwable -> mStationsView.showLoadingStationsError());
            mSubscriptions.add(subscription);
        }
    }

    private boolean isPlaybackStateInit(int state) {
        switch (state) {
            case PlaybackStateCompat.STATE_BUFFERING:
            case PlaybackStateCompat.STATE_PLAYING:
            case PlaybackStateCompat.STATE_CONNECTING:
                return true;
            case PlaybackStateCompat.STATE_NONE:
            case PlaybackStateCompat.STATE_PAUSED:
                return false;
            default:
                break;
        }
        return false;
    }

    private void updateBottomView(int state, boolean playing) {
        if (mStationsView != null) {
            String status = PlaybackStatus.getStatusValue(state);
            mStationsView.updateBottomView(status, playing);
        }
    }

    private class MediaControllerCallback extends MediaControllerCompat.Callback {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            switch (state.getState()) {
                case PlaybackStateCompat.STATE_NONE:
                case PlaybackStateCompat.STATE_STOPPED:
                    updateBottomView(PlaybackStatus.PLAYBACK_STOP.getId(), false);
                    Timber.i("State Stopped/None");
                    break;
                case PlaybackStateCompat.STATE_BUFFERING:
                    updateBottomView(PlaybackStatus.PLAYBACK_BUFFERING.getId(), true);
                    Timber.i("State Buffering");
                    break;
                case PlaybackStateCompat.STATE_PLAYING:
                    updateBottomView(PlaybackStatus.PLAYBACK_START.getId(), true);
                    Timber.i("State Playing");
                    break;
                case PlaybackStateCompat.STATE_ERROR:
                    updateBottomView(PlaybackStatus.PLAYBACK_ERROR.getId(), false);
                    Timber.i("State Error");
                    break;
                default:
                    break;

            }
        }
    }
}
