package com.example.user.sesilmovieapi.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.example.user.sesilmovieapi.favorite.MovieFavorite;
import com.example.user.sesilmovieapi.model.Movie;
import com.example.user.sesilmovieapi.R;
import com.example.user.sesilmovieapi.api.MovieApi;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

public class StackMovieRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{
    private ArrayList<Movie> movielist;
    private RealmResults<MovieFavorite> movieFavorite;
    private Context context;
    private Realm realm;

    public StackMovieRemoteViewsFactory(Context context) {
        this.context = context;
        movielist = new ArrayList<>();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        try {
            Realm.init(context);
            realm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException e){
            Realm.deleteRealm(realm.getDefaultConfiguration());
            realm = Realm.getDefaultInstance();
        }
        movieFavorite = realm.where(MovieFavorite.class).findAll();
        if (!movieFavorite.isEmpty()){
            for (int i = 0; i<movieFavorite.size(); i++){
                Movie dummy = new Movie();
                dummy.setId(movieFavorite.get(i).getId());
                dummy.setName(movieFavorite.get(i).getName());
                dummy.setPoster(movieFavorite.get(i).getPoster());
                movielist.add(dummy);
            }
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return movielist.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        try {
            Realm.init(context);
            realm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException e){
            Realm.deleteRealm(realm.getDefaultConfiguration());
            realm = Realm.getDefaultInstance();
        }
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        Bitmap bitmap = null;
        String posterPath = movielist.get(i).getPoster();
        String name = movielist.get(i).getName();
        Log.d("Load",posterPath);
        if (movielist.size() > 0){
            try {
                bitmap = Glide.with(context).asBitmap()
                        .load(MovieApi.getPoster(posterPath))
                        .into(800,600)
                        .get();
                Log.d("Load","Succes");
            } catch (InterruptedException | ExecutionException e){
                Log.d("Load","Fail");
            }
            remoteViews.setImageViewBitmap(R.id.movie_view,bitmap);
            remoteViews.setTextViewText(R.id.movie_title,name);
        }

        Bundle extras = new Bundle();
        extras.putInt(ImageBannerMovieWidget.EXTRA_ITEM, i);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        remoteViews.setOnClickFillInIntent(R.id.movie_view, fillInIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
