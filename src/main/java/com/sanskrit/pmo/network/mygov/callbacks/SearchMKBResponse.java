package com.sanskrit.pmo.network.mygov.callbacks;

import com.sanskrit.pmo.network.mygov.models.gallery.ImageGalleryModel;
import com.sanskrit.pmo.network.server.models.MkbAudio;

import java.util.List;

/**
 * Created by manoj on 28/3/18.
 */

public class SearchMKBResponse {

    public String Items;

    public List<MkbAudio> content;

    public String getItems() {
        return this.Items;
    }

    public List<MkbAudio> getContent() {
        return this.content;
    }
}
