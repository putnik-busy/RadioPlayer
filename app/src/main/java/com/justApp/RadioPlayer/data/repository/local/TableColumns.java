package com.justApp.RadioPlayer.data.repository.local;

import android.provider.BaseColumns;

/**
 * @author Sergey Rodionov
 */

public final class TableColumns {

    public TableColumns() {
    }

    public static abstract class StationEntry implements BaseColumns {

        public static final String TABLE_NAME = "Station";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_SLUG = "slug";
        public static final String COLUMN_WEBSITE = "website";
        public static final String COLUMN_TWITTER = "twitter";
        public static final String COLUMN_FACEBOOK = "facebook";
        public static final String COLUMN_TOTAL_LISTENER = "total_listeners";
        public static final String COLUMN_CREATED_AT = "createdAt";
        public static final String COLUMN_UPDATED_AT = "updatedAt";
        public static final String COLUMN_LAST_UPDATE = "last_update";
    }

    public static abstract class CategoryEntry implements BaseColumns {

        public static final String TABLE_NAME = "Category";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_SLUG = "slug";
        public static final String COLUMN_ANCESTRY = "ancestry";
        public static final String COLUMN_STATION_ID = "station_id";
    }

    public static abstract class ImageEntry implements BaseColumns {

        public static final String TABLE_NAME = "Image";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_STATION_ID = "station_id";
    }

    public static abstract class StreamEntry implements BaseColumns {

        public static final String TABLE_NAME = "Stream";
        public static final String COLUMN_STREAM = "stream";
        public static final String COLUMN_BITRATE = "bitrate";
        public static final String COLUMN_CONTENT_TYPE = "content_type";
        public static final String COLUMN_LISTENERS = "listeners";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_STATION_ID = "station_id";
    }

    public static abstract class ThumbEntry implements BaseColumns {

        public static final String TABLE_NAME = "Thumb";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_IMAGE_ID = "image_id";
    }

    public static abstract class NowPlayingStationEntry implements BaseColumns {

        public static final String TABLE_NAME = "NowPlayingStation";
        public static final String COLUMN_STATION_ID = "station_id";
    }
}
