package a13070817.ticketmanagementsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
    }

    //onclick take user to CreateJob activity
    public void createJob(View view) {
        Intent newCreate = new Intent(this, CreateJob.class);
        startActivity(newCreate);
    }

    //onclick take user to JobLookup activity
    public void jobLookup(View view) {
        Intent newLookup = new Intent(this, JobLookup.class);
        startActivity(newLookup);
    }
}