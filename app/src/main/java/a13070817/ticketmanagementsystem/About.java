package a13070817.ticketmanagementsystem;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class About extends AppCompatActivity {

    private Toolbar toolbar;
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    //Pie chart variables
    private ArrayList<PieEntry> jobEntries = new ArrayList<>();
    private ArrayList<PieEntry> severityEntries = new ArrayList<>();
    private ArrayList<HorizontalBarChart> weekEntries = new ArrayList<>();
    private PieChart pieChartJob, pieChartSeverity;
    private PieData dataJob, dataSeverity;
    private PieDataSet dataSetJob, dataSetSeverity;
    private TextView openCountText, closedCountText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Statistics");
        setSupportActionBar(toolbar);
        dbHelper = new DatabaseHelper(this.getApplicationContext());
        db = dbHelper.getWritableDatabase();

        /**
         * Queries Job table for jobs both complete and incomplete and assigns to respective Cursor,
         * then stored as int by accessing number of rows from each Cursor.
         */
        Cursor openCursor = db.rawQuery("SELECT * FROM " + dbHelper.JOB_TABLE_NAME + " WHERE COMPLETE = 0", null);
        Cursor closedCursor = db.rawQuery("SELECT * FROM " + dbHelper.JOB_TABLE_NAME + " WHERE COMPLETE = 1", null);
        int open = openCursor.getCount();
        int closed = closedCursor.getCount();

        //https://github.com/PhilJay/MPAndroidChart/wiki/Setting-Data
        pieChartJob = (PieChart) findViewById(R.id.piechart);
        jobEntries.add(new PieEntry(open, "Open"));
        jobEntries.add(new PieEntry(closed, "Closed"));
        dataSetJob = new PieDataSet(jobEntries, "");
        dataJob = new PieData(dataSetJob);
        pieChartJob.setData(dataJob);
        pieChartJob.getDescription().setText("");
        dataSetJob.setColors(ColorTemplate.MATERIAL_COLORS);
        pieChartJob.setEntryLabelTextSize(16);
        pieChartJob.setCenterText("Number of Calls");
        pieChartJob.invalidate();
        dataJob.setValueTextColor(Color.WHITE);
        dataJob.setValueTextSize(18);

        /**
         * Queries Job table for jobs based on severity which is then cast to bar chart
         */
        Cursor lowCursor = db.rawQuery("SELECT * FROM " + dbHelper.JOB_TABLE_NAME + " WHERE SEVERITY = 4 AND COMPLETE = 0", null);
        Cursor mediumCursor = db.rawQuery("SELECT * FROM " + dbHelper.JOB_TABLE_NAME + " WHERE SEVERITY = 3 AND COMPLETE = 0", null);
        Cursor highCursor = db.rawQuery("SELECT * FROM " + dbHelper.JOB_TABLE_NAME + " WHERE SEVERITY = 2 AND COMPLETE = 0", null);
        Cursor criticalCursor = db.rawQuery("SELECT * FROM " + dbHelper.JOB_TABLE_NAME + " WHERE SEVERITY = 1 AND COMPLETE = 0", null);

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

        /**
         * Queries the number of jobs created and closed in the last week
         * http://www.sqlite.org/lang_datefunc.html
         */
        Cursor weekCursor = db.rawQuery("SELECT * FROM " + dbHelper.JOB_TABLE_NAME + " WHERE DATE_CREATED >= date('now', '-7 day')", null);
        Cursor weekClosedCursor = db.rawQuery("SELECT * FROM " + dbHelper.JOB_TABLE_NAME + " WHERE DATE_CREATED >= date('now', '-7 day') AND COMPLETE = 1", null);
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



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_about, menu);
        return true;
    }

    //http://stackoverflow.com/a/4780009/7087139
    @Override
    public void onBackPressed() {
        Intent newCreate = new Intent(this, MainActivity.class);
        startActivity(newCreate);
    }

    public void saveToGallery(MenuItem menuItem){
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String string = dateFormat.format(date);
            string.replaceAll("\\s+", "");
            pieChartJob.saveToGallery("Ticket_Job_Chart_" + string, 100);
            Toast.makeText(this, "Image downloaded to gallery", Toast.LENGTH_LONG).show();
        } catch (Exception e){
            Toast.makeText(this, "Image could not be downloaded", Toast.LENGTH_LONG).show();
        }
    }
}
