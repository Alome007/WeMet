package com.urbler.wemet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class timeLineAdapter extends Adapter<timeLineAdapter.MyViewHolder> {

    private ArrayList<priData> myPos = new ArrayList();

    Context mContext;

    /* renamed from: chat4meal.com.bikeman.myPosts$2 */
    public class MyViewHolder extends ViewHolder {
        ImageView p1,p2,l1,dot;
        TextView topic,value;
        public MyViewHolder(View view) {
            super(view);
            topic=view.findViewById(R.id.topic);
        }
    }

    public timeLineAdapter(List<priData> contactList, Context context ) {
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
        final priData myP = (priData) myPos.get(position);
        holder.topic.setText(myP.getTilte());
        holder.value.setText(myP.getDes());
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
