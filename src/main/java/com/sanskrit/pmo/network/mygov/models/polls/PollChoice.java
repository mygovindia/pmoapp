package com.sanskrit.pmo.network.mygov.models.polls;

import org.parceler.Parcel;

@Parcel
public class PollChoice implements Comparable {
    public String chid;
    public String chtext;
    public String chvotes;
    public String weight;

    public String getChid() {
        return this.chid;
    }

    public String getChvotes() {
        return this.chvotes;
    }

    public String getChtext() {
        return this.chtext;
    }

    public String getWeight() {
        return this.weight;
    }

    public int compareTo(Object another) {
        PollChoice other = (PollChoice) another;
        if (Integer.parseInt(other.getChvotes()) == Integer.parseInt(getChvotes())) {
            return 0;
        }
        if (Integer.parseInt(other.getChvotes()) < Integer.parseInt(getChvotes())) {
            return -1;
        }
        return 1;
    }
}
