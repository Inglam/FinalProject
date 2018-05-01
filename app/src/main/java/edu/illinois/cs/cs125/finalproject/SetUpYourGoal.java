package edu.illinois.cs.cs125.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.widget.Button;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Intent;


public class SetUpYourGoal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_your_goal);
        final EditText years =findViewById(R.id.year);
        final EditText months =findViewById(R.id.month);
        final EditText dates =findViewById(R.id.date);

        Button login=findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year = years.getText().toString();
                String month = months.getText().toString();
                String date = dates.getText().toString();
                FileOutputStream fos=null;
                try {
                    fos = openFileOutput("login",MODE_PRIVATE);
                    fos.write((year+" "+month+" "+date).getBytes());
                    fos.flush();
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    if(fos!=null){
                        try {
                            fos.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
                Intent intent = new Intent(SetUpYourGoal.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}