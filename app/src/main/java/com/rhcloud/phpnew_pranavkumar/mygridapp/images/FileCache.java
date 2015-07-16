package com.rhcloud.phpnew_pranavkumar.mygridapp.images;

/**
 * Created by my on 7/13/2015.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.toolbox.*;
import com.android.volley.toolbox.ImageLoader;
import com.rhcloud.phpnew_pranavkumar.mygridapp.DeviceUtils;

import rapid.decoder.BitmapDecoder;

public class FileCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    private File cacheDir;

    public FileCache(Context context,int max){
        super(max);
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"TTImages_cache");
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return super.sizeOf(key, value);
    }

    @Override
    public Bitmap getBitmap(String url) {
        File root = Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath()+"/saved_images");
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename=String.valueOf(url.hashCode());
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        String fname = "Image-"+ filename +".jpg";
        File f = new File(file, fname);
        Log.d("image", url);
        Log.d("imageDownloader", fname);

        Bitmap bitmap = BitmapDecoder.from(f.getAbsolutePath()).decode();

        return bitmap;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {

        String filename=String.valueOf(url.hashCode());
        //String a="aaaaa";
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);

        File root = Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath()+"/saved_images");
        // File myDir=new File("/sdcard/saved_images");
        file.mkdirs();
        File f = new File(cacheDir, filename);

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);


        String fname = "Image-"+ filename +".jpg";
        File file1= new File(file, fname);
        if (file1.exists ()) file1.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file1);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.d("image", fname);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public Bitmap get(String url) {
//
//        File root = Environment.getExternalStorageDirectory();
//        File file = new File(root.getAbsolutePath()+"/saved_images");
//        //I identify images by hashcode. Not a perfect solution, good for the demo.
//        String filename=String.valueOf(url.hashCode());
//        //Another possible solution (thanks to grantland)
//        //String filename = URLEncoder.encode(url);
//        String fname = "Image-"+ filename +".jpg";
//        File f = new File(file, fname);
//        Log.d("got", fname);
//
//        Bitmap bitmap = decodeFile(f);
//
//        return bitmap;
//        //return null;
//    }
//
//
//    public void put(String url, Bitmap bitmap)
//    {
//        String filename=String.valueOf(url.hashCode());
//        //String a="aaaaa";
//        //Another possible solution (thanks to grantland)
//        //String filename = URLEncoder.encode(url);
//
//        File root = Environment.getExternalStorageDirectory();
//        File file = new File(root.getAbsolutePath()+"/saved_images");
//        // File myDir=new File("/sdcard/saved_images");
//        file.mkdirs();
//        File f = new File(cacheDir, filename);
//
//        Random generator = new Random();
//        int n = 10000;
//        n = generator.nextInt(n);
//        String fname = "Image-"+ filename +".jpg";
//        File file1= new File(file, fname);
//        if (file1.exists ()) file1.delete ();
//        try {
//            FileOutputStream out = new FileOutputStream(file1);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//            out.flush();
//            out.close();
//            Log.d("added", fname);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public File getFile(String url){
        File root = Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath()+"/saved_images");
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename=String.valueOf(url.hashCode());
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        String fname = "Image-"+ filename +".jpg";
        File f = new File(file, fname);
        Log.d("got", fname);
        return f;


    }


    public File addFile(String url,Bitmap bitmap){
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename=String.valueOf(url.hashCode());
        //String a="aaaaa";
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);

        File root = Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath()+"/saved_images");
        // File myDir=new File("/sdcard/saved_images");
        file.mkdirs();
        File f = new File(cacheDir, filename);

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ filename +".jpg";
        File file1= new File(file, fname);
        if (file1.exists ()) file1.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file1);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.d("added", fname);

        } catch (Exception e) {
            e.printStackTrace();
        }

//        if (f.exists ()) f.delete ();
//        try {
//            FileOutputStream out = new FileOutputStream(f);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//            out.flush();
//            out.close();
//            Log.d("added", filename);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return null;

    }

    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
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
            //int width_tmp=150, height_tmp=150;
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
           // int w= DeviceUtils.getScreenHeight(mcontext);
            //int h=DeviceUtils.getScreenWidth(mcontext);
            // o2.inSampleSize=calculateInSampleSize(o2,width_tmp,height_tmp);
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }



}
