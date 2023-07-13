package com.example.retrofit_networking;

import com.google.gson.annotations.SerializedName;

import kotlin.jvm.internal.SerializedIr;

public class Post_model {
    @SerializedName("Name")
    private String username;
    @SerializedName("Email")
    private String emailUser;
    @SerializedName("About")
    private String aboutUser;
    @SerializedName("Img_Url")
    private String imgurl;
    @SerializedName("date_time")
    private String date_time;


    @SerializedName("_id")
    private String _id;
    @SerializedName("message_code")
    private  int message_code;

    public Post_model(String username, String email, String about, String imgurl, String date_time) {
        this.username = username;
        this.emailUser = email;
        this.aboutUser = about;
        this.imgurl = imgurl;
        this.date_time = date_time;
    }

    public Post_model(String username, String emailUser, String aboutUser, String imgurl, String date_time, String _id) {
        this.username = username;
        this.emailUser = emailUser;
        this.aboutUser = aboutUser;
        this.imgurl = imgurl;
        this.date_time = date_time;
        this._id = _id;

    }

    public Post_model(String _id) {
        this._id = _id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmail(String email) {
        this.emailUser = email;
    }

    public String getAboutUser() {
        return aboutUser;
    }

    public void setAbout(String about) {
        this.aboutUser = about;
    }

    public Post_model() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public int getMessage_code() {
        return message_code;
    }

    public void setMessage_code(int message_code) {
        this.message_code = message_code;
    }
}
