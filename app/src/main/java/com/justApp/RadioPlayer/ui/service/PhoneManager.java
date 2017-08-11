package com.justApp.RadioPlayer.ui.service;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * @author Sergey Rodionov
 */
public class PhoneManager {

    private final RadioPlayerService mRadioPlayerService;
    private final PlaybackManager mPlayback;
    private boolean mOngoingCall;
    private PhoneStateListener mPhoneStateListener;
    private TelephonyManager mTelephonyManager;

    public PhoneManager(RadioPlayerService service, PlaybackManager playback) {
        mRadioPlayerService = service;
        mPlayback = playback;
    }

    public void callStateListener() {
        mTelephonyManager = (TelephonyManager) mRadioPlayerService.
                getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneStateListener = new PhoneListener();
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public void removePhoneStateListener() {
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    private class PhoneListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_OFFHOOK:
                case TelephonyManager.CALL_STATE_RINGING:
                    if (mPlayback.isPlaying()) {
                        mPlayback.stop();
                        mOngoingCall = true;
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mOngoingCall) {
                        mOngoingCall = false;
                        mPlayback.resume();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
