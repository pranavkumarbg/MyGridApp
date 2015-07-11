package com.rhcloud.phpnew_pranavkumar.mygridapp;

/**
 * Created by my on 7/10/2015.
 */
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.SyncStateContract;
import android.util.Log;

public class DownloadUtil {
    public static Bitmap downloadBitmap(String url) {

        InputStream inputStream = null;
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url)
                    .openConnection();
            inputStream = con.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            Log.w("", "I/O error while retrieving bitmap from "
                    + url, e);
        } catch (IllegalStateException e) {
            Log.w("", "Incorrect URL: " + url);
        } catch (Exception e) {
            Log.w("", "Error while retrieving bitmap from "
                    + url, e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}