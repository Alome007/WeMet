package com.urbler.wemet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
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
import com.google.zxing.WriterException;
import com.squareup.picasso.Picasso;
import com.vistrav.ask.Ask;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

/**
 * Created by Alome on 3/19/2019.
 * 2019 Urbler
 */

public  class MainActivity extends AppCompatActivity implements View.OnClickListener,DragLayout.GotoDetailListener{
   // private static final int PICK =1 ;
    private List<weMeetPojo> movieList = new ArrayList<>();
    View view;
    String userId0;
    final int SELECT_PHOTO = 1;

    //DatabaseReference userData;
    ProgressBar pro;
   // String youid;
    private FirebaseUser user0 = FirebaseAuth.getInstance().getCurrentUser();
    RecyclerView recyclerView;
    StorageReference storageRef, imageRef;
   CoordinatorLayout constraintLayout;
    String data;
    private Boolean isFabOpen = false;
    FloatingActionButton fab,fab1,fab2;
    Animation fab_open,fab_close,rotate_forward,rotate_backward;
    ImageView profile,dots,search;
  //  private String random;
    ImageView noP;
    TextView username,country,noT;
    private Uri uriImage;
    location appLocationService;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case R.id.change:
               Intent intent=new Intent(MainActivity.this,addInfo.class);
               startActivity(intent);
       }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.we_meet);
        appLocationService = new location(
              MainActivity.this);
        SharedPreferences sharedPreferences=getSharedPreferences("show",MODE_PRIVATE);
        boolean first=sharedPreferences.getBoolean("run",true);
        if (first){
            askF();
        }

//        netWork netWork=new netWork();
//        netWork.check(this);
        pro=findViewById(R.id.prog);
        username=findViewById(R.id.userName);
        constraintLayout=findViewById(R.id.constraint);
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.cons));
        country=findViewById(R.id.affirmation);
        noT=findViewById(R.id.noT);
        noP=findViewById(R.id.noP);
        profile=findViewById(R.id.useLogo);
        dots=findViewById(R.id.menu);
        search=findViewById(R.id.search);
        userId0= user0.getUid();
        recyclerView=findViewById(R.id.recyc);
        fab1=findViewById(R.id.fab1);
        fab2=findViewById(R.id.fab2);
        fab=findViewById(R.id.ScanR);
//        DatabaseReference database=FirebaseDatabase.getInstance().getReference();
//        if (database == null)
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        dots.setOnClickListener(this);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        fillData(username,country,profile);
        changeQrCodePub();
        changeQrCodePri();
       // String requiredPermission = "android.permission.WRITE_EXTERNAL_STORAGE";
       // final int checkVal = this.checkCallingOrSelfPermission(requiredPermission);
        //Set the custom view
        Ask.on(this)
                .id(2) // in case you are invoking multiple time Ask from same activity or fragment
                .forPermissions(android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA
                        , android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withRationales("We need your permission !",
                        "In order to save file you will need to grant storage permission","Camera Permission is needed !") //optional
                .go();
       always();
        //todo profile picture changer...
//        profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (MainActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                            == PackageManager.PERMISSION_GRANTED){
//                        ChooseImage();
//                    }
//                    else
//                    {
//                        Toast.makeText(MainActivity.this, "Please grant the Camera Permission", Toast.LENGTH_SHORT).show();
//                        Ask.on(MainActivity.this)
//                                .id(2) // in case you are invoking multiple time Ask from same activity or fragment
//                                .forPermissions(android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA
//                                        , android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                                .withRationales("We need your permission !",
//                                        "In order to save file you will need to grant storage permission","Camera Permission is needed !") //optional
//                                .go();
//                    }
//                }
//            }
//        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myProfileView();
            }
        });


        dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showenu(dots);
            }
        });
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.tool);
//        setSupportActionBar(toolbar);
//        ActionBar actionbar = getSupportActionBar();
//        if (actionbar != null) {
//            toolbar.setNavigationIcon(null);
//            actionbar.setTitle("WeMet");
//        }
//        assert actionbar != null;
//        actionbar.setDisplayHomeAsUpEnabled(true);
        //   Toast.makeText(MainActivity.this, myData(),Toast.LENGTH_LONG).show();

        myPosts mAdapter = new myPosts(movieList, this);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
       // todo uncomment readmyPost();
