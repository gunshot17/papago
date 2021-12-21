package com.il.papago.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.il.papago.R;
import com.il.papago.data.DatabaseHandler;
import com.il.papago.model.Post;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    Post post;
    ArrayList<Post> postArrayList;

    public RecyclerViewAdapter(Context context, ArrayList<Post> postArrayList) {
        this.context =context;
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.words,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position){

        post = postArrayList.get(position);
        String origintext =post.getOrigintext();
        String Text = post.getText();

        holder.txtTranstext.setText(origintext);
        holder.txtTranlated.setText(Text);

    }

    @Override
    public int getItemCount(){
        return postArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        CardView cardView;
        TextView txtTranstext;
        TextView txtTranlated;
        ImageButton deleteBtn;

        DatabaseHandler dh;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            txtTranstext = itemView.findViewById(R.id.txtTranstext);
            txtTranlated = itemView.findViewById(R.id.txtTranlated);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);


            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setMessage("삭제하실건가요?");
                    alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dh = new DatabaseHandler(context);
                            postArrayList = dh.getAllPost();
                            post = postArrayList.get(getAdapterPosition());
                            dh.deletePost(post);

                            postArrayList = dh.getAllPost();
                            notifyDataSetChanged();

                        }
                    });
                    alertDialog.setNegativeButton("아니오",null);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            });

        }
    }




}
