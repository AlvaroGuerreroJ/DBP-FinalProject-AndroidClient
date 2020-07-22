package com.example.flaskrer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    public void logIn(View view) throws JSONException {
        String username = ((EditText) findViewById(R.id.log_in_activity_username)).getText().toString();
        String password = ((EditText) findViewById(R.id.log_in_activity_password)).getText().toString();

        if (username.length() == 0 || password.length() == 0) {
            MainActivity.showMessage(this, "You must fill all fields");
            return;
        }

        JSONObject content = new JSONObject();
        content.put("username", username);
        content.put("password", password);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                MainActivity.buildUrl("api", "login"),
                content,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MainActivity.showMessage(getApplicationContext(), "You have logged in");
                        try {
                            MainActivity.loggedInUser = new User(
                                    response.getString("username"),
                                    response.getString("full_name")
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MainActivity.showMessage(getApplicationContext(), "Couldn't log in");
                    }
                }
        );

        MainActivity.queue.add(request);
    }
}
