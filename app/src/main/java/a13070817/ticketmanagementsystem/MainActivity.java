package a13070817.ticketmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //https://www.youtube.com/watch?v=LpiIBjLzhh4
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    //onclick take user to CreateJob activity
    //http://stackoverflow.com/a/17396896/7087139
    public void createJob(MenuItem menuItem) {
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

}