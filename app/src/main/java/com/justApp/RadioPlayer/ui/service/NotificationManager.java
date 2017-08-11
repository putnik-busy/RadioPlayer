package com.justApp.RadioPlayer.ui.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.justApp.RadioPlayer.R;
import com.justApp.RadioPlayer.ui.activity.StationsActivity;

import static com.justApp.RadioPlayer.ui.service.RadioPlayerService.ACTION_NEXT;
import static com.justApp.RadioPlayer.ui.service.RadioPlayerService.ACTION_PAUSE;
import static com.justApp.RadioPlayer.ui.service.RadioPlayerService.ACTION_PLAY;
import static com.justApp.RadioPlayer.ui.service.RadioPlayerService.ACTION_PREV;
import static com.justApp.RadioPlayer.ui.service.RadioPlayerService.ACTION_STOP;

/**
 * @author Sergey Rodionov
 */

public class NotificationManager {

    public static final int NOTIFICATION_ID = 101;
    private static final String TAG = NotificationManager.class.getSimpleName();
    private static final String PREV = "Previous";
    private static final String NEXT = "Next";
    private static final String PLAY_PAUSE = "PlayPause";
    private final Context mContext;
    private final PlaybackManager mPlayback;
    private NotificationManagerCompat mNotificationManager;
    private Notification mNotification;
    private NotificationCompat.MediaStyle mMediaStyle;
    private MediaDescriptionCompat mMediaDescription;
    private PendingIntent mClickIntent;

    public NotificationManager(Context context, PlaybackManager playback) {
        mContext = context;
        mPlayback = playback;
        mNotificationManager = NotificationManagerCompat.from(mContext);
    }

    public void createNotification(MediaMetadataCompat metadata) {
        mMediaDescription = metadata.getDescription();
        mMediaStyle = new NotificationCompat.MediaStyle();

        Intent launchIntent = new Intent(mContext, StationsActivity.class);
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mClickIntent = PendingIntent.getActivity(mContext, 0, launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        loadIconNotification(mMediaDescription.getIconUri());
    }

    public void cancelNotify() {
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    private void buildNotification(Bitmap resource) {
        int state = mPlayback.getPlaybackState();
        int playButtonResId = state == PlaybackStateCompat.STATE_PLAYING
                ? R.drawable.ic_pause : R.drawable.ic_play;
        String playPauseAction = state == PlaybackStateCompat.STATE_PLAYING
                ? ACTION_PAUSE : ACTION_PLAY;

        Intent stopIntent = new Intent(mContext, RadioPlayerService.class);
        stopIntent.setAction(ACTION_STOP);
        PendingIntent removeNotification = PendingIntent.getService(mContext, 1, stopIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setContentIntent(mClickIntent)
                .setTicker(mContext.getString(R.string.notification_playing_stream))
                .setSmallIcon(R.drawable.ic_default_art)
                .setContentTitle(mMediaDescription.getTitle())
                .setContentText(mMediaDescription.getDescription())
                .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                .setLargeIcon(resource)
                .setStyle(mMediaStyle)
                .setShowWhen(false)
                .setOngoing(true)
                .addAction(generateAction(R.drawable.ic_skip_previous, PREV, ACTION_PREV))
                .addAction(generateAction(playButtonResId, PLAY_PAUSE, playPauseAction))
                .addAction(generateAction(R.drawable.ic_skip_next, NEXT, ACTION_NEXT))
                .setDeleteIntent(removeNotification);

        mNotification = builder.build();
    }

    private void loadIconNotification(Uri uri) {
        Glide.with(mContext)
                .load(uri)
                .asBitmap()
                .animate(android.R.anim.fade_in)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        buildNotification(resource);
                        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
                    }
                });
    }

    private NotificationCompat.Action generateAction(int icon, String title, String intentAction) {
        Intent intent = new Intent(mContext, RadioPlayerService.class);
        intent.setAction(intentAction);
        PendingIntent pendingIntent = PendingIntent.getService(mContext, 1, intent, 0);
        return new NotificationCompat.Action.Builder(icon, title, pendingIntent).build();
    }
}
