package com.urbler.wemet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

/**
 * Created by Alome on 5/4/2019.
 * WeMet
 */
public class Register extends AppCompatActivity {
    EditText name,country;
    ImageButton register;
    Button con;
    LinearLayout linearLayout;
    TextView signup;
    CheckBox checkBox;
    private Context c=this;
    String user;
    RelativeLayout rl;
    private int tL=Toast.LENGTH_LONG;
    DatabaseReference mRef;
    ProgressDialog progressDialog;
    DatabaseReference users;
    ImageView im;
    FirebaseUser user0;
    private Uri uriImage;
    private String url;
    private location appLocationService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg);
        appLocationService=new location(Register.this);
        loc();
        user0 = FirebaseAuth.getInstance().getCurrentUser();
         progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        name=findViewById(R.id.name);
        country=findViewById(R.id.email);
//      //  state=findViewById(R.id.password);
        register=findViewById(R.id.signup);
        checkBox=findViewById(R.id.checkbox);
        linearLayout=findViewById(R.id.linearlayout);
        con=findViewById(R.id.already);
        signup=findViewById(R.id.signup2);
        rl=findViewById(R.id.rec);
        im=findViewById(R.id.profilePic);
        user=user0.getUid();
        country.setEnabled(false);

     //Toast.makeText(Register.this,"ALome Damiel",Toast.LENGTH_LONG).show();
        mRef= FirebaseDatabase.getInstance().getReference();
        users=mRef.child("Users").child(user).child("profile").child("account");
        rl.setVisibility(View.GONE);
       /// unhideView();
        isRegistered();
       register.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
        if (validate()){
                 completeRegistration();
               // Toast.makeText(c,"Good",tL).show();
             }
               Intent intent=new Intent(c,MainActivity.class);
               ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(Register.this,im, ViewCompat.getTransitionName(im));
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                   startActivity(intent,options.toBundle());
               }
               finish();
           }
       });
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(Register.this,
                        con, ViewCompat.getTransitionName(con));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(new Intent(Register.this,MainActivity.class),options.toBundle());
                    SharedPreferences.Editor editor = getSharedPreferences("Reg", MODE_PRIVATE).edit();
                    editor.putBoolean("firstStart", false);
                    editor.apply();
                    startActivity(new Intent(c,MainActivity.class));
                    finish();
                }
            }
        });
    }
    //    private void initViews() {
//
//    }
    private boolean validate() {
        boolean canMove=false;
        String na=name.getText().toString();
        String count=country.getText().toString();
       // String sta=state.getText().toString();
        if (na.isEmpty())
            name.setError("Name Required");
        else
        if (count.isEmpty())
            country.setError("Country Required");


        else
        if(!checkBox.isChecked())
            Toast.makeText(Register.this,"Please Agree to our Terms",Toast.LENGTH_LONG).show();
        else
            canMove=true;
        return canMove;
    }
    private void completeRegistration() {
        final ProgressDialog progressDialog0=new ProgressDialog(this);
        progressDialog0.setMessage("Creating Account Please wait...");
        progressDialog0.show();
        Intent intent=new Intent(c,MainActivity.class);
        ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(Register.this,im, ViewCompat.getTransitionName(im));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent,options.toBundle());
        }
        finish();
        //todo change the registerPojo value to the edit text values...
        registerPojo registerPojo=new registerPojo(name.getText().toString(),country.getText().toString(),"","http://wemet.urbler.com/img/qrcode.png");

        users.setValue(registerPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog0.dismiss();
                SharedPreferences.Editor editor = getSharedPreferences("Reg", MODE_PRIVATE).edit();
                editor.putBoolean("firstStart", false);
                editor.apply();
            }
        });
        //todo still want to animate here....
    }

    public void loc(){
        Location location = appLocationService
                .getLocation(LocationManager.GPS_PROVIDER);

        //you can hard-code the lat & long if you have issues with getting it
        //remove the below if-condition and use the following couple of lines
        //double latitude = 37.422005;
        //double longitude = -122.084095

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(latitude, longitude,
                    getApplicationContext(), new Register.GeocoderHandler());
        } else {
            showSettingsAlert();
        }

    }


        private void showSettingsAlert() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    Register.this);
            alertDialog.setTitle("SETTINGS");
            alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                           Register.this.startActivity(intent);
                        }
                    });
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();

    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            country.setEnabled(false);
          country.setText(locationAddress);
        }
    }
    public void isRegistered(){
//        final String userName=user0.getUid(); todo

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    progressDialog.dismiss();
                    unhideView();
                 //   Toast.makeText(Register.this,"User not in existence",Toast.LENGTH_LONG).show();
                }
                else {

                   users.addValueEventListener(new ValueEventListener() {
                        @SuppressLint({"SetTextI18n", "ResourceAsColor"})
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            linearLayout.setVisibility(View.INVISIBLE);
                            progressDialog.dismiss();
                            con.setVisibility(View.VISIBLE);
                            rl.setVisibility(View.VISIBLE);
                            rl.setBackgroundColor(R.color.hmm);
                            registerPojo value = dataSnapshot.getValue(registerPojo.class);
                            String title = value != null ? value.getName() : "Alome Daniel";
                            con.setText("Continue as"+" "+title);
                            SharedPreferences.Editor editor = getSharedPreferences("Reg", MODE_PRIVATE).edit();
                            editor.putBoolean("firstStart",true);
                            editor.apply();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("Alomeeeee", "loadPost:onCancelled", databaseError.toException());
                            // ...
                        }

                    });



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Urbler:dbError", databaseError.getMessage()); //Don't ignore errors!
            }
        };
    users.addListenerForSingleValueEvent(eventListener);

    }

    private void unhideView() {
        linearLayout.setVisibility(View.VISIBLE);
        signup.setVisibility(View.VISIBLE);
        rl.setVisibility(View.VISIBLE);
    }
    public void ChooseImage() {
        Intent photoPickerIntent = new Intent("android.intent.action.PICK");
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 1:
                if (resultCode == -1) {
                    uriImage = imageReturnedIntent.getData();
                    UploadFoto();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void UploadFoto() {
        final ProgressDialog progressD=new ProgressDialog(Register.this);
        progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressD.show();
        StorageReference storageRef;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
       final StorageReference imageRef = storageRef.child("Wemet101/" + UUID.randomUUID().toString());
        imageRef.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Users").child(user).child("profile").child("acount");
                        db.child("imageUrl").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                        progressD.dismiss();
                        Log.d("IMAGE", "onSuccess: uri= "+ uri.toString());

                    }
                });
            }
        });
    }

    private String getUrl(String id) {
        DatabaseReference you= FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("profile").child("account");
        you.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                registerPojo weMeetPojo=dataSnapshot.getValue(registerPojo.class);
                url =weMeetPojo.geturl();

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
}

