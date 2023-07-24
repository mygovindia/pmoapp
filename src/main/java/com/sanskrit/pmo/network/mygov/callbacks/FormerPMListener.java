package com.sanskrit.pmo.network.mygov.callbacks;

import com.sanskrit.pmo.network.mygov.models.FormerPM;

import java.util.List;

public interface FormerPMListener {
    void failure();

    void success(List<FormerPM> list);
}
