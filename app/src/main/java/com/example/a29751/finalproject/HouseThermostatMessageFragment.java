package com.example.a29751.finalproject;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HouseThermostatMessageFragment extends Fragment {
    HouseThermostat houseThermostat;
    EditText myText1,myText2, myText3, myText4;
    Button btDelete, btSave, btUpdate;
    String messageId, weekS, timeS, tempS;

    public HouseThermostatMessageFragment() {
        // Required empty public constructor
    }

    public static HouseThermostatMessageFragment newInstance(HouseThermostat houseThermostat) {
        HouseThermostatMessageFragment fragment = new HouseThermostatMessageFragment();
        Bundle args = new Bundle();
        fragment.houseThermostat = houseThermostat;//to judge houseThermostat is null or not
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Bundle bundle = this.getArguments();
        messageId = bundle.getString("messageId");
 //       String myValue   = bundle.getString("message");
        weekS = bundle.getString("mweek");
        timeS = bundle.getString("mtime");
        tempS = bundle.getString("mtemp");

        //       mId= bundle.getLong("mId");
        View myView = inflater.inflate(R.layout.fragment_house_thermostat_message, container, false);
//        return inflater.inflate(R.layout.fragment_house_thermostat_message, container, false);


        myText1 = (EditText) myView.findViewById(R.id.textView_IDHT);


        myText2 = (EditText) myView.findViewById(R.id.textView_WeekHT);

        myText3 = (EditText) myView.findViewById(R.id.textView_TiemHT);

        myText4 = (EditText) myView.findViewById(R.id.textView_TempHT);


        btDelete   = (Button)myView.findViewById(R.id.deleteButton_HT);
        btDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(houseThermostat==null) {//on phone
                    // Code here executes on main thread after user presses button
                    Intent intent = new Intent(getActivity(), HouseThermostat.class);
                    //Intent intent = new Intent();
                    //getActivity().setResult(Activity.RESULT_OK, data);


                    intent.putExtra("btnType",1);

                    getActivity().setResult(Integer.parseInt(messageId), intent);//void setResult (int resultCode, Intent data)
                    //  Call this to set the result that your activity will return to its caller.
                    getActivity().finish();
                    //startActivityForResult(intent, Integer.parseInt(messageId));
                }
                else//on tablet
                {
                    houseThermostat.deleteTabletMsg(Integer.parseInt(messageId));
                }
            }
        });

        btSave   = (Button)myView.findViewById(R.id.saveButton_HT);
        btSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                weekS = myText2.getText().toString();
                timeS = myText3.getText().toString();
                tempS = myText4.getText().toString();
                if(houseThermostat==null) {//on phone
                    // Code here executes on main thread after user presses button
                    Intent intent = new Intent(getActivity(), HouseThermostat.class);

                    intent.putExtra("btnType",2);
                    intent.putExtra("weekS",weekS);
                    intent.putExtra("timeS",timeS);
                    intent.putExtra("tempS",tempS);
                    //Intent intent = new Intent();
                    //getActivity().setResult(Activity.RESULT_OK, data);
                    getActivity().setResult(Integer.parseInt(messageId), intent);//void setResult (int resultCode, Intent data)
                    //  Call this to set the result that your activity will return to its caller.
                    getActivity().finish();
                    //startActivityForResult(intent, Integer.parseInt(messageId));
                }
                else//on tablet
                {

                    houseThermostat.saveNewTabletMsg(Integer.parseInt(messageId), weekS, timeS, tempS);
                }
            }
        });

        btUpdate = (Button)myView.findViewById(R.id.editButton_HT);
        btUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                weekS = myText2.getText().toString();
                timeS = myText3.getText().toString();
                tempS = myText4.getText().toString();
                if(houseThermostat==null) {//on phone
                    // Code here executes on main thread after user presses button
                    Intent intent = new Intent(getActivity(), HouseThermostat.class);

                    intent.putExtra("btnType",3);
                    intent.putExtra("weekS",weekS);
                    intent.putExtra("timeS",timeS);
                    intent.putExtra("tempS",tempS);
                    //Intent intent = new Intent();
                    //getActivity().setResult(Activity.RESULT_OK, data);
                    getActivity().setResult(Integer.parseInt(messageId), intent);//void setResult (int resultCode, Intent data)
                    //  Call this to set the result that your activity will return to its caller.
                    getActivity().finish();
                    //startActivityForResult(intent, Integer.parseInt(messageId));
                }
                else//on tablet
                {

                    houseThermostat.updateTabletMsg(Integer.parseInt(messageId), weekS, timeS, tempS);
                }
            }
        });


        myText1.setText("");
        myText1.setText(messageId);

        myText2.setText("");
        myText2.setText(weekS);

        myText3.setText("");
        myText3.setText(timeS);

        myText4.setText("");
        myText4.setText(tempS);


        // return inflater.inflate(R.layout.activity_message_details, container, false);
        return myView;
        //      return inflater.inflate(R.layout.fragment_message, container, false);
    }

}
