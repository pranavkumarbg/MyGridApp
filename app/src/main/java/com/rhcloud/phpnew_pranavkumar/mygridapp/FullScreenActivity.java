package com.rhcloud.phpnew_pranavkumar.mygridapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.RequestFuture;
import com.loopj.android.image.SmartImage;
import com.loopj.android.image.SmartImageView;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.banner.Banner;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by my on 6/28/2015.
 */
public class FullScreenActivity extends ActionBarActivity implements View.OnTouchListener {

    ProgressDialog mProgressDialog;
    private ImageView myImage;
    private ImageLoader imageLoader;
    ImageLoader.ImageCache imageCache;
    private static final int IO_BUFFER_SIZE = 4 * 1024;

    private static final String TAG = "Touch";
    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 1f, MAX_ZOOM = 1f;

    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    ImageSwitcher Switch;
    float initialX;
    int position, columnIndex;
    private Cursor cursor;
    String flag;
    DisplayMetrics dm;
  //  BitmapWorkTask mBitmapWorkTask;
    public static int deviceHeight;
    public static int deviceWidth;
    /**
     * StartAppAd object declaration
     */
    private StartAppAd startAppAd = new StartAppAd(this);
    public Bitmap bitmaptwo;
    private final ImageDownloader imageDownloader = new ImageDownloader(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreanactivity);
       // displayIntialize();
        /** Add banner programmatically (within Java code, instead of within the layout xml) **/
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.rela);

        // Create new StartApp banner
        Banner startAppBanner = new Banner(this);
        RelativeLayout.LayoutParams bannerParameters =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        bannerParameters.addRule(RelativeLayout.CENTER_HORIZONTAL);
        bannerParameters.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        // Add the banner to the main layout
        mainLayout.addView(startAppBanner, bannerParameters);


        Intent i = getIntent();

        flag = i.getStringExtra("flag");

        myImage = (ImageView) findViewById(R.id.img_thumbnailfull);



       new BackgroundTask().execute(flag);
       // startParsingTask();

       // imageDownloader.download(flag,myImage);

        //imageLoader.get(flag, ImageLoader.getImageListener(myImage, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        //imageLoader.get(url,ImageLoader.getImageListener());
       // myImage.setImageUrl(flag, imageLoader);
        // myImage.setImageUrl(flag);
         myImage.setOnTouchListener(this);
       // mBitmapWorkTask=new BitmapWorkTask();
        //mBitmapWorkTask.execute(flag);

        //Switch=(ImageSwitcher)findViewById(R.id.viewFlipper);
    }

