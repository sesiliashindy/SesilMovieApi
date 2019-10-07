package com.example.user.sesilmovieapi.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.user.sesilmovieapi.model.Movie;
import com.example.user.sesilmovieapi.NetworkUtils;
import com.example.user.sesilmovieapi.R;
import com.example.user.sesilmovieapi.adapter.GridMovieAdapter;
import com.example.user.sesilmovieapi.api.MovieApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SearchMovieActivity extends AppCompatActivity {
    public static final String EXTRA_QUERY = "extra_query";
    private RecyclerView rvMovie;
    private ArrayList<Movie> movies;
    private ProgressBar pbMovie;
    private GridMovieAdapter gridMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        rvMovie = findViewById(R.id.rv_search_movie);
        pbMovie = findViewById(R.id.progressbar_search_movie);
        gridMovieAdapter = new GridMovieAdapter(this);
        movies = new ArrayList<>();
        String query = getIntent().getStringExtra(EXTRA_QUERY);
        Log.e("query",query);

        showRecyclerGridMovie();
        if (savedInstanceState == null){
            URL url = MovieApi.getSearch(query);
            Log.e("url",url.toString());
            new MovieAsyncTask().execute(url);
        } else{
            movies = savedInstanceState.getParcelableArrayList("movie_list");
            if (movies != null){
                gridMovieAdapter.setMovies(movies);
            }
        }
    }

    private void showRecyclerGridMovie() {
        rvMovie.setLayoutManager(new GridLayoutManager(this,3));
        rvMovie.setAdapter(gridMovieAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movie_list",movies);
        super.onSaveInstanceState(outState);
    }

    private class  MovieAsyncTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rvMovie.setVisibility(View.GONE);
            pbMovie.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String result = null;
            try {
                result = NetworkUtils.getFromNetwork(url);
            } catch (IOException e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            rvMovie.setVisibility(View.VISIBLE);
            pbMovie.setVisibility(View.GONE);
            Log.e("MOVIE_DATA_UP", s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    Movie movie = new Movie(object);
                    movies.add(movie);
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
            gridMovieAdapter.setMovies(movies);
        }
    }
}
