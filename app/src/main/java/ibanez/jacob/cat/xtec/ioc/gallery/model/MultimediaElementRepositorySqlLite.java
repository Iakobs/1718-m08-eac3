package ibanez.jacob.cat.xtec.ioc.gallery.model;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

/**
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
    private static final String COLUMN_DESCRIPTION = "LATITUDE";
    private static final String COLUMN_PUB_DATE = "LONGITUDE";

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
                    COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                    COLUMN_PUB_DATE + " TEXT NOT NULL" +
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
     * @return A ready-to-use connection to the database
     * @throws SQLException If an error occurs
     */
    private MultimediaElementRepositorySqlLite open(boolean withWriteAccess) throws SQLException {
        if (withWriteAccess) {
            mDatabase = mHelper.getWritableDatabase();
        } else {
            mDatabase = mHelper.getReadableDatabase();
        }
        return this;
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
    public MultimediaElement getItemById(long id) {
        return null;
    }

    @Override
    public List<MultimediaElement> getAllItems() {
        return null;
    }

    @Override
    public void addItem(MultimediaElement multimediaElement) {

    }

    private void notifyMultimediaElementCreationListener() {
        if (mMultimediaElementCreatedListener != null) {
            mMultimediaElementCreatedListener.onMultimediaElementCreation();
        }
    }

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
