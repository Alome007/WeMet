package com.urbler.wemet;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.EditText;
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
public class Detail extends AppCompatActivity {
    RecyclerView recyclerView;
    timeLineAdapter timeLineAdapter;
    ArrayList<priData> timeLineApps=new ArrayList<>();
    ImageView imageView,backBtn,add0;
    EditText editText;
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
        add0=findViewById(R.id.addCon);
        editText=findViewById(R.id.edit);
        timeLineAdapter=new timeLineAdapter(timeLineApps,this);
       progressBar.setVisibility(View.VISIBLE);
       //loadTimeLine();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDat=database.getReference();
        if (mDat== null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        add0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setVisibility(View.VISIBLE);
                expand(editText);
            }
        });
    }
//    @Override
//    public void onClick(View v) {
//        int id=v.getId();
//        switch (id){
//            case R.id.back_btn:
//                startActivity(new Intent(Detail.this,MainActivity.class));
//                finish();
//                break;
//            case R.id.imageView2:
//
//              //  showOptionsMenu(imageView2);
//                break;
//            case R.id.addCon:
//                editText.setVisibility(View.VISIBLE);
//                Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
//                break;
//
//        }
//    }
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
public static void expand(final View view) {
    int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) view.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
    int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    view.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
    final int targetHeight = view.getMeasuredHeight();

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    view.getLayoutParams().height = 1;
    view.setVisibility(View.VISIBLE);
    Animation a = new Animation()
    {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            view.getLayoutParams().height = interpolatedTime == 1
                    ? ViewGroup.LayoutParams.WRAP_CONTENT
                    : (int)(targetHeight * interpolatedTime);
            view.requestLayout();
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    };

    // 1dp/ms
    a.setDuration((int)(targetHeight / view.getContext().getResources().getDisplayMetrics().density));
    view.startAnimation(a);
}

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
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
