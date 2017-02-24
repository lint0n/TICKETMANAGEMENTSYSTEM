package a13070817.ticketmanagementsystem;

/**
 * Created by Sam on 24/02/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Samuel Linton 13070817 on 08/02/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //Database variables
    static final int DATABASE_VERSION = 6;
    static final String DATABASE_NAME = "ticketDatabase.db";

    //Job table columns
    static final String JOB_TABLE_NAME = "JOB";
    static final String JOB_ID = "ID";
    static final String JOB_ENGINEER = "ENGINEER";
    static final String JOB_TITLE = "TITLE";
    static final String JOB_ASSET = "ASSET";
    static final String JOB_CUSTOMER = "CUSTOMER";
    static final String JOB_DESCRIPTION = "DESCRIPTION";
    static final String JOB_SEVERITY = "SEVERITY";
    static String JOB_STATUS = "COMPLETE";
    static String JOB_DATE = "DATE_CREATED";

    //    Asset table variables
    static final String ASSET_TABLE_NAME = "ASSET";
    static final String ASSET_DESCRIPTION = "DESCRIPTION";
    static final String ASSET_TAG = "TAG";
    static final String ASSET_CLIENT = "CLIENT_ID";
    static String ASSET_LOCATION = "LOCATION";

    static final String CLIENT_TABLE_NAME = "CLIENT";
    static final String CLIENT_NAME = "NAME";
    static final String CLIENT_ID = "CLIENT_ID";
    static final String CLIENT_LOCATION = "LOCATION";
    static final String CLIENT_PHONE = "PHONE_NUMBER";
    static final String CLIENT_EMAIL = "EMAIL";

    static final String LOCATION_TABLE_NAME = "LOCATION";
    static final String LOCATION_ID = "LOCATION_ID";
    static final String LOCATION_NAME = "LOCATION_NAME";

    SQLiteDatabase db;

    //Create Job table
    private static final String CREATE_JOB_TABLE =
            "CREATE TABLE " + JOB_TABLE_NAME + " (" +
                    JOB_ID + " INTEGER PRIMARY KEY," +
                    JOB_TITLE + " TEXT," +
                    JOB_ENGINEER + " TEXT," +
                    JOB_CUSTOMER + " TEXT," +
                    JOB_DESCRIPTION + " TEXT," +
                    JOB_ASSET + " TEXT," +
                    JOB_STATUS + " INTEGER," +
                    JOB_SEVERITY + " INTEGER," +
                    JOB_DATE + " TEXT," +
                    "FOREIGN KEY(" + JOB_ASSET + ") REFERENCES " + ASSET_TABLE_NAME + "(TAG)" + ")";

    //creates an asset table
    private static final String CREATE_ASSET_TABLE =
            "CREATE TABLE " + ASSET_TABLE_NAME + " (" +
                    ASSET_TAG + " TEXT PRIMARY KEY," +
                    ASSET_DESCRIPTION + " TEXT," +
                    ASSET_CLIENT + " TEXT," +
                    ASSET_LOCATION + " TEXT," +
                    "FOREIGN KEY(" + ASSET_CLIENT + ") REFERENCES " + CLIENT_TABLE_NAME + "(ID)" + ")";

    //creates a client table
    private static final String CREATE_CLIENT_TABLE =
            "CREATE TABLE " + CLIENT_TABLE_NAME + " (" +
                    CLIENT_ID + " INTEGER PRIMARY KEY," +
                    CLIENT_NAME + " TEXT," +
                    CLIENT_LOCATION + " TEXT," +
                    CLIENT_PHONE + " INTEGER," +
                    CLIENT_EMAIL + " TEXT)";

    //creates a location table
    private static final String CREATE_LOCATION_TABLE =
            "CREATE TABLE " + LOCATION_TABLE_NAME + " (" +
                    LOCATION_ID + " INTEGER PRIMARY KEY," +
                    LOCATION_NAME + " TEXT)";

    //Delete table
    private static final String DELETE_JOB_TABLE = "DROP TABLE IF EXISTS " + JOB_TABLE_NAME;
    private static final String DELETE_ASSET_TABLE = "DROP TABLE IF EXISTS " + ASSET_TABLE_NAME;
    private static final String DELETE_CLIENT_TABLE = "DROP TABLE IF EXISTS " + CLIENT_TABLE_NAME;
    private static final String DELETE_LOCATION_TABLE = "DROP TABLE IF EXISTS " + LOCATION_TABLE_NAME;

    //Constructor
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_JOB_TABLE);
        db.execSQL(CREATE_ASSET_TABLE);
        db.execSQL(CREATE_CLIENT_TABLE);
        db.execSQL(CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_JOB_TABLE);
        db.execSQL(DELETE_ASSET_TABLE);
        db.execSQL(DELETE_CLIENT_TABLE);
        db.execSQL(DELETE_LOCATION_TABLE);
        onCreate(db);
    }

    public void addData(){
        //db = this.getWritableDatabase();
        //add example data
    }
}