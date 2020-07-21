package com.example.flaskrer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    User loggedInUser;

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
}
