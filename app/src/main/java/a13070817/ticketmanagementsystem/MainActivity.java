package a13070817.ticketmanagementsystem;

import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //https://www.youtube.com/watch?v=LpiIBjLzhh4
    private Toolbar toolbar;
    private SQLiteDatabase db;
    private ArrayList<String> results = new ArrayList<String>();
    private ListView lv;
    private RelativeLayout relativeLayout;
    private String queryResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lv = (ListView) findViewById(R.id.listview);
        lv.setCacheColorHint(Color.WHITE);
        listQueryDB();
        displayList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        new ComponentName(this, Search.class);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    //http://stackoverflow.com/a/3725042/7087139
    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
    }

    //onclick take user to Create activity
    //http://stackoverflow.com/a/17396896/7087139
    public void createJob(View view) {
        Intent newCreate = new Intent(this, Create.class);
        startActivity(newCreate);
    }

    //onclick take user to Search activity
    public void jobLookup(MenuItem menuItem) {
        Intent newLookup = new Intent(this, Search.class);
        startActivity(newLookup);
    }

    public void about(MenuItem menuItem) {
        Intent newAbout = new Intent(this, Statistics.class);
        startActivity(newAbout);
    }

    //http://saigeethamn.blogspot.co.uk/2011/02/listview-of-data-from-sqlitedatabase.html
    void listQueryDB() {
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(this.getApplicationContext());
            db = dbHelper.getWritableDatabase();
            String severity;
            //http://www.1keydata.com/sql/sqlorderby.html
            Cursor c = db.rawQuery("SELECT ID, TITLE, DESCRIPTION, SEVERITY FROM " + dbHelper.JOB_TABLE_NAME +
            " WHERE COMPLETE = 0 ORDER BY SEVERITY ASC", null);

            if (c != null){
                if (c.moveToFirst()){
                    do {
                        int ID = c.getInt(c.getColumnIndex("ID"));
                        String Title = c.getString(c.getColumnIndex("TITLE"));
                        String IDtoString = Integer.toString(ID);
                        queryResult = ("#" + IDtoString + " - " + Title);
                        results.add(queryResult);
                    }while (c.moveToNext());
                }
            }
            c.close();
        } catch (SQLiteException exc) {
            Toast.makeText(this, "No jobs could be found", Toast.LENGTH_LONG).show();
        }
    }

    //http://stackoverflow.com/a/34328384/7087139
    void displayList() {
        final Intent jobLookup = new Intent(this, Search.class);
        if (results.size() == 0) {
            TextView tv = new TextView(this);
            tv.setTextSize(18);
            tv.setPadding(10, 10, 10, 10);
            tv.setGravity(Gravity.CENTER);
            tv.setText("You currently have no open jobs");
            lv.addHeaderView(tv);
        }
        //http://stackoverflow.com/a/18903852
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                //takes item from ArrayList, removes the head of string up to "ID:".
                    //e.g. "[High] - ID: 1 - Title"    ->    "1 - Title"
                String r1 = results.get(i);
                String r2 = r1.substring(r1.lastIndexOf("#")+1);
                //removes the end of string from "-" onwards then trims String whitespace
                    //e.g. "1 - Title"    ->    "1"
                String r3 = r1.substring(r1.lastIndexOf("-"));
                String r4 = r2.replace(r3, "");
                r4.trim();
                jobLookup.putExtra("string", r4);
                startActivity(jobLookup);
                finish();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}