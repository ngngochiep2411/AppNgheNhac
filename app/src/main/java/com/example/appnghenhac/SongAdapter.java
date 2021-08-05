package com.example.appnghenhac;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    ArrayList<MusicFiles> list;
    Context context;


    public SongAdapter(ArrayList<MusicFiles> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_song, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SongAdapter.ViewHolder holder, int position) {
        MusicFiles musicFiles=list.get(position);
        holder.imgAvtSong.setImageResource(musicFiles.getAvtBaiHat());
        holder.txtNameSong.setText(musicFiles.getNameSong());
        holder.txtSingleSong.setText(musicFiles.getSingleSong());
        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,PlayMusicActivity.class);
                intent.putExtra("position",position);
                Bundle bundle=new Bundle();
                bundle.putSerializable("object_bai_hat",musicFiles);
                intent.putExtras(bundle);
                context.startActivity(intent);
                Toast.makeText(context,""+position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvtSong;
        TextView txtNameSong,txtSingleSong;
        LinearLayout layout_item;
        public ViewHolder(View itemView) {
            super(itemView);
            imgAvtSong=itemView.findViewById(R.id.imgAvtSong);
            txtNameSong=itemView.findViewById(R.id.txtName);
            txtSingleSong=itemView.findViewById(R.id.txtCasi);
            layout_item=itemView.findViewById(R.id.layout_item);
        }
    }

}
