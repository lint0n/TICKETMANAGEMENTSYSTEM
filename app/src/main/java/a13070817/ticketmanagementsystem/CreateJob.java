package a13070817.ticketmanagementsystem;

/**
 * Created by Sam on 24/02/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class CreateJob extends AppCompatActivity{

    private EditText jobDescription, jobTitle, jobEngineer, jobAsset, jobCustomer;
    private TextInputLayout layoutTitle;
    private SQLiteDatabase db;
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

//        jobTitle.addTextChangedListener(new MyTextWatcher(jobTitle));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_create, menu);
        return true;
    }

    //http://www.androidhive.info/2015/09/android-material-design-floating-labels-for-edittext/
//    private boolean validateTitle() {
//        if (jobTitle.getText().toString().trim().isEmpty()) {
//            layoutTitle.setError(getString(R.string.error_title));
////            requestFocus(jobTitle);
//            return false;
//        } else {
//            layoutTitle.setErrorEnabled(false);
//        }
//
//        return true;
//    }

    //http://stackoverflow.com/a/4780009/7087139
    @Override
    public void onBackPressed(){
        returnMain();
    }

//    @Override
//    public void onClick(View view) {
//        InputMethodManager imm = (InputMethodManager) view.getContext()
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }

    public void insertData(MenuItem menuItem) {
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
            if((jt.isEmpty() || (je.isEmpty() || (ja.isEmpty() || (jc.isEmpty() || (jd.isEmpty()))))))
            {
                throw new Exception();
            }
            else {
                long newRowId = db.insert(DatabaseHelper.JOB_TABLE_NAME, null, values);
                Toast.makeText(this, "Job " + newRowId + " created", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }

        catch(Exception exc){
            Toast.makeText(this, "Error: Job not created", Toast.LENGTH_LONG).show();
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

//    //http://www.androidhive.info/2015/09/android-material-design-floating-labels-for-edittext/
//    private void requestFocus(View view){
//        if (view.requestFocus()){
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
//    }

//    //http://www.androidhive.info/2015/09/android-material-design-floating-labels-for-edittext/
//    private class MyTextWatcher implements TextWatcher{
//        private View view;
//
//        private MyTextWatcher(View view){
//            this.view = view;
//        }
//
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        public void afterTextChanged(Editable editable){
//            switch (view.getId()){
//                case R.id.job_title:
//                    validateTitle();
//                    break;
//            }
//        }
//    }
}
