package com.rhcloud.phpnew_pranavkumar.mygridapp;

/**
 * Created by my on 7/10/2015.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.rhcloud.phpnew_pranavkumar.mygridapp.images.FileCache;
import com.rhcloud.phpnew_pranavkumar.mygridapp.images.Utils;

/**
 * This helper class download images from the Internet and binds those with the
 * provided ImageView.
 *
 * <p>
 * It requires the INTERNET permission, which should be added to your
 * application's manifest file.
 * </p>
 *
 * A local cache of downloaded images is maintained internally to improve
 * performance.
 */
public class ImageDownloader {
Context mcontext;
ImageDownloader(Context context)
{
    this.mcontext=context;

    fileCache=new FileCache(context);
}
    /**
     * Download the specified image from the Internet and binds it to the
     * provided ImageView. The binding is immediate if the image is found in the
     * cache and will be done asynchronously otherwise. A null bitmap will be
     * associated to the ImageView if an error occurs.
     *
     * @param url
     *            The URL of the image to download.
     * @param imageView
     *            The ImageView to bind the downloaded image to.
     */
    public void download(String url, ImageView imageView) {
        resetPurgeTimer();
        Bitmap bitmap = getBitmapFromCache(url);
        //forceDownload(url, imageView);
        if (bitmap == null) {
            forceDownload(url, imageView);
        } else {
            cancelPotentialDownload(url, imageView);
            imageView.setImageBitmap(bitmap);
        }
    }

    private static boolean cancelPotentialDownload(String url, ImageView imageView) {
        BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.url;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                bitmapDownloaderTask.cancel(true);
            } else {
                // The same URL is already being downloaded.
                return false;
            }
        }
        return true;
    }
    /**
     * Same as download but the image is always downloaded and the cache is not
     * used. Kept private at the moment as its interest is not clear.
     */
    private void forceDownload(String url, ImageView imageView) {
        // State sanity: url is guaranteed to never be null in
        // DownloadedDrawable and cache keys.
        if (url == null) {
            imageView.setImageDrawable(null);
            return;
        }

        BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
        DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
        imageView.setImageDrawable(downloadedDrawable);
        //imageView.setMinimumHeight(156);
        task.execute(url);
    }

    /**
     * @param imageView
     *            Any imageView
     * @return Retrieve the currently active download task (if any) associated
     *         with this imageView. null if there is no such task.
     */
    private static BitmapDownloaderTask getBitmapDownloaderTask(
            ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

    /**
     * The actual AsyncTask that will asynchronously download the image.
     */
    class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private String url;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * Actual download method.
         */
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
            if (isCancelled()) {
                bitmap = null;
            }
            addBitmapToCache(url, bitmap);

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
                // Change bitmap only if this process is still associated with
                // it
                // Or if we don't use any bitmap to task association
                // (NO_DOWNLOADED_DRAWABLE mode)
                if ((this == bitmapDownloaderTask)) {
                    imageView.setImageBitmap(bitmap);
                }
            }


        }
    }

    /**
     * A fake Drawable that will be attached to the imageView while the download
     * is in progress.
     *
     * <p>
     * Contains a reference to the actual download task, so that a download task
     * can be stopped if a new binding is required, and makes sure that only the
     * last started download process can bind its result, independently of the
     * download finish order.
     * </p>
     */
    static class DownloadedDrawable extends ColorDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

        public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
            super(Color.BLACK);
            bitmapDownloaderTaskReference = new WeakReference<BitmapDownloaderTask>(
                    bitmapDownloaderTask);
        }

        public BitmapDownloaderTask getBitmapDownloaderTask() {
            return bitmapDownloaderTaskReference.get();
        }
    }

	/*
	 * Cache-related fields and methods.
	 *
	 * We use a hard and a soft cache. A soft reference cache is too
	 * aggressively cleared by the Garbage Collector.
	 */


    private static final int HARD_CACHE_CAPACITY = 10;
    private static final int DELAY_BEFORE_PURGE = 10 * 1000; // in milliseconds
    FileCache fileCache;
    // Hard cache, with a fixed maximum capacity and a life duration
    private final HashMap<String, Bitmap> sHardBitmapCache =
            new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(LinkedHashMap.Entry<String, Bitmap> eldest) {
                    if (size() > HARD_CACHE_CAPACITY) {
                        // Entries push-out of hard reference cache are transferred to soft reference cache
                        sSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
                        return true;
                    } else
                        return false;
                }
            };

    // Soft cache for bitmaps kicked out of hard cache
    private final static ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache =
            new ConcurrentHashMap<String, SoftReference<Bitmap>>(HARD_CACHE_CAPACITY / 2);

    private final Handler purgeHandler = new Handler();

    private final Runnable purger = new Runnable() {
        public void run() {
            clearCache();
        }
    };

    /**
     * Adds this bitmap to the cache.
     * @param bitmap The newly downloaded bitmap.
     */
    private void addBitmapToCache(String url, Bitmap bitmap) {
        if (bitmap != null) {
//            synchronized (sHardBitmapCache) {
//                sHardBitmapCache.put(url, bitmap);
//
//            }
            Log.d("adding", url);
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            fileCache.addFile(url, bitmap);


        }
    }

    /**
     * @param url The URL of the image that will be retrieved from the cache.
     * @return The cached bitmap or null if it was not found.
     */
    private Bitmap getBitmapFromCache(String url) {
        // First try the hard reference cache
//        synchronized (sHardBitmapCache) {
//            final Bitmap bitmap = sHardBitmapCache.get(url);
//
//            if (bitmap != null) {
//                // Bitmap found in hard cache
//                // Move element to first position, so that it is removed last
//
//                sHardBitmapCache.remove(url);
//                sHardBitmapCache.put(url, bitmap);
//                return bitmap;
//            }
//        }
        File f = fileCache.getFile(url);

//        Log.d("decoding", url);
       Bitmap b = decodeFile(f);
        //Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/saved_images/Image-1266255309.jpg");
           // BitmapFactory.decodeResource()
            //Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

        if(b!=null)
            return b;


       //Bitmap bitmap = decodeSampledBitmapFromFile(f,100,100);





//        // Then try the soft reference cache
//        SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(url);
//        if (bitmapReference != null) {
//            final Bitmap bitmap = bitmapReference.get();
//            if (bitmap != null) {
//                // Bitmap found in soft cache
//                return bitmap;
//            } else {
//                // Soft reference has been Garbage Collected
//                sSoftBitmapCache.remove(url);
//            }
//        }

       return null;
    }


    /**
     * Clears the image cache used internally to improve performance. Note that for memory
     * efficiency reasons, the cache will automatically be cleared after a certain inactivity delay.
     */
    public void clearCache() {
        sHardBitmapCache.clear();
        sSoftBitmapCache.clear();
        fileCache.clear();
    }

    /**
     * Allow a new delay before the automatic cache clear is done.
     */
    private void resetPurgeTimer() {
        purgeHandler.removeCallbacks(purger);
        purgeHandler.postDelayed(purger, DELAY_BEFORE_PURGE);
    }

    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=100;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true)
            {
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            int w=DeviceUtils.getScreenHeight(mcontext);
            int h=DeviceUtils.getScreenWidth(mcontext);
           // o2.inSampleSize=calculateInSampleSize(o2,width_tmp,height_tmp);
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }


    public static Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);


        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filename, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // BEGIN_INCLUDE (calculate_sample_size)
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            long totalPixels = width * height / inSampleSize;

            // Anything more than 2x the requested pixels we'll sample down further
            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
        return inSampleSize;
        // END_INCLUDE (calculate_sample_size)
    }


}