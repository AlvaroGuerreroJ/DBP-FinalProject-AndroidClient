package com.example.flaskrer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.flaskrer.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class Post implements Parcelable {
    private String username;
    private String createdOn;
    private String description;
    private String title;
    private int id;

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                @Override
                public Post createFromParcel(Parcel parcel) {
                    return new Post(parcel);
                }

                @Override
                public Post[] newArray(int i) {
                    return new Post[i];
                }
            };

    public Post(JSONObject jo) throws JSONException {
        this.username = jo.getString("username");
        this.createdOn = jo.getString("created");
        this.description = jo.getString("description");
        this.title = jo.getString("title");
        this.id = jo.getInt("id");
    }

    private Post(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(createdOn);
        parcel.writeString(description);
        parcel.writeString(title);
        parcel.writeInt(id);
    }

    private void readFromParcel(Parcel in) {
        username = in.readString();
        createdOn = in.readString();
        description = in.readString();
        title = in.readString();
        id = in.readInt();
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

    public String getImageURL() {
        return MainActivity.buildUrl(
                "uploads",
                "id",
                Integer.toString(this.id)
        );
    }

    public String generateAbout() {
        return "Posted on " + createdOn + " by " + username;
    }
}
