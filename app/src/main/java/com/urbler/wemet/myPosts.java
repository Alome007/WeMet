package com.urbler.wemet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class myPosts extends Adapter<myPosts.MyViewHolder> {
    DisplayMetrics metrics=new DisplayMetrics();
    ArrayList<View> itemViewList = new ArrayList();
    private ArrayList<weMeetPojo> myPos = new ArrayList();
    private ArrayList<myPosts> result;
    CardView cardView;
    private int it=0;
    AnimationClick animationClick;
    int currentItemWidth;
    CardView cardView2;
    DragLayout dragLayout;
  Context mContext;



    /* renamed from: chat4meal.com.bikeman.myPosts$2 */
    public class MyViewHolder extends ViewHolder {
        TextView friendsName;
        TextView date;

        public ImageView snapedP,imageView;
        CardView c;

//

        public MyViewHolder(View view) {
            super(view);

            snapedP = view.findViewById(R.id.image);
            //todo
            friendsName=view.findViewById(R.id.address4);
            date=view.findViewById(R.id.address5);
            c=view.findViewById(R.id.car);
            imageView=view.findViewById(R.id.dod);
imageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        showMenu(imageView);

    }
});
        }
    }

    private void showMenu(View imageView) {
        PopupMenu popup2 = new PopupMenu(mContext,imageView);
        MenuInflater inflater = popup2.getMenuInflater();
        inflater.inflate(R.menu.show_shared, popup2.getMenu());
        popup2.setOnMenuItemClickListener(new menuClicker());
        popup2.show();
    }

    public myPosts(List<weMeetPojo> contactList, Context context ) {
        myPos = (ArrayList) contactList;
        mContext=context;
    }
    class menuClicker implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.info:
//                    Toast.makeText(mContext, "View shared info", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.add:
                    addToyouTimeLine();
                    return true;
            }
            return false;
        }
    }
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_common, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final weMeetPojo myP = (weMeetPojo) myPos.get(position);
        Picasso.get().load(myP.getFriendsPicUrl()).into(holder.snapedP);
        holder.snapedP.setTransitionName("thumbnailTransition");
        holder.friendsName.setText("You Met With"+" "+myP.getFriendsName());
        holder.date.setText(myP.getDate());
//        holder.c.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                    Pair<View, String> pair1 = null;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                        pair1 = Pair.create((View) holder.snapedP, holder.snapedP.getTransitionName());
//                    }
//                    Intent intent=new Intent(mContext,Detail.class);
//                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, pair1);
//                    mContext.startActivity(intent, optionsCompat.toBundle());
//            }
//        });

    }
        public void addToyouTimeLine(){
            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            String id=user.getUid();
            DatabaseReference mData= FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("profile").child("timeline");

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
    void setUp(ArrayList<myPosts> result) {
        result = result;
        notifyDataSetChanged();
    }

}
