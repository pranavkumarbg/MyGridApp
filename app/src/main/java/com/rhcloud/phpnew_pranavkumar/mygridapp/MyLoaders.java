package com.rhcloud.phpnew_pranavkumar.mygridapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by my on 7/7/2015.
 */
public class MyLoaders extends AsyncTaskLoader<ArrayList<String>> {

    private ArrayList<String> feedItemListcache;
    public MyLoaders(Context context) {
        super(context);
    }


    @Override
    protected void onStartLoading()
    {
       if(feedItemListcache == null) {
            forceLoad();
           Log.d("new data","new data");
        }
        else {
           super.deliverResult(feedItemListcache);
           Log.d("cache data", "cache data");
        }
    }

    @Override
    public ArrayList<String> loadInBackground()
    {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String json = JSONfunctions.getJSONfromURL("http://newjson-pranavkumar.rhcloud.com/GridViewJson");
        ArrayList<String> feedItemList= new ArrayList<>();
        Log.d("new data","new data");
        try {
            // Locate the array name in JSON
            JSONObject reader = new JSONObject(json);
            JSONArray jsonarray = reader.getJSONArray("images");

            for (int i = 0; i < jsonarray.length(); i++) {
                // HashMap<String, String> map = new HashMap<String, String>();
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                // Retrive JSON Objects

               // FeedItem item = new FeedItem();

                //item.setThumbnail(jsonobject.optString("image"));
                feedItemList.add(jsonobject.optString("image"));
               // Toast.makeText(getContext(),jsonobject.getString("image"),Toast.LENGTH_LONG).show();

            }
        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return feedItemList;
    }

    @Override
    public void deliverResult(ArrayList<String> data) {
        feedItemListcache=data;
        super.deliverResult(data);
    }


}
