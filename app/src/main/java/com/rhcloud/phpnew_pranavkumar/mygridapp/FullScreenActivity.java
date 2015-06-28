package com.rhcloud.phpnew_pranavkumar.mygridapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.loopj.android.image.SmartImageView;

/**
 * Created by my on 6/28/2015.
 */
public class FullScreenActivity extends ActionBarActivity {

    SmartImageView myImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreanactivity);

        Intent i = getIntent();

        String flag = i.getStringExtra("flag");


        //   Toast.makeText(getApplicationContext(), flag, Toast.LENGTH_LONG).show();
        // Locate the ImageView in singleitemview.xml
        //iv = (ImageView) findViewById(R.id.imageViewg);
        myImage = (SmartImageView) this.findViewById(R.id.img_thumbnailfull);
        myImage.setImageUrl(flag);
       // myImage.setOnTouchListener(this);
    }
}
