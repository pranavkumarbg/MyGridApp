package com.rhcloud.phpnew_pranavkumar.mygridapp;

import android.app.Application;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.toolbox.NetworkImageView;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdDisplayListener;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;
import com.startapp.android.publish.nativead.NativeAdDetails;
import com.startapp.android.publish.nativead.NativeAdPreferences;
import com.startapp.android.publish.nativead.StartAppNativeAd;
import com.startapp.android.publish.splash.SplashConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public class MainActivity extends ActionBarActivity {

    RecyclerView mRecyclerView;
    private NetworkImageView imgThumbnail;
    RecyclerView.LayoutManager mLayoutManager;
    GridAdapter mAdapter;
    MyNewAdapter myNewAdapter;
    StaggeredGridLayoutManager mstaggeredGridLayoutManager;
    ProgressDialog mProgressDialog;
    JSONObject jsonobject;
    JSONArray jsonarray;
    DatabaseHandler db;
    String b;
    int p;
    // MyAdapter myAdapter;
    private ArrayList<FeedItem> feedItemList = new ArrayList<FeedItem>();
    //List<String> list= Arrays.asList();
   // ArrayList<String> feedItemList=new ArrayList<>();
    String st;


    /** StartAppAd object declaration */
    private StartAppAd startAppAd = new StartAppAd(this);

    /** StartApp Native Ad declaration */
    private StartAppNativeAd startAppNativeAd = new StartAppNativeAd(this);
    private NativeAdDetails nativeAd = null;

    private ImageView imgFreeApp = null;
    private TextView txtFreeApp = null;

    /** Native Ad Callback */
    private AdEventListener nativeAdListener = new AdEventListener() {

        @Override
        public void onReceiveAd(Ad ad) {

            // Get the native ad
            ArrayList<NativeAdDetails> nativeAdsList = startAppNativeAd.getNativeAds();
            if (nativeAdsList.size() > 0){
                nativeAd = nativeAdsList.get(0);
            }

            // Verify that an ad was retrieved
            if (nativeAd != null){

                // When ad is received and displayed - we MUST send impression
                nativeAd.sendImpression(MainActivity.this);

                if (imgFreeApp != null && txtFreeApp != null){

                    // Set button as enabled
                    imgFreeApp.setEnabled(true);
                    txtFreeApp.setEnabled(true);

                    // Set ad's image
                    imgFreeApp.setImageBitmap(nativeAd.getImageBitmap());

                    // Set ad's title
                    txtFreeApp.setText(nativeAd.getTitle());
                }
            }
        }

        @Override
        public void onFailedToReceiveAd(Ad ad) {

            // Error occurred while loading the native ad
            if (txtFreeApp != null) {
                txtFreeApp.setText("Error while loading Native Ad");
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StartAppSDK.init(this, "105752746", "206452131", true); //TODO: Replace with your IDs

        /** Create Splash Ad **/
        StartAppAd.showSplash(this, savedInstanceState,
                new SplashConfig()
                        .setTheme(SplashConfig.Theme.GLOOMY)
                        .setLogo(R.drawable.logo)
                        .setAppName("Loading")
        );

        setContentView(R.layout.activity_main);

       // Toast.makeText(this, "oncreate", Toast.LENGTH_LONG).show();


        /** Add Slider **/
        StartAppAd.showSlider(this);

        /**
         * Load Native Ad with the following parameters:
         * 1. Only 1 Ad
         * 2. Download ad image automatically
         * 3. Image size of 150x150px
         */
        startAppNativeAd.loadAd(
                new NativeAdPreferences()
                        .setAdsNumber(1)
                        .setAutoBitmapDownload(true)
                        .setImageSize(NativeAdPreferences.NativeAdBitmapSize.SIZE150X150),
                nativeAdListener);

        new DownloadJSON().execute();
        imgThumbnail= (NetworkImageView )findViewById(R.id.img_thumbnail);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
       // mstaggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);


        //mAdapter = new GridAdapter(MainActivity.this, feedItemList);
       // myNewAdapter=new MyNewAdapter(MainActivity.this, feedItemList);
       // mRecyclerView.setAdapter(myNewAdapter);

        //getSupportLoaderManager().initLoader(5,null,loaderCallbacks);
       // getLoaderManager().initLoader(2, null, loaderCallbacks);
        //getSupportLoaderManager().initLoader(5,null,loaderCallbacks);

    }



  /*  private LoaderManager.LoaderCallbacks<ArrayList<String>> loaderCallbacks= new LoaderManager.LoaderCallbacks<ArrayList<String>>() {
        @Override
        public Loader<ArrayList<String>> onCreateLoader(int id, Bundle args) {
            return new MyLoaders(getApplicationContext());
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<String>> loader, ArrayList<String> data) {

            feedItemList=data;
            myNewAdapter=new MyNewAdapter(getApplicationContext(),data);
            mRecyclerView.setAdapter(myNewAdapter);
            //System.out.println(listString);
            myNewAdapter.SetOnItemClickListener(new MyNewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    nextactivity(position);
                }
            });


        }

        @Override
        public void onLoaderReset(Loader<ArrayList<String>> loader) {
           // feedItemList=Collections.<String>emptyList();
            myNewAdapter=new MyNewAdapter(MainActivity.this,null);
        }
    };*/



   private class DownloadJSON extends AsyncTask<Void, Void, Void> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
//            mProgressDialog = new ProgressDialog(MainActivity.this);
//            // Set progressdialog title
//            mProgressDialog.setTitle("MyGridApp");
//            // Set progressdialog message
//            mProgressDialog.setMessage("Loading...");
//            mProgressDialog.setIndeterminate(false);
//            // Show progressdialog
//            mProgressDialog.show();

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
        protected void onPostExecute(Void args) {



                mAdapter = new GridAdapter(getApplicationContext(), feedItemList);

                // PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(MainActivity.this);
                // layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                //layoutManager.setExtraLayoutSpace(DeviceUtils.getScreenHeight(MainActivity.this));
                // mRecyclerView.setLayoutManager(layoutManager);
                //mRecyclerView.setItemAnimator(new SlideInUpAnimator());

                mRecyclerView.setAdapter(mAdapter);


           // mProgressDialog.dismiss();

            //AdBuddiz.showAd(MainActivity.this);

            mAdapter.SetOnItemClickListener(new GridAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View v, int position) {
                    // do something with position
                    nextactivity(position);
                }
            });

        }
    }

    private void nextactivity(int position) {

        b = feedItemList.get(position).getThumbnail();
       // p = feedItemList.get(position);
        String p1 = Integer.toString(position);



//        for (String s : feedItemList)
//        {
//            String listString = s + "\t";
//            Log.i("my",listString);
//        }



        // Show an Ad
        startAppAd.showAd(new AdDisplayListener() {




            /**
             * Callback when Ad has been hidden
             * @param ad
             */
            @Override
            public void adHidden(Ad ad) {


                Intent intent = new Intent(getApplicationContext(), NewFullScreanActivity.class);

                // Pass all data flag
                intent.putExtra("flag", b);

                //intent.putExtra("pos",p1);
                //intent.putExtra("FILES_TO_SEND", feedItemList);
                // FeedItem f=new FeedItem();
                // ArrayList<String> al=new ArrayList<String>();
//        FullScreenActivity f=new FullScreenActivity();
//        f.mylist(feedItemList);
                // FullScreenActivity f= new FullScreenActivity(MainActivity.this,feedItemList);
                //al.add(f.getThumbnail());
                //intent.putStringArrayListExtra("arr",al);

                // Start SingleItemView Class

                    if(b!= null) {
                        startActivity(intent);
                    }
                else
                    {
                        Toast.makeText(MainActivity.this,"",Toast.LENGTH_LONG).show();
                    }



            }

            /**
             * Callback when ad has been displayed
             * @param ad
             */
            @Override
            public void adDisplayed(Ad ad) {

            }

            /**
             * Callback when ad has been clicked
             * @param
             */
            @Override
            public void adClicked(Ad arg0) {

            }

            @Override
            public void adNotDisplayed(Ad ad) {

            }
        });



    }


    /**
     * Runs when the native ad is clicked (either the image or the title).
     * @param view
     */
    public void freeAppClick(View view){
        if (nativeAd != null){
            nativeAd.sendClick(this);
        }
    }

    /**
     * Part of the activity's life cycle, StartAppAd should be integrated here.
     */
    @Override
    public void onResume() {
        super.onResume();
        startAppAd.onResume();
    }

    /**
     * Part of the activity's life cycle, StartAppAd should be integrated here
     * for the home button exit ad integration.
     */
    @Override
    public void onPause() {
        super.onPause();
        startAppAd.onPause();
        //mProgressDialog.dismiss();
    }

    /**
     * Part of the activity's life cycle, StartAppAd should be integrated here
     * for the back button exit ad integration.
     */
    @Override
    public void onBackPressed() {
        startAppAd.onBackPressed();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindDrawables(findViewById(R.id.relmain));
        System.gc();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
}
