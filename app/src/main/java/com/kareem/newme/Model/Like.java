
package com.kareem.newme.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Like {

    public Like(Integer userId) {
        this.userId = userId;
    }
    public Like() {
    }
    @SerializedName("userId")
    @Expose
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
