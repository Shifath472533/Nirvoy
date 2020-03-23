package com.example.nirvoy;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetNearbyPlacesNumber extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap googleMap;
    String url;


    @Override
    protected String doInBackground(Object... objects) {

        googleMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();

        try {
            googlePlacesData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {

        Log.d("nearbyplacesphone", "onPostExecute: " + s);
    }

}
