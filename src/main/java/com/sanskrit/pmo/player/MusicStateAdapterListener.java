package com.sanskrit.pmo.player;

public interface MusicStateAdapterListener {
    void onMetaChanged();

    void onMusicBufferingEnded();

    void onMusicBufferingStarted();

    void onMusicStarted();

    void onMusicStreaming();

    void onStateChanged();
}
