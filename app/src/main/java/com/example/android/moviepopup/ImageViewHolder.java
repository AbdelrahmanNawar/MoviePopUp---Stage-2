package com.example.android.moviepopup;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView imageView;
    public int index;
    public SendingIndex main;

    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.movie_image_id);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        index = getAdapterPosition();
        this.main.onClick(index);
    }
}
