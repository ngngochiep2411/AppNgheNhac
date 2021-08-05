package com.example.appnghenhac;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class SongsFragment extends Fragment  {

    static ArrayList<MusicFiles> list;
    SongAdapter adapter;
    RecyclerView rcvSong;


    public SongsFragment() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_songs, container, false);
        rcvSong=view.findViewById(R.id.rcv_song);
        addMusic();
        adapter=new SongAdapter(list,getContext());
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rcvSong.setAdapter(adapter);
        rcvSong.setLayoutManager(layoutManager);
        rcvSong.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
        return view;
    }
    private void addMusic(){
        list=new ArrayList<>();
        list.add(new MusicFiles(R.drawable.laxaliacanh,R.raw.bolo1nguoiremix,"Lá xa lìa cành","Lê Bảo Bình"));
        list.add(new MusicFiles(R.drawable.niuduyen,R.raw.niuduyen,"Níu duyên","Lê Bảo Bình"));
        list.add(new MusicFiles(R.drawable.saigondaulongqua,R.raw.saigondaulongqua,"Sài gòn đau lòng quá","Hứa Kim Tuyền"));
        list.add(new MusicFiles(R.drawable.thichthiden,R.raw.thichthidenremix,"Thích thì đến","Lê Bảo Bình"));
        list.add(new MusicFiles(R.drawable.tractro,R.raw.tractro,"Trắc Trở","X2X"));
        list.add(new MusicFiles(R.drawable.trentinhban,R.raw.trentinhbanduoitinhyeu,"Trên tình bạn dưới tình yêu","MIN"));
        list.add(new MusicFiles(R.drawable.namdoibantay,R.raw.namdoibantay,"Nắm đôi bàn tay","Kay Trần"));
        list.add(new MusicFiles(R.drawable.minhbuocquadoinhau,R.raw.minhbuocquadoinhauremix,"Mình bước qua đời nhau","Lê Bảo Bình"));
        list.add(new MusicFiles(R.drawable.bolo1nguoi,R.raw.bolo1nguoiremix,"Bỏ lỡ 1 người","Lê Bảo Bình"));
        list.add(new MusicFiles(R.drawable.binz,R.raw.bigcityboi,"Big City Boy","BinZ"));
    }




}