//        weMeetPojo weMeetPojo=new weMeetPojo("Alome Daniel","snjbdede","2nd May","Yaba","private","aasnkdkned");
//        movieList.add(weMeetPojo);
//        weMeetPojo=new weMeetPojo("Alome Daniel","snjbdede","2nd May","Yaba","private","aasnkdkned");
//        movieList.add(weMeetPojo);weMeetPojo=new weMeetPojo("Alome Daniel","snjbdede","2nd May","Yaba","private","aasnkdkned");
//        movieList.add(weMeetPojo);weMeetPojo=new weMeetPojo("Alome Daniel","snjbdede","2nd May","Yaba","private","aasnkdkned");
//        movieList.add(weMeetPojo);weMeetPojo=new weMeetPojo("Alome Daniel","snjbdede","2nd May","Yaba","private","aasnkdkned");
//        movieList.add(weMeetPojo);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                String id = movieList.get(position).getId();
                String ty = movieList.get(position).getType();
                SharedPreferences sharedPreferences=getSharedPreferences("yourid",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("id",id);
                editor.apply();
                SharedPreferences sharedPreferences2=getSharedPreferences("type",MODE_PRIVATE);
                SharedPreferences.Editor editor2=sharedPreferences2.edit();
                editor2.putString("val",ty);
                editor2.apply();
                startActivity(new Intent(MainActivity.this,Detail.class));
            }
            @Override
            public void onLongItemClick(final View view, final int position) {

            }
        }));
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
                getApplicationContext(), new GeocoderHandler());
    } else {
        showSettingsAlert();
    }

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
            DatabaseReference users=FirebaseDatabase.getInstance().getReference();
            users.child("Users").child(userId0).child("profile").child("account").child("country").setValue(locationAddress);
        }
    }
    private void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
               MainActivity.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                     MainActivity.this.startActivity(intent);
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

    void myProfileView(){
    ActivityOptions options = null;
    // start the new activity
    Intent intent=new Intent(MainActivity.this,myProfile.class);

    startActivity(intent);
        overridePendingTransition(R.anim.slide_up,  R.anim.no_animation);
}
//todo remove comment
//    private void readmyPost() {
//        final DatabaseReference me= FirebaseDatabase.getInstance().getReference().child("Users").child(userId0).child("profile").child("posts");
//      me.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                   weMeetPojo value = ds.getValue(weMeetPojo.class);
//                    String title = value.getFriendsName();
//                    String location = value.getLocation();
//                    String date=value.getDate();
//                    String profile=value.getFriendsPicUrl();
//                    weMeetPojo meetPojo = new weMeetPojo();
//                    meetPojo.setFriendsName(title);
//                    meetPojo.setFriendsPicUrl(profile);
//                    meetPojo.setDate(date+" "+"at"+userId0+location);
//                    movieList.add(meetPojo);
//                    int po=mAdapter.getItemCount();
//                    if (po>0){
//                        constraintLayout.setBackgroundColor(getResources().getColor(R.color.cons2));
//                        noP.setVisibility(View.GONE);
//                        noT.setVisibility(View.GONE);
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w("Alomeeeee", "loadPost:onCancelled", databaseError.toException());
//                // ...
//            }
//
//        });
//    }

    private void scanCodeUpPri() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        @SuppressLint("InflateParams") final View mView = layoutInflaterAndroid.inflate(R.layout.my_qr_code, null);
        final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);

        final ImageView qrImage;
        qrImage=mView.findViewById(R.id.qr);
        final ProgressBar progressBar=mView.findViewById(R.id.proSc);
        final TextView code1=mView.findViewById(R.id.code);
        fab.startAnimation(rotate_backward);
        fab1.startAnimation(fab_close);
        fab2.startAnimation(fab_close);
        fab1.setClickable(false);
        fab2.setClickable(false);
        isFabOpen = false;
        final DatabaseReference num= FirebaseDatabase.getInstance().getReference().child("Users").child(userId0).child("profile").child("scanProgress").child("Private");
       num.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               if (!dataSnapshot.toString().isEmpty()) {
                   //if not working change to  String code100 = dataSnapshot.getValue().toString();
                  String code100 = String.valueOf(dataSnapshot.getValue());
                   progressBar.setVisibility(View.GONE);
                   try {
                       num.keepSynced(false);
                       // Toast.makeText(MainActivity.this,code,Toast.LENGTH_LONG).show();
                       String code0=userId0+code100;
                      // Toast.makeText(MainActivity.this,code0.length(),Toast.LENGTH_LONG).show();
                       code1.setText(getCode(code100));
                       code1.setVisibility(View.VISIBLE);
                     //  Toast.makeText(MainActivity.this,"This is the code ooooo"+ " "+ code100,Toast.LENGTH_LONG).show();
                       QRGEncoder qrgEncoder = new QRGEncoder(code0, null, QRGContents.Type.TEXT, 1000);
                       // Getting QR-Code as Bitmap
                       Bitmap bitmap = qrgEncoder.encodeAsBitmap();
                       // Setting Bitmap to ImageView

                       qrImage.setImageBitmap(bitmap);
                       //QRGSaver.save("storage/","UrblerAuth" , bitmap, QRGContents.ImageType.IMAGE_JPEG);

                   } catch (WriterException e) {
                       Log.v("AlomeQr", e.toString());
                   }
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
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







    private void scanCodeUpPub() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        @SuppressLint("InflateParams") final View mView = layoutInflaterAndroid.inflate(R.layout.my_qr_code, null);
        final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        final ImageView qrImage;
        qrImage=mView.findViewById(R.id.qr);
        final ProgressBar progressBar=mView.findViewById(R.id.proSc);
        final TextView code1=mView.findViewById(R.id.code);
        fab.startAnimation(rotate_backward);
        fab1.startAnimation(fab_close);
        fab2.startAnimation(fab_close);
        fab1.setClickable(false);
        fab2.setClickable(false);
        isFabOpen = false;
        final DatabaseReference num= FirebaseDatabase.getInstance().getReference().child("Users").child(userId0).child("profile").child("scanProgress").child("Public");
        num.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.toString().isEmpty()) {
                    //todo change back to  String code100 = dataSnapshot.getValue().toString();
                    String code100 = String.valueOf(dataSnapshot.getValue());
                    progressBar.setVisibility(View.GONE);
                    try {
                        num.keepSynced(false);
                        // Toast.makeText(MainActivity.this,code,Toast.LENGTH_LONG).show();
                        String code0=userId0+code100;
                        // Toast.makeText(MainActivity.this,code0.length(),Toast.LENGTH_LONG).show();
                        code1.setText(getCode(code100));
                        code1.setVisibility(View.VISIBLE);
                        //  Toast.makeText(MainActivity.this,"This is the code ooooo"+ " "+ code100,Toast.LENGTH_LONG).show();
                        QRGEncoder qrgEncoder = new QRGEncoder(code0, null, QRGContents.Type.TEXT, 1000);
                        // Getting QR-Code as Bitmap
                        Bitmap bitmap = qrgEncoder.encodeAsBitmap();
                        // Setting Bitmap to ImageView

                        qrImage.setImageBitmap(bitmap);
                        //QRGSaver.save("storage/","UrblerAuth" , bitmap, QRGContents.ImageType.IMAGE_JPEG);

                    } catch (WriterException e) {
                        Log.v("AlomeQr", e.toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        // Toast.makeText(MainActivity.this, "Closing",Toast.LENGTH_LONG).show();
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(final View view) {
        int id = view.getId();
        switch (id){
            case R.id.ScanR:
                animateFAB();

                break;
            case R.id.fab1:
                animateFAB();
             //   SharedPreferences sharedPreferences=getSharedPreferences("type",MODE_PRIVATE);
              //  final SharedPreferences.Editor editor=sharedPreferences.edit();
                CharSequence colors[] = new CharSequence[]{"Show Public DC", "Show Private DC"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Option");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //   editor.putString("val","public");
                            // editor.apply();

                            scanCodeUpPub();

                        }
                            else
                            {
                                scanCodeUpPri();

                            }

                    }
                });
                builder.show();
                break;
            case R.id.fab2:
                animateFAB();
                SharedPreferences sharedPreferences=getSharedPreferences("type",MODE_PRIVATE);
                final SharedPreferences.Editor editor=sharedPreferences.edit();
                CharSequence colors1[] = new CharSequence[]{"Scan DC", "Enter DC"};
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("Option");
                builder1.setItems(colors1, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //todo
//                           editor.putString("val","public");
//                           editor.apply();
                            if (MainActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED){
                                ReadDatapri();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Please Grant Us Camera permission from the settings -> app ->WeMet -> permission", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            if (MainActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED){
                               enterCode();
                                //todo
//                                editor.putString("val","private");
//                                editor.apply();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Please Grant Us Camera permission from the settings -> app ->WeMet -> permission", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder1.show();
                break;

        }
    }

    private void enterCode() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        @SuppressLint("InflateParams") final View mView = layoutInflaterAndroid.inflate(R.layout.enter, null);
        final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        fab.startAnimation(rotate_backward);
        fab1.startAnimation(fab_close);
        fab2.startAnimation(fab_close);
        fab1.setClickable(false);
        fab2.setClickable(false);
        isFabOpen = false;

        alertDialogBuilderUserInput
                .setCancelable(true)
                .setPositiveButton("Verify", new DialogInterface.OnClickListener() {
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

    public void animateFAB(){
        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("AlomeFab", "close");

        } else {
            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("AlomeFab","open");
        }
    }
    public void ReadDatapri(){
        startActivity(new Intent(MainActivity.this, scan.class));

    }
public  void fillData(final TextView t1, final TextView t2, final ImageView imageView){
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
                pro.setVisibility(View.GONE);
            }
            else{
                imageView.setImageResource(R.drawable.ic_account_circle_black_24dp);
            }
            if (weMeetPojo != null) {
                yourName = weMeetPojo.getName();
                pro.setVisibility(View.GONE);
            }
            String youUrl= null;
            if (weMeetPojo != null) {
                youUrl = weMeetPojo.getCountry();
            }
            t1.setText(yourName);
            pro.setVisibility(View.GONE);
          t2.setText(youUrl);
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("Alomeeeee", "loadPost:onCancelled", databaseError.toException());
            // ...
        }

    });
}

    public  void  checkFab(){
        final Handler handler = new Handler();
        final int delay = 50; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){

                SharedPreferences sharedPreference=getSharedPreferences("fab", Context.MODE_PRIVATE);
                String val=sharedPreference.getString("yes",null);
                if (val!=null){
                   fab.setVisibility(View.GONE);
                }else{
                    fab.setVisibility(View.VISIBLE);
                }


                handler.postDelayed(this, delay);
            }
        }, delay);


    }
