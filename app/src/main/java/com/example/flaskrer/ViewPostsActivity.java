package com.example.flaskrer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.flaskrer.adapters.PostsAdapter;

import org.json.JSONArray;

public class ViewPostsActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_posts);

        final SwipeRefreshLayout swipeRefreshLayout =
                findViewById(R.id.view_posts_activity_pull_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getPosts();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        recyclerView = findViewById(R.id.view_posts_activity_recycler_view);

        getPosts();
    }

    private void getPosts() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                MainActivity.buildUrl("api", "post"),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        PostsAdapter postsAdapter = new PostsAdapter(response);

                        recyclerView.setAdapter(postsAdapter);
                        recyclerView.setLayoutManager(
                                new GridLayoutManager(getApplicationContext(), 2)
                        );
                        // recyclerView.addItemDecoration(
                        //         new DividerItemDecoration(
                        //                 getApplicationContext(),
                        //                 DividerItemDecoration.VERTICAL
                        //         )
                        // );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MainActivity.showMessage(getApplicationContext(), "Couldn't download posts");
                        finish();
                    }
                }
        );

        MainActivity.queue.add(request);
    }
}
