package com.example.flaskrer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.flaskrer.adapters.CommentsAdapter;
import com.example.flaskrer.models.Post;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ViewPostActivity extends AppCompatActivity {
    Post post;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        post = (Post) getIntent().getExtras().get("post");
        if (post == null) {
            MainActivity.showMessage(this, "Couldn't load post");
            finish();
        }

        // Pull to refresh
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.view_post_activity_pull_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getComments();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        // Hide add comment if not logged in
        if (MainActivity.loggedInUser == null) {
            findViewById(R.id.view_post_activity_create_comment_linear_layout).setVisibility(View.GONE);
        }

        ImageView imageView = findViewById(R.id.view_post_activity_image);
        Picasso.get().load(post.getImageURL()).into(imageView);

        imageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String[] urls = {post.getImageURL()};
                        new StfalconImageViewer.Builder<>(view.getContext(), urls, new ImageLoader<String>() {
                            @Override
                            public void loadImage(ImageView imageView, String image) {
                                Picasso.get().load(image).into(imageView);
                            }
                        }).show();
                    }
                }
        );

        ((TextView) findViewById(R.id.view_post_activity_title)).setText(post.getTitle());
        ((TextView) findViewById(R.id.view_post_activity_about)).setText(post.generateAbout());

        String description = post.getDescription();
        TextView descriptionTextView = findViewById(R.id.view_post_activity_description);
        if (description.length() == 0) {
            descriptionTextView.setVisibility(View.GONE);
        } else {
            descriptionTextView.setText(description);
        }

        recyclerView = findViewById(R.id.view_post_activity_recycler_view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getComments();
    }

    private void getComments() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                MainActivity.buildUrl("api", "comment", Integer.toString(post.getId())),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        CommentsAdapter commentsAdapter = new CommentsAdapter(response);
                        Log.w("Log", Integer.toString(response.length()));
                        recyclerView.setAdapter(commentsAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.addItemDecoration(
                                new DividerItemDecoration(
                                        getApplicationContext(),
                                        DividerItemDecoration.VERTICAL
                                )
                        );
                        recyclerView.setNestedScrollingEnabled(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MainActivity.showMessage(getApplicationContext(), "Couldn't get comments");
                    }
                }
        );

        MainActivity.queue.add(request);
    }

    public void submitComment(View view) {
        final EditText commentEditText = findViewById(R.id.view_post_activity_comment_edit_text);
        String comment = commentEditText.getText().toString();

        if (comment.length() == 0) {
            MainActivity.showMessage(this, "No comment written");
            return;
        }

        JSONObject content = new JSONObject();
        try {
            content.put("content", comment);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                MainActivity.buildUrl("api", "comment", Integer.toString(post.getId())),
                content,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        commentEditText.setText("");
                        getComments();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MainActivity.showMessage(getApplicationContext(), "Couldn't create comment");
                        error.printStackTrace();
                    }
                }

        );
        MainActivity.queue.add(request);
    }
}
