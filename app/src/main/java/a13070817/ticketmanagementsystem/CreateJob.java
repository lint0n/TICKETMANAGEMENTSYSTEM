package a13070817.ticketmanagementsystem;

/**
 * Created by Sam on 24/02/2017.
 */

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateJob extends AppCompatActivity{

    private EditText jobDescription, jobTitle, jobEngineer, jobAsset, jobCustomer;
    SQLiteDatabase db;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job);
        jobDescription = (EditText) findViewById(R.id.job_description);
        jobTitle = (EditText) findViewById(R.id.job_title);
        jobEngineer = (EditText) findViewById(R.id.job_engineer);
        jobAsset = (EditText) findViewById(R.id.job_asset);
        jobCustomer = (EditText) findViewById(R.id.job_customer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_create, menu);
        return true;
    }

    //http://stackoverflow.com/a/4780009/7087139
    @Override
    public void onBackPressed() {
        Intent newCreate = new Intent(this, MainActivity.class);
        startActivity(newCreate);
    }

    public void insertData(View view) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
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
            values.put(DatabaseHelper.JOB_DATE, System.currentTimeMillis());

            //if editText contains unpopulated fields do not create job otherwise create
            if((jt.equals("")) || (je.equals("")) || (ja.equals("")) || (jc.equals("")) || (jd.equals("")))
            {
                Toast.makeText(this, "Job not created due to missing fields", Toast.LENGTH_LONG).show();
            }
            else {
                long newRowId = db.insert(DatabaseHelper.JOB_TABLE_NAME, null, values);
                Toast.makeText(this, "Job " + newRowId + " created", Toast.LENGTH_LONG).show();
            }
        }

        catch(Exception exc){
            Toast.makeText(this, "Error: Job not created", Toast.LENGTH_LONG).show();
        }
    }

    public void returnMain(MenuItem menuItem){
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}
