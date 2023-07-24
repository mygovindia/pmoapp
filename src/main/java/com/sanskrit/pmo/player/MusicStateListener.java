package com.sanskrit.pmo.player;

public interface MusicStateListener {
    void onMetaChanged();

    void onMusicBufferingEnded();

    void onMusicBufferingStarted();

    void onMusicStarted();

    void onMusicStreaming();

    void onStateChanged();
}

