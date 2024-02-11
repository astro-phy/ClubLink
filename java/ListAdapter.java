package com.example.clublink;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>  {

    ArrayList<RequestsClass> list;
    ArrayList<RequestsClass> filteredList;
    SharedPreferences sharedPreferences;

    public ListAdapter(ArrayList<RequestsClass> list){
        this.list = list;
        this.filteredList = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.single_requests, parent, false);

        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference ref = database.getReference().child("requests").child(uid);
        String uid = filteredList.get(position).getUid();
        String nameText = filteredList.get(position).getName();
        int id = filteredList.get(position).getRole();
        String priorityText = filteredList.get(position).getPriority();
        String phaseText = filteredList.get(position).getPhase();
        String dateTime = filteredList.get(position).getDateTime();
        holder.name.setText(nameText);
        holder.datetime.setText(DateTime.getTime(dateTime));

        String userRole="";
        if (id==0){
            userRole="Member";
        } else if (id==1){
            userRole="Club FA";
        }
        else if (id==2){
            userRole="Society FA";
        }
        else if (id==3){
            userRole="ChairSAP";
        } else if (id==4){
            userRole="Dean Students";
        }

        holder.role.setText(userRole);
        holder.priority.setText(priorityText+", "+phaseText);
        String image = list.get(position).getImage();


        if (!image.equals("default")) {
            Picasso.get().load(image).into(holder.profile_image);
        }

        holder.title.setText(list.get(position).getTitle());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(holder.itemView.getContext(), ViewRequest.class);
                intent.putExtra("uid", uid);
                intent.putExtra("title", list.get(position).getTitle());
                holder.itemView.getContext().startActivity(intent);



            }
        });

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

//
//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                String query = constraint.toString().toLowerCase();
//                ArrayList<RequestsClass> filtered = new ArrayList<>();
//                if (query.isEmpty()) {
//                    filtered.addAll(list);
//                } else {
//                    for (RequestsClass item : list) {
//                        if (item.getName().toLowerCase().contains(query)) {
//                            filtered.add(item);
//                        }
//                    }
//                }
//                FilterResults results = new FilterResults();
//                results.values = filtered;
//                return results;
//            }
//
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                filteredList.clear();
//                filteredList.addAll((ArrayList<RequestsClass>) results.values);
//                notifyDataSetChanged();
//            }
//        };
//    }




    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title, priority, name, role, datetime;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            role = itemView.findViewById(R.id.role);

            title = itemView.findViewById(R.id.title);
            priority = itemView.findViewById(R.id.priority);

            profile_image = itemView.findViewById(R.id.profile_image_main);
            datetime = itemView.findViewById(R.id.dateTime);

        }
    }

    public void updateList(ArrayList<RequestsClass> filterList){
        list = filterList;
        notifyDataSetChanged();
    }


}