//    private void startParsingTask() {
//        Thread threadA = new Thread() {
//            public void run() {
//
//                try {
//                    new BackgroundTask().execute(flag).get(10, TimeUnit.SECONDS);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (TimeoutException e) {
//                    e.printStackTrace();
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                       try
//                       {
//                           myImage.setImageBitmap(bitmaptwo);
//                           Toast.makeText(FullScreenActivity.this,"set the bitmap",Toast.LENGTH_LONG).show();
//                       }
//                       catch (Exception e)
//                       {
//
//                       }
//                    }
//                });
//            }
//        };
//        threadA.start();
//    }




 /*   static Bitmap downloadBitmap(String...url) {
        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        final HttpGet getRequest = new HttpGet(url[0]);
        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {

                Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url);
                return null;
            }
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    bis.mark(1024 * 1024);
                    final Bitmap bitmap = decodeSampledBitmapFromResource(bis,deviceWidth,deviceHeight);
                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {           // Could provide a more explicit error message for IOException or IllegalStateException
            getRequest.abort();
            //Log.w("ImageDownloader", "Error while retrieving bitmap from " + url, e.toString());
            Log.e("LOG_TAG", "Error while retrieving bitmap from" + url + e.toString());
        } finally {
            if (client != null) {
                client.close();
            }
        }       return null;

    }

    public static Bitmap decodeSampledBitmapFromResource(BufferedInputStream inputStream,int reqWidth, int reqHeight) {       // First decode with
        //inJustDecodeBounds=true
        //to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);       // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);       // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        try {
            inputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return BitmapFactory.decodeStream(inputStream, null, options);


    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {    // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }    return inSampleSize;
    }




    private void displayIntialize(){
        dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        deviceHeight=dm.heightPixels;
        deviceWidth=dm.widthPixels;

    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

       // MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_main, menu);

        menu.add("setaswall").setOnMenuItemClickListener(this.SetWallpaperClickListener).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_settings:
//                //openSearch();
//                return true;
            case R.id.action_settings:
               // openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Capture actionbar menu item click
    MenuItem.OnMenuItemClickListener SetWallpaperClickListener = new MenuItem.OnMenuItemClickListener() {

        public boolean onMenuItemClick(MenuItem item) {


            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            // get the height and width of screen
            int height = metrics.heightPixels;
            int width = metrics.widthPixels;
            // Retrieve a WallpaperManager
            WallpaperManager myWallpaperManager = WallpaperManager
                    .getInstance(FullScreenActivity.this);

            try {

               // Toast.makeText(FullScreenActivity.this, "Wallpaper changing", Toast.LENGTH_SHORT).show();


                //Bitmap bitmap=imageCache.getBitmap(flag);
                // Bitmap bitmap = BitmapFactory.decodeFile(flag);

                // Change the current system wallpaper
                // myWallpaperManager.setResource(R.drawable.wallpaper);
                myWallpaperManager.setBitmap(bitmaptwo);

                myWallpaperManager.suggestDesiredDimensions(height, width);

                // Show a toast message on successful change
                Toast.makeText(FullScreenActivity.this, "Wallpaper successfully changed", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                // TODO Auto-generated catch block
            }

            return false;
            //
        }
    };


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        dumpEvent(event);
        //hi
        // Handle touch events here...

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: // first finger down only
                initialX = event.getX();
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG"); // write to LogCat
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP:


            case MotionEvent.ACTION_POINTER_UP:

                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY()
                            - start.y); /*
                                     * create the transformation in the matrix
                                     * of points
                                     */
                } else if (mode == ZOOM) {
                    // pinch zooming
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 5f) {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist;
                    /*
                     * setting the scaling of the matrix...if scale > 1 means
                     * zoom in...if scale < 1 means zoom out
                     */
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix); // display the transformation on screen

        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }


    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
        Log.d("Touch Event", sb.toString());
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
        mProgressDialog.dismiss();
        startAppAd.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myImage.setImageDrawable(null);
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
        mProgressDialog.dismiss();
       unbindDrawables(findViewById(R.id.rela));
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

    //----------------------------------------------------------------------------------------------------//


    private class BackgroundTask extends AsyncTask<String, Void, Bitmap> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(FullScreenActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("MyGridApp");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        protected Bitmap doInBackground(String... url) {
            //--- download an image ---
           // bitmaptwo = DownloadImage(url[0]);
            Bitmap mBitmap=null;

           // mBitmap=downloadBitmap(url);
            return mBitmap;


            //return bitmaptwo;
        }

        protected void onPostExecute(Bitmap bitmap) {
            // ImageView image = (ImageView) findViewById(R.id.imageView1);
            bitmaptwo=bitmap;
            //image.setImageBitmap(bitmap);
            // bitmaptwo = bitmap;
            //  Toast.makeText(FullScreenActivity.this, "post excute", Toast.LENGTH_LONG).show();
            myImage.setImageBitmap(bitmap);
            mProgressDialog.dismiss();
        }
    }

    private InputStream OpenHttpConnection(String... url) throws IOException {
        InputStream in = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            //HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(new HttpGet(url[0]));
            HttpEntity entity = response.getEntity();
            in = entity.getContent();

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }

        return in;
    }

    private Bitmap DownloadImage(String URL) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e1) {
            Toast.makeText(this, e1.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();

            e1.printStackTrace();
        }
        return bitmap;
    }


    //----------------------------------------------------------------------------------------------------//
}
