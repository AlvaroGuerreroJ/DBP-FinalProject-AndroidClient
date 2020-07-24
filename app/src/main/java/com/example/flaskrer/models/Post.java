package com.example.flaskrer.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Post {
    private String username;
    private String createdOn;
    private String description;
    private String title;
    private int id;

    public Post(String username, String createdOn, String description, String title, int id) {
        this.username = username;
        this.createdOn = createdOn;
        this.description = description;
        this.title = title;
        this.id = id;
    }

    public Post(JSONObject jo) throws JSONException {
        this.username = jo.getString("username");
        this.createdOn = jo.getString("created");
        this.description = jo.getString("description");
        this.title = jo.getString("title");
        this.id = jo.getInt("id");
    }

    public String getUsername() {
        return username;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }
}
