package com.example.flaskrer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;

public class CreatePostActivity extends AppCompatActivity {
    private static final int ACTIVITY_CHOOSE_FILE = 1;

    String pathToImage;
    Uri image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
    }

    public void selectImage(View view) {
        Intent chooseFile;
        Intent intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("image/*");
        intent = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || requestCode != ACTIVITY_CHOOSE_FILE) {
            return;
        }

        image = data.getData();
        ((TextView) findViewById(R.id.create_post_activity_path_image)).setText(image.getPath());
    }

    public void createPost(View view) {
        if (image == null) {
            MainActivity.showMessage(this, "No image selected");
            return;
        }

        String title = ((EditText) findViewById(R.id.create_post_activity_title)).getText().toString();
        String alternativeText = ((EditText) findViewById(R.id.create_post_activity_alternative_text)).getText().toString();
        String description = ((EditText) findViewById(R.id.create_post_activity_description)).getText().toString();

        String[] userInputs = {title, alternativeText};
        for (String s : userInputs) {
            if (s.length() == 0) {
                MainActivity.showMessage(this, "You must fill all fields");
                return;
            }
        }

        // Convert file to a Base64 string
        String fileBase64ed;
        try {
            fileBase64ed = convertToBase64(image);
        } catch (Exception e) {
            e.printStackTrace();
            MainActivity.showMessage(this, "Couldn't encode picture");
            return;
        }

        JSONObject content = new JSONObject();
        try {
        content.put("title", title);
        content.put("alternative_text", alternativeText);
        if (description.length() != 0) {
            content.put("description", description);
        }
        content.put("picture", fileBase64ed);
        } catch (JSONException e) {
            e.printStackTrace();
            MainActivity.showMessage(this, "Couldn't create post");
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                MainActivity.buildUrl("api", "post"),
                content,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MainActivity.showMessage(getApplicationContext(), "Post created");
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MainActivity.showMessage(getApplicationContext(), "Couldn't create post");
                    }
                }
        );
        MainActivity.queue.add(request);
    }

    public String convertToBase64(Uri uri) throws IOException {
        // InputStream inputStream = new FileInputStream("/storage/emulated/0/Download/dailyPicShare.jpg");
        InputStream inputStream = getContentResolver().openInputStream(uri);
        byte[] buffer = new byte[16384];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream output64 = new Base64OutputStream(output, 0);
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        output64.close();

        return output.toString();
    }
}
