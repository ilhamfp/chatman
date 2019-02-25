package com.chatman.service;

import android.location.Location;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WeatherNetworkUtils {
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?";
    private static final String LAT_PARAM = "lat";
    private static final String LONG_PARAM = "lon";
    private static final String APPID = "APPID";
    private static final String UNITS = "units";

    public static String getWeatherInfo(Location location) {
        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJSON = null;

        try {
            Uri buildURI = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(LAT_PARAM, Double.toString(location.getLatitude()))
                    .appendQueryParameter(LONG_PARAM, Double.toString(location.getLongitude()))
                    .appendQueryParameter(UNITS, "metric")
                    .appendQueryParameter(APPID, "2a4ab4c143bb11a626c17f4aef3d5e5a")
                    .build();

            URL requestURL = new URL(buildURI.toString());

            urlConnection = (HttpsURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();

            String line = "";
            while((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() == 0) {
                return null;
            }

            resultJSON = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultJSON;
    }
}
