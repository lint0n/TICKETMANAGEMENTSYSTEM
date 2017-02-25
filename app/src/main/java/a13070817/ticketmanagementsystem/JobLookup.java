package a13070817.ticketmanagementsystem;

/**
 * Created by Sam on 24/02/2017.
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import java.util.Date;

public class JobLookup extends AppCompatActivity{

    //Fields
    private EditText jobLookup, jobID, jobTitle, jobAsset, jobCustomer, jobDescription, jobEngineer, jobDate;
    private Button lookupButton, updateButton;
    CheckBox lookupCheckbox;
    private SQLiteDatabase db;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_lookup);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //EditText instances
        jobLookup = (EditText) findViewById(R.id.job_lookup_input);
        jobID = (EditText) findViewById(R.id.JobID);
        jobTitle = (EditText) findViewById(R.id.JobTitle);
        jobAsset = (EditText) findViewById(R.id.JobAsset);
        jobCustomer = (EditText) findViewById(R.id.JobCustomer);
        jobDescription = (EditText) findViewById(R.id.JobDescription);
        jobEngineer = (EditText) findViewById(R.id.JobEngineer);

        //Button instances
        lookupButton = (Button) findViewById(R.id.lookup_button);
        updateButton = (Button) findViewById(R.id.lookup_update_button);

        //CheckBox instance
        lookupCheckbox = (CheckBox) findViewById(R.id.checkBox);
        lookupCheckbox = (CheckBox) findViewById(R.id.checkBox);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_back, menu);
        return true;
    }

    public void lookupJob(View view) {

        //Local String instance getting text from JobLookup EditText
        String jl = jobLookup.getText().toString();

        //Local instance of DatabaseHelper class
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        //try catch block - if job exists in database return data. If job doesn't not exist, catch Exception and return Toast text.
        try {

            //Cursor storing SQLite query, accessing ID/TITLE/ASSET/ENGINEER/CUSTOMER/DESCRIPTION/COMPLETE columns.
            Cursor cursor = db.query("JOB", new String[]{"ID", "TITLE",  "ASSET", "ENGINEER", "CUSTOMER", "DESCRIPTION", "COMPLETE"}, "ID = ?", new String[]{jl}, null, null, null, null);

            cursor.moveToFirst();
            String idText = cursor.getString(0);
            String titleText = cursor.getString(1);
            String assetText = cursor.getString(2);
            String engineerText = cursor.getString(3);
            String customerText = cursor.getString(4);
            String descriptionText = cursor.getString(5);
            Integer complete = cursor.getInt(6);

            //ensures that EditText elements are empty before setting text
            jobDescription.setText("");
            jobTitle.setText("");
            jobEngineer.setText("");
            jobID.setText("");
            jobAsset.setText("");
            jobCustomer.setText("");

            //sets text using local String variables
            jobID.setText(idText);
            jobTitle.setText(titleText);
            jobEngineer.setText(engineerText);
            jobAsset.setText(assetText);
            jobCustomer.setText(customerText);
            jobDescription.setText(descriptionText);

            //stops user from accessing these EditTexts once a job had been looked up
            jobLookup.setFocusable(false);
            jobLookup.setClickable(false);
            jobID.setFocusable(false);
            jobID.setClickable(false);
            lookupButton.setFocusable(false);
            lookupButton.setClickable(false);

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
        catch (Exception exc) {
            Toast.makeText(this, "Error: Job " + jl + " cannot be found in the database", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateJob(View view) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

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

            //if the CheckBox is checked then change the entitity's COMPLETE column to 1 otherwise leave as 0
            if(lookupCheckbox.isChecked()){
                values.put(DatabaseHelper.JOB_STATUS, 1);
            } else {
                values.put(DatabaseHelper.JOB_STATUS, 0);
            }

            //take user back to home screen
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);

            //Toast confirming job + jobID has been updated
            db.update(DatabaseHelper.JOB_TABLE_NAME, values, DatabaseHelper.JOB_ID + "=" + ji, null);
            Toast.makeText(this, "Job " + ji + " updated", Toast.LENGTH_LONG).show();
        }

        //catch Exception, take input and convert to String. Then display as Toast informing of an unsuccessful query
        catch(Exception exc){
            Toast.makeText(this, "Error: Job not updated", Toast.LENGTH_LONG).show();
        }
    }

    public void returnMain(MenuItem menuItem){
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}