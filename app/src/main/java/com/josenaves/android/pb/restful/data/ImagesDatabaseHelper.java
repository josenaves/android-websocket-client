package com.josenaves.android.pb.restful.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

final class ImagesDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = ImagesDatabaseHelper.class.getSimpleName();

    private static ImagesDatabaseHelper instance;

    // Database Info
    private static final String DATABASE_NAME = "imageDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // table name
    public static final String TABLE_IMAGES = "images";

    // image table columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "image_date";
    public static final String COLUMN_NAME = "image_name";
    public static final String COLUMN_IMAGE = "storage_location";

    private static final String CREATE_IMAGE_POST = "create table " + TABLE_IMAGES +
            "(" +
            COLUMN_ID + " TEXT PRIMARY KEY," +
            COLUMN_NAME + " TEXT, " +
            COLUMN_DATE + " DATE, " +
            COLUMN_IMAGE + " TEXT" +
            ")";

    private Context context;

    public static synchronized ImagesDatabaseHelper getInstance(Context context){
        if (instance == null) {
            instance = new ImagesDatabaseHelper(context);
        }
        return instance;
    }

    private ImagesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);

        Log.d(TAG, "onConfigure...");
        //db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_IMAGE_POST);
        Log.d(TAG, "onCreate...");
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade...");

        if (oldVersion != newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");

            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
            onCreate(db);
        }
    }

    protected Context getContext() {
        return context;
    }
}

