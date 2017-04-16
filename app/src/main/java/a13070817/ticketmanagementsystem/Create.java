package a13070817.ticketmanagementsystem;

/**
 * Created by Samuel Linton 13070817 on 24/02/2017.
 */

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Create extends AppCompatActivity {

    private EditText jobDescription, jobTitle, jobEngineer, jobAsset, jobCustomer;
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    private Toolbar toolbar;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        jobDescription = (EditText) findViewById(R.id.job_description);
        jobTitle = (EditText) findViewById(R.id.job_title);
        jobEngineer = (EditText) findViewById(R.id.job_engineer);
        jobAsset = (EditText) findViewById(R.id.job_asset);
        jobCustomer = (EditText) findViewById(R.id.job_customer);

        //https://developer.android.com/guide/topics/ui/controls/spinner.html
        spinner = (Spinner) findViewById(R.id.createspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.create_severity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Create");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_create, menu);
        return true;
    }

    //http://stackoverflow.com/a/4780009/7087139
    @Override
    public void onBackPressed() {
        returnMain();
    }

    public void insertData(MenuItem menuItem) {
        //https://developer.android.com/reference/java/text/SimpleDateFormat.html
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        Date date = new Date();
        try {
            String jd = jobDescription.getText().toString();
            String jt = jobTitle.getText().toString();
            String je = jobEngineer.getText().toString();
            String ja = jobAsset.getText().toString();
            String jc = jobCustomer.getText().toString();

            //Head First Android Chapter 2 p 63
            String sev = String.valueOf(spinner.getSelectedItem());

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.JOB_TITLE, jt);
            values.put(DatabaseHelper.JOB_ENGINEER, je);
            values.put(DatabaseHelper.JOB_ASSET, ja);
            values.put(DatabaseHelper.JOB_CUSTOMER, jc);
            values.put(DatabaseHelper.JOB_DESCRIPTION, jd);
            values.put(DatabaseHelper.JOB_STATUS, 0);
            values.put(DatabaseHelper.JOB_DATE, dateFormat.format(date));
            if (sev.contains("1-Critical")) {
                values.put(DatabaseHelper.JOB_SEVERITY, 1);
            } else if (sev.contains("2-High")) {
                values.put(DatabaseHelper.JOB_SEVERITY, 2);
            } else if (sev.contains("3-Medium")) {
                values.put(DatabaseHelper.JOB_SEVERITY, 3);
            } else if (sev.contains("4-Low")) {
                values.put(DatabaseHelper.JOB_SEVERITY, 4);
            }
            //if editText contains unpopulated fields do not create job otherwise create
            if (sev.contains("[Select severity]") || jt.isEmpty() || (je.isEmpty() || (ja.isEmpty() || (jc.isEmpty() || (jd.isEmpty()))))) {
                throw new Exception();
            } else {
                Long newRowId = db.insert(DatabaseHelper.JOB_TABLE_NAME, null, values);
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Job " + newRowId + " created.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        } catch (Exception exc) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Job not created, try again." , Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    public void returnMain() {
        final Intent mainIntent = new Intent(this, MainActivity.class);
        //http://stackoverflow.com/a/2478662/7087139
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //yes
                        startActivity(mainIntent);

                    case DialogInterface.BUTTON_NEGATIVE:
                        //no
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Input will not be saved. Are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }

    public void erase(MenuItem menuItem) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //yes - erase
                        jobDescription.setText("");
                        jobTitle.setText("");
                        jobEngineer.setText("");
                        jobAsset.setText("");
                        jobCustomer.setText("");
                    case DialogInterface.BUTTON_NEGATIVE:
                        //no
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Erase data");
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }
}
