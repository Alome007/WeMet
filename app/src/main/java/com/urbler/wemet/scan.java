package com.urbler.wemet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scan extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
     boolean exist;
    private String url;
    ProgressDialog pd;
    private boolean is;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        pd=new ProgressDialog(scan.this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(final Result rawResult) {
        // Do something with the result here
        // Log.v("tag", rawResult.getText()); // Prints scan results
        // Log.v("tag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
       // Toast.makeText(scan.this,rawResult.toString(),Toast.LENGTH_LONG).show();
        getId(rawResult.getText());
        //verifying your Id
        DatabaseReference num= FirebaseDatabase.getInstance().getReference().child("Users").child(getId(rawResult.getText())).child("profile").child("scanProgress");
        num.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pd.setMessage("Verifying... ");
                pd.show();
                String s=dataSnapshot.getValue().toString();
                if (s.equals(getCode(rawResult.getText()))){
                    SharedPreferences sh=getSharedPreferences("true",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sh.edit();
                    editor.putBoolean("ok",true);

        }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Alomeeeee", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
        if(exist()){
          //  num.removeValue();
            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            String userId= null;
            if (user != null) {
                userId = user.getUid();
            }
            pd.dismiss();
            requestAccess();
            addMeToyourPost(userId,getId(rawResult.getText()));
            addyouTomyPost(userId,getId(rawResult.getText()));
           // so you can read the Data;
            //add to your post add to my post !.... my id and your Id.. then==> Back Pressed !...
            onBackPressed();
}
else{
            pd.dismiss();
            scanCodeUpPri();
            Log.d("Not Existing !", "handleResult: Not In Existence");
}
        mScannerView.resumeCameraPreview(this);
    }

    private void requestAccess() {

    }

    private boolean exist() {
        final Handler handler = new Handler();
        final int delay = 50; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){

                SharedPreferences sharedPreference=getSharedPreferences("true", Context.MODE_PRIVATE);
                Boolean val=sharedPreference.getBoolean("ok",false);
                if (val)
                    is=true;
                handler.postDelayed(this, delay);
            }
        }, delay);
        return is;
    }


    public  String getId(String word){
        // this should be get Access code... Create another method for stripping acess codes
        String input = word;     //input string
        String firstFourChars = "";     //substring containing first 4 characters
        firstFourChars = input.substring(0, 28);
        System.out.println(firstFourChars);
      //  Toast.makeText(scan.this,word,Toast.LENGTH_LONG).show();
        return firstFourChars;

    }
    public static String getCode(String word){
        // this should be get Access code... Create another method for stripping acess codes
        String input = word;     //input string
        String firstFourChars = "";     //substring containing first 4 characters
        firstFourChars = input.substring(28, 32);
        System.out.println(firstFourChars);
        return firstFourChars;
    }
    private void addyouTomyPost(final String s, final String u) {
        //link to oyur account...
        DatabaseReference you= FirebaseDatabase.getInstance().getReference().child("Users").child(s).child("profile").child("account");
        you.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                weMeetPojo weMeetPojo=dataSnapshot.getValue(weMeetPojo.class);
                String yourName=weMeetPojo.getFriendsName();
                String youUrl=getUrl(s);
                String location=weMeetPojo.getLocation();
                String date=weMeetPojo.getDate();
                String typ=weMeetPojo.getType();
                weMeetPojo add=new weMeetPojo(yourName,youUrl,location,date, typ, s);
                //link to mine...
                DatabaseReference me= FirebaseDatabase.getInstance().getReference().child("Users").child(u).child("profile").child("posts").push();
                me.setValue(add).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Added101","Added you to my posts Successfully");
                    }
                });


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Alomeeeee", "loadPost:onCancelled", databaseError.toException());
                // ...
            }

        });


    }
    private void addMeToyourPost(final String s,final  String o) {
        //link to my account...
        //todo s is my id
        DatabaseReference you= FirebaseDatabase.getInstance().getReference().child("Users").child(o).child("profile").child("account");
        you.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SharedPreferences sharedPreferences=getSharedPreferences("yurl",MODE_PRIVATE);
                String youUrl=sharedPreferences.getString("urr",null);
                weMeetPojo weMeetPojo=dataSnapshot.getValue(weMeetPojo.class);
                String yourName= null;
                if (weMeetPojo != null) {
                    yourName = weMeetPojo.getFriendsName();
                }
                String location= null;
                if (weMeetPojo != null) {
                    location = weMeetPojo.getLocation();
                }
                String date= null;
                if (weMeetPojo != null) {
                    date = weMeetPojo.getDate();
                }
                String typ= null;
                if (weMeetPojo != null) {
                    typ = weMeetPojo.getType();
                }
                weMeetPojo add=new weMeetPojo(yourName,youUrl,location,date, typ, s);
                //link to mine...
                DatabaseReference me= FirebaseDatabase.getInstance().getReference().child("Users").child(s).child("profile").child("posts").push();
                me.setValue(add).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Added101","Added me to your posts Successfully");
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Alomeeeee", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

    }
    private String getUrl(String id) {
        SharedPreferences sharedPreferences=getSharedPreferences("yurl",MODE_PRIVATE);
        final SharedPreferences.Editor edito=sharedPreferences.edit();
        DatabaseReference you= FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("profile").child("account");
        you.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                registerPojo weMeetPojo=dataSnapshot.getValue(registerPojo.class);
                if (weMeetPojo != null) {
                    edito.putString("urr",    url=weMeetPojo.geturl());
                    edito.apply();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Alomeeeee", "loadPost:onCancelled", databaseError.toException());
                // ...
            }

        });
        return url;
    }
    private void scanCodeUpPri() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        @SuppressLint("InflateParams") final View mView = layoutInflaterAndroid.inflate(R.layout.not, null);
        final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(true)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });


        final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
        alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogAndroid.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public int count(String number){
        int result;
        result=number.length();
        return result;
    }
}