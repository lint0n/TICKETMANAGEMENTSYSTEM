package a13070817.ticketmanagementsystem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

public class Statistics extends AppCompatActivity {

    Toolbar toolbar;
    SQLiteDatabase db;
    DatabaseHelper dbHelper;

    //Pie chart variables
    private ArrayList<PieEntry> jobEntries = new ArrayList<>();
    private ArrayList<PieEntry> severityEntries = new ArrayList<>();
    private ArrayList<HorizontalBarChart> weekEntries = new ArrayList<>();
    private PieChart pieChartTicket, pieChartSeverity;
    PieData dataJob, dataSeverity;
    PieDataSet dataSetJob, dataSetSeverity;
    TextView openCountText, closedCountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Statistics");
        setSupportActionBar(toolbar);
        dbHelper = new DatabaseHelper(this.getApplicationContext());
        db = dbHelper.getWritableDatabase();


        /**
         * Queries Job table for jobs both complete and incomplete and assigns to respective Cursor,
         * then stored as int by accessing number of rows from each Cursor.
         */
        try {
            Cursor openCursor = db.rawQuery("SELECT * FROM " + TICKET_TABLE_NAME + " WHERE STATUS = 0", null);
            Cursor closedCursor = db.rawQuery("SELECT * FROM " + TICKET_TABLE_NAME + " WHERE STATUS = 1", null);
            int open = openCursor.getCount();
            int closed = closedCursor.getCount();

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
            pieChartTicket.setCenterText("Number of Calls");
            pieChartTicket.invalidate();
            dataJob.setValueTextColor(Color.WHITE);
            dataJob.setValueTextSize(18);

            openCursor.close();
            closedCursor.close();

            /**
             * Queries Job table for jobs based on severity which is then cast to bar chart
             */
            Cursor lowCursor = db.rawQuery("SELECT * FROM " + TICKET_TABLE_NAME + " WHERE SEVERITY = 4 AND STATUS = 0", null);
            Cursor mediumCursor = db.rawQuery("SELECT * FROM " + TICKET_TABLE_NAME + " WHERE SEVERITY = 3 AND STATUS = 0", null);
            Cursor highCursor = db.rawQuery("SELECT * FROM " + TICKET_TABLE_NAME + " WHERE SEVERITY = 2 AND STATUS = 0", null);
            Cursor criticalCursor = db.rawQuery("SELECT * FROM " + TICKET_TABLE_NAME + " WHERE SEVERITY = 1 AND STATUS = 0", null);

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
            pieChartSeverity.setCenterText("Open Calls by Severity");
            pieChartSeverity.invalidate();
            dataSeverity.setValueTextColor(Color.WHITE);
            dataSeverity.setValueTextSize(18);

            lowCursor.close();
            mediumCursor.close();
            highCursor.close();
            criticalCursor.close();

            //Queries the number of jobs created and closed in the last week
            //http://www.sqlite.org/lang_datefunc.html

            Cursor weekCursor = db.rawQuery("SELECT * FROM " + TICKET_TABLE_NAME + " WHERE DATE_CREATED >= date('now', '-7 day')", null);
            Cursor weekClosedCursor = db.rawQuery("SELECT * FROM " + TICKET_TABLE_NAME + " WHERE DATE_CREATED >= date('now', '-7 day') AND STATUS = 1", null);
            int weekCursorCount = weekCursor.getCount();
            int weekClosedCursorCount = weekClosedCursor.getCount();
            String weekCount = "Jobs Created Past 7 Days: \n\n" + weekCursorCount;
            String weekClosedCount = "Jobs Closed Past 7 Days: \n\n" + weekClosedCursorCount;

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

    //http://stackoverflow.com/a/4780009/7087139
    @Override
    public void onBackPressed() {
        Intent newCreate = new Intent(this, Main.class);
        startActivity(newCreate);
    }

    public void saveToGallery(MenuItem menuItem) {
        try {
            pieChartTicket.saveToGallery("Tickets", 100);
            pieChartSeverity.saveToGallery("Severity", 100);
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Images downloaded to gallery.", Snackbar.LENGTH_LONG);
            snackbar.show();
        } catch (Exception e) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Image could not be downloaded.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
