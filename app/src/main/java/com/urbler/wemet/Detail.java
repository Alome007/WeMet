package com.urbler.wemet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
/**
 * Created by Alome on 5/7/2019.
 * WeMet
 */
public class Detail extends AppCompatActivity implements View.OnClickListener{
    RecyclerView recyclerView;
    timeLineAdapter timeLineAdapter;
    ArrayList<priData> timeLineApps=new ArrayList<>();
    ImageView imageView,backBtn,imageView2;
    private PopupMenu popup2;
    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        recyclerView=findViewById(R.id.recyclerT);
        progressBar=findViewById(R.id.progTime);
        imageView=findViewById(R.id.imageP);
        backBtn=findViewById(R.id.back_btn);
        timeLineAdapter=new timeLineAdapter(timeLineApps,this);
       progressBar.setVisibility(View.VISIBLE);
       loadTimeLine();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDat=database.getReference();
        if (mDat== null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }




    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.back_btn:
                startActivity(new Intent(Detail.this,MainActivity.class));
                finish();
                break;
            case R.id.imageView2:
              //  showOptionsMenu(imageView2);
                break;
        }
    }
//    private void showOptionsMenu(View menu) {
//        popup2 = new PopupMenu(this, menu);
//        MenuInflater inflater = popup2.getMenuInflater();
//        inflater.inflate(R.menu.action, popup2.getMenu());
//        popup2.setOnMenuItemClickListener(new menuClicker());
//        popup2.show();
//
//    }
//    class menuClicker implements PopupMenu.OnMenuItemClickListener {
//        @Override
//        public boolean onMenuItemClick(MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.action_settings:
//                    Toast.makeText(Detail.this, "Settings ! ", Toast.LENGTH_SHORT).show();
//                    return true;
//            }
//            return false;
//        }

  private void loadTimeLine() {
      SharedPreferences sharedPreferences=getSharedPreferences("yourId",MODE_PRIVATE);
      String id=sharedPreferences.getString("id",null);

      //todo id remember
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(id).child("profile").child(type());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                priData value = ds.getValue(priData.class);
                    priData fire = new priData();
                    String topic = value.getTilte();
                    String like = value.getDes();
                    fire.setTilte(topic);
                    fire.setDes(like);
                    timeLineApps.add(fire);
                    recyclerView.setAdapter(timeLineAdapter);
                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Alomeeeee", "loadPost:onCancelled", databaseError.toException());
                // ...
            }

        });
    }
    public  String type(){
        SharedPreferences sharedPreferences2=getSharedPreferences("type",MODE_PRIVATE);
        String type=sharedPreferences2.getString("val",null);
        if (type.equals("private"))
            type="private";
        else
            type="public";
        return type;
    }
}
