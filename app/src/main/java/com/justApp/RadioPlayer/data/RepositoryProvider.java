package com.justApp.RadioPlayer.data;

import com.justApp.RadioPlayer.App;
import com.justApp.RadioPlayer.data.repository.StationRepository;
import com.justApp.RadioPlayer.data.repository.local.LocalSource;
import com.justApp.RadioPlayer.data.repository.remote.RemoteSource;

/**
 * @author Sergey Rodionov
 */

public class RepositoryProvider {

    public static StationRepository provideStationRepository() {
        return StationRepository.getInstance(RemoteSource.getInstance(),
                LocalSource.getInstance(App.getInstance()));
    }
}
