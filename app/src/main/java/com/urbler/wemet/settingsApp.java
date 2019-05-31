package com.urbler.wemet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by Alome on 5/3/2019.
 WeMet
 */

public class settingsApp extends AppCompatActivity {
    RecyclerView recyclerView;
    betaAgentAdapter mAdapter;
    ArrayList lists=new ArrayList();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
//        recyclerView=findViewById(R.id.betaRecy);
//        mAdapter=new betaAgentAdapter(lists,this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
//        recyclerView.setAdapter(mAdapter);
//        betaAgentApp betaAgentApp=new betaAgentApp("Lekki","Lwdkjefdihef", Color.parseColor("#FFD60072"),"NGN 550,000","Room and Parlour");
//        lists.add(betaAgentApp);
//        betaAgentApp=new betaAgentApp("Lekki","Lwdkjefdihef",Color.parseColor("#FF2EC97E"),"NGN 550,000","Room and Parlour");
//        lists.add(betaAgentApp);
//        betaAgentApp=new betaAgentApp("Lekki","Lwdkjefdihef",Color.parseColor("#FFB32828"),"NGN 550,000","Room and Parlour");
//        lists.add(betaAgentApp);
//        betaAgentApp=new betaAgentApp("Lekki","Lwdkjefdihef",Color.parseColor("#FF2EC97E"),"NGN 550,000","Room and Parlour");
//        lists.add(betaAgentApp);
//        betaAgentApp=new betaAgentApp("Lekki","Lwdkjefdihef",Color.parseColor("#FFD60072"),"NGN 550,000","Room and Parlour");
//        lists.add(betaAgentApp);

    }
}
