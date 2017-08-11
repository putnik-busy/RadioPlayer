package com.justApp.RadioPlayer.data.repository.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.justApp.RadioPlayer.data.pojo.Category;
import com.justApp.RadioPlayer.data.pojo.Image;
import com.justApp.RadioPlayer.data.pojo.NowPlaying;
import com.justApp.RadioPlayer.data.pojo.Station;
import com.justApp.RadioPlayer.data.pojo.Stream;
import com.justApp.RadioPlayer.data.pojo.Thumb;
import com.justApp.RadioPlayer.data.repository.DataSource;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * @author Sergey Rodionov
 */
public class LocalSource implements DataSource {

    private static LocalSource INSTANCE;
    private DbHelper mDbHelper;

    private LocalSource(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = new DbHelper(context);
    }

    public static LocalSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalSource(context);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Station>> getStations() {
        List<Station> stationList = new ArrayList<>();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TableColumns.StationEntry.TABLE_NAME +
                " INNER JOIN " + TableColumns.ImageEntry.TABLE_NAME +
                " ON (" + TableColumns.StationEntry.TABLE_NAME + "." + TableColumns.StationEntry._ID + " = " +
                TableColumns.ImageEntry.TABLE_NAME + "." + TableColumns.ImageEntry.COLUMN_STATION_ID + ")" +
                " INNER JOIN " + TableColumns.ThumbEntry.TABLE_NAME +
                " ON (" + TableColumns.StationEntry.TABLE_NAME + "." + TableColumns.ImageEntry._ID + " = " +
                TableColumns.ThumbEntry.TABLE_NAME + "." + TableColumns.ThumbEntry.COLUMN_IMAGE_ID +
                ")";

        Cursor c = db.rawQuery(sql, null);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                stationList.add(getStationFromCursor(c));
            }
        }
        if (c != null) {
            c.close();
        }
        db.close();
        if (stationList.isEmpty()) {
            return Observable.empty();
        }
        return Observable.just(stationList);
    }

    private Station getStationFromCursor(Cursor c) {
        Station station = new Station();
        station.setId(c.getInt(c.getColumnIndex(TableColumns.StationEntry.COLUMN_ID)));
        station.setName(c.getString(c.getColumnIndex(TableColumns.StationEntry.COLUMN_NAME)));
        station.setCountry(c.getString(c.getColumnIndex(TableColumns.StationEntry.COLUMN_COUNTRY)));
        station.setImage(getImageFromCursor(c));
        station.setSlug(c.getString(c.getColumnIndex(TableColumns.StationEntry.COLUMN_SLUG)));
        station.setWebsite(c.getString(c.getColumnIndex(TableColumns.StationEntry.COLUMN_WEBSITE)));
        station.setTwitter(c.getString(c.getColumnIndex(TableColumns.StationEntry.COLUMN_TWITTER)));
        station.setFacebook(c.getString(c.getColumnIndex(TableColumns.StationEntry.COLUMN_FACEBOOK)));
        station.setTotalListeners(c.getInt(c.getColumnIndex(TableColumns.StationEntry
                .COLUMN_TOTAL_LISTENER)));
        station.setCategories(getCategoryStationList());
        station.setStreams(getStreamStationList());
        station.setCreatedAt(c.getString(c.getColumnIndex(TableColumns.StationEntry.COLUMN_CREATED_AT)));
        station.setUpdatedAt(c.getString(c.getColumnIndex(TableColumns.StationEntry.COLUMN_UPDATED_AT)));
        station.setLastUpdate(c.getString(c.getColumnIndex(TableColumns.StationEntry.COLUMN_LAST_UPDATE)));
        return station;
    }

    private Image getImageFromCursor(Cursor c) {
        Image image = new Image();
        image.setUrl(c.getString(c.getColumnIndex(TableColumns.ImageEntry.COLUMN_URL)));
        image.setThumb(getThumbFromCursor(c));
        image.setIdStation(c.getInt(c.getColumnIndex(TableColumns.ImageEntry.COLUMN_STATION_ID)));
        return image;
    }

    private Thumb getThumbFromCursor(Cursor c) {
        Thumb thumb = new Thumb();
        thumb.setUrl(c.getString(c.getColumnIndex(TableColumns.ThumbEntry.COLUMN_URL)));
        thumb.setIdImage(c.getInt(c.getColumnIndex(TableColumns.ThumbEntry.COLUMN_IMAGE_ID)));
        return thumb;
    }

    private List<Category> getCategoryStationList() {
        List<Category> categoryList = new ArrayList<>();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TableColumns.CategoryEntry.TABLE_NAME;

        Cursor c = db.rawQuery(sql, null);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                categoryList.add(getCategoryStationListFromCursor(c));
            }
        }

        if (c != null) {
            c.close();
        }
        return categoryList;
    }

    private Category getCategoryStationListFromCursor(Cursor c) {
        Category category = new Category();
        category.setId(c.getInt(c.getColumnIndex(TableColumns.CategoryEntry.COLUMN_ID)));
        category.setTitle(c.getString(c.getColumnIndex(TableColumns.CategoryEntry.COLUMN_TITLE)));
        category.setDescription(c.getString(c.getColumnIndex(TableColumns.CategoryEntry.COLUMN_DESCRIPTION)));
        category.setSlug(c.getString(c.getColumnIndex(TableColumns.CategoryEntry.COLUMN_SLUG)));
        category.setAncestry(c.getString(c.getColumnIndex(TableColumns.CategoryEntry.COLUMN_ANCESTRY)));
        category.setIdStation(c.getInt(c.getColumnIndex(TableColumns.CategoryEntry.COLUMN_STATION_ID)));
        return category;
    }

    private List<Stream> getStreamStationList() {
        List<Stream> streamList = new ArrayList<>();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TableColumns.StreamEntry.TABLE_NAME;

        Cursor c = db.rawQuery(sql, null);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                streamList.add(getStreamStationListFromCursor(c));
            }
        }
        if (c != null) {
            c.close();
        }
        return streamList;
    }

    private Stream getStreamStationListFromCursor(Cursor c) {
        Stream stream = new Stream();
        stream.setStream(c.getString(c.getColumnIndex(TableColumns.StreamEntry.COLUMN_STREAM)));
        stream.setBitrate(c.getInt(c.getColumnIndex(TableColumns.StreamEntry.COLUMN_BITRATE)));
        stream.setContentType(c.getString(c.getColumnIndex(TableColumns.StreamEntry.COLUMN_CONTENT_TYPE)));
        stream.setListeners(c.getInt(c.getColumnIndex(TableColumns.StreamEntry.COLUMN_LISTENERS)));
        stream.setStatus(c.getInt(c.getColumnIndex(TableColumns.StreamEntry.COLUMN_STATUS)));
        stream.setIdStation(c.getInt(c.getColumnIndex(TableColumns.StreamEntry.COLUMN_STATION_ID)));
        return stream;
    }

    private NowPlaying getNowPlayingFromCursor(Cursor c) {
        NowPlaying nowPlaying = new NowPlaying();
        nowPlaying.setStationId(c.getInt(c.getColumnIndex(TableColumns.NowPlayingStationEntry.COLUMN_STATION_ID)));
        return nowPlaying;
    }

    @Override
    public Observable<Station> getStation(@NonNull Integer id) {
        Station station = new Station();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TableColumns.StationEntry.TABLE_NAME +
                " INNER JOIN " + TableColumns.ImageEntry.TABLE_NAME +
                " ON (" + TableColumns.StationEntry.TABLE_NAME + "." + TableColumns.StationEntry._ID + " = " +
                TableColumns.ImageEntry.TABLE_NAME + "." + TableColumns.ImageEntry.COLUMN_STATION_ID + ")" +
                " INNER JOIN " + TableColumns.ThumbEntry.TABLE_NAME +
                " ON (" + TableColumns.StationEntry.TABLE_NAME + "." + TableColumns.ImageEntry._ID + " = " +
                TableColumns.ThumbEntry.TABLE_NAME + "." + TableColumns.ThumbEntry.COLUMN_IMAGE_ID + ")" +
                "WHERE (" + TableColumns.StationEntry.TABLE_NAME + "." + TableColumns.StationEntry.COLUMN_ID + " = " +
                String.valueOf(id) + ")";

        Cursor c = db.rawQuery(sql, null);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                station = getStationFromCursor(c);
            }
        }
        if (c != null) {
            c.close();
        }
        db.close();

        if (station == null) {
            return Observable.empty();
        }
        return Observable.just(station);
    }

    @Override
    public void saveStations(@NonNull List<Station> stationList) {
        checkNotNull(stationList);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values;
        int stationListSize = stationList.size();
        long currentTime = System.currentTimeMillis();

        for (int i = 0; i < stationListSize; i++) {
            values = addStation(stationList.get(i), currentTime);
            long idStationRow = db.insert(TableColumns.StationEntry.TABLE_NAME, null, values);
            values.clear();

            values = addImage(stationList.get(i), idStationRow);
            long idImageRow = db.insert(TableColumns.ImageEntry.TABLE_NAME, null, values);
            values.clear();

            values = addThumb(stationList.get(i), idImageRow);
            db.insert(TableColumns.ThumbEntry.TABLE_NAME, null, values);
            values.clear();

            List<Stream> streamList = stationList.get(i).getStreams();
            int sizeStreamList = streamList.size();

            for (int j = 0; j < sizeStreamList; j++) {
                values = addStream(streamList.get(j), idStationRow);
                db.insert(TableColumns.StreamEntry.TABLE_NAME, null, values);
                values.clear();
            }

            List<Category> categoryList = stationList.get(i).getCategories();
            int sizeCategoryList = categoryList.size();

            for (int k = 0; k < sizeCategoryList; k++) {
                values = addCategory(categoryList.get(k), idStationRow);
                db.insert(TableColumns.CategoryEntry.TABLE_NAME, null, values);
                values.clear();
            }
        }

        db.close();
    }

    private ContentValues addStation(Station station, long currentTime) {
        ContentValues cv = new ContentValues();
        cv.put(TableColumns.StationEntry.COLUMN_ID, station.getId());
        cv.put(TableColumns.StationEntry.COLUMN_NAME, station.getName());
        cv.put(TableColumns.StationEntry.COLUMN_COUNTRY, station.getCountry());
        cv.put(TableColumns.StationEntry.COLUMN_SLUG, station.getSlug());
        cv.put(TableColumns.StationEntry.COLUMN_WEBSITE, station.getWebsite());
        cv.put(TableColumns.StationEntry.COLUMN_TWITTER, station.getTwitter());
        cv.put(TableColumns.StationEntry.COLUMN_FACEBOOK, station.getFacebook());
        cv.put(TableColumns.StationEntry.COLUMN_TOTAL_LISTENER, station.getTotalListeners());
        cv.put(TableColumns.StationEntry.COLUMN_CREATED_AT, station.getCreatedAt());
        cv.put(TableColumns.StationEntry.COLUMN_UPDATED_AT, station.getUpdatedAt());
        cv.put(TableColumns.StationEntry.COLUMN_LAST_UPDATE, currentTime);
        return cv;
    }

    private ContentValues addImage(Station station, long idStationRow) {
        ContentValues cv = new ContentValues();
        Image image = station.getImage();
        cv.put(TableColumns.ImageEntry.COLUMN_URL, image.getUrl());
        cv.put(TableColumns.ImageEntry.COLUMN_STATION_ID, idStationRow);
        return cv;
    }

    private ContentValues addThumb(Station station, long idImageRow) {
        ContentValues cv = new ContentValues();
        Thumb thumb = station.getImage().getThumb();
        cv.put(TableColumns.ThumbEntry.COLUMN_URL, thumb.getUrl());
        cv.put(TableColumns.ThumbEntry.COLUMN_IMAGE_ID, idImageRow);
        return cv;
    }

    private ContentValues addStream(Stream stream, long idStationRow) {
        ContentValues cv = new ContentValues();
        cv.put(TableColumns.StreamEntry.COLUMN_STREAM, stream.getStream());
        cv.put(TableColumns.StreamEntry.COLUMN_BITRATE, stream.getBitrate());
        cv.put(TableColumns.StreamEntry.COLUMN_CONTENT_TYPE, stream.getContentType());
        cv.put(TableColumns.StreamEntry.COLUMN_LISTENERS, stream.getListeners());
        cv.put(TableColumns.StreamEntry.COLUMN_STATUS, stream.getStatus());
        cv.put(TableColumns.StreamEntry.COLUMN_STATION_ID, idStationRow);
        return cv;
    }

    private ContentValues addCategory(Category category, long idStationRow) {
        ContentValues cv = new ContentValues();
        cv.put(TableColumns.CategoryEntry.COLUMN_ID, category.getId());
        cv.put(TableColumns.CategoryEntry.COLUMN_TITLE, category.getTitle());
        cv.put(TableColumns.CategoryEntry.COLUMN_DESCRIPTION, category.getDescription());
        cv.put(TableColumns.CategoryEntry.COLUMN_SLUG, category.getSlug());
        cv.put(TableColumns.CategoryEntry.COLUMN_ANCESTRY, (String) category.getAncestry());
        cv.put(TableColumns.CategoryEntry.COLUMN_STATION_ID, idStationRow);
        return cv;
    }

    private ContentValues addNowPlayingStation(NowPlaying nowPlaying) {
        ContentValues cv = new ContentValues();
        cv.put(TableColumns.NowPlayingStationEntry.COLUMN_STATION_ID, nowPlaying.getStationId());
        return cv;
    }

    @Override
    public void refreshStations(@NonNull List<Station> stationList) {
        checkNotNull(stationList);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values;
        int stationListSize = stationList.size();
        long currentTime = System.currentTimeMillis();

        for (int i = 0; i < stationListSize; i++) {
            Station station = stationList.get(i);
            values = addStation(station, currentTime);
            long idStationRow = db.update(TableColumns.StationEntry.TABLE_NAME, values,
                    TableColumns.StationEntry.COLUMN_ID + " =?",
                    new String[]{String.valueOf(station.getId())});
            values.clear();

            values = addImage(station, idStationRow);
            long idImageRow = db.update(TableColumns.ImageEntry.TABLE_NAME, values,
                    TableColumns.ImageEntry.COLUMN_STATION_ID + " =?",
                    new String[]{String.valueOf(idStationRow)});
            values.clear();

            values = addThumb(station, idImageRow);
            db.update(TableColumns.ThumbEntry.TABLE_NAME, values,
                    TableColumns.ThumbEntry.COLUMN_IMAGE_ID,
                    new String[]{String.valueOf(station.getImage().getThumb().getIdImage())});
            values.clear();

            List<Stream> streamList = station.getStreams();
            int sizeStreamList = streamList.size();

            for (int j = 0; j < sizeStreamList; j++) {
                Stream stream = streamList.get(j);
                values = addStream(stream, idStationRow);
                db.update(TableColumns.StreamEntry.TABLE_NAME, values,
                        TableColumns.StreamEntry.COLUMN_STATION_ID + " =?",
                        new String[]{String.valueOf(stream.getIdStation())});
                values.clear();
            }

            List<Category> categoryList = station.getCategories();
            int sizeCategoryList = categoryList.size();

            for (int k = 0; k < sizeCategoryList; k++) {
                Category category = categoryList.get(k);
                values = addCategory(category, idStationRow);
                db.update(TableColumns.CategoryEntry.TABLE_NAME, values,
                        TableColumns.CategoryEntry.COLUMN_STATION_ID + " =?",
                        new String[]{String.valueOf(category.getIdStation())});
                values.clear();
            }
        }

        db.close();
    }

    @Override
    public long getLastUpdate() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        long lastUpdate = -1;

        String sql = "SELECT " + TableColumns.StationEntry.COLUMN_LAST_UPDATE + " FROM " +
                TableColumns.StationEntry.TABLE_NAME;

        Cursor c = db.rawQuery(sql, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            lastUpdate = c.getLong(c.getColumnIndex(TableColumns.StationEntry.COLUMN_LAST_UPDATE));
        }

        if (c != null) {
            c.close();
        }
        db.close();

        return lastUpdate;
    }

    @Override
    public void saveNowPlaying(@NonNull NowPlaying nowPlaying) {
        checkNotNull(nowPlaying);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = addNowPlayingStation(nowPlaying);
        db.insert(TableColumns.NowPlayingStationEntry.TABLE_NAME, null, values);
    }

    @Override
    public Observable<NowPlaying> getNowPlayingStation() {
        NowPlaying nowPlaying = null;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + TableColumns.NowPlayingStationEntry.TABLE_NAME;

        Cursor c = db.rawQuery(sql, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            nowPlaying = getNowPlayingFromCursor(c);
        }
        if (c != null) {
            c.close();
        }
        db.close();
        if (nowPlaying == null) {
            return Observable.empty();
        }
        return Observable.just(nowPlaying);
    }

    @Override
    public void refreshNowPlaying(@NonNull NowPlaying nowPlaying) {
        checkNotNull(nowPlaying);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = addNowPlayingStation(nowPlaying);
        db.update(TableColumns.NowPlayingStationEntry.TABLE_NAME, values,
                TableColumns.NowPlayingStationEntry.COLUMN_STATION_ID + " =?",
                new String[]{String.valueOf(nowPlaying.getStationId())});
    }
}
