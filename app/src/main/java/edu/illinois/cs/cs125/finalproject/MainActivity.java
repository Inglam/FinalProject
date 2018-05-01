package edu.illinois.cs.cs125.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Intent;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;


import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.content.Context;


public class MainActivity extends AppCompatActivity {
    /**
     * Default logging tag for messages from the main activity.
     */

    public static String year = "";
    public static String month = "";
    public static String date = "";
    private String EVENT_DATE_TIME = "";
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private LinearLayout linear_layout_1, linear_layout_2;
    private TextView tv_days, tv_hour, tv_minute, tv_second;
    private Handler handler = new Handler();
    private Runnable runnable;
    private static final String TAG = "Final Project";


    EditText textIn;
    Button buttonAdd;
    LinearLayout container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FileInputStream fis = null;
        byte[] buffer = new byte[3];
        try {
            fis=openFileInput("login");
            buffer=new byte[fis.available()];
            fis.read(buffer);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(fis!=null){
                try {
                    fis.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        TextView y = findViewById(R.id.year);
        TextView m = findViewById(R.id.month);
        TextView d = findViewById(R.id.date);

        String data=new String(buffer);
        year = data.split(" ")[0];
        month = data.split(" ")[1];
        date = data.split(" ")[2];

        initUI();
        countDownStart();

        final ImageButton openSideBar = findViewById(R.id.SideBar);
        openSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Open Side Bar");
            }
        });


        final ImageButton openSettings = findViewById(R.id.Settings);
        openSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (final View v){
                Log.d(TAG, "Open Settings");
                setnewgoal(findViewById(R.id.Settings));
            }
        });

        textIn = (EditText)findViewById(R.id.textin);
        buttonAdd = (Button)findViewById(R.id.add);
        container = (LinearLayout)findViewById(R.id.container);

        buttonAdd.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.row, null);
                TextView textOut = (TextView)addView.findViewById(R.id.textout);
                textOut.setText(textIn.getText().toString());
                Button buttonRemove = (Button)addView.findViewById(R.id.remove);
                buttonRemove.setOnClickListener(new OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        ((LinearLayout)addView.getParent()).removeView(addView);
                    }
                });

                container.addView(addView);
            }
        });
    }



    public void setnewgoal(View view) {
        Intent intent = new Intent(this, SetUpYourGoal.class);
        startActivity(intent);
    }

    public void inputdate() {

    }


    private void initUI() {
        linear_layout_1 = findViewById(R.id.linear_layout_1);
        linear_layout_2 = findViewById(R.id.linear_layout_2);
        tv_days = findViewById(R.id.tv_days);
        tv_hour = findViewById(R.id.tv_hour);
        tv_minute = findViewById(R.id.tv_minute);
        tv_second = findViewById(R.id.tv_second);
    }

    private void countDownStart() {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    EVENT_DATE_TIME = year + "-" + month + "-" + date + " 00:00:00";
                    handler.postDelayed(this, 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(EVENT_DATE_TIME); //用户输入
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;
                        tv_days.setText(String.format("%02d", Days));
                        tv_hour.setText(String.format("%02d", Hours));
                        tv_minute.setText(String.format("%02d", Minutes));
                        tv_second.setText(String.format("%02d", Seconds));
                    } else {
                        linear_layout_1.setVisibility(View.VISIBLE);
                        linear_layout_2.setVisibility(View.GONE);
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

}
