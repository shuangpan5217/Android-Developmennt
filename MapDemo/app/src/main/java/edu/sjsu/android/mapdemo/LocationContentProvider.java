package edu.sjsu.android.mapdemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LocationContentProvider extends ContentProvider {
    private SQLiteDatabase db;
    private LocationDB dbHelper;

    static final String PROVIDER_NAME = "edu.sjsu.android.mapdemo.Map";
    static final String URL = "content://" + PROVIDER_NAME + "/marker";
    static final Uri CONTENT_URI = Uri.parse(URL);

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new LocationDB(context);
        // Create a write able database which will
        // trigger its * creation if it doesn't already exist.
        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return dbHelper.query(db, uri, projection, selection, selectionArgs, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return dbHelper.insert(db, uri, contentValues);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return dbHelper.delete(db, uri, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
