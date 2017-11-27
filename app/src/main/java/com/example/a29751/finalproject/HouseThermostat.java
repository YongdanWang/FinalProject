package com.example.a29751.finalproject;
/**
 * Date: 2017-10-31
 * Finished by Cheng Yan
 * This is about House Thermostat
 */
import android.content.ContentValues;
import android.content.Context;
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
import android.widget.TextView;

import java.util.ArrayList;

public class HouseThermostat extends AppCompatActivity {

    private ListView listView;
    private EditText eTextWeek, eTextTime, eTextTemp;
    private Button buttonWeek, buttonTime, buttonTemp;
    ChatAdapter messageAdapter;
    ArrayList<String[]> chatMessageArr = new ArrayList<>();
    ArrayList<String> chatMessage = new ArrayList<>();
    protected static final String ACTIVITY_NAME = "HouseThermostat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_thermostat);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        listView = (ListView)findViewById(R.id.listView_HouseThermostat);
        eTextWeek   = (EditText) findViewById(R.id.edittext_Week_HouseThermostat);
        eTextTime   = (EditText) findViewById(R.id.edittext_Time_HouseThermostat);
        eTextTemp   = (EditText) findViewById(R.id.edittext_Temp_HouseThermostat);
        //listView.setAdapter();
        messageAdapter =new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        HouseThermostatDatabaseHelper chatDbHelper = new HouseThermostatDatabaseHelper(this);
        //      chatDbHelper = new ChatDatabaseHelper(this);
        final SQLiteDatabase db = chatDbHelper.getWritableDatabase();

        //   Cursor cursor = db.query("HouseThermostatInfo", null, null, null, null, null, null);
        Cursor cursor = db.rawQuery("select * from HouseThermostatInfo", null);

        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(HouseThermostatDatabaseHelper.WEEK_MESSAGE)));

                String newStringWeek1 = cursor.getString(cursor.getColumnIndex(HouseThermostatDatabaseHelper.WEEK_MESSAGE));
                String newStringTime1 = cursor.getString(cursor.getColumnIndex(HouseThermostatDatabaseHelper.TIME_MESSAGE));
                String newStringTemp1 = cursor.getString(cursor.getColumnIndex(HouseThermostatDatabaseHelper.TEMP_MESSAGE));
                String [] newStringArr1 = new String []{newStringWeek1, newStringTime1, " Temp -> ", newStringTemp1};

                chatMessageArr.add(newStringArr1);
                chatMessage.add(newStringWeek1+newStringTime1+" Temp -> "+newStringTemp1);
                //           chatMessage.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }

        Log.i(ACTIVITY_NAME, "Cursor's  column count =" + cursor.getColumnCount() );

        for(int i = 0; i < cursor.getColumnCount(); i++){
            System.out.println(cursor.getColumnName(i));
        }

        buttonWeek = (Button)findViewById(R.id.addButton_HouseThermostat);
        buttonWeek.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newStringWeek = eTextWeek.getText().toString();
                String newStringTime = eTextTime.getText().toString();
                String newStringTemp = eTextTemp.getText().toString();
                String [] newStringArr = new String []{newStringWeek, newStringTime, " Temp -> ", newStringTemp};
                chatMessageArr.add(newStringArr);
                chatMessage.add(newStringWeek+newStringTime+" Temp -> "+newStringTemp);
                //       chatMessage.asList("everton", "liverpool", "swansea", "chelsea");
                messageAdapter.notifyDataSetChanged();
                eTextWeek.setText("");
                eTextTime.setText("");
                eTextTemp.setText("");

               ContentValues cValues = new ContentValues();
                cValues.put("week", newStringWeek);
                cValues.put("time", newStringTime);
                cValues.put("temp", newStringTemp);
                db.insert("HouseThermostatInfo", null, cValues);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
     //           int selected_row = position;
   //             String[] selectedItem = (String[]) parent.getItemAtPosition(selected_row);
                eTextWeek.setText(messageAdapter.getItem(position));
                eTextWeek.setText(messageAdapter.getItemArr(position)[0]);
                eTextTime.setText(messageAdapter.getItemArr(position)[1]);
                eTextTemp.setText(messageAdapter.getItemArr(position)[3]);
           }
        });
    }


    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx){
            super(ctx, 0);
        }

        public int getCount(){
            return chatMessage.size();
        }

        public  String getItem(int position){
            return chatMessage.get(position);
        }

        public  String[] getItemArr(int position){
            return chatMessageArr.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = HouseThermostat.this.getLayoutInflater();
            View result = null ;
                result = inflater.inflate(R.layout.housethermostat_row, null);
            TextView houseThermostatMessage = (TextView)result.findViewById(R.id.textview_HouseThermostat);
            houseThermostatMessage.setText(getItem(position)); // get the string at position
            return result;
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}
