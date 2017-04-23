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

/**
 * @author Samuel Linton 13070817
 */
public class Main extends AppCompatActivity {
    Toolbar toolbar;
    SQLiteDatabase sqLiteDatabase;
    private ArrayList<String> results = new ArrayList<>();
    private ListView listView;
    String queryResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.listview);
        listView.setCacheColorHint(Color.WHITE);
        listQueryDb();
        displayList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
    }

    public void createTicket(View view) {
        Intent newCreate = new Intent(this, Create.class);
        startActivity(newCreate);
    }

    public void ticketLookup(MenuItem menuItem) {
        Intent newLookup = new Intent(this, Search.class);
        startActivity(newLookup);
    }

    public void statistics(MenuItem menuItem) {
        Intent newAbout = new Intent(this, Statistics.class);
        startActivity(newAbout);
    }

    /**
     * Ticket database query. Orders incomplete tickets based on severity.
     * Code adapted from: http://saigeethamn.blogspot.co.uk/2011/02/listview-of-data-from-sqlitedatabase.html
     */
    void listQueryDb() {
        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(this.getApplicationContext());
            sqLiteDatabase = databaseHelper.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT ID, TITLE, SEVERITY FROM " + TICKET_TABLE_NAME +
                    " WHERE STATUS = 0 ORDER BY SEVERITY ASC", null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex("ID"));
                        String title = cursor.getString(cursor.getColumnIndex("TITLE"));
                        String idToString = Integer.toString(id);
                        queryResult = ("#" + idToString + " - " + title);
                        results.add(queryResult);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (SQLiteException exc) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No tickets could be found.", Snackbar.LENGTH_LONG);
            snackbar.show();
            exc.printStackTrace();
        }
    }

    /**
     * Displays the resulting ArrayAdapter using results gained from listQueryDb().
     * Code adapted from: http://stackoverflow.com/a/34328384/7087139
     */
    void displayList() {
        final Intent intent = new Intent(this, Search.class);
        if (results.isEmpty()) {
            TextView textView = new TextView(this);
            listView.setDivider(null);
            textView.setTextSize(20);
            textView.setPadding(15, 15, 15, 15);
            textView.setGravity(Gravity.CENTER);
            textView.setText("No unresolved tickets");
            listView.addHeaderView(textView);
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, results);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String temp1 = results.get(i);
                    String temp2 = temp1.substring(temp1.lastIndexOf("#") + 1);
                    String temp3 = temp1.substring(temp1.lastIndexOf("-"));
                    String ticketToSearch = temp2.replace(temp3, "");
                    intent.putExtra("ticketToSearch", ticketToSearch);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}