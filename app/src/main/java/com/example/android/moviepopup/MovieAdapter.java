package com.example.android.moviepopup;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    public SendingIndex sendingIndex;
    private ArrayList<MovieStructure> sortHolderType;


    public MovieAdapter(SendingIndex mySendingIndex) {
        sendingIndex = mySendingIndex;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_viewholder, viewGroup, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        Picasso.get()
                .load("http://image.tmdb.org/t/p/w500/" + sortHolderType.get(i).imageMovie)
                .placeholder(R.drawable.loadingicon)
                .error(R.drawable.internetfailed)
                .into(imageViewHolder.imageView);
        imageViewHolder.main = sendingIndex;
    }

    @Override
    public int getItemCount() {
        return sortHolderType.size();
    }

    public void setMovieTask(ArrayList<MovieStructure> createEntities) {

        sortHolderType = createEntities;
        notifyDataSetChanged();
    }


}
