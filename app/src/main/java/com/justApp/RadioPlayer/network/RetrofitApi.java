package com.justApp.RadioPlayer.network;

import com.justApp.RadioPlayer.data.pojo.Station;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * @author Sergey Rodionov
 */

public interface RetrofitApi {

    @GET("popular")
    Observable<List<Station>> getStationList();
}
