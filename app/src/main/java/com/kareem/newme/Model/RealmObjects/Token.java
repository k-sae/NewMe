package com.kareem.newme.Model.RealmObjects;

import io.realm.RealmObject;

/**
 * Created by kareem on 8/5/17.
 */

public class Token extends RealmObject{
    private String id;

    public Token(String id) {
        this.id = id;
    }

    public Token() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
