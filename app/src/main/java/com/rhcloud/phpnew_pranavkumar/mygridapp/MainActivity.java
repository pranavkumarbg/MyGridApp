package com.rhcloud.phpnew_pranavkumar.mygridapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    GridAdapter mAdapter;
    ProgressDialog mProgressDialog;
    JSONObject jsonobject;
    JSONArray jsonarray;
    DatabaseHandler db;
    // MyAdapter myAdapter;
    private ArrayList<FeedItem> feedItemList = new ArrayList<FeedItem>();
    String st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //db = new DatabaseHandler(this);
        new DownloadJSON().execute();

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }


    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        ArrayList<String> list1 = new ArrayList<String>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(MainActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("MyGridApp");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create an array

            // Retrieve JSON Objects from the given URL address
            //String json = JSONfunctions.getJSONfromURL("http://env-6425390.jelasticlw.com.br/grid/");

            String json = JSONfunctions.getJSONfromURL("http://newjson-pranavkumar.rhcloud.com/GridViewJson");

            try {
                // Locate the array name in JSON
                JSONObject reader = new JSONObject(json);
                jsonarray = reader.getJSONArray("images");

                for (int i = 0; i < jsonarray.length(); i++) {
                   // HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);
                    // Retrive JSON Objects

                    FeedItem item = new FeedItem();

                    item.setThumbnail(jsonobject.optString("image"));
                    feedItemList.add(item);

                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args)
        {
            mAdapter = new GridAdapter(MainActivity.this,feedItemList);
            mRecyclerView.setAdapter(mAdapter);

            mProgressDialog.dismiss();

            mAdapter.SetOnItemClickListener(new GridAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View v , int position) {
                    // do something with position
                    nextactivity(position);
                }
            });

        }
    }

    private void nextactivity(int position)
    {
        String b=feedItemList.get(position).getThumbnail();

        Intent intent = new Intent(getApplicationContext(), FullScreenActivity.class);

        String p=Integer.toString(position);
        // Pass all data flag
        intent.putExtra("flag", b);
        //intent.putExtra("pos",position);
        intent.putExtra("pos", p);
       // FeedItem f=new FeedItem();
       // ArrayList<String> al=new ArrayList<String>();
//        FullScreenActivity f=new FullScreenActivity();
//        f.mylist(feedItemList);

        //al.add(f.getThumbnail());
        //intent.putStringArrayListExtra("arr",al);

        // Start SingleItemView Class
        startActivity(intent);
    }
}
