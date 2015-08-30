package com.pundroid.bestmoviesapp.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by pumba30 on 30.08.2015.
 */
public class Session implements Serializable {

    @SerializedName("success")
    private boolean success;

    @SerializedName("session_id")
    private String session_id;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}
