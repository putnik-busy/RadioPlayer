package com.justApp.RadioPlayer.ui.contract;

import android.support.annotation.NonNull;

import com.justApp.RadioPlayer.data.pojo.Station;
import com.justApp.RadioPlayer.ui.base.BasePresenter;
import com.justApp.RadioPlayer.ui.base.BaseView;

import java.util.List;

/**
 * @author Sergey Rodionov
 */

public interface StationsContract {

    interface View extends BaseView {

        void setLoading(boolean active);

        void showStations(List<Station> stations);

        void showPlayingStation(Station station);

        void showLoadingStationsError();

        void showNoStations();

        void updateBottomView(String status, boolean playing);

        void hideBottomView();
    }

    interface Presenter extends BasePresenter<View> {

        void loadStations(boolean forceUpdate);

        void saveNowPlayingStation(@NonNull Station station);

        void playPauseStation();

        void playSpecifiedStation(int id, Integer stationId);

        void playNext();

        void playPrev();
    }
}
