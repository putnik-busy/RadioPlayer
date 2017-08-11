package com.justApp.RadioPlayer.ui.base;

import android.support.annotation.NonNull;

/**
 * @author Sergey Rodionov
 */
public interface BasePresenter<V extends BaseView> {

    void subscribe(@NonNull V view);

    void unSubscribe();
}
