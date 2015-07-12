package com.rhcloud.phpnew_pranavkumar.mygridapp;

/**
 * Created by my on 7/10/2015.
 */
import java.io.FilterInputStream;
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
            return BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
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

    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
}