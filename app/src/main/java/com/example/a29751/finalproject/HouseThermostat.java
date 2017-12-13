package com.example.a29751.finalproject;
/**
 * Date: 2017-10-31
 * Finished by Cheng Yan
 * This is about House Thermostat
 */
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class HouseThermostat extends AppCompatActivity {

    private ListView listView;
    private EditText eTextWeek, eTextTime, eTextTemp;
    private Button buttonAdd;
    HouseThermostatDatabaseHelper houseThermostatDatabaseHelper;
    HouseThermostatAdapter messageAdapterHT;
    Cursor cursor;
    ArrayList<String[]> chatMessageArr = new ArrayList<>();
    ArrayList<String> chatMessage = new ArrayList<>();
    protected static final String ACTIVITY_NAME = "HouseThermostat";
    SQLiteDatabase db;
    HouseThermostatMessageFragment houseThermostatMessageFragment;
    Boolean fb1;
    private ProgressBar pBar;

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
        messageAdapterHT =new HouseThermostatAdapter(this);
        listView.setAdapter(messageAdapterHT);
        Log.d("wen","messageId");

        fb1 = findViewById(R.id.frameLayout_houseThermostat) != null;//layout-sw600dp/activity_chat_window.xml

        pBar = (ProgressBar)findViewById(R.id.progress_bar);
        pBar.setVisibility(View.VISIBLE);
        pBar.setProgress(0);


        houseThermostatDatabaseHelper = new HouseThermostatDatabaseHelper(this);
        //      chatDbHelper = new ChatDatabaseHelper(this);
        db = houseThermostatDatabaseHelper.getWritableDatabase();

        //new HTQuery().execute();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long id) {
                String messa=(String)adapter.getItemAtPosition(position);
                //      Log.d("**********", string);
                //      db = chatDbHelper.getWritableDatabase();
                displayProgressBar();
                String weekS = messageAdapterHT.getItemArr(position)[0];
                String timeS = messageAdapterHT.getItemArr(position)[1];
                String tempS = messageAdapterHT.getItemArr(position)[2] +messageAdapterHT.getItemArr(position)[3];

                long mId  = messageAdapterHT.getItemId(position);
                String messageId =String.valueOf( mId);
                Log.d("wen",messageId);
                /*
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                if (size.x > size.y) */
                //Tablet
                if(fb1||getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                {
                    //You can use Bundle or Class to pass message to Fragment
                    Bundle bundle = new Bundle();
                    //String myMessage = "Stackoverflow is cool!";

                    bundle.putString("message", messa );
                    bundle.putLong("mId",mId);
                    bundle.putString("mweek",weekS);
                    bundle.putString("mtime",timeS);
                    bundle.putString("mtemp",tempS);
                    bundle.putString("messageId", messageId );
                    houseThermostatMessageFragment = HouseThermostatMessageFragment.newInstance(HouseThermostat.this);//chatWindow not null, on tablet
                    //messageFragment.myText1.setText(string);
                    //messageFragment.myText2.setText(messageId);
                    houseThermostatMessageFragment.setArguments(bundle);//Supply the construction arguments for this fragment.

                    getFragmentManager().beginTransaction().replace(R.id.frameLayout_houseThermostat, houseThermostatMessageFragment).commit();//in layout-sw600dp//activity_chat_windows.xml
                    //FragmentTransaction ft = getFragmentManager().beginTransaction();
                    //abstract FragmentTransaction	add(int containerViewId, Fragment fragment)   Calls add(int, Fragment, String) with a null tag.
                    //Add a fragment to the activity state.
                } else {//on phone
                    //Please Bundle the Result Here

                    //i.putExtra("myParam", 1);

                    Intent intent = new Intent(HouseThermostat.this, HouseThermostatMessageDetails.class);
                    intent.putExtra("message", messa);
                    intent.putExtra("messageId", messageId);
                    intent.putExtra("mweek", weekS);
                    intent.putExtra("mtime", timeS);
                    intent.putExtra("mtemp", tempS);
                    startActivityForResult(intent, 10);

                }
            }
        });



        //   Cursor cursor = db.query("HouseThermostatInfo", null, null, null, null, null, null);
        cursor = db.rawQuery("select * from HouseThermostatInfo", null);

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


        buttonAdd = (Button)findViewById(R.id.addButton_HouseThermostat);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newStringWeek = eTextWeek.getText().toString();
                String newStringTime = eTextTime.getText().toString();
                String newStringTemp = eTextTemp.getText().toString();
                String [] newStringArr = new String []{newStringWeek, newStringTime, " Temp -> ", newStringTemp};
                chatMessageArr.add(newStringArr);
                chatMessage.add(newStringWeek + newStringTime+" Temp -> "+newStringTemp);
                //       chatMessage.asList("everton", "liverpool", "swansea", "chelsea");
                messageAdapterHT.notifyDataSetChanged();
                eTextWeek.setText("");
                eTextTime.setText("");
                eTextTemp.setText("");

               ContentValues cValues = new ContentValues();
                cValues.put("week", newStringWeek);
                cValues.put("time", newStringTime);
                //cValues.put("temp", "Temp-->" + newStringTemp);
                cValues.put("temp", " " + newStringTemp);
                db.insert("HouseThermostatInfo", null, cValues);

                cursor = db.rawQuery("select * from HouseThermostatInfo", null);
                chatMessage.clear();
                chatMessageArr.clear();
                if(cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        String s = cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.WEEK_MESSAGE))
                                +cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TIME_MESSAGE))
                                +" Temp -> "
                                +cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TEMP_MESSAGE));
                        Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + s);
                        String [] newStringArr1 = new String []{cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.WEEK_MESSAGE)),
                                cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TIME_MESSAGE)), " Temp -> ",
                                cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TEMP_MESSAGE))};
                        chatMessage.add(s);
                        chatMessageArr.add(newStringArr1);
                        //           chatMessage.add(cursor.getString(1));
                        cursor.moveToNext();
                    }
                }

                messageAdapterHT.notifyDataSetChanged();

            }
        });

   /*     listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
     //           int selected_row = position;
   //             String[] selectedItem = (String[]) parent.getItemAtPosition(selected_row);
                eTextWeek.setText(messageAdapterHT.getItem(position));
                eTextWeek.setText(messageAdapterHT.getItemArr(position)[0]);
                eTextTime.setText(messageAdapterHT.getItemArr(position)[1]);
                eTextTemp.setText(messageAdapterHT.getItemArr(position)[3]);
           }
        });*/
    }


    private class HouseThermostatAdapter extends ArrayAdapter<String> {
        public HouseThermostatAdapter(Context ctx){
            super(ctx, 0);
        }

        public int getCount(){
            return chatMessage.size();
        }

        public  String getItem(int position){
            return chatMessage.get(position);
        }

        public long getItemId(int position) {
            //      db = chatDbHelper.getWritableDatabase();
            cursor.moveToPosition(position);
            long dbId = 0;
            if (cursor.getCount() > position) {
                dbId = cursor.getLong(0);
            }
            //     Log.d("ww",String.valueOf(dbId));
            return dbId;
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

    public void deleteMsg(int id) {
        db = houseThermostatDatabaseHelper.getWritableDatabase();
        db.delete("HouseThermostatInfo", "id=?", new String[]{String.valueOf(id)});

        /*
        cursor = db.rawQuery("select * from HouseThermostatInfo", null);
        chatMessage.clear();
        chatMessageArr.clear();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String s = cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.WEEK_MESSAGE))
                        +cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TIME_MESSAGE))
                        +" Temp -> "
                        +cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TEMP_MESSAGE));

                String [] newStringArr1 = new String []{cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.WEEK_MESSAGE)),
                        cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TIME_MESSAGE)), " Temp -> ",
                        cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TEMP_MESSAGE))};


                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + s);
                chatMessage.add(s);
                chatMessageArr.add(newStringArr1);
                //           chatMessage.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }
        */
        //    db.close();
    }

    public void deleteTabletMsg(int id) {
        deleteMsg(id);
        messageAdapterHT.notifyDataSetChanged();

        //           listView.invalidate();//Invalidate the whole view. If the view is visible, onDraw(android.graphics.Canvas) will be called at some point in the future.
//            listView.refreshDrawableState();//all this to force a view to update its drawable state. This will cause drawableStateChanged to be called on this view. Views that are interested in the new state should call getDrawableState.
        getFragmentManager().beginTransaction().remove(houseThermostatMessageFragment).commit();
    }

    public void updateListView(){
        cursor = db.rawQuery("select * from HouseThermostatInfo", null);
        chatMessage.clear();
        chatMessageArr.clear();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String s = cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.WEEK_MESSAGE))
                        +cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TIME_MESSAGE))
                        +" Temp -> "
                        +cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TEMP_MESSAGE));
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + s);
                chatMessage.add(s);
                String [] newStringArr1 = new String []{cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.WEEK_MESSAGE)),
                        cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TIME_MESSAGE)), " Temp -> ",
                        cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TEMP_MESSAGE))};
                chatMessageArr.add(newStringArr1);
                //           chatMessage.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }
        messageAdapterHT.notifyDataSetChanged();
        listView.invalidate();//Invalidate the whole view. If the view is visible, onDraw(android.graphics.Canvas) will be called at some point in the future.
        listView.refreshDrawableState();
    }

    public void saveNewMsg(int id, String weekS, String timeS, String tempS) {
        db = houseThermostatDatabaseHelper.getWritableDatabase();

        ContentValues cValues1 = new ContentValues();
        cValues1.put("week", weekS);
        cValues1.put("time", timeS);
        String[] parts = tempS.split(">");
        tempS  = parts[1];
        cValues1.put("temp", tempS);
        db.insert("HouseThermostatInfo", null, cValues1);


   //     db.insert("HouseThermostatInfo", "id=?", new String[]{String.valueOf(id), weekS, timeS, tempS});
        /*
        cursor = db.rawQuery("select * from HouseThermostatInfo", null);
        chatMessage.clear();
        chatMessageArr.clear();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String s = cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.WEEK_MESSAGE))
                        +cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TIME_MESSAGE))
                        +" Temp -> "
                        +cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TEMP_MESSAGE));
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + s);
                chatMessage.add(s);
                String [] newStringArr1 = new String []{cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.WEEK_MESSAGE)),
                        cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TIME_MESSAGE)), " Temp -> ",
                        cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TEMP_MESSAGE))};
                chatMessageArr.add(newStringArr1);
                //           chatMessage.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }

        */
        //    db.close();
    }

    public void displayProgressBar(){

        pBar.setMax(30);

        final Thread pBarThread = new Thread() {
            @Override
            public void run() {
                try {
                    int progress =1;
                    while(progress<=30) {
                        pBar.setProgress(progress);
                        sleep(1000);
                        ++progress;
                    }
                }
                catch(InterruptedException e) {
                }
            }
        };

        pBarThread.start();


    }

    public void saveNewTabletMsg(int id, String weekS, String timeS, String tempS) {
        saveNewMsg(id, weekS, timeS, tempS);
 //       messageAdapterHT.notifyDataSetChanged();

        //           listView.invalidate();//Invalidate the whole view. If the view is visible, onDraw(android.graphics.Canvas) will be called at some point in the future.
//            listView.refreshDrawableState();//all this to force a view to update its drawable state. This will cause drawableStateChanged to be called on this view. Views that are interested in the new state should call getDrawableState.
  //      getFragmentManager().beginTransaction().add(houseThermostatMessageFragment, null).commit();
    }


    public void updateMsg(int id, String weekS, String timeS, String tempS) {
        db = houseThermostatDatabaseHelper.getWritableDatabase();

        ContentValues cValues1 = new ContentValues();
        cValues1.put("week", weekS);
        cValues1.put("time", timeS);
        String[] parts = tempS.split(">");
        tempS  = parts[1];
        cValues1.put("temp", tempS);
        db.update("HouseThermostatInfo", cValues1, "id=?", new String[]{String.valueOf(id)});

//int update (String table, ContentValues values, String whereClause, String[] whereArgs)
        //     db.insert("HouseThermostatInfo", "id=?", new String[]{String.valueOf(id), weekS, timeS, tempS});

        /*
        cursor = db.rawQuery("select * from HouseThermostatInfo", null);
        chatMessage.clear();
        chatMessageArr.clear();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String s = cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.WEEK_MESSAGE))
                        +cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TIME_MESSAGE))
                        +" Temp -> "
                        +cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TEMP_MESSAGE));
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + s);
                chatMessage.add(s);
                String [] newStringArr1 = new String []{cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.WEEK_MESSAGE)),
                        cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TIME_MESSAGE)), " Temp -> ",
                        cursor.getString(cursor.getColumnIndex(houseThermostatDatabaseHelper.TEMP_MESSAGE))};




                chatMessageArr.add(newStringArr1);

                //           chatMessage.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }
        //    db.close();
        */
    }

    public void updateTabletMsg(int id, String weekS, String timeS, String tempS) {
        updateMsg(id, weekS, timeS, tempS);
//        messageAdapterHT.notifyDataSetChanged();

        //           listView.invalidate();//Invalidate the whole view. If the view is visible, onDraw(android.graphics.Canvas) will be called at some point in the future.
//            listView.refreshDrawableState();//all this to force a view to update its drawable state. This will cause drawableStateChanged to be called on this view. Views that are interested in the new state should call getDrawableState.
        //      getFragmentManager().beginTransaction().add(houseThermostatMessageFragment, null).commit();
    }


    private class HTQuery extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) {
            int i = urls.length;
            String result = null;

            String test = urls[0];
            switch (urls[0]){
                case "1" :   deleteMsg(Integer.valueOf(urls[1]));
                    result = test;
                             //messageAdapterHT.notifyDataSetChanged();
                             break;
                case "2":    //new HTQuery().execute(btnType, weekS, timeS, tempS);
                             saveNewTabletMsg(Integer.valueOf(urls[1]), urls[2],urls[3],urls[4]);
                    result = test;
                             //messageAdapterHT.notifyDataSetChanged();
                             break;
                case "3" :   updateTabletMsg(Integer.valueOf(urls[1]),  urls[2],urls[3],urls[4]);
                    result = test;
                             //messageAdapterHT.notifyDataSetChanged();
                             break;
                default:   break;
            }

            /*
             for(int ii=1;ii<5;ii++) {
                 try {
                     Thread.sleep(1000);
                 }//SystemClock.sleep(1000);
                 catch (InterruptedException e) {
                     e.printStackTrace();
                 }
                 publishProgress(ii * 25);

             }
             */
   /*         cursor = db.rawQuery("select * from HouseThermostatInfo", null);
            int rowCount = cursor.getCount();
            int i =1;
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    try{Thread.sleep(1000);}//SystemClock.sleep(1000);
                    catch (InterruptedException e){e.printStackTrace();}
                    publishProgress(i/rowCount*100);
                    i++;

                    cursor.moveToNext();

                    case 2:  db.update("HouseThermostatInfo", cValues1, "id=?", new String[]{String.valueOf(id)});
                }
            }*/
            //    db.close();


        return result;
        }

        @Override
        protected void onProgressUpdate(Integer... value) {
            pBar.setVisibility(View.VISIBLE);
            //pBar.setProgress(value[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //pBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // pBar.setVisibility(View.VISIBLE);
        }
    }







    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10&& data != null && data.getExtras() != null) {   //Come back from Cell Phone delete

            int a = resultCode;
            int btnType  = data.getIntExtra("btnType",-1);
            String weekS = data.getStringExtra("weekS");
            String timeS = data.getStringExtra("timeS");
            String tempS = data.getStringExtra("tempS");

            String [] tmp = new String[5];
            HTQuery htQuery = new HTQuery();

    //        htQuery.execute(String.valueOf(btnType),String.valueOf(resultCode),weekS, timeS, tempS);
            //if(htQuery.getStatus()==AsyncTask.Status.FINISHED)
             //   updateListView();
            try {
                String aa =htQuery.execute(String.valueOf(btnType),String.valueOf(resultCode),weekS, timeS, tempS).get();
                while(aa==null){}
                updateListView();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            //updateListView();
/*            switch (btnType){
                case 1 :   deleteMsg(a);
                           messageAdapterHT.notifyDataSetChanged();
                           break;
                case 2 :   new HTQuery().execute(2);
                           saveNewTabletMsg(resultCode, weekS, timeS, tempS);

                           break;
                case 3 : new HTQuery().execute(3);
                           updateTabletMsg(resultCode, weekS, timeS, tempS);

                           break;
                default:   break;
            }*/



//                listView.invalidate();
        }
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}
