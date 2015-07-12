package com.rhcloud.phpnew_pranavkumar.mygridapp;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.rhcloud.phpnew_pranavkumar.mygridapp.images.ImageCacheManager;
import com.rhcloud.phpnew_pranavkumar.mygridapp.images.RequestManager;

/**
 * Created by my on 7/12/2015.
 */
public class MyApplication extends Application {

    private static MyApplication sInstance;
    private static int DISK_IMAGECACHE_SIZE = 1024*1024*10;
    private static Bitmap.CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
    private static int DISK_IMAGECACHE_QUALITY = 100;  //PNG is lossless so quality is ignored but must be provided

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        init();

    }

    private void init() {
        RequestManager.init(this);
        createImageCache();
    }

    /**
     * Create the image cache. Uses Memory Cache by default. Change to Disk for a Disk based LRU implementation.
     */
    private void createImageCache(){
        ImageCacheManager.getInstance().init(this,
                this.getPackageCodePath()
                , DISK_IMAGECACHE_SIZE
                , DISK_IMAGECACHE_COMPRESS_FORMAT
                , DISK_IMAGECACHE_QUALITY
                , ImageCacheManager.CacheType.MEMORY);
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}
