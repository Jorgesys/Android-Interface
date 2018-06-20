package com.jorgesys.aninterface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


/*Implements "Communique" Interface*/
public class MainActivity extends AppCompatActivity implements Communique{

    private static TextView tvInstructions;
    //Declare your json file with navigation instructions!
    private String urlJson = "http://www.json-generator.com/api/json/get/ceyWihxzOq?indent=2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        (findViewById(R.id.btnDownload)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JsonTask(MainActivity.this).execute(urlJson);
            }
        });


    }

    /*Override methods of Interface */
    @Override
    public void respond(String information) {
        tvInstructions = findViewById(R.id.textView);
        tvInstructions.setText(information);
    }

    @Override
    public void otherMethod(String information) {

    }
}
