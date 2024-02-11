package com.example.clublink;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ApprovalListAdapter extends RecyclerView.Adapter<ApprovalListAdapter.ViewHolder> {

    private final ArrayList<ApprovalClass> list;
    String post_uuid;
    SharedPreferences sharedPreferences;

    public ApprovalListAdapter(ArrayList<ApprovalClass> list, String post_uuid){
        this.list = list;
        this.post_uuid = post_uuid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.single_comment, parent, false);

        sharedPreferences = parent.getContext().getSharedPreferences("details", Context.MODE_PRIVATE);


        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String comment, role, author_name, datetime, image;
        String approved;




        comment = list.get(position).getContent();
        author_name = list.get(position).getName();
        approved = list.get(position).getApproved();
        datetime = list.get(position).getDatetime();
        comment = list.get(position).getContent();
        role = list.get(position).getRole();
        image = list.get(position).getImage();


        if (!image.equals("default")) {
            Picasso.get().load(image).into(holder.profile_image);
        }


        holder.comment.setText(comment);
        holder.name_role.setText(author_name+", "+role);
        holder.comment.setText(comment);
        holder.datetime.setText(DateTime.getTime(datetime));
        holder.approved.setText(approved.toUpperCase());




        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!image.equals("default")) {

                    Dialog builder = new Dialog(holder.itemView.getContext());
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    builder.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                        }
                    });

                    ImageView imageView = new ImageView(holder.itemView.getContext());
                    Picasso.get().load(image).into(imageView);

                    builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    builder.show();

                }


            }
        });




    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView comment, name_role, approved, datetime;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            comment = itemView.findViewById(R.id.item_text);
            name_role = itemView.findViewById(R.id.item_details);
            approved = itemView.findViewById(R.id.approved);
            datetime = itemView.findViewById(R.id.date_time);

            profile_image = itemView.findViewById(R.id.profile_image_comment);

        }
    }

}