//String yourId(){
//
//   DatabaseReference getUrId = FirebaseDatabase.getInstance().getReference().child("Users").child("scanners").child(random);
//   getUrId.addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//
//                //reads all data on the private node....
//                random=UUID.randomUUID().toString();
//                priData value = dataSnapshot.getValue(priData.class);
//            if (value != null) {
//                youid= value.getMyId();
//            }
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//            // Getting Post failed, log a message
//            Log.w("Alomeeeee", "loadPost:onCancelled", databaseError.toException());
//            // ...
//        }
//
//    });
//    return youid;
//
//}

    @Override
    public void gotoDetail() {
       // Toast.makeText(MainActivity.this,"Hide now",Toast.LENGTH_LONG).show();
    }
    public void showenu(View v){
            PopupMenu popup2 = new PopupMenu(this,v);
            MenuInflater inflater = popup2.getMenuInflater();
            inflater.inflate(R.menu.action, popup2.getMenu());
            popup2.setOnMenuItemClickListener(new menuClicker());
            popup2.show();

    }
    class menuClicker implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.change:
startActivity(new Intent(MainActivity.this,addInfo.class));
                    break;
                case R.id.help:
                   logOut();
                    return true;
            }
            return false;
        }
    }

    private void logOut() {
      Handler handler;
        handler = new Handler();
        final ProgressDialog p=new ProgressDialog(MainActivity.this);
        p.setMessage("Logging Out..");
        p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        p.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                p.dismiss();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,PhoneNumberAuthetication.class));
                finish();
            }
        }, 5000);
    }

