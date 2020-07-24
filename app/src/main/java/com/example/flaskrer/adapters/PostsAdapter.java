package com.example.flaskrer.adapters;

import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flaskrer.MainActivity;
import com.example.flaskrer.R;
import com.example.flaskrer.ViewPostActivity;
import com.example.flaskrer.models.Post;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {
    private JSONArray posts;

    public PostsAdapter(JSONArray posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.post_item,
                parent,
                false);

        return new PostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
        final Post post;
        try {
            post = new Post(posts.getJSONObject(position));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Picasso.get().load(post.getImageURL()).into(holder.pic);

        holder.pic.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), ViewPostActivity.class);
                        intent.putExtra("post", post);
                        view.getContext().startActivity(intent);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return posts.length();
    }

    public class PostsViewHolder extends RecyclerView.ViewHolder {
        private final ImageView pic;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.pic = itemView.findViewById(R.id.post_item_image);
        }
    }
}
