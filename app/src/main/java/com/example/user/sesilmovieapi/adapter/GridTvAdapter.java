package com.example.user.sesilmovieapi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.user.sesilmovieapi.activity.DetailTvActivity;
import com.example.user.sesilmovieapi.R;
import com.example.user.sesilmovieapi.model.Tv;
import com.example.user.sesilmovieapi.api.TvShowApi;

import java.util.ArrayList;

public class GridTvAdapter extends RecyclerView.Adapter<GridTvAdapter.GridViewHolder> {
    private ArrayList<Tv> tvs;
    private Context context;

    public GridTvAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Tv> getTvs() {
        return tvs;
    }

    public void setTvs(ArrayList<Tv> tvs) {
        this.tvs = tvs;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_grid_movie_tv,viewGroup,false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder gridViewHolder, final int i) {
        String posterPath = getTvs().get(i).getPoster();
        Glide.with(context)
                .load(TvShowApi.getPoster(posterPath))
                .apply(new RequestOptions().override(500,500))
                .into(gridViewHolder.tvPhoto);

        gridViewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DetailTvActivity.class);
                intent.putExtra("TV",getTvs().get(i));
                Log.e("Name",getTvs().get(i).getName());
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getTvs().size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        ImageView tvPhoto;
        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPhoto = itemView.findViewById(R.id.img_item_photo);
        }
    }
}
