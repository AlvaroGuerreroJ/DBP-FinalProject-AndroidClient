package com.example.flaskrer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void register(View view) throws JSONException {
        String username = ((EditText) findViewById(R.id.register_activity_username)).getText().toString();
        String fullName = ((EditText) findViewById(R.id.register_activity_full_name)).getText().toString();
        String password = ((EditText) findViewById(R.id.register_activity_password)).getText().toString();
        String rePassword = ((EditText) findViewById(R.id.register_activity_repassword)).getText().toString();

        String[] userInputs = {username, fullName, password, rePassword};
        for (String s : userInputs) {
            if (s.length() == 0) {
                MainActivity.showMessage(this, "You must fill all fields");
                return;
            }
        }

        if (!password.equals(rePassword)) {
            MainActivity.showMessage(
                    this,
                    "Password confirmation field does not match password"
            );
            return;
        }


        JSONObject content = new JSONObject();
        content.put("username", username);
        content.put("full_name", fullName);
        content.put("password", password);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                MainActivity.buildUrl("api", "register"),
                content,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            MainActivity.showMessage(getApplicationContext(), response.getString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MainActivity.showMessage(getApplicationContext(), "Couldn't register");
                    }
                }
        );

        MainActivity.queue.add(request);
    }
}
