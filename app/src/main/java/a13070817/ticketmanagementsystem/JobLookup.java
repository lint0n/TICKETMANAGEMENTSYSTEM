package a13070817.ticketmanagementsystem;

/**
 * Created by Sam on 24/02/2017.
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class JobLookup extends AppCompatActivity{

    //Fields
    private EditText jobID, jobTitle, jobAsset, jobCustomer, jobDescription, jobEngineer, jobDate, jobUpdate, jobSeverity;
    private EditText jobLookup;
    private FloatingActionButton updateButton;
    private CheckBox lookupCheckbox;
    private SQLiteDatabase db;
    private Date date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_lookup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Lookup");
        setSupportActionBar(toolbar);
        //EditText instances
        jobLookup = (EditText) findViewById(R.id.lookupEditText);
        jobID = (EditText) findViewById(R.id.JobID);
        jobTitle = (EditText) findViewById(R.id.JobTitle);
        jobAsset = (EditText) findViewById(R.id.JobAsset);
        jobCustomer = (EditText) findViewById(R.id.JobCustomer);
        jobDescription = (EditText) findViewById(R.id.JobDescription);
        jobEngineer = (EditText) findViewById(R.id.JobEngineer);
        jobDate = (EditText) findViewById(R.id.dateCreated);
        jobSeverity = (EditText) findViewById(R.id.JobSeverity);
        //Button instance
        updateButton = (FloatingActionButton) findViewById(R.id.fabUpdate);
        //CheckBox instance
        lookupCheckbox = (CheckBox) findViewById(R.id.checkBox);

        try{
            String s = getIntent().getExtras().getString("string");
            jobLookup.setText(s);
            lookupJob(null);
        } catch(Exception e) {
            new JobLookup();
        }
    }

    //http://stackoverflow.com/a/4780009/7087139
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_update, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        returnMain();
    }

    public void lookupJob(MenuItem menuItem) {

        //Local String instance getting text from JobLookup EditText
        String jl = jobLookup.getText().toString();

        //Local instance of DatabaseHelper class
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        //try catch block - if job exists in database return data. If job doesn't not exist, catch Exception and return Toast text.
        try {
            //Cursor storing SQLite query, accessing ID/TITLE/ASSET/ENGINEER/CUSTOMER/DESCRIPTION/COMPLETE columns.
            Cursor cursor = db.query("JOB", new String[]{"ID", "TITLE",  "ASSET", "ENGINEER", "CUSTOMER", "DESCRIPTION", "COMPLETE", "SEVERITY", "DATE_CREATED", "DATE_UPDATED"}, "ID = ?", new String[]{jl}, null, null, null, null);

            cursor.moveToFirst();
            String idText = cursor.getString(0);
            String titleText = cursor.getString(1);
            String assetText = cursor.getString(2);
            String engineerText = cursor.getString(3);
            String customerText = cursor.getString(4);
            String descriptionText = cursor.getString(5);
            Integer complete = cursor.getInt(6);
            Integer severity = cursor.getInt(7);
            String dateTime = cursor.getString(8);

            //Retrieves String equivalent from db then parses into Date representation
            //Takes Date representation then converts to long
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

            //ensures that EditText elements are empty before setting text
            jobDescription.setText("");
            jobTitle.setText("");
            jobEngineer.setText("");
            jobID.setText("");
            jobAsset.setText("");
            jobCustomer.setText("");
            jobSeverity.setText("");

            //sets text using local String variables
            jobID.setText(idText);
            jobTitle.setText(titleText);
            jobEngineer.setText(engineerText);
            jobAsset.setText(assetText);
            jobCustomer.setText(customerText);
            jobDescription.setText(descriptionText);
            jobDate.setText(finalDateTime);

            if(severity == 4){
                jobSeverity.setText("Low");
            }
            else if (severity == 3) {
                jobSeverity.setText("Medium");
            }
            else if (severity == 2) {
                jobSeverity.setText("High");
            }
            else if (severity == 1) {
                jobSeverity.setText("Critical");
            }
            //stops user from accessing these EditTexts once a job had been looked up
            jobLookup.setFocusable(false);
            jobLookup.setClickable(false);
            jobID.setFocusable(false);
            jobID.setClickable(false);
            jobSeverity.setFocusable(false);
            jobSeverity.setClickable(false);
            jobDate.setFocusable(false);
            jobDate.setClickable(false);

            //hides virtual keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            //if the entities' COMPLETE column is set to 1, sets CheckBox to checked otherwise will leave unchecked
            if(complete == 1){
                lookupCheckbox.setChecked(true);
            }

            cursor.close();
            db.close();
        }

        //if the try statement causes an exception. Returns a Toast
        catch (SQLiteException exc) {
            exc.printStackTrace();
            Toast.makeText(this, "Job " + jl + " cannot be found in the database", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateJob(View view) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        Date date = new Date();

        //takes the EditText and puts them to String
        try{
            String jd = jobDescription.getText().toString();
            String jt = jobTitle.getText().toString();
            String je = jobEngineer.getText().toString();
            String ja = jobAsset.getText().toString();
            String jc = jobCustomer.getText().toString();
            String ji = jobID.getText().toString();

            //ContentValues that takes the String variables and then puts them to their respective table column
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.JOB_ID, ji);
            values.put(DatabaseHelper.JOB_TITLE, jt);
            values.put(DatabaseHelper.JOB_ENGINEER, je);
            values.put(DatabaseHelper.JOB_ASSET, ja);
            values.put(DatabaseHelper.JOB_CUSTOMER, jc);
            values.put(DatabaseHelper.JOB_DESCRIPTION, jd);
            values.put(DatabaseHelper.JOB_DATE_UPDATED, dateFormat.format(date));

            //if the CheckBox is checked then change the entitity's COMPLETE column to 1 otherwise leave as 0
            if(lookupCheckbox.isChecked()){
                values.put(DatabaseHelper.JOB_STATUS, 1);
            } else {
                values.put(DatabaseHelper.JOB_STATUS, 0);
            }

            //Toast confirming job + jobID has been updated
            db.update(DatabaseHelper.JOB_TABLE_NAME, values, DatabaseHelper.JOB_ID + "=" + ji, null);
            Toast.makeText(this, "Job " + ji + " updated", Toast.LENGTH_LONG).show();

            //take user back to home screen
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);

            db.close();
        }

        //catch Exception, take input and convert to String. Then display as Toast informing of an unsuccessful query
        catch(Exception exc){
            Toast.makeText(this, "Job not updated", Toast.LENGTH_LONG).show();
        }
    }

    public void returnMain(){
        final Intent mainIntent = new Intent(this, MainActivity.class);
        //http://stackoverflow.com/a/2478662/7087139
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
        builder.setMessage("Input will not be updated. Are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }
}