package com.example.anvanthinh.music;

import java.io.Serializable;

/**
 * Created by An Van Thinh on 4/4/2017.
 */

public class Music implements Serializable {
    private String name_song;
    private String name_singer;
    private byte[] avatar;
    private long duration;
    private String like;
    private String path;
    private long albumId;
    private int stt;

    public String getName_song() {
        return name_song;
    }

    public void setName_song(String name_song) {
        this.name_song = name_song;
    }

    public String getName_singer() {
        return name_singer;
    }

    public void setName_singer(String name_singer) {
        this.name_singer = name_singer;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long time) {
        this.duration = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }
}
