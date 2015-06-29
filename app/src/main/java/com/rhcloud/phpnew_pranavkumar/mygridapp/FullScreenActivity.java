package com.rhcloud.phpnew_pranavkumar.mygridapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.loopj.android.image.SmartImage;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by my on 6/28/2015.
 */
public class FullScreenActivity extends ActionBarActivity{

    SmartImageView myImage;


    ImageSwitcher Switch;
    float initialX;
    int position;

    String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip);

        Intent i = getIntent();

        flag = i.getStringExtra("flag");
        String p=i.getStringExtra("pos");



        //Toast.makeText(getApplicationContext(),p,Toast.LENGTH_SHORT).show();

        position=Integer.parseInt(p);

        myImage = (SmartImageView)findViewById(R.id.img_thumbnailfull);
        myImage.setImageUrl(flag);

        Switch=(ImageSwitcher)findViewById(R.id.viewFlipper);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float finalX = event.getX();
                if (initialX > finalX)
                {

                   // String next=feedItemList.get(position);
                   // myImage.setImageUrl(flag, position++);
                   // myImage.setImageUrl(feedItemList.get(position++).getThumbnail());
                   // myImage.setImageUrl("http://www.images.behindwoods.com/photo-galleries-q1-09/tamil-photo-gallery/charmi-02/charmi-01.jpg");
                    Switch.showNext();
                    Toast.makeText(getApplicationContext(), "Next Image",
                            Toast.LENGTH_LONG).show();
                    position++;
                }
                else
                {
                    if(position > 0)
                    {
                       // myImage.setImageUrl(flag, position - 1);
                       // myImage.setImageUrl(feedItemList.get(position).getThumbnail());
                        Toast.makeText(getApplicationContext(), "previous Image",
                                Toast.LENGTH_LONG).show();
                        Switch.showPrevious();
                        position= position-1;
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No More Images To Swipe",
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        return false;
    }


}
