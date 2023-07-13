package com.example.retrofit_networking;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    Context context;
    ArrayList<Post_model> arrayList;
    MyAdapter(Context context,ArrayList<Post_model> arrayList){
        this.context=context;
        this.arrayList=arrayList;


    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView dp;
        TextView name,email,about,_id;
        LinearLayout ll;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            email=itemView.findViewById(R.id.email);
            about=itemView.findViewById(R.id.about);
            dp=itemView.findViewById(R.id.dp);
            _id=itemView.findViewById(R.id._id);
            ll=itemView.findViewById(R.id.ll);

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=  LayoutInflater.from(context).inflate(R.layout.text_row_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getUsername());
        holder.email.setText(arrayList.get(position).getEmailUser());
        holder.about.setText(arrayList.get(position).getAboutUser());
        holder._id.setText("_id : "+arrayList.get(position).get_id());
        int x=holder.getAdapterPosition();

        if(!arrayList.get(position).getImgurl().equals("noImage")){
            Picasso.get().load(arrayList.get(position).getImgurl()).fit().into(holder.dp);
        }
        else {
            holder.dp.setImageResource(R.drawable.baseline_person_24);
        }

        holder.ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.layout);
                TextView update=dialog.findViewById(R.id.update);
                TextView delete=dialog.findViewById(R.id.delete);
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                       Intent intent=new Intent(holder._id.getContext(),MainActivity.class);
                       intent.putExtra("name",arrayList.get(x).getUsername());
                       intent.putExtra("_id",arrayList.get(x).get_id());
                       intent.putExtra("about",arrayList.get(x).getAboutUser());
                       intent.putExtra("img_url",arrayList.get(x).getImgurl());
                       intent.putExtra("email",arrayList.get(x).getEmailUser());
                       intent.putExtra("pointer",x);
                       holder._id.getContext().startActivity(intent);
                       dialog.dismiss();

                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ApiSet apiSet=ApiController.getInstance().deleteData();
                        Post_model pm=new Post_model(arrayList.get(x).get_id());
                        Call<Post_model> call=apiSet.deleteData(pm);
                        call.enqueue(new Callback<Post_model>() {
                            @Override
                            public void onResponse(Call<Post_model> call, Response<Post_model> response) {
                               if(response.body().getMessage_code() ==301){
                                   Toast.makeText(holder._id.getContext(), arrayList.get(x).getUsername()+": deleted successfully", Toast.LENGTH_SHORT).show();
                               }
                               else {
                                   Toast.makeText(holder._id.getContext(), "failed to delete", Toast.LENGTH_SHORT).show();
                               }
                               Intent intent=new Intent(holder._id.getContext(),HomeScreen.class);
                               
                               MyAdapter.this.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Call<Post_model> call, Throwable t) {
                                Toast.makeText(holder._id.getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();

                    }

                });

                dialog.show();
                return false;
            }

        });
    }

//    public void WaytoUpdate(String name, String id, String about, String img_url, String email) {
//        Intent intent=new Intent(String.valueOf(context));
//        intent.putExtra("name",name);
//        intent.putExtra("email",email);
//        intent.putExtra("_id",id);
//        intent.putExtra("about",about);
//        intent.putExtra("img_url",img_url);
//
//    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
