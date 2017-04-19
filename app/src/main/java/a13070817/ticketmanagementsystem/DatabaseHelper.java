package a13070817.ticketmanagementsystem;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Samuel Linton SRN 13070817 on 08/02/2017.
 */

class DatabaseHelper extends SQLiteOpenHelper {

    //Database variables
    private static final int DATABASE_VERSION = 13;
    private static final String DATABASE_NAME = "ticketDatabase.db";
    //Job table columns
    static final String TICKET_TABLE_NAME = "TICKET";
    static final String TICKET_ID = "ID";
    static final String TICKET_ENGINEER = "ENGINEER";
    static final String TICKET_TITLE = "TITLE";
    static final String TICKET_ASSET = "ASSET";
    static final String TICKET_CUSTOMER = "CUSTOMER";
    static final String TICKET_DESCRIPTION = "DESCRIPTION";
    static final String TICKET_SEVERITY = "SEVERITY";
    static final String TICKET_STATUS = "STATUS";
    static final String TICKET_DATE = "DATE_CREATED";
    //Create Job table
    private static final String CREATE_TICKET_TABLE =
            "CREATE TABLE " + TICKET_TABLE_NAME + " (" +
                     TICKET_ID + " INTEGER PRIMARY KEY," +
                    TICKET_TITLE + " TEXT," +
                    TICKET_ENGINEER + " TEXT," +
                    TICKET_CUSTOMER + " TEXT," +
                    TICKET_DESCRIPTION + " TEXT," +
                    TICKET_ASSET + " TEXT," +
                    TICKET_STATUS + " INTEGER," +
                    TICKET_SEVERITY + " INTEGER," +
                    TICKET_DATE + " INTEGER)";

    //Delete table
    private static final String DELETE_TICKET_TABLE = "DROP TABLE IF EXISTS " + TICKET_TABLE_NAME;

    //Constructor
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TICKET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TICKET_TABLE);
        onCreate(db);
    }
}