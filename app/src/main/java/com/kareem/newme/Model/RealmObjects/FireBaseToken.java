package com.kareem.newme.Model.RealmObjects;

import io.realm.RealmObject;

/**
 * Created by kareem on 8/5/17.
 */

public class FireBaseToken extends RealmObject{
    private String id;

    public FireBaseToken(String id) {
        this.id = id;
    }

    public FireBaseToken() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
