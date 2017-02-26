package a13070817.ticketmanagementsystem;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //https://www.youtube.com/watch?v=LpiIBjLzhh4
    private Toolbar toolbar;
    private SQLiteDatabase db;
    private ArrayList<String> results = new ArrayList<String>();
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listQueryDB();
        displayList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    //onclick take user to CreateJob activity
    //http://stackoverflow.com/a/17396896/7087139
    public void createJob(View view) {
        Intent newCreate = new Intent(this, CreateJob.class);
        startActivity(newCreate);
    }

    //onclick take user to JobLookup activity
    public void jobLookup(MenuItem menuItem) {
        Intent newLookup = new Intent(this, JobLookup.class);
        startActivity(newLookup);
    }

    public void displayList(View view) {
        Intent newList = new Intent(this, JobList.class);
        startActivity(newList);
    }

    public void about(MenuItem menuItem) {
        Intent newAbout = new Intent(this, About.class);
        startActivity(newAbout);
    }

    //http://saigeethamn.blogspot.co.uk/2011/02/listview-of-data-from-sqlitedatabase.html
    void listQueryDB() {
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(this.getApplicationContext());
            db = dbHelper.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT ID, TITLE, DESCRIPTION FROM " + dbHelper.JOB_TABLE_NAME +
            " where COMPLETE = 0", null);

            if (c != null){
                if (c.moveToFirst()){
                    do {
                        int ID = c.getInt(c.getColumnIndex("ID"));
                        String Title = c.getString(c.getColumnIndex("TITLE"));
                        results.add("Job ID: " + ID + "   " + Title);
                    }while (c.moveToNext());
                }
            }
        } catch (SQLiteException exc) {
            Toast.makeText(this, "No jobs could be found", Toast.LENGTH_LONG).show();
        }
    }

    //http://stackoverflow.com/a/34328384/7087139
    void displayList() {
        TextView tv = new TextView(this);
        tv.setText("     Jobs currently open:");

        lv = (ListView) findViewById(R.id.listview);
        lv.addHeaderView(tv);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results);
        lv.setAdapter(adapter);
    }

}