package com.nedatatech.datatechportal;

import android.content.Context;
import android.util.Log;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

  private static final String logtag = "MAIN_ACT_LOGS";
  private DatabaseOpenerTask task; // Global access to the Task.

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    task = new DatabaseOpenerTask(); // Initialize the task.
    task.execute(); // Call the tasks execute method. This is the only way to start the threading task. There are options for running it different ways like in parallel. See Docs.
    Log.v(logtag, task.toString());
  }

  // Start AsyncTask class and methods.
  // First attempt at implementing AsyncTask. Experimenting with proper usage and parameters. See Google docs reference on AsyncTask and my comments here for basic help.
  private class DatabaseOpenerTask extends AsyncTask<Void, Integer, String> { // This is working but needs optimizing for the Progress bar to actually show the progress as its happening.
    // Though it is working also. Might be too fast to see it right now. Which is obviously a good thing. Need to experiment more and use in other places but will need to also be adapted
    // for the various operations that we will need the whole AsyncTask system for.
    private DatabaseOperations dataOps = new DatabaseOperations(MainActivity.this); // Global field variables for the task class. Database instance and progress bar reference.
    ProgressBar taskProgress = (ProgressBar) findViewById(R.id.mainActProgBar);

    @Override
    protected void onPreExecute() { // Would set the UI to show some kind of progress updates here and any pre execute code that may be needed.
      super.onPreExecute();
      Log.v(logtag, "BackGround Thread Starting");
      taskProgress.setVisibility(View.VISIBLE); // Should be invisible before this starts but for experimenting I have added one through layout xml and already set visible.
      taskProgress.incrementProgressBy(25); // Layout xml already has progress set to 25 to start then we start adding to the amount for it to show.
    }

    @Override
    protected String doInBackground(Void... params) { // An very simple example of the actual task being executed and telling the Progress to update.
      Log.v(logtag, "BackGround Thread Running");
      publishProgress(25); // Calls the onProgressUpdate method and adds another 25 to the progress amount which is the values parameter.
      dataOps.openDB(); // The actual job we want the thread to perform.
      return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) { // An example of handling the amount of progress to show when the Thread is running.
      super.onProgressUpdate(values);
      taskProgress.incrementProgressBy(values[0]);
      if(taskProgress.getProgress() >= 70){
      taskProgress.incrementProgressBy(25);
        // Could set taskProgress to INVISIBLE here if desired or setProgress to 100, but better to show its finished in PostExecute Method.
      }
      Log.v(logtag, "values: " + values[0].toString());
      Log.v(logtag, "progress bar: " + String.valueOf(taskProgress.getProgress()));
    }

    @Override
    protected void onPostExecute(String s) { // The final method that is run when the Thread finishes and sends updates to the UI.
      super.onPostExecute(s);
      Log.v(logtag, "BackGround Thread Finished");
      // taskProgress.setProgress(100); // Could set progress to 100 here if desired and update the UI to reflect that to the user.
      if(taskProgress.getProgress() > 99) {
        //taskProgress.setVisibility(View.INVISIBLE); // An example of how to handle the UI progress when the Thread would finish.
      }
    }
  } // End AsyncTask Class and Methods.

  public void customerButton(View view){
    Intent customerStart = new Intent(this, CustomerMainActivity.class);
    startActivity(customerStart);
  }

  public void jobButton(View view){
    Intent jobStart = new Intent(this, JobActivity.class);
    startActivity(jobStart);
  }

  public void inventoryButton(View view){
    Intent inventoryStart = new Intent(this, InventoryActivity.class);
    startActivity(inventoryStart);
  }

  public void companyButton(View view){
    Intent companyStart = new Intent(this, CompanyActivity.class);
    startActivity(companyStart);
  }

  // Calling this method just to make sure the App gets closed out and the database closed properly.
  @Override
  public void onBackPressed() {
    super.onBackPressed();
    task.dataOps.closeDB(); // Should close the database when back is pressed in this Activity and Destroy the app.
    finish(); // Kills this activity but seems like the app still runs in the background or just looks that way because of android studio looking like it still has an open connection to the app.
  }

  @Override
  protected void onDestroy() {
    super.onDestroy(); // Should close the Database here but only destroy the main activity when the user chooses to close the whole app.
                        // Will need to do some research and testing on this further.
    System.exit(0); // Experimenting ways to make sure the App exits completely for security. Not sure how this affects memory allocations and GC yet though.
  }
}
/* ToDo Should probably call the onPause and onResume methods for safety and open and close the
ToDo database respectively but will want to test and see how this affects performance.*/
