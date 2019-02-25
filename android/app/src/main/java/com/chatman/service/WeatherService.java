package com.chatman.service;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import com.chatman.helper.BotMessageHelper;

import org.json.JSONObject;

import java.util.Calendar;

public class WeatherService extends AsyncTask<Location, Void, String> {
    private Context mContext;

    public WeatherService(Context context) {
        this.mContext = context;
    }

    @Override
    protected String doInBackground(Location... locations) {
        return WeatherNetworkUtils.getWeatherInfo(locations[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            StringBuilder messageBuilder = new StringBuilder();
            JSONObject jsonObject = new JSONObject(s);
            JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
            JSONObject main_data = jsonObject.getJSONObject("main");
            JSONObject clouds = jsonObject.getJSONObject("clouds");
            JSONObject system = jsonObject.getJSONObject("sys");

            //Calculate time
            Calendar cl = Calendar.getInstance();
            cl.setTimeInMillis(jsonObject.getLong("dt")*1000);
            String date = "" + cl.get(Calendar.DAY_OF_MONTH) + "/" + cl.get(Calendar.MONTH + 1) + "/" + cl.get(Calendar.YEAR);
            String time = "" + cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE) + ":" + cl.get(Calendar.SECOND) + 0;

            messageBuilder.append(weather.getString("description"));
            messageBuilder.setCharAt(0, Character.toTitleCase(messageBuilder.charAt(0)));
            messageBuilder.append(" in ");
            messageBuilder.append(jsonObject.getString("name") + ", ");
            messageBuilder.append(system.getString("country"));
            messageBuilder.append(" with a temperature of ");
            messageBuilder.append(main_data.getString("temp"));
            messageBuilder.append(" C, and a chance of raining at ");
            messageBuilder.append(clouds.getString("all"));
            messageBuilder.append("%\n\n");
            messageBuilder.append("Updated at: ");
            messageBuilder.append(date);
            messageBuilder.append(" ");
            messageBuilder.append(time);

            BotMessageHelper.sendBotMessage(mContext, messageBuilder.toString());

        } catch (Exception e) {
            BotMessageHelper.sendBotMessage(mContext, "Weather data cannot be acquired");
            e.printStackTrace();
        }
    }
}
