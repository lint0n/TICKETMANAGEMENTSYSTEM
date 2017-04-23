package a13070817.ticketmanagementsystem;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Samuel Linton 13070817
 */
public class Search extends AppCompatActivity{

    private EditText ticketID, ticketTitle, ticketAsset, ticketCustomer, ticketDescription, ticketEngineer, ticketDate, ticketSeverity;
    private EditText ticketLookup;
    private CheckBox ticketStatus;
    private SQLiteDatabase db;
    private Date date1;
    private String jl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Search");
        setSupportActionBar(toolbar);
        //EditText instances
        ticketLookup = (EditText) findViewById(R.id.searchLookup);
        ticketID = (EditText) findViewById(R.id.JobID);
        ticketTitle = (EditText) findViewById(R.id.JobTitle);
        ticketAsset = (EditText) findViewById(R.id.JobAsset);
        ticketCustomer = (EditText) findViewById(R.id.JobCustomer);
        ticketDescription = (EditText) findViewById(R.id.JobDescription);
        ticketEngineer = (EditText) findViewById(R.id.JobEngineer);
        ticketDate = (EditText) findViewById(R.id.dateCreated);
        ticketSeverity = (EditText) findViewById(R.id.JobSeverity);
        //CheckBox instance
        ticketStatus = (CheckBox) findViewById(R.id.checkBox);

        try{
            String intentResult = getIntent().getExtras().getString("ticketToSearch");
            ticketLookup.setText(intentResult);
            lookupTicket(null);
        } catch(Exception e) {
            new Search();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        returnMain();
    }

    /**
     * Searches database for specified ticket
     * @param menuItem MenuItem interface object
     */
    public void lookupTicket(MenuItem menuItem) {
        jl = ticketLookup.getText().toString();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.query("TICKET", new String[]{"ID", "TITLE",  "ASSET",
                    "ENGINEER", "CUSTOMER", "DESCRIPTION",
                    "STATUS", "SEVERITY", "DATE_CREATED"},
                    "ID = ?", new String[]{jl}, null, null, null, null);
                if(cursor.moveToFirst()){
                String idText = cursor.getString(0);
                String titleText = cursor.getString(1);
                String assetText = cursor.getString(2);
                String engineerText = cursor.getString(3);
                String customerText = cursor.getString(4);
                String descriptionText = cursor.getString(5);
                Integer complete = cursor.getInt(6);
                Integer severity = cursor.getInt(7);
                String dateTime = cursor.getString(8);
                //http://stackoverflow.com/a/14256017
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    date1 = format.parse(dateTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long createWhen = date1.getTime();
                int flags = 0;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
                flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;
                String finalDateTime = android.text.format.DateUtils.formatDateTime(this,
                        createWhen + TimeZone.getDefault().getOffset(createWhen), flags);
                ticketDescription.setText("");
                ticketTitle.setText("");
                ticketEngineer.setText("");
                ticketID.setText("");
                ticketAsset.setText("");
                ticketCustomer.setText("");
                ticketSeverity.setText("");
                //sets text using local String variables
                ticketID.setText(idText);
                ticketTitle.setText(titleText);
                ticketEngineer.setText(engineerText);
                ticketAsset.setText(assetText);
                ticketCustomer.setText(customerText);
                ticketDescription.setText(descriptionText);
                ticketDate.setText(finalDateTime);
                if (severity == 4) {
                    ticketSeverity.setText("Low");
                } else if (severity == 3) {
                    ticketSeverity.setText("Medium");
                } else if (severity == 2) {
                    ticketSeverity.setText("High");
                } else if (severity == 1) {
                    ticketSeverity.setText("Critical");
                }
                //stops user from accessing these EditTexts once a job had been looked up
                ticketLookup.setFocusable(false);
                ticketLookup.setClickable(false);
                ticketID.setFocusable(false);
                ticketID.setClickable(false);
                ticketSeverity.setFocusable(false);
                ticketSeverity.setClickable(false);
                ticketDate.setFocusable(false);
                ticketDate.setClickable(false);
                //hides virtual keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                //if the entities' COMPLETE column is set to 1, sets CheckBox to checked otherwise will leave unchecked
                if (complete == 1) {
                    ticketStatus.setChecked(true);
                }
                    cursor.close();
                    db.close();
            } else {
                    cursor.close();
                    db.close();
                    throw new SQLiteException();
            }
        }
        catch (SQLiteException exc) {
            exc.printStackTrace();
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Ticket " + jl + " cannot be found in the database.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    public void updateTicket(View view) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        Date date = new Date();

        //takes the EditText and puts them to String
        try{
            String jd = ticketDescription.getText().toString();
            String jt = ticketTitle.getText().toString();
            String je = ticketEngineer.getText().toString();
            String ja = ticketAsset.getText().toString();
            String jc = ticketCustomer.getText().toString();
            String ji = ticketID.getText().toString();

            //ContentValues that takes the String variables and then puts them to their respective table column
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.TICKET_ID, ji);
            values.put(DatabaseHelper.TICKET_TITLE, jt);
            values.put(DatabaseHelper.TICKET_ENGINEER, je);
            values.put(DatabaseHelper.TICKET_ASSET, ja);
            values.put(DatabaseHelper.TICKET_CUSTOMER, jc);
            values.put(DatabaseHelper.TICKET_DESCRIPTION, jd);

            //if the CheckBox is checked then change the entity's COMPLETE column to 1 otherwise leave as 0
            if(ticketStatus.isChecked()){
                values.put(DatabaseHelper.TICKET_STATUS, 1);
            } else {
                values.put(DatabaseHelper.TICKET_STATUS, 0);
            }
            db.update(DatabaseHelper.TICKET_TABLE_NAME, values, DatabaseHelper.TICKET_ID + "=" + ji, null);

            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Ticket " + ji + " updated." , Snackbar.LENGTH_LONG);
            snackbar.setAction("Home", new returnMainSnackBar());
            snackbar.show();

            db.close();
        }

        //catch Exception, take input and convert to String. Then display as Toast informing of an unsuccessful query
        catch(Exception exc){
            //Toast.makeText(this, "Ticket not updated", Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Ticket not updated." , Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    /**
     * http://stackoverflow.com/a/2478662/7087139
     */
    public void returnMain(){
        final Intent mainIntent = new Intent(this, Main.class);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        startActivity(mainIntent);

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Changes will not be updated. Are you sure?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }

    /**
     * Nested class used for Snackbar return home action
     */
    private class returnMainSnackBar implements View.OnClickListener{

        @Override
        public void onClick(View v){
            startActivity(new Intent(Search.this, Main.class));
        }
    }
}