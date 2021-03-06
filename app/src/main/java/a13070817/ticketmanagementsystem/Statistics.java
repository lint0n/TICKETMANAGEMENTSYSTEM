package a13070817.ticketmanagementsystem;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import static a13070817.ticketmanagementsystem.DatabaseHelper.TICKET_TABLE_NAME;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * @author Samuel Linton 13070817
 */
public class Statistics extends AppCompatActivity {
    Toolbar toolbar;
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper databaseHelper;
    TextView openCountText, closedCountText;
    private ArrayList<PieEntry> jobEntries = new ArrayList<>();
    private ArrayList<PieEntry> severityEntries = new ArrayList<>();
    private PieChart pieChartTicket, pieChartSeverity;
    PieData dataJob, dataSeverity;
    PieDataSet dataSetJob, dataSetSeverity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Statistics");
        setSupportActionBar(toolbar);
        databaseHelper = new DatabaseHelper(this.getApplicationContext());
        sqLiteDatabase = databaseHelper.getWritableDatabase();
         //Queries Job table for jobs both complete and incomplete and assigns to respective Cursor,
         //it is then stored as int by accessing number of rows from each Cursor.
        try {
            Cursor openCursor = sqLiteDatabase.rawQuery("SELECT * FROM "
                    + TICKET_TABLE_NAME
                    + " WHERE STATUS = 0", null);
            Cursor closedCursor = sqLiteDatabase.rawQuery("SELECT * FROM "
                    + TICKET_TABLE_NAME
                    + " WHERE STATUS = 1", null);
            int open = openCursor.getCount();
            int closed = closedCursor.getCount();

            //MPAndroidChart library
            //https://github.com/PhilJay/MPAndroidChart/wiki/Setting-Data
            pieChartTicket = (PieChart) findViewById(R.id.piechart);
            jobEntries.add(new PieEntry(open, "Open"));
            jobEntries.add(new PieEntry(closed, "Closed"));
            dataSetJob = new PieDataSet(jobEntries, "");
            dataJob = new PieData(dataSetJob);
            pieChartTicket.setData(dataJob);
            pieChartTicket.getDescription().setText("");
            dataSetJob.setColors(ColorTemplate.MATERIAL_COLORS);
            pieChartTicket.setEntryLabelTextSize(16);
            pieChartTicket.setCenterText("Number of Tickets");
            pieChartTicket.invalidate();
            dataJob.setValueTextColor(Color.WHITE);
            dataJob.setValueTextSize(18);
            openCursor.close();
            closedCursor.close();

            //Queries Job table for jobs based on severity which is then cast to pie charts
            Cursor lowCursor = sqLiteDatabase.rawQuery("SELECT * FROM " +
                    TICKET_TABLE_NAME + " WHERE SEVERITY = 4 AND STATUS = 0", null);
            Cursor mediumCursor = sqLiteDatabase.rawQuery("SELECT * FROM " +
                    TICKET_TABLE_NAME + " WHERE SEVERITY = 3 AND STATUS = 0", null);
            Cursor highCursor = sqLiteDatabase.rawQuery("SELECT * FROM " +
                    TICKET_TABLE_NAME + " WHERE SEVERITY = 2 AND STATUS = 0", null);
            Cursor criticalCursor = sqLiteDatabase.rawQuery("SELECT * FROM " +
                    TICKET_TABLE_NAME + " WHERE SEVERITY = 1 AND STATUS = 0", null);

            int low = lowCursor.getCount();
            int medium = mediumCursor.getCount();
            int high = highCursor.getCount();
            int critical = criticalCursor.getCount();
            pieChartSeverity = (PieChart) findViewById(R.id.piechartseverity);
            severityEntries.add(new PieEntry(low, "Low"));
            severityEntries.add(new PieEntry(medium, "Medium"));
            severityEntries.add(new PieEntry(high, "High"));
            severityEntries.add(new PieEntry(critical, "Critical"));
            dataSetSeverity = new PieDataSet(severityEntries, "");
            dataSeverity = new PieData(dataSetSeverity);
            pieChartSeverity.setData(dataSeverity);
            pieChartSeverity.getDescription().setText("");
            dataSetSeverity.setColors(ColorTemplate.MATERIAL_COLORS);
            pieChartSeverity.setEntryLabelTextSize(16);
            pieChartSeverity.setCenterText("Open Tickets by Severity");
            pieChartSeverity.invalidate();
            dataSeverity.setValueTextColor(Color.WHITE);
            dataSeverity.setValueTextSize(18);
            lowCursor.close();
            mediumCursor.close();
            highCursor.close();
            criticalCursor.close();
            //Queries the number of jobs created and closed in the last week
            //http://www.sqlite.org/lang_datefunc.html
            Cursor weekCursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TICKET_TABLE_NAME + " WHERE DATE_CREATED >= date('now', '-7 day')", null);
            Cursor weekClosedCursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TICKET_TABLE_NAME + " WHERE DATE_CREATED >= date('now', '-7 day') AND STATUS = 1", null);
            int weekCursorCount = weekCursor.getCount();
            int weekClosedCursorCount = weekClosedCursor.getCount();
            String weekCount = "Tickets Created Past 7 Days: \n\n" + weekCursorCount;
            String weekClosedCount = "Tickets Closed Past 7 Days: \n\n" + weekClosedCursorCount;
            openCountText = (TextView) findViewById(R.id.openCountText);
            openCountText.setText(weekCount);
            openCountText.setTextSize(24);
            openCountText.setTextColor(Color.BLACK);
            openCountText.setGravity(Gravity.CENTER);
            closedCountText = (TextView) findViewById(R.id.closedCountText);
            closedCountText.setText(weekClosedCount);
            closedCountText.setTextSize(24);
            closedCountText.setTextColor(Color.BLACK);
            closedCountText.setGravity(Gravity.CENTER);
            weekCursor.close();
            weekClosedCursor.close();
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_statistics, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent newCreate = new Intent(this, Main.class);
        startActivity(newCreate);
    }

    /**
     * Utilises MPAndroidChart method that saves chart graphics as .jpeg files to internal storage
     * @param menuItem MenuItem interface object
     */
    public void saveToGallery(MenuItem menuItem) {
        try {
            int permissionCheck = ContextCompat.checkSelfPermission(Statistics.this, WRITE_EXTERNAL_STORAGE);
            if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Statistics.this, new String[]{WRITE_EXTERNAL_STORAGE}, 1);
            }
            pieChartTicket.saveToGallery("Tickets.jpg", 100);
            pieChartSeverity.saveToGallery("Severity.jpg", 100);
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Images downloaded to internal storage.", Snackbar.LENGTH_LONG);
            snackbar.show();
        } catch (Exception e) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Image could not be downloaded.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
