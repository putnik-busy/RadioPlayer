package com.justApp.RadioPlayer.data.repository;

import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;

import com.justApp.RadioPlayer.data.pojo.NowPlaying;
import com.justApp.RadioPlayer.data.pojo.Station;
import com.justApp.RadioPlayer.data.repository.remote.RemoteSource;

import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;

import rx.Observable;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * @author Sergey Rodionov
 */
public class StationRepository implements DataSource {

    private static final long EXPIRED_TIME = 60 * 60 * 1000;
    private static final int CACHE_SIZE = 30;
    private static StationRepository INSTANCE;
    private final RemoteSource mStationsRemoteDataSource;
    private final DataSource mStationsLocalDataSource;
    private LruCache<String, Station> mCacheStations;
    private boolean mIsExpiredData;
    private boolean mIsNowPlaying;

    private StationRepository(RemoteSource stationsRemoteDataSource, DataSource
            stationsLocalDataSource) {
        mStationsRemoteDataSource = stationsRemoteDataSource;
        mStationsLocalDataSource = stationsLocalDataSource;
    }

    public static StationRepository getInstance(RemoteSource stationsRemoteDataSource,
                                                DataSource stationsLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new StationRepository(stationsRemoteDataSource, stationsLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<Station>> getStations() {
        long lastUpdate = getLastUpdate();

        if (mCacheStations == null) {
            mCacheStations = new LruCache<>(CACHE_SIZE);
        }

        if (lastUpdate == -1) {
            return getAndSaveRemoteDataSource();
        } else {
            mIsExpiredData = Calendar.getInstance().after(lastUpdate + EXPIRED_TIME);

            if (!mIsExpiredData && !mCacheStations.snapshot().isEmpty()) {
                return Observable.from(mCacheStations.snapshot().values()).toList();
            }

            if (mIsExpiredData) {
                return getAndRefreshRemoteDataSource();
            } else {
                return getAndCacheLocalStations();
            }
        }
    }

    @Override
    public Observable<Station> getStation(@NonNull final Integer stationId) {
        checkNotNull(stationId);

        final Station cachedStation = getStationWithId(stationId);

        if (cachedStation != null) {
            return Observable.just(cachedStation);
        }

        if (mCacheStations == null) {
            mCacheStations = new LruCache<>(30);
        }

        Observable<Station> localStation = getStationWithIdFromLocalRepository(stationId);
        Observable<Station> remoteStation = getAndSaveRemoteDataSource()
                .flatMap(Observable::from)
                .filter(station -> station.getId().equals(stationId));

        return Observable.concat(localStation, remoteStation).first()
                .map(station -> {
                    if (station == null) {
                        throw new NoSuchElementException("No station found with Id " +
                                stationId);
                    }
                    return station;
                });
    }

    @Override
    public void saveStations(@NonNull List<Station> stationList) {
        checkNotNull(stationList);
        mStationsLocalDataSource.saveStations(stationList);
    }

    @Override
    public void refreshStations(@NonNull List<Station> stationList) {
        checkNotNull(stationList);
        mStationsLocalDataSource.refreshStations(stationList);
    }

    @Override
    public long getLastUpdate() {
        return mStationsLocalDataSource.getLastUpdate();
    }

    @Override
    public void saveNowPlaying(@NonNull NowPlaying nowPlaying) {
        checkNotNull(nowPlaying);
        if (isNowPlaying()) {
            refreshNowPlaying(nowPlaying);
        } else {
            mStationsLocalDataSource.saveNowPlaying(nowPlaying);
        }
        mIsNowPlaying = true;
    }

    @Override
    public Observable<NowPlaying> getNowPlayingStation() {
        return mStationsLocalDataSource.getNowPlayingStation();
    }

    @Override
    public void refreshNowPlaying(@NonNull NowPlaying nowPlaying) {
        mStationsLocalDataSource.refreshNowPlaying(nowPlaying);
    }

    private Observable<List<Station>> getAndSaveRemoteDataSource() {
        return mStationsRemoteDataSource
                .loadStationsList()
                .flatMap(stations -> {
                    saveStations(stations);
                    saveCache(stations);
                    return Observable.just(stations);
                });

    }

    private Observable<List<Station>> getAndCacheLocalStations() {
        return mStationsLocalDataSource.getStations()
                .flatMap(stations -> {
                    saveCache(stations);
                    return Observable.just(stations);
                });
    }

    private Observable<List<Station>> getAndRefreshRemoteDataSource() {
        return mStationsRemoteDataSource
                .loadStationsList()
                .flatMap(stations -> {
                    refreshStations(stations);
                    saveCache(stations);
                    return Observable.just(stations);
                });
    }

    private Station getStationWithId(@NonNull Integer id) {
        checkNotNull(id);
        if (mCacheStations == null || mCacheStations.snapshot().isEmpty()) {
            return null;
        } else {
            return mCacheStations.get(String.valueOf(id));
        }
    }

    private boolean isNowPlaying() {
        getNowPlayingStation()
                .subscribe(nowPlaying -> mIsNowPlaying = true);
        return mIsNowPlaying;
    }

    @NonNull
    private Observable<Station> getStationWithIdFromLocalRepository(@NonNull final Integer stationsId) {
        return mStationsLocalDataSource
                .getStation(stationsId)
                .doOnNext(station ->
                        mCacheStations.put(String.valueOf(station.getId()), station))
                .first();
    }

    private void saveCache(List<Station> stations) {
        mCacheStations.evictAll();
        for (Station station : stations) {
            mCacheStations.put(station.getId().toString(), station);
        }
    }

    public void setExpiredData(boolean expiredData) {
        mIsExpiredData = expiredData;
    }
}
