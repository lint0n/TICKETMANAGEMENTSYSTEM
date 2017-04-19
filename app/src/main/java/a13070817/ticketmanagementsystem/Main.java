package a13070817.ticketmanagementsystem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;
import java.util.ArrayList;
import static a13070817.ticketmanagementsystem.DatabaseHelper.TICKET_TABLE_NAME;
import static android.R.attr.author;

/**
 * Created by Samuel Linton SRN 13070817
 */

public class Main extends AppCompatActivity {
    Toolbar toolbar;
    SQLiteDatabase db;
    private ArrayList<String> results = new ArrayList<String>();
    private ListView lv;
    String queryResult;

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
    public boolean onCreateOptionsMenu(Menu menu) {
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
    public void createTicket(View view) {
        Intent newCreate = new Intent(this, Create.class);
        startActivity(newCreate);
    }

    //onclick take user to Search activity
    public void ticketLookup(MenuItem menuItem) {
        Intent newLookup = new Intent(this, Search.class);
        startActivity(newLookup);
    }

    public void statistics(MenuItem menuItem) {
        Intent newAbout = new Intent(this, Statistics.class);
        startActivity(newAbout);
    }

    //http://saigeethamn.blogspot.co.uk/2011/02/listview-of-data-from-sqlitedatabase.html
    void listQueryDB() {
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(this.getApplicationContext());
            db = dbHelper.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT ID, TITLE, DESCRIPTION, SEVERITY FROM " + TICKET_TABLE_NAME +
                    " WHERE STATUS = 0 ORDER BY SEVERITY ASC", null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        int ID = c.getInt(c.getColumnIndex("ID"));
                        String Title = c.getString(c.getColumnIndex("TITLE"));
                        String IDtoString = Integer.toString(ID);
                        queryResult = ("#" + IDtoString + " - " + Title);
                        results.add(queryResult);
                    } while (c.moveToNext());
                }
            }
            c.close();
        } catch (SQLiteException exc) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No tickets could be found.", Snackbar.LENGTH_LONG);
            snackbar.show();
            exc.printStackTrace();
        }
    }

    //http://stackoverflow.com/a/34328384/7087139
    void displayList() {
        final Intent intent = new Intent(this, Search.class);
        if (results.isEmpty()) {
            TextView tv = new TextView(this);
            lv.setDivider(null);
            tv.setTextSize(20);
            tv.setPadding(15, 15, 15, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setText("No unresolved tickets");
            lv.addHeaderView(tv);
        }
        //http://stackoverflow.com/a/18903852
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String r1 = results.get(i);
                    String r2 = r1.substring(r1.lastIndexOf("#") + 1);
                    String r3 = r1.substring(r1.lastIndexOf("-"));
                    String r4 = r2.replace(r3, "");
                    intent.putExtra("ticketToSearch", r4);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}