package com.example.flaskrer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final String serverIpAddress = "192.168.1.21";
    static final String serverPort = "8080";

    static RequestQueue queue;

    static User loggedInUser;

    Button logInButton;
    Button registerButton;
    Button logOutButton;
    Button viewPostsButton;
    Button createPostButton;

    final ArrayList<Button> allButtons = new ArrayList<>();
    final ArrayList<Button> buttonsVisibleLogged = new ArrayList<>();
    final ArrayList<Button> buttonsVisibleNotLogged = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // From: https://stackoverflow.com/questions/16680701/using-cookies-with-android-volley-library
        CookieHandler.setDefault(new CookieManager());

        queue = Volley.newRequestQueue(this);

        loggedInUser = null;

        logInButton = findViewById(R.id.main_activity_button_log_in);
        registerButton = findViewById(R.id.main_activity_button_register);
        logOutButton = findViewById(R.id.main_activity_button_log_out);
        viewPostsButton = findViewById(R.id.main_activity_button_view_posts);
        createPostButton = findViewById(R.id.main_activity_button_create_post);

        allButtons.add(logInButton);
        allButtons.add(registerButton);
        allButtons.add(logOutButton);
        allButtons.add(viewPostsButton);
        allButtons.add(createPostButton);

        buttonsVisibleLogged.add(logOutButton);
        buttonsVisibleLogged.add(viewPostsButton);
        buttonsVisibleLogged.add(createPostButton);

        buttonsVisibleNotLogged.add(logInButton);
        buttonsVisibleNotLogged.add(registerButton);
        buttonsVisibleNotLogged.add(viewPostsButton);
    }

    @Override
    protected void onResume() {
        super.onResume();

        showAndHideButtons();
    }

    private void showAndHideButtons() {
        for (Button b : allButtons) {
            b.setVisibility(View.GONE);
        }

        boolean isUserLoggedIn = loggedInUser != null;
        ArrayList<Button> toIterate = isUserLoggedIn ? buttonsVisibleLogged : buttonsVisibleNotLogged;

        for (Button b : toIterate) {
            b.setVisibility(View.VISIBLE);
        }
    }

    public void goLogInActivity(View view) {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    public void goRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void logOut(View view) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                buildUrl("api", "log_out"),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            showMessage(getApplicationContext(), response.getString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loggedInUser = null;
                        showAndHideButtons();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showMessage(getApplicationContext(), "Couldn't log out");
                    }
                }
        );

        queue.add(request);
    }

    static public String buildUrl(String ...parts) {
        StringBuilder ret = new StringBuilder("http://" + serverIpAddress + ":" + serverPort);

        for (String s : parts) {
            ret.append("/");
            ret.append(s);
        }

        return ret.toString();
    }

    static public void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    public void goCreatePostActivity(View view) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        startActivity(intent);
    }

    public void goViewPostsActivity(View view) {
        Intent intent = new Intent(this, ViewPostsActivity.class);
        startActivity(intent);
    }
}
