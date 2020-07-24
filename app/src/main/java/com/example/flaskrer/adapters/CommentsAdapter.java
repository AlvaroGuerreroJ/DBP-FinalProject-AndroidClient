package com.example.flaskrer.adapters;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flaskrer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {
    private JSONArray comments;

    public CommentsAdapter(JSONArray comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.comment_item,
                parent,
                false
        );

        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        JSONObject jo;
        String author;
        String content;
        try {
            jo = comments.getJSONObject(position);
            author = jo.getString("username");
            content = jo.getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        holder.author.setText(author);
        holder.content.setText(content);
    }

    @Override
    public int getItemCount() {
        return comments.length();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView content;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.author = itemView.findViewById(R.id.comment_item_author);
            this.content = itemView.findViewById(R.id.comment_item_content);
        }
    }
}
