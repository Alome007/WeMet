package com.urbler.wemet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.DisplayMetrics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class pubAdapter extends Adapter<pubAdapter.MyViewHolder> {
    DisplayMetrics metrics=new DisplayMetrics();

    private ArrayList<publicData> myPos = new ArrayList();

    Context mContext;

    /* renamed from: chat4meal.com.bikeman.myPosts$2 */
    public class MyViewHolder extends ViewHolder {
        public ImageView snapedP;
        TextView title;
        TextView des;
        public MyViewHolder(View view) {
            super(view);
            title=view.findViewById(R.id.title);
            des=view.findViewById(R.id.desc);

        }
    }

    public pubAdapter(List<publicData> contactList, Context context ) {
        myPos = (ArrayList) contactList;
        mContext=context;
    }


    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.priv_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final publicData myP = (publicData) myPos.get(position);
        holder.title.setText(myP.getTilte());
        holder.des.setText(myP.getDes());

    }

    public int getItemCount() {
        if (myPos == null) {
            return 0;
        }
        return myPos.size();
    }



    @SuppressLint("SimpleDateFormat")
    private String formatDate(String dateStr) {
        try {
            return new SimpleDateFormat("MMM d").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr));
        } catch (ParseException e) {
            return "";
        }
    }
}
