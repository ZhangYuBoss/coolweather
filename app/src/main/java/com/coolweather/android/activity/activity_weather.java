package com.coolweather.android.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolweather.android.R;
import com.coolweather.android.gson.Forecast;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class activity_weather extends AppCompatActivity {
    private ScrollView weatherLayout;
    private TextView titleCity;

    private TextView titleUpdataTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       if(Build.VERSION.SDK_INT>=21){
           View decorView=getWindow().getDecorView();
           decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
           getWindow().setStatusBarColor(Color.TRANSPARENT);
       }

        setContentView(R.layout.activity_weather);
        weatherLayout =(ScrollView) findViewById(R.id.weather_layout);
        titleCity =(TextView)findViewById(R.id.title_city);
        titleUpdataTime=(TextView)findViewById(R.id.tile_updata_tiem);
        degreeText=(TextView)findViewById(R.id.degree_text);
        weatherInfoText=(TextView)findViewById(R.id.weather_info_text);
        forecastLayout=(LinearLayout)findViewById(R.id.forecast_layout);
        aqiText=(TextView)findViewById(R.id.api_text);
        pm25Text=(TextView)findViewById(R.id.pm25_text);
        comfortText=(TextView)findViewById(R.id.comfort_text);
        carWashText=(TextView)findViewById(R.id.car_wash_text);
        sportText=(TextView)findViewById(R.id.sport_text);
        bingImage=(ImageView)findViewById(R.id.bing_pic_img) ;

        SharedPreferences preft= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=preft.getString("weather",null);
        if(weatherString!=null){
            Weather weather= Utility.HandleWeaherResponse(weatherString);
            showWeatherInfo(weather);
        }else{
            String weatherId=getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
           requestWeather(weatherId);
        }

        String bingePic=preft.getString("Image_pig",null);
        if(bingePic!=null){
            Glide.with(activity_weather.this).load(bingePic).into(bingImage);
        }else{
            loadBingPic();
        }
    }

    public void requestWeather(final String weatherId){


        String weatherUrl="http://guolin.tech/api/weather?cityid="+weatherId
                +"&key=d819a45af0b745dea9fe717b47ebacc9";
        HttpUtil.setOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Weather weather=Utility.HandleWeaherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor=PreferenceManager
                                    .getDefaultSharedPreferences(activity_weather.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(activity_weather.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                loadBingPic();
            }

            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity_weather.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showWeatherInfo(Weather weather){
        String cityName=weather.basic.cityNanme;
        String updataTime=weather.basic.update.updataTime.split(" ")[1];
        String degree=weather.now.temperatuer+"℃";
        String weatherInfo=weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdataTime.setText(updataTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for(Forecast forecast:weather.forecastlist){
            View view= LayoutInflater.from(activity_weather.this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText=(TextView)view.findViewById(R.id.data_text);
            TextView infoText=(TextView)view.findViewById(R.id.info_text);
            TextView maxText=(TextView)view.findViewById(R.id.max_text);
            TextView minText=(TextView)view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if(weather.aqi!=null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort="舒适度："+weather.suggestion.comfort.info;
        String CarWash="洗车指数:"+weather.suggestion.carWash.info;
        String sport="运动建议:"+weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(CarWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }

    private void  loadBingPic(){
        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.setOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity_weather.this,"获取图片失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String bingpic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager
                        .getDefaultSharedPreferences(activity_weather.this).edit();
                editor.putString("bing_pic",bingpic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(activity_weather.this).load(bingpic).into(bingImage);
                    }
                });
            }
        });
    }

}
