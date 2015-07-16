package com.rhcloud.phpnew_pranavkumar.mygridapp;

/**
 * Created by my on 6/26/2015.
 */
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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


import com.android.volley.VolleyError;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.loopj.android.image.SmartImageView;
import com.rhcloud.phpnew_pranavkumar.mygridapp.images.FileCache;
import com.rhcloud.phpnew_pranavkumar.mygridapp.images.ImageCacheManager;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Edwin on 28/02/2015.
 */
public class GridAdapter  extends RecyclerView.Adapter<GridAdapter.ViewHolder>{

    private VolleySingleton volleySingleton;

    private List<FeedItem> feedItemList=new ArrayList<FeedItem>();
    private Context mContext;

    OnItemClickListener mItemClickListener;
    ImageView imageView;
    String flag;
    private ImageLoader imageLoader;
    FileCache fileCache;
    //private com.rhcloud.phpnew_pranavkumar.mygridapp.images.ImageLoader imageLoadernew;
   // ImageDownloader imageDownloader;

    public GridAdapter(Context context,List<FeedItem> feedItemList)
    {

        this.feedItemList = feedItemList;
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
            //volleySingleton=VolleySingleton.getInstance();
            //imageLoader=volleySingleton.getImageLoader();

            //imageLoader=CustomVolleyRequestQueue.getInstance(mContext).getImageLoader();

            String url = feedItemList.get(pos).getThumbnail();
          //  Log.d("image", url);
           // imageLoader.get(url, ImageLoader.getImageListener(viewHolder.imgThumbnail, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

           // imageLoader=MyVolleySingleton.getInstance(mContext).getImageLoader();

//           Bitmap bitmap= ImageCacheManager.getInstance().getBitmap(url);
//
//
//            if(bitmap!=null)
//            {
//                Log.d("image", "not null");
//                viewHolder.imgThumbnail.setImageBitmap(bitmap);
//            }
//            else
//            {
                //Log.d("image", "null");

            imageLoader=ImageCacheManager.getInstance().getImageLoader();

                imageLoader.get(url, ImageLoader.getImageListener(viewHolder.imgThumbnail, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
                viewHolder.imgThumbnail.setImageUrl(url, imageLoader);


          //  }

           // imageDownloader.download(url,viewHolder.imgThumbnail);
           // imgLoader.DisplayImage(url, viewHolder.imgThumbnail);

            //new MybitmapDownloaderTasknew().execute();

//            if(url!=null)
//            {
//               imageLoader.get(url, new ImageLoader.ImageListener() {
//                   @Override
//                   public void onErrorResponse(VolleyError error) {
//
//                   }
//
//                   @Override
//                   public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
//
//                       viewHolder.imgThumbnail.setImageBitmap(response.getBitmap());
//                   }
//               });
//
//
//            }


           // viewHolder.imgThumbnail.setImageBitmap(imageDownloader.download(url,viewHolder.imgThumbnail));

        }
        catch (Exception e)
        {

        }


    }

    @Override
    public int getItemCount() {

        return feedItemList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        private NetworkImageView imgThumbnail;
        private ProgressBar spinner;

        public ViewHolder(View itemView)
        {
            super(itemView);
            spinner = (ProgressBar)itemView.findViewById(R.id.progressBar);

            imgThumbnail= (NetworkImageView )itemView.findViewById(R.id.img_thumbnail);

                itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

                mItemClickListener.onItemClick(v, getPosition());


        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }




}