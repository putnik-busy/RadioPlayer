package com.justApp.RadioPlayer.data.repository.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Sergey Rodionov
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Stations.db";
    private static final String TEXT_TYPE = "TEXT";
    private static final String INTEGER_TYPE = "INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SPACE = " ";

    private static final String CREATE_STATION_TABLE =
            "CREATE TABLE " + TableColumns.StationEntry.TABLE_NAME + " (" +
                    TableColumns.StationEntry._ID + SPACE + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    TableColumns.StationEntry.COLUMN_ID + SPACE + INTEGER_TYPE + COMMA_SEP +
                    TableColumns.StationEntry.COLUMN_NAME + SPACE + TEXT_TYPE + COMMA_SEP +
                    TableColumns.StationEntry.COLUMN_COUNTRY + SPACE + TEXT_TYPE + COMMA_SEP +
                    TableColumns.StationEntry.COLUMN_SLUG + SPACE + TEXT_TYPE + COMMA_SEP +
                    TableColumns.StationEntry.COLUMN_WEBSITE + SPACE + TEXT_TYPE + COMMA_SEP +
                    TableColumns.StationEntry.COLUMN_TWITTER + SPACE + TEXT_TYPE + COMMA_SEP +
                    TableColumns.StationEntry.COLUMN_FACEBOOK + SPACE + TEXT_TYPE + COMMA_SEP +
                    TableColumns.StationEntry.COLUMN_TOTAL_LISTENER + SPACE + INTEGER_TYPE +
                    COMMA_SEP +
                    TableColumns.StationEntry.COLUMN_CREATED_AT + SPACE + TEXT_TYPE + COMMA_SEP +
                    TableColumns.StationEntry.COLUMN_UPDATED_AT + SPACE + TEXT_TYPE + COMMA_SEP +
                    TableColumns.StationEntry.COLUMN_LAST_UPDATE + SPACE + TEXT_TYPE +
                    " )";

    private static final String CREATE_CATEGORY_TABLE =
            "CREATE TABLE " + TableColumns.CategoryEntry.TABLE_NAME + " (" +
                    TableColumns.CategoryEntry._ID + SPACE + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    TableColumns.CategoryEntry.COLUMN_ID + SPACE + INTEGER_TYPE + COMMA_SEP +
                    TableColumns.CategoryEntry.COLUMN_TITLE + SPACE + TEXT_TYPE + COMMA_SEP +
                    TableColumns.CategoryEntry.COLUMN_DESCRIPTION + SPACE + TEXT_TYPE + COMMA_SEP +
                    TableColumns.CategoryEntry.COLUMN_SLUG + SPACE + TEXT_TYPE + COMMA_SEP +
                    TableColumns.CategoryEntry.COLUMN_ANCESTRY + SPACE + TEXT_TYPE + COMMA_SEP +
                    TableColumns.CategoryEntry.COLUMN_STATION_ID + SPACE + INTEGER_TYPE +
                    " REFERENCES " +
                    TableColumns.StationEntry.TABLE_NAME + "(" + TableColumns.StationEntry._ID + ")" +
                    " ON UPDATE CASCADE ON DELETE CASCADE" +
                    " )";

    private static final String CREATE_IMAGE_TABLE =
            "CREATE TABLE " + TableColumns.ImageEntry.TABLE_NAME + " (" +
                    TableColumns.ImageEntry._ID + SPACE + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    TableColumns.ImageEntry.COLUMN_URL + SPACE + TEXT_TYPE + COMMA_SEP +
                    TableColumns.ImageEntry.COLUMN_STATION_ID + SPACE + INTEGER_TYPE +
                    " REFERENCES " +
                    TableColumns.StationEntry.TABLE_NAME + "(" + TableColumns.StationEntry._ID + ")" +
                    " ON UPDATE CASCADE ON DELETE CASCADE" +
                    " )";

    private static final String CREATE_STREAM_TABLE =
            "CREATE TABLE " + TableColumns.StreamEntry.TABLE_NAME + " (" +
                    TableColumns.StreamEntry._ID + SPACE + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    TableColumns.StreamEntry.COLUMN_STREAM + SPACE + TEXT_TYPE + COMMA_SEP +
                    TableColumns.StreamEntry.COLUMN_BITRATE + SPACE + INTEGER_TYPE + COMMA_SEP +
                    TableColumns.StreamEntry.COLUMN_CONTENT_TYPE + SPACE + TEXT_TYPE + COMMA_SEP +
                    TableColumns.StreamEntry.COLUMN_LISTENERS + SPACE + INTEGER_TYPE + COMMA_SEP +
                    TableColumns.StreamEntry.COLUMN_STATUS + SPACE + INTEGER_TYPE + COMMA_SEP +
                    TableColumns.StreamEntry.COLUMN_STATION_ID + SPACE + INTEGER_TYPE +
                    " REFERENCES " +
                    TableColumns.StationEntry.TABLE_NAME + "(" + TableColumns.StationEntry._ID + ")" +
                    " ON UPDATE CASCADE ON DELETE CASCADE" +
                    " )";

    private static final String CREATE_THUMB_TABLE =
            "CREATE TABLE " + TableColumns.ThumbEntry.TABLE_NAME + " (" +
                    TableColumns.ThumbEntry._ID + SPACE + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    TableColumns.ThumbEntry.COLUMN_URL + SPACE + TEXT_TYPE + COMMA_SEP +
                    TableColumns.ThumbEntry.COLUMN_IMAGE_ID + SPACE + INTEGER_TYPE +
                    " REFERENCES " +
                    TableColumns.ImageEntry.TABLE_NAME + " (" + TableColumns.ImageEntry._ID + ")" +
                    " ON UPDATE CASCADE ON DELETE CASCADE" +
                    " )";

    private static final String NOW_PLAYING_STATION_TABLE =
            "CREATE TABLE " + TableColumns.NowPlayingStationEntry.TABLE_NAME + " (" +
                    TableColumns.NowPlayingStationEntry._ID + SPACE + INTEGER_TYPE +
                    " PRIMARY KEY" + COMMA_SEP +
                    TableColumns.NowPlayingStationEntry.COLUMN_STATION_ID + SPACE +
                    INTEGER_TYPE +
                    " )";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATION_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_IMAGE_TABLE);
        db.execSQL(CREATE_STREAM_TABLE);
        db.execSQL(CREATE_THUMB_TABLE);
        db.execSQL(NOW_PLAYING_STATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
