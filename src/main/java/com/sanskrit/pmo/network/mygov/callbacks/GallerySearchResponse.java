package com.sanskrit.pmo.network.mygov.callbacks;

import com.sanskrit.pmo.network.mygov.models.gallery.ImageGalleryModel;

import java.util.List;

/**
 * Created by manoj on 28/3/18.
 */

public class GallerySearchResponse {
    public String Items;

    public List<ImageGalleryModel> content;

    public String getItems() {
        return this.Items;
    }

    public List<ImageGalleryModel> getContent() {
        return this.content;
    }
}
