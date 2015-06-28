package com.rhcloud.phpnew_pranavkumar.mygridapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    RecyclerView.Adapter mAdapter;
    ProgressDialog mProgressDialog;
    JSONObject jsonobject;
    JSONArray jsonarray;
    DatabaseHandler db;
    // MyAdapter myAdapter;
    String st;
    ArrayList<HashMap<String, String>> arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        // Calling the RecyclerView
       /* mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GridAdapter();
        mRecyclerView.setAdapter(mAdapter);*/


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
            arraylist = new ArrayList<HashMap<String, String>>();
            // Retrieve JSON Objects from the given URL address
            //String json = JSONfunctions.getJSONfromURL("http://env-6425390.jelasticlw.com.br/grid/");

            String json = JSONfunctions.getJSONfromURL("http://newjson-pranavkumar.rhcloud.com/GridViewJson");

            try {
                // Locate the array name in JSON
                JSONObject reader = new JSONObject(json);
                jsonarray = reader.getJSONArray("images");

                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);
                    // Retrive JSON Objects

                    map.put("flag", jsonobject.getString("image"));

                    // Set the JSON Objects into the array
                    arraylist.add(map);
                    //jsonobject = jsonarray.getJSONObject(i);
                    // Retrive JSON Objects

                    //st=jsonobject.getString("image");
                    //list1.add(st);




                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {


            //Toast.makeText(getApplicationContext(),st,Toast.LENGTH_LONG).show();

         /*   for(int i=0; i<list1.size(); i++)
            {

                String my=list1.get(i).toString();
                //Toast.makeText(getApplicationContext(),my,Toast.LENGTH_LONG).show();
                Contact c=new Contact();
                c.set_name(my);
                db.addContact(c);
                Log.d("Insert: ", "Inserting ..");


            }*/







            mAdapter = new GridAdapter(MainActivity.this,arraylist);
            mRecyclerView.setAdapter(mAdapter);

            mProgressDialog.dismiss();

        }
    }
}
