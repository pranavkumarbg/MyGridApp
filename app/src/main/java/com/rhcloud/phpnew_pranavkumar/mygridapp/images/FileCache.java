package com.rhcloud.phpnew_pranavkumar.mygridapp.images;

/**
 * Created by my on 7/13/2015.
 */
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class FileCache {

    private File cacheDir;

    public FileCache(Context context){
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"TTImages_cache");
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public File getFile(String url){
        File root = Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath()+"/saved_images");
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename=String.valueOf(url.hashCode());
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        String fname = "Image-"+ filename +".jpg";
        File f = new File(file, filename);
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
        File file1= new File(file, filename);
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

}
