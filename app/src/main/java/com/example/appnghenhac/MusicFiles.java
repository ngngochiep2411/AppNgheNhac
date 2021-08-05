package com.example.appnghenhac;

import java.io.Serializable;

public class MusicFiles implements Serializable {

   private int avtBaiHat;
   private int fileMp3;
   private String nameSong;
   private String singleSong;

    public MusicFiles(int avtBaiHat, int fileMp3, String nameSong, String singleSong) {
        this.avtBaiHat = avtBaiHat;
        this.fileMp3 = fileMp3;
        this.nameSong = nameSong;
        this.singleSong = singleSong;
    }

    public int getAvtBaiHat() {
        return avtBaiHat;
    }

    public void setAvtBaiHat(int avtBaiHat) {
        this.avtBaiHat = avtBaiHat;
    }

    public int getFileMp3() {
        return fileMp3;
    }

    public void setFileMp3(int fileMp3) {
        this.fileMp3 = fileMp3;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getSingleSong() {
        return singleSong;
    }

    public void setSingleSong(String singleSong) {
        this.singleSong = singleSong;
    }
}
