package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/3/12/012.
 */

public class Basic {
    @SerializedName("city")
    public String cityNanme;

    @SerializedName("id")
    public String weatherId;
    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updataTime;
    }

}
