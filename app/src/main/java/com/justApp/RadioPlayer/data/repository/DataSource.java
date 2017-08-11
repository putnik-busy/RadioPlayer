package com.justApp.RadioPlayer.data.repository;

import android.support.annotation.NonNull;

import com.justApp.RadioPlayer.data.pojo.NowPlaying;
import com.justApp.RadioPlayer.data.pojo.Station;

import java.util.List;

import rx.Observable;

/**
 * @author Sergey Rodionov
 */

public interface DataSource {

    Observable<List<Station>> getStations();

    Observable<Station> getStation(@NonNull Integer id);

    void saveStations(@NonNull List<Station> stationList);

    void refreshStations(@NonNull List<Station> stationList);

    long getLastUpdate();

    void saveNowPlaying(@NonNull NowPlaying nowPlaying);

    Observable<NowPlaying> getNowPlayingStation();

    void refreshNowPlaying(@NonNull NowPlaying nowPlaying);
}
