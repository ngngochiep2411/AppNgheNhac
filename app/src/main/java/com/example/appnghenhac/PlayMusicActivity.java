package com.example.appnghenhac;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.Random;

import static com.example.appnghenhac.SongsFragment.list;

public class PlayMusicActivity extends AppCompatActivity {

    TextView song_name,single_name,duration_player,duration_total;
    ImageView nextBtn,previousBtn,backBtn,shuffleBtn,replayBtn,cover_art;
    FloatingActionButton playPauseBtn;
    SeekBar seekBar;
    int position=-1;
    static ArrayList<MusicFiles> listSongs=new ArrayList<>();
    private MediaPlayer mediaPlayer;
    int fileMp3;
    private Handler handler=new Handler();
    int gocQuay=0;
    private Thread playThread,prevThread,nextThread;
    static  boolean playRandom=false ;
    static boolean rePlay=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        initView();
        getIntentData();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayMusicActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                    seekBar.setProgress(mCurrentPosition);
                    duration_player.setText(convertToMinute(mCurrentPosition));

                }
                handler.postDelayed(this,1000);
            }
        });
        PlayMusicActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gocQuay++;
                if(gocQuay==360){
                    gocQuay=0;
                }
                cover_art.setRotation(gocQuay);
                handler.postDelayed(this,50);
            }
        });

        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playRandom==false){
                    playRandom=true;
                    Toast.makeText(PlayMusicActivity.this,"Đã bật phát ngẫu nhiên",Toast.LENGTH_LONG).show();
                    shuffleBtn.setImageResource(R.drawable.ic_random_on);
                }else {
                    playRandom=false;
                    shuffleBtn.setImageResource(R.drawable.ic_random_off);
                    Toast.makeText(PlayMusicActivity.this,"Đã tắt phát ngẫu nhiên",Toast.LENGTH_LONG).show();
                }

            }
        });

        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rePlay==false){
                    rePlay=true;
                    replayBtn.setImageResource(R.drawable.ic_relay_on);
                    Toast.makeText(PlayMusicActivity.this,"Bật phát lại",Toast.LENGTH_SHORT).show();
                }else {
                    rePlay=false;
                    replayBtn.setImageResource(R.drawable.ic_replay);
                    Toast.makeText(PlayMusicActivity.this,"Tắt phát lại ",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        playThreadBtn();
        nextThreadBtn();
       prevThreadBtn();
    }

    private void playThreadBtn() {
        playThread=new Thread(){
            @Override
            public void run() {
                playPauseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });
                super.run();
            }
        };
        playThread.start();
    }

    private void playPauseBtnClicked() {
        if(mediaPlayer.isPlaying()){
            playPauseBtn.setImageResource(R.drawable.ic_play);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }else {
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
    }

    private void prevThreadBtn() {
        prevThread=new Thread(){
            @Override
            public void run() {
                previousBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prevBtnClicked();
                    }
                });
                super.run();
            }
        };
        prevThread.start();
    }

    private void prevBtnClicked() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();

            chuyenBaiHat(-1);
            if(playRandom && !rePlay){
                position=getRandom(listSongs.size()-1);
            }
            mediaPlayer=MediaPlayer.create(getApplicationContext(),fileMp3);

            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.start();
        }


    }

    private void nextThreadBtn() {
        nextThread=new Thread(){
            @Override
            public void run() {
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextBtnClicked();
                    }
                });
                super.run();
            }
        };
        nextThread.start();
    }

    private void nextBtnClicked() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            chuyenBaiHat(+1);
            mediaPlayer=MediaPlayer.create(getApplicationContext(),fileMp3);

            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.start();
        }
    }
    private void chuyenBaiHat(int i){
        position=position+i;
        if(position==listSongs.size()){
            position=0;
        }
        if(position==-1){
            position=listSongs.size()-1;
        }


        song_name.setText(listSongs.get(position).getNameSong());
        single_name.setText(listSongs.get(position).getSingleSong());
        cover_art.setImageResource(listSongs.get(position).getAvtBaiHat());
        fileMp3=listSongs.get(position).getFileMp3();
    }

    private int getRandom(int i) {
        Random random=new Random();
        return random.nextInt(i+1);
    }

    private String convertToMinute(int i) {
        int minutes=i/60;
        int seconds=i % 60;

        return checkNum10(minutes)+":"+checkNum10(seconds);
    }
    private String checkNum10(int i) {
        if(i<10){
            return "0"+i;
        }else {
            return ""+i;
        }
    }


    private void initView(){
        playPauseBtn=findViewById(R.id.img_play_pause);
        cover_art=findViewById(R.id.imgBaiHat);
        song_name=findViewById(R.id.songName);
        single_name=findViewById(R.id.singleName);
        duration_player=findViewById(R.id.durationPlayer);
        duration_total=findViewById(R.id.durationTotal);;
        seekBar=findViewById(R.id.seekbar);
        cover_art=findViewById(R.id.imgBaiHat);
        nextBtn=findViewById(R.id.imgNext);
        previousBtn=findViewById(R.id.img_prev);
        shuffleBtn=findViewById(R.id.img_play_random);
        replayBtn=findViewById(R.id.imgReplay);


    }
    private void getIntentData() {
        position=getIntent().getIntExtra("position",0);
        listSongs= SongsFragment.list;
        if(listSongs!=null){
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            song_name.setText(listSongs.get(position).getNameSong());
            single_name.setText(listSongs.get(position).getSingleSong());

            cover_art.setImageResource(listSongs.get(position).getAvtBaiHat());
            fileMp3=listSongs.get(position).getFileMp3();

        }
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer=MediaPlayer.create(getApplicationContext(),fileMp3);
        mediaPlayer.start();

        seekBar.setMax(mediaPlayer.getDuration()/1000);
        duration_total.setText(convertToMinute(mediaPlayer.getDuration()/1000));
    }

    public void ImageAnimation(){

    }


}