//    public void ChooseImage() {
//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//        photoPickerIntent.setType("image/*");
//        startActivityForResult(photoPickerIntent, SELECT_PHOTO);


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                 uriImage= imageReturnedIntent.getData();
                 UploadFoto();
                }
        }

    }

    private void always() {
        final Handler handler = new Handler();
        final int delay = 1000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                loc();
                handler.postDelayed(this, delay);
            }
        }, delay);

    }
    public void UploadFoto() {
        final ProgressDialog progressD=new ProgressDialog(MainActivity.this);
        progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressD.setMessage("Update profile picture...");
        progressD.show();
        imageRef = storageRef.child("Wemet101/"+ UUID.randomUUID().toString());
        imageRef.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Users").child(userId0).child("profile").child("account");
                        db.child("url").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                        progressD.dismiss();
                        Log.d("IMAGE", "onSuccess: uri= "+ uri.toString());
                     //   Toast.makeText(MainActivity.this,uri.toString(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
   public void askF(){
       final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);

       alertDialogBuilderUserInput
               .setCancelable(true)
               .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               })
               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {

                   }
               });
       final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
       alertDialogAndroid.setMessage("Welcome on Board !, it nice to have you.." +
               " "+ "Do you want to set up your Public Data and Private data now ?");
       alertDialogAndroid.show();
       alertDialogAndroid.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               SharedPreferences sharedPreferences=getSharedPreferences("show",MODE_PRIVATE);
               SharedPreferences.Editor editor=sharedPreferences.edit();
               editor.putBoolean("run",false);
               editor.apply();
               alertDialogAndroid.dismiss();
           }
       });
       alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               SharedPreferences sharedPreferences=getSharedPreferences("show",MODE_PRIVATE);
               SharedPreferences.Editor editor=sharedPreferences.edit();
               editor.putBoolean("run",false);
               editor.apply();
               alertDialogAndroid.dismiss();
               startActivity(new Intent(MainActivity.this,addInfo.class));
           }
       });

   }

