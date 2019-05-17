package com.urbler.wemet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by alome daniel on 10/2/2018, been Tuesday
 */

public class Private extends Fragment  {
    ArrayList<priData> arraList=new ArrayList<>();
    priAdater adapter;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    String userId;
    DatabaseReference mData;
    DatabaseReference mRoot;
    FloatingActionButton fab;
    RecyclerView recycler;
    private View view;
TextView noP;
ImageView noP2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.priv, container, false);
                userId=user.getUid();
        mRoot= FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("profile").child("private");
        // Inflate the layout for this fragment
        fab=view.findViewById(R.id.add);
        noP=view.findViewById(R.id.noT);
        noP2=view.findViewById(R.id.noP);
        adapter=new priAdater(arraList,getContext());
        recycler=view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        loadData();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show modal
                modalAppPri fragment = new modalAppPri();
                fragment.show(getActivity().getSupportFragmentManager(), "TAG");
            }
        });
        return view;

    }


    public void loadData(){
        mRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    arraList.clear();
                    priData value = ds.getValue(priData.class);
                    priData fire = new priData();
                    String title = value.getTilte();
                    String desc = value.getDes();
                    fire.setTilte(title);
                    fire.setDes(desc);
                    arraList.add(fire);
                    int add=adapter.getItemCount();
                    if (add>0){
                        //todo hide
                        noP.setVisibility(View.GONE);
                        noP2.setVisibility(View.GONE);
                        recycler.setAdapter(adapter);
                    }

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
}