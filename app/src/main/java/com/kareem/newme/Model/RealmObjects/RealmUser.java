package com.kareem.newme.Model.RealmObjects;

import io.realm.RealmObject;

/**
 * Created by kareem on 7/30/17.
 */

public class RealmUser extends RealmObject {
    private String id;
    private String name;
    private String userType;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
