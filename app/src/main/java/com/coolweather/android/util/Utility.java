package com.coolweather.android.util;

import android.text.TextUtils;

import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;
import com.coolweather.android.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/25/025.
 */

public class Utility  {

    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvince=new JSONArray(response);
                for (int i=0;i<allProvince.length();i++){
                    JSONObject provinceObject=allProvince.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }return false;
    }

   public static boolean handleCityResponse(String response,int probinceId) {
       if(!TextUtils.isEmpty(response)){
           try{
               JSONArray allCity=new JSONArray(response);
               for(int i=0;i<allCity.length();i++){
                   JSONObject cityObject=allCity.getJSONObject(i);
                   City city=new City();
                   city.setCityName(cityObject.getString("name"));
                   city.setCityCode(cityObject.getInt("id"));
                   city.setProvinceId(probinceId);
                   city.save();
               }return true;
           }catch (JSONException e){
               e.printStackTrace();
           }
       }return  false;
   }

    public static boolean handleCountyResponse(String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            try{
                JSONArray allcounties=new JSONArray(response);
                for (int i=1;i<allcounties.length();i++){
                    JSONObject countObject=allcounties.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(countObject.getString("name"));
                    county.setWeatherId(countObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }return false;
    }

    public static Weather HandleWeaherResponse(String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
            String weatherContent=jsonArray.getString(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }return  null;
    }


}
