package com.example.nirvoy;


import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class ContactAdapter extends FirebaseRecyclerAdapter<ContactData, ContactAdapter.PastViewHolder> {

    private Context context;
    FirebaseUser user;
    String uid;

    public ContactAdapter(@NonNull FirebaseRecyclerOptions<ContactData> options,Context context) {
        super(options);
        this.context = context;
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
    }


    @Override
    protected void onBindViewHolder(@NonNull PastViewHolder holder, final int i, @NonNull final ContactData contactData) {



        holder.name.setText(contactData.getName());
        holder.number.setText(contactData.getNumber());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure, You want to delete this contact ?      ");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                FirebaseDatabase.getInstance().getReference()
                                        .child("ContactDatas")
                                        .child(uid)
                                        .child(getRef(i).getKey())
                                        .setValue(null)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("delete", "Not deleted");
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialog = DialogPlus.newDialog(context)
                        .setGravity(Gravity.CENTER)
                        .setMargin(50,0,50,0)
                        .setContentHolder(new ViewHolder(R.layout.edit_contact))
                        .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();

                View holderView = (LinearLayout)dialog.getHolderView();

                final EditText name = holderView.findViewById(R.id.name);
                final EditText number = holderView.findViewById(R.id.number);


                name.setText(contactData.getName());
                number.setText(contactData.getNumber());

                Button update = holderView.findViewById(R.id.update);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String,Object> map = new HashMap<>();
                        map.put("name",name.getText().toString().trim());
                        map.put("number",number.getText().toString().trim());

                        FirebaseDatabase.getInstance().getReference()
                                .child("ContactDatas")
                                .child(uid)
                                .child(getRef(i).getKey())
                                .updateChildren(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialog.dismiss();
                                    }
                                });
                    }
                });

                dialog.show();
            }
        });


    }

    @NonNull
    @Override
    public PastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_details, parent, false);
        return new PastViewHolder(view);
    }

    class PastViewHolder extends RecyclerView.ViewHolder{

        TextView name,number;
        ImageView edit,delete;

        public PastViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);


        }
    }
}

