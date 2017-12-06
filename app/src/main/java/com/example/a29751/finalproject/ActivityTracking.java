package com.example.a29751.finalproject;
/**
 * Date: 2017-10-31
 * Finished by Wang Yongdan
 * This is about Activity Tracking
 *
 */
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.media.CamcorderProfile.get;

public class ActivityTracking extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ActivityTracking";
    Button saveButton;
    Spinner activitySpinner;
    EditText timeText;
    EditText commentsText;
    ListView listView;
    activityTrackingDatabaseHelper dbHelper;
    SQLiteDatabase db;
    ArrayList<Activites> messages = new ArrayList<Activites>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        saveButton=(Button)findViewById(R.id.saveButton);
        activitySpinner=(Spinner)findViewById(R.id.spinner);
        timeText=(EditText)findViewById(R.id.editText1);
        commentsText=(EditText)findViewById(R.id.editText2);
        listView = (ListView) findViewById(R.id.listView);

        String[] arraySpinner= new String[] {
                "Running", "Walking", "Biking", "Swimming", "Skating"
        };
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        activitySpinner.setAdapter(spinnerAdapter);

        // Create a progress bar
        ProgressBar progressBar = new ProgressBar(this);
        //progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
        //LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        listView.setEmptyView(progressBar);

        dbHelper = new activityTrackingDatabaseHelper(this) ;
        db = dbHelper.getWritableDatabase();

        Cursor c = db.rawQuery("select * from " + dbHelper.TABLE_NAME,null);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE " + c.getString(c.getColumnIndex(dbHelper.KEY_TYPE)));
            //messages.add(c.getString(1));
            c.moveToNext();
        }

        final ActivityTrackingAdapter adapter =  new ActivityTrackingAdapter(this);
        listView.setAdapter(adapter);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ContentValues initialValues = new ContentValues();

                String type = activitySpinner.getSelectedItem().toString();
                int time=0;
                String comments = commentsText.getText().toString();

                if (!timeText.getText().toString().isEmpty()) {
                    time=Integer.parseInt(timeText.getText().toString());
                }

                if (comments.isEmpty()) {
                    comments = "No comment";
                }
                Activites act = new Activites(type, time, comments);
                messages.add(act);
                adapter.notifyDataSetChanged();
                //adapter.notifyDataSetChanged();
                //initialValues.put(dbHelper.KEY_TYPE,activitySpinner.getContext().toString());
                //initialValues.put(dbHelper.KEY_TIME,timeText.getText().toString());
                //initialValues.put(dbHelper.KEY_COMMENTS,commentsText.getText().toString());
                //db.insert(dbHelper.TABLE_NAME,null,initialValues);
                timeText.setText("");
                commentsText.setText("");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Activites act = adapter.getActivity(position);

                Toast.makeText(getBaseContext(), act.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    private class ActivityTrackingAdapter extends ArrayAdapter<String> {
        public ActivityTrackingAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return messages.size();
        }

        public String getItem (int position) {
            return messages.get(position).getType();
        }

        public Activites getActivity (int position) {
            return messages.get(position);
        }

        public View getView (int position, View converView, ViewGroup parent){

            LayoutInflater inflater = ActivityTracking.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.activity_tracking_listview, null);

            TextView message = (TextView)result.findViewById(R.id.textView);
            message.setText( getItem(position)); // get the string at position
            return result;

        }
    }

    private class Activites {
        String type;
        int minutes;
        String comments;
        Date currentTime;

        public Activites(String t, int m, String c){
            type=t;
            minutes=m;
            comments=c;
            currentTime = Calendar.getInstance().getTime();
        }

        public String getType(){
            return  type;
        }

        public int getMinutes(){
            return minutes;
        }

        public String getComments(){
            return comments;
        }

        public Date getTime(){
            return currentTime;
        }

        public String toString(){
            String details= "Activity: " + type
                    + ", spent " + minutes + " minutes."
                    + " \nComments: " + comments
                    + " \nRecord time: " + currentTime;
            return details;
        }
    }
}
