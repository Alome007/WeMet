package com.urbler.wemet;
 /*Created by Alome on 2/16/2019.
 WeMet
 */
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class modalAppPri extends BottomSheetDialogFragment {
    EditText title,value;
    View view;
    ImageButton add;
    ProgressDialog progressDialog;
    @SuppressLint({"RestrictedApi", "InflateParams"})
    public void setupDialog(final Dialog dialog, final int style) {
        super.setupDialog(dialog, style);
       view= LayoutInflater.from(getContext()).inflate(R.layout.add_pri, null);
        dialog.setContentView(view);
        initViews();
        setCancelable(true);
        ((View) view.getParent()).getLayoutParams();
        BottomSheetBehavior.from(dialog.findViewById(R.id.design_bottom_sheet)).setPeekHeight(10000);
        ((View) view.getParent()).setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Start UploadTofirebase */
                if (validate()){
                     progressDialog=new ProgressDialog(getContext());
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("Uploading...");
                    progressDialog.show();
                String tit=title.getText().toString();
                String val=value.getText().toString();
                UploadToFirebase(tit,val);
                }}
        });
    }

    private void UploadToFirebase(String title,String value) {
        FirebaseUser users= FirebaseAuth.getInstance().getCurrentUser();
        String userId= null;
        if (users != null) {
            userId = users.getUid();
        }
        if (userId != null) {
            DatabaseReference mData= FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("profile").child("private").push();
            priData priData=new priData(title,value,userId);
            mData.setValue(priData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),"Values Added successfully !",Toast.LENGTH_LONG).show();
                    getDialog().dismiss();
                }
            });
        }

    }
    private void initViews() {
        title=view.findViewById(R.id.titleAdd);
        value=view.findViewById(R.id.valueadd);
        add=view.findViewById(R.id.add);
    }

    private boolean validate() {
        boolean canMove=false;
        String name = title.getText().toString();
        String add = value.getText().toString();
        if (name.isEmpty())
            title.setError("Title Required");
        else
        if (add.isEmpty())
            value.setError("Value Required");
        else
            canMove=true;
        return canMove;
    }
}