public void changeQrCodePub(){
    Log.d("ID ooooooooo", "changeQrCode: "+userId0);
    final Handler handler = new Handler();
    final int delay = 10000;
    handler.postDelayed(new Runnable(){
        @SuppressLint("DefaultLocale")
        public void run()
        {
            SecureRandom random1 = new SecureRandom();
            int num1 = random1.nextInt(10000);
            @SuppressLint("DefaultLocale") String formatted = String.format("%04d",    num1);
            DatabaseReference num;
            num=FirebaseDatabase.getInstance().getReference().child("Users").child(userId0).child("profile").child("scanProgress").child("Public");
           // Random random=new Random();
            num.keepSynced(false);
          //  int Rnumber = Integer.parseInt(String.format("%04d", random.nextInt(700700703)));
            data=userId0+formatted;
            num.setValue(data);
            handler.postDelayed(this, delay);
            // Toast.makeText(MainActivity.this,"The string is "+" "+formatted,Toast.LENGTH_LONG).show();

        }
    }, delay);
}
    public static String getCode(String word){
        // this should be get Access code... Create another method for stripping acess codes
        String firstFourChars;     //substring containing first 4 characters
        firstFourChars = word.substring(28, 32);
        return firstFourChars;
    }
    private void changeQrCodePri() {
        final Handler handler = new Handler();
        final int delay = 10000;
        handler.postDelayed(new Runnable(){
            @SuppressLint("DefaultLocale")
            public void run()
            {
                SecureRandom random1 = new SecureRandom();
                int num1 = random1.nextInt(10000);
                @SuppressLint("DefaultLocale") String formatted = String.format("%04d", num1);
                DatabaseReference num;
                num=FirebaseDatabase.getInstance().getReference().child("Users").child(userId0).child("profile").child("scanProgress").child("Private");
              //  Random random=new Random();
                num.keepSynced(false);
             // int  Rnumber = Integer.parseInt(String.format("%04d", random.nextInt(700700703)));
                data=userId0+formatted;
                num.setValue(data);
                handler.postDelayed(this, delay);

                // Toast.makeText(MainActivity.this,"The string is "+" "+formatted,Toast.LENGTH_LONG).show();
            }
        }, delay);
    }
}
