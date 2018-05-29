package com.coolweather.android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.coolweather.android.R;
import com.coolweather.android.gson.Weather;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prfs= PreferenceManager.getDefaultSharedPreferences(this);
        if(prfs.getString("weather",null)!=null){
            Intent intent=new Intent(this, activity_weather.class);
            startActivity(intent);
            finish();
        }
    }

}
