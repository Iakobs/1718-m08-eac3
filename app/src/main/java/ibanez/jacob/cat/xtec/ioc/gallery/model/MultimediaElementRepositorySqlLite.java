package ibanez.jacob.cat.xtec.ioc.gallery.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Implementation of {@link MultimediaElementRepository} which gets data from a Sqlite database.
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class MultimediaElementRepositorySqlLite implements MultimediaElementRepository {

    //Tag for logging purposes
    private static final String TAG = MultimediaElementRepositorySqlLite.class.getSimpleName();

    //Database columns
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_PATH = "PATH";
    private static final String COLUMN_TYPE = "TYPE";
    private static final String COLUMN_LATITUDE = "LATITUDE";
    private static final String COLUMN_LONGITUDE = "LONGITUDE";

    //Database variables
    private static final String DB_NAME = "GALLERY_DB";
    private static final String TABLE_MULTIMEDIA_ELEMENTS = "MULTIMEDIA_ELEMENTS";
    private static final int VERSION = 1;

    //Database queries
    private static final String CREATE_TABLE_MULTIMEDIA_ELEMENTS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_MULTIMEDIA_ELEMENTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL UNIQUE," +
                    COLUMN_PATH + " TEXT NOT NULL," +
                    COLUMN_TYPE + " TEXT NOT NULL," +
                    COLUMN_LATITUDE + " TEXT NOT NULL," +
                    COLUMN_LONGITUDE + " TEXT NOT NULL" +
                    ");";

    //class members
    private DBHelper mHelper;
    private SQLiteDatabase mDatabase;
    private OnMultimediaElementCreatedListener mMultimediaElementCreatedListener;

    //Constructor
    public MultimediaElementRepositorySqlLite(Context context) {
        this.mHelper = new DBHelper(context);
    }

    //Open and close methods

    /**
     * This method opens the connection to the database.
     * <p>
     * You can specify if you want only read access or both read/write access
     *
     * @param withWriteAccess If {@code true}, the connection can write to the database
     * @throws SQLException If an error occurs
     */
    private void open(boolean withWriteAccess) throws SQLException {
        if (withWriteAccess) {
            mDatabase = mHelper.getWritableDatabase();
        } else {
            mDatabase = mHelper.getReadableDatabase();
        }
    }

    /**
     * Closes the connection to the database
     */
    private void close() {
        mHelper.close();
    }

    @Override
    public void setMultimediaElementCreatedListener(OnMultimediaElementCreatedListener multimediaElementCreatedListener) {
        this.mMultimediaElementCreatedListener = multimediaElementCreatedListener;
    }

    @Override
    public MultimediaElement getMultimediaElementById(long id) {
        MultimediaElement multimediaElement = null;

        //first open database with only read access
        open(false);

        //search by name
        Cursor cursor = mDatabase.query(
                TABLE_MULTIMEDIA_ELEMENTS,
                null,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        //if there are any results, retrieve data and create object
        if (cursor != null && cursor.moveToFirst()) {
            multimediaElement = getMultimediaElement(cursor);

            //close cursor so it's not needed anymore
            cursor.close();
        }

        //close the connection to the database
        close();

        return multimediaElement;
    }

    @Override
    public Gallery getGallery() {
        Gallery gallery = new Gallery();

        //open read access to the database
        open(false);

        //perform query to the database
        Cursor cursor = mDatabase.query(
                TABLE_MULTIMEDIA_ELEMENTS,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) { //check if it have any result first
            do {
                gallery.add(getMultimediaElement(cursor));
            }
            while (cursor.moveToNext()); //continue adding while there are more items in the cursor

            //close the cursor, it's not needed anymore
            cursor.close();
        }

        //close the connection to the database
        close();

        return gallery;
    }

    @Override
    public void addMultimediaElement(MultimediaElement multimediaElement) {
        //Check that the item is not null
        if (multimediaElement == null) {
            String msg = "The multimedia element must not be null";
            Log.e(TAG, msg);
            throw new IllegalArgumentException(msg);
        }

        if (!existsByName(multimediaElement.getName())) { //avoid duplicates
            //open the database with write access
            open(true);

            //fill all columns
            ContentValues initialValues = new ContentValues();

            initialValues.put(COLUMN_NAME, multimediaElement.getName());
            initialValues.put(COLUMN_PATH, multimediaElement.getPath());
            initialValues.put(COLUMN_TYPE, String.valueOf(multimediaElement.getType()));
            initialValues.put(COLUMN_LATITUDE, String.valueOf(multimediaElement.getLatLng().latitude));
            initialValues.put(COLUMN_LONGITUDE, String.valueOf(multimediaElement.getLatLng().longitude));

            //create the item
            mDatabase.insert(TABLE_MULTIMEDIA_ELEMENTS, null, initialValues);

            //database access is not needed anymore
            close();

            notifyMultimediaElementCreationListener();
        }
    }

    /**
     * Creates a {@link MultimediaElement} from the data inside a cursor.
     *
     * @param cursor The cursor of the current database, pointing to an actual row.
     * @return The {@link MultimediaElement} in the cursor.
     */
    private MultimediaElement getMultimediaElement(Cursor cursor) {
        MultimediaElement multimediaElement;

        long id = Long.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        String path = cursor.getString(cursor.getColumnIndex(COLUMN_PATH));
        int type = Integer.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
        double latitude = Double.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_LATITUDE)));
        double longitude = Double.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_LONGITUDE)));
        LatLng latLng = new LatLng(latitude, longitude);

        multimediaElement = new MultimediaElement();
        multimediaElement.setId(id);
        multimediaElement.setName(name);
        multimediaElement.setPath(path);
        multimediaElement.setType(type);
        multimediaElement.setLatLng(latLng);

        return multimediaElement;
    }

    /**
     * This method is to be called whenever wanted to notify the listener that there has been changes
     * in the database information.
     */
    private void notifyMultimediaElementCreationListener() {
        if (mMultimediaElementCreatedListener != null) {
            mMultimediaElementCreatedListener.onMultimediaElementCreation();
        }
    }

    /**
     * Checks if a {@link MultimediaElement} already exists, searching by its {@link MultimediaElement#name}
     *
     * @param name The {@link MultimediaElement#name}
     * @return {@code true} if the item exists in the database. {@code false} otherwise.
     */
    private boolean existsByName(String name) {
        //first open database with only read access
        open(false);

        //search by name
        Cursor cursor = mDatabase.query(
                TABLE_MULTIMEDIA_ELEMENTS,
                new String[]{COLUMN_ID},
                COLUMN_NAME + " = ?",
                new String[]{name},
                null,
                null,
                null
        );

        //if there is a single coincidence, it means that the item already exists
        boolean exists = cursor.moveToFirst();

        //close cursor so it's not needed anymore, and so the connection to the database
        cursor.close();
        close();

        return exists;
    }

    /**
     * A class extending {@link SQLiteOpenHelper} for low level access to the database.
     */
    private static class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context con) {
            super(con, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_MULTIMEDIA_ELEMENTS);
            } catch (SQLException e) {
                Log.w(TAG, "Error executing statement " + CREATE_TABLE_MULTIMEDIA_ELEMENTS, e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Updating database from " + oldVersion + " to " + newVersion +
                    ". This process wil erase all data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MULTIMEDIA_ELEMENTS);

            onCreate(db);
        }
    }
}
