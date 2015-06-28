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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


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
public class GridAdapter  extends RecyclerView.Adapter<GridAdapter.ViewHolder> implements View.OnClickListener {



    ArrayList<HashMap<String,String>> contacts;
    HashMap<String, String> resultp = new HashMap<String, String>();
    //Map<String, String> imageViews = Collection.synchronizedMap(new WeakHashMap<String, String>());
    List<Contact> contacts2;
    DatabaseHandler db;
    String nature;
    public GridAdapter(Context context,ArrayList<HashMap<String,String>> arrayList)
    {
        super();
       // contacts = new ArrayList<HashMap<String, String>>();
        //this.context=context;
       contacts=arrayList;

        contacts.add(resultp);

        String n=resultp.get("flag");

        db=new DatabaseHandler(context.getApplicationContext());
        //hi somechanges
        ;
      /*  try {
            contacts = new ArrayList<Contact>();
           // contacts2 = new ArrayList<Contact>();

            //db=new DatabaseHandler();
            Log.d("hi","bye");
            //db=new DatabaseHandler(Context);
            contacts = db.getAllContacts();
            Log.d("Name: ", "enterd");
               // Contact c= new Contact();
            Contact c = new Contact();
            //c.get_name();
            for (int i = 0; i < contacts.size(); i++) {


                for (Contact cn : contacts) {
                    String log = cn.get_name();

                    c.set_name(log);

                }
                contacts.add(c);
            }


        }
        catch (Exception e)
        {

        }*/

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        resultp = contacts.get(i);
            try {
               // String log=nature.get_name();
                //Log.d("image",log);
                nature=resultp.get("flag").toString();
                viewHolder.imgThumbnail.setImageUrl(nature);

            }
            catch (Exception e)
            {

            }


    }

    @Override
    public int getItemCount() {

        return contacts.size();
    }

    @Override
    public void onClick(View v) {

          //  Toast.makeText(, "Visiting Card Clicked is ==>", Toast.LENGTH_SHORT).show();

    }

    class ViewHolder extends RecyclerView.ViewHolder{


        public SmartImageView imgThumbnail;
        public ViewHolder(final View itemView) {
            super(itemView);

            imgThumbnail= (SmartImageView)itemView.findViewById(R.id.img_thumbnail);
            final RecyclerView mRecyclerView = (RecyclerView)itemView.findViewById(R.id.recycler_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String next=resultp.get("flag").toString();

                    int itemPosition = mRecyclerView.getChildPosition(itemView);
                    String item = resultp.get(itemPosition);
                    Toast.makeText(v.getContext(), item, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(v.getContext(), FullScreenActivity.class);

                    // Pass all data flag
                    intent.putExtra("flag", next);
                    // Start SingleItemView Class
                    v.getContext().startActivity(intent);
                }
            });

        }
    }
}