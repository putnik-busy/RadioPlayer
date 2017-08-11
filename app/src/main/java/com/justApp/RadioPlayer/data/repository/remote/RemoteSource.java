package com.justApp.RadioPlayer.data.repository.remote;

import com.justApp.RadioPlayer.network.RetrofitApi;
import com.justApp.RadioPlayer.network.RetrofitBuilder;
import com.justApp.RadioPlayer.data.pojo.Station;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Sergey Rodionov
 */

public class RemoteSource {

    private static RemoteSource INSTANCE;

    private RemoteSource() {
    }

    public static RemoteSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteSource();
        }
        return INSTANCE;
    }

    public Observable<List<Station>> loadStationsList() {
        return RetrofitBuilder.createRetrofitService(RetrofitApi.class)
                .getStationList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
