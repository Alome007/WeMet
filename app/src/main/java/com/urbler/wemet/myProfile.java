package com.urbler.wemet;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by Alome on 5/25/2019.
 * WeMet
 */

public class myProfile extends AppCompatActivity {
    ImageView myProfile;
    TextView name;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private String userId0;
View lin;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view);
        userId0=user.getUid();
        name=findViewById(R.id.us);
     //   lin=findViewById(R.id.lin);
//        lin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//                overridePendingTransition(R.anim.no_animation, R.anim.no_animation);
//            }
//        });
        myProfile=findViewById(R.id.im);
        fillData(name,myProfile);

    }
    public  void fillData(final TextView t1, final ImageView imageView){
        //link to my account...
        DatabaseReference you= FirebaseDatabase.getInstance().getReference().child("Users").child(userId0).child("profile").child("account");
        you.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                registerPojo weMeetPojo=dataSnapshot.getValue(registerPojo.class);
                String yourName= null;
                if (weMeetPojo != null) {
                    String imageUrl=weMeetPojo.geturl();
                    Picasso.get().load(imageUrl).into(imageView);

                }
                else{
                    imageView.setImageResource(R.drawable.ic_account_circle_black_24dp);
                }
                if (weMeetPojo != null) {
                    yourName = weMeetPojo.getName();

                }
                String youUrl= null;
                if (weMeetPojo != null) {
                    youUrl = weMeetPojo.getCountry();
                }
                t1.setText(yourName);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Alomeeeee", "loadPost:onCancelled", databaseError.toException());
                // ...
            }

        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.no_animation);

        super.onBackPressed();
    }
}
