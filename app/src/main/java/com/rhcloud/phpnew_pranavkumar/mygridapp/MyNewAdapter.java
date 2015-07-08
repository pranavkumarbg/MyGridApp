package com.rhcloud.phpnew_pranavkumar.mygridapp;

/**
 * Created by my on 6/26/2015.
 */
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Edwin on 28/02/2015.
 */
public class MyNewAdapter  extends RecyclerView.Adapter<MyNewAdapter.ViewHolder>{



    private Context mContext;
    ArrayList<String> feedItem= new ArrayList<>();
    OnItemClickListener mItemClickListener;
    private ImageLoader imageLoader;
    NetworkImageView imgThumbnail;

    public MyNewAdapter(Context context,ArrayList<String> feedItemList)
    {

        this.feedItem = feedItemList;
        this.mContext=context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item, viewGroup, false);
        // ViewHolder viewHolder = new ViewHolder(v);
        //return viewHolder;

        final LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        final View sView = mInflater.inflate(R.layout.grid_item, viewGroup, false);
        return new ViewHolder(sView);
    }




    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int pos) {

        try
        {
            //int a=Integer.parseInt(feedItemList.get(pos).getThumbnail());
            //Bitmap bitmap=imageCache.get(a);
            //if(bitmap!=null) {
            // viewHolder.spinner.setVisibility(pos);
            viewHolder.imgThumbnail.setImageUrl(feedItem.get(pos));
            //viewHolder.spinner.setVisibility(View.GONE);

            //}

        }
        catch (Exception e)
        {

        }


    }

    @Override
    public int getItemCount() {

        return feedItem.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        public SmartImageView imgThumbnail;
        private ProgressBar spinner;

        public ViewHolder(View itemView)
        {
            super(itemView);

            imgThumbnail= (SmartImageView)itemView.findViewById(R.id.img_thumbnail);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            mItemClickListener.onItemClick(v,getPosition());

        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}