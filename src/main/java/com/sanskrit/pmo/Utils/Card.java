package com.sanskrit.pmo.utils;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.BindingValues;

public class Card {
    @SerializedName("binding_values")
    public final BindingValues bindingValues;
    @SerializedName("name")
    public final String name;

    public Card(BindingValues bindingValues, String name) {
        this.bindingValues = bindingValues;
        this.name = name;
    }
}
