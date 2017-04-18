package a13070817.ticketmanagementsystem;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Samuel Linton SRN 13070817 on 08/02/2017.
 */

class DatabaseHelper extends SQLiteOpenHelper {

    //Database variables
    private static final int DATABASE_VERSION = 12;
    private static final String DATABASE_NAME = "ticketDatabase.db";

    //Job table columns
    static final String JOB_TABLE_NAME = "JOB";
    static final String JOB_ID = "ID";
    static final String JOB_ENGINEER = "ENGINEER";
    static final String JOB_TITLE = "TITLE";
    static final String JOB_ASSET = "ASSET";
    static final String JOB_CUSTOMER = "CUSTOMER";
    static final String JOB_DESCRIPTION = "DESCRIPTION";
    static final String JOB_SEVERITY = "SEVERITY";
    static final String JOB_STATUS = "COMPLETE";
    static final String JOB_DATE = "DATE_CREATED";

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
                    JOB_DATE + " INTEGER)";

    //Delete table
    private static final String DELETE_JOB_TABLE = "DROP TABLE IF EXISTS " + JOB_TABLE_NAME;

    //Constructor
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_JOB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_JOB_TABLE);
        onCreate(db);
    }
}