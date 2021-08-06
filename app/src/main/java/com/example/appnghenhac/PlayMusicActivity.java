package com.example.appnghenhac;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.Random;

import static com.example.appnghenhac.ApplicationClass.ACTION_PLAY;
import static com.example.appnghenhac.ApplicationClass.ACTION_PREVIOUS;
import static com.example.appnghenhac.ApplicationClass.CHANNEL_ID_2;

public class PlayMusicActivity extends AppCompatActivity implements ActionPlaying, ServiceConnection {

    TextView song_name,single_name,duration_player,duration_total;
    ImageView nextBtn,previousBtn,backBtn,shuffleBtn,replayBtn,cover_art;
    FloatingActionButton playPauseBtn;
    SeekBar seekBar;
    int position=-1;
    static ArrayList<MusicFiles> listSongs=new ArrayList<>();
    //private MediaPlayer mediaPlayer;
    int fileMp3;
    private Handler handler=new Handler();
    int gocQuay=0;
    private Thread playThread,prevThread,nextThread;
    static  boolean playRandom=false ;
    static boolean rePlay=false;
    MusicService musicService;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        getSupportActionBar().hide();
        initView();
        getIntentData();
        //mediaSessionCompat=new MediaSessionCompat(getBaseContext(),"My Audio");


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(musicService!=null && fromUser){
                    musicService.seekTo(progress*1000);
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
                if(musicService!=null){
                    int mCurrentPosition=musicService.getCurrentPosition()/1000;
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
        Intent intent=new Intent(this,MusicService.class);
        bindService(intent,this,BIND_AUTO_CREATE);
        super.onResume();
        playThreadBtn();
        nextThreadBtn();
       prevThreadBtn();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
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

    public void playPauseBtnClicked() {
        if(musicService.isPlaying()){
            playPauseBtn.setImageResource(R.drawable.ic_play);
            showNotification(R.drawable.ic_play);
            musicService.pause();
            seekBar.setMax(musicService.getDuration()/1000);
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicService!=null){
                        int mCurrentPosition=musicService.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }else {
            showNotification(R.drawable.ic_pause);
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            musicService.start();
            seekBar.setMax(musicService.getDuration()/1000);
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicService!=null){
                        int mCurrentPosition=musicService.getCurrentPosition()/1000;
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

    public void prevBtnClicked() {
        if(musicService.isPlaying()){
            musicService.stop();
            musicService.release();

            chuyenBaiHat(-1);

            //mediaPlayer=MediaPlayer.create(getApplicationContext(),fileMp3);
            musicService.createMediaPlayer(position);

            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicService!=null){
                        int mCurrentPosition=musicService.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            musicService.start();
        }else {
            musicService.stop();
            musicService.release();
            chuyenBaiHat(-1);
            musicService.createMediaPlayer(position);
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicService!=null){
                        int mCurrentPosition=musicService.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            musicService.OnCompleted();
            showNotification(R.drawable.ic_pause);
            playPauseBtn.setImageResource(R.drawable.ic_play);
            musicService.start();
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

    public void nextBtnClicked() {
        if(musicService.isPlaying()){
            musicService.stop();
            musicService.release();
            chuyenBaiHat(+1);
//            mediaPlayer=MediaPlayer.create(getApplicationContext(),fileMp3);
            musicService.createMediaPlayer(position);

            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(musicService!=null){
                        int mCurrentPosition=musicService.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            musicService.OnCompleted();
            showNotification(R.drawable.ic_pause);
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            musicService.start();
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
        position=getIntent().getIntExtra("position",-1);
        listSongs= SongsFragment.list;
        if(listSongs!=null){
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            song_name.setText(listSongs.get(position).getNameSong());
            single_name.setText(listSongs.get(position).getSingleSong());

            cover_art.setImageResource(listSongs.get(position).getAvtBaiHat());
            fileMp3=listSongs.get(position).getFileMp3();

        }
        if(musicService!=null){
            musicService.stop();
            musicService.release();
        }
        showNotification(R.drawable.ic_pause);
        Intent intent=new Intent(this,MusicService.class);
        intent.putExtra("servicePosition",position);
        startService(intent);
//        musicService=MediaPlayer.create(getApplicationContext(),fileMp3);
//        musicService.createMediaPlayer(position);
//        musicService.start();



    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder myBinder= (MusicService.MyBinder) service;
        musicService=myBinder.getService();
        Toast.makeText(this,"Connected"+musicService,Toast.LENGTH_SHORT).show();
        seekBar.setMax(musicService.getDuration()/1000);
        song_name.setText(listSongs.get(position).getNameSong());
        single_name.setText(listSongs.get(position).getSingleSong());
        cover_art.setImageResource(listSongs.get(position).getAvtBaiHat());
        fileMp3=listSongs.get(position).getFileMp3();

        seekBar.setMax(musicService.getDuration()/1000);
        duration_total.setText(convertToMinute(musicService.getDuration()/1000));
        musicService.OnCompleted();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService=null;
    }

    void showNotification(int playPauseBtn){


        Intent intent=new Intent(this,PlayMusicActivity.class);
        PendingIntent contentIntent=PendingIntent.getActivity(this,0,intent,0);

        Intent prevIntent=new Intent(this, NotificationReceiver.class).setAction(ACTION_PREVIOUS);
        PendingIntent prevPending =PendingIntent.getBroadcast(this,0,prevIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent=new Intent(this, NotificationReceiver.class).setAction(ACTION_PLAY);
        PendingIntent pausePending =PendingIntent.getBroadcast(this,0,pauseIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent=new Intent(this, NotificationReceiver.class).setAction(ACTION_PREVIOUS);
        PendingIntent nextPending =PendingIntent.getBroadcast(this,0,nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        MediaSessionCompat mediaSessionCompat=new MediaSessionCompat(this,"aaa");
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),listSongs.get(position).getAvtBaiHat());
        Notification notification =new NotificationCompat.Builder(this,CHANNEL_ID_2)
                .setSmallIcon(playPauseBtn)
                .setContentTitle(listSongs.get(position).getNameSong())
                .setContentText(listSongs.get(position).getSingleSong())
                .addAction(R.drawable.ic_previous,"Previous",prevPending)
                .addAction(playPauseBtn,"Pause",pausePending)
                .addAction(R.drawable.ic_next,"Next",nextPending)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(mediaSessionCompat.getSessionToken()))
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLargeIcon(bitmap)
                .build();

        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,notification);

    }
}