package com.rhcloud.phpnew_pranavkumar.mygridapp;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.image.SmartImage;
import com.loopj.android.image.SmartImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by my on 7/11/2015.
 */
public class NewFullScreanActivity extends ActionBarActivity
{
    private ImageView myImage;
    NewImageDownloader newImageDownloader;
    private final ImageDownloader imageDownloader = new ImageDownloader();
    String flag;
    Bitmap b;
    DownloadUtil downloadUtil= new DownloadUtil();
    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreanactivity);
        myImage = (ImageView) findViewById(R.id.img_thumbnailfull);
        spinner=(ProgressBar)findViewById(R.id.progressBar2);
        Intent i = getIntent();

        flag = i.getStringExtra("flag");

        imageDownloader.download(flag, myImage);

        spinner.setVisibility(View.GONE);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_main, menu);

        menu.add("setaswall").setOnMenuItemClickListener(this.SetWallpaperClickListener).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);

    }


    // Capture actionbar menu item click
    MenuItem.OnMenuItemClickListener SetWallpaperClickListener = new MenuItem.OnMenuItemClickListener() {

        public boolean onMenuItemClick(MenuItem item) {




            try {

                new MybitmapDownloaderTask().execute(flag);



            } catch (Exception e) {
                // TODO Auto-generated catch block
            }

            return false;
            //
        }
    };


    class MybitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            return DownloadUtil.downloadBitmap(url);
        }

        /**
         * Once the image is downloaded, associates it to the imageView
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            // get the height and width of screen
            int height = metrics.heightPixels;
            int width = metrics.widthPixels;
            // Retrieve a WallpaperManager
            WallpaperManager myWallpaperManager = WallpaperManager.getInstance(NewFullScreanActivity.this);
            try {
                myWallpaperManager.setBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            myWallpaperManager.suggestDesiredDimensions(height, width);



            spinner.setVisibility(View.GONE);
            // Show a toast message on successful change
            Toast.makeText(NewFullScreanActivity.this, "Wallpaper successfully changed", Toast.LENGTH_SHORT).show();
        }
    }
}
