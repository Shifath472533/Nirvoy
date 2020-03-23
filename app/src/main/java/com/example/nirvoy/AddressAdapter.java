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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class AddressAdapter extends FirebaseRecyclerAdapter<CurrentLocation, AddressAdapter.PastViewHolder> {

    private Context context;
    FirebaseUser user;
    String uid;

    public AddressAdapter(@NonNull FirebaseRecyclerOptions<CurrentLocation> options, Context context) {
        super(options);
        this.context = context;
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
    }


    @Override
    protected void onBindViewHolder(@NonNull PastViewHolder holder, final int i, @NonNull final CurrentLocation currentLocation) {
        holder.address.setText(currentLocation.getAddress());

    }

    @NonNull
    @Override
    public PastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_details, parent, false);
        return new PastViewHolder(view);
    }

    class PastViewHolder extends RecyclerView.ViewHolder{

        TextView address;

        public PastViewHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address);

        }
    }
}

