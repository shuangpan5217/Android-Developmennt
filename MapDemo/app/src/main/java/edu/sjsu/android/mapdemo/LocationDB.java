package edu.sjsu.android.mapdemo;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class LocationDB extends SQLiteOpenHelper {
    private Context context;

    static final String DATABASE_NAME = "Map";
    static final String MARKER_TABLE_NAME = "marker";
    static final int DATABASE_VERSION = 1;

    //static final String _ID = "_id";
    static final String LATITUDE = "latitude";
    static final String LONGITUDE = "longitude";
    static final String ZOOM = "zoom_level";

    private static HashMap<String, String> STUDENTS_PROJECTION_MAP = null;

    static final String CREATE_MARKER_TABLE =
            " CREATE TABLE IF NOT EXISTS " + MARKER_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " latitude TEXT NOT NULL, " +
                    " longitude TEXT NOT NULL, " +
                    " zoom_level TEXT NOT NULL);";

    public LocationDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MARKER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + MARKER_TABLE_NAME);
        onCreate(db);
    }

    public Cursor query(SQLiteDatabase db, Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder){
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(MARKER_TABLE_NAME);
        qb.setProjectionMap(STUDENTS_PROJECTION_MAP);

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(context.getContentResolver(), uri);

        return c;
    }

    public Uri insert(SQLiteDatabase db, Uri uri, ContentValues contentValues){
        long rowID = db.insert(MARKER_TABLE_NAME, "", contentValues);

        if(rowID > 0){
            Uri _uri = ContentUris.withAppendedId(LocationContentProvider.CONTENT_URI, rowID);
            context.getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    public int delete(SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs){
        int count = db.delete(MARKER_TABLE_NAME, selection, selectionArgs);
        context.getContentResolver().notifyChange(uri, null);
        return count;
    }
}
