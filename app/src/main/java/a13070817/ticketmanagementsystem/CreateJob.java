package a13070817.ticketmanagementsystem;

/**
 * Created by Sam on 24/02/2017.
 */

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateJob extends Activity{

    private EditText jobDescription, jobTitle, jobEngineer, jobAsset, jobCustomer;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job);
        jobDescription = (EditText) findViewById(R.id.job_description);
        jobTitle = (EditText) findViewById(R.id.job_title);
        jobEngineer = (EditText) findViewById(R.id.job_engineer);
        jobAsset = (EditText) findViewById(R.id.job_asset);
        jobCustomer = (EditText) findViewById(R.id.job_customer);
    }

    public void insertData(View view) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        //http://stackoverflow.com/questions/754684/how-to-insert-a-sqlite-record-with-a-datetime-set-to-now-in-android-applicatio
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date d = new Date();

        try {
            String jd = jobDescription.getText().toString();
            String jt = jobTitle.getText().toString();
            String je = jobEngineer.getText().toString();
            String ja = jobAsset.getText().toString();
            String jc = jobCustomer.getText().toString();

            jobDescription.setText("");
            jobTitle.setText("");
            jobEngineer.setText("");
            jobAsset.setText("");
            jobCustomer.setText("");

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.JOB_TITLE, jt);
            values.put(DatabaseHelper.JOB_ENGINEER, je);
            values.put(DatabaseHelper.JOB_ASSET, ja);
            values.put(DatabaseHelper.JOB_CUSTOMER, jc);
            values.put(DatabaseHelper.JOB_DESCRIPTION, jd);
            values.put(DatabaseHelper.JOB_STATUS, 0);
            values.put(DatabaseHelper.JOB_DATE, dateFormat.format(d));

            long newRowId = db.insert(DatabaseHelper.JOB_TABLE_NAME, null, values);
            Toast.makeText(this, "Job " + newRowId + " created at: \n " + d, Toast.LENGTH_LONG).show();
        }

        catch(Exception exc){
            Toast.makeText(this, "ERROR: Job not created", Toast.LENGTH_LONG).show();
        }
    }
}
