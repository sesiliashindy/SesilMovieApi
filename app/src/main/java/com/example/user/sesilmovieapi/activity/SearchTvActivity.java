package com.example.user.sesilmovieapi.activity;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.user.sesilmovieapi.NetworkUtils;
import com.example.user.sesilmovieapi.R;
import com.example.user.sesilmovieapi.adapter.GridTvAdapter;
import com.example.user.sesilmovieapi.api.TvShowApi;
import com.example.user.sesilmovieapi.model.Tv;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SearchTvActivity extends AppCompatActivity {
    public static final String EXTRA_QUERY = "extra_query";
    private RecyclerView rvTv;
    private ArrayList<Tv> tvs;
    private ProgressBar pbTv;
    private GridTvAdapter gridTvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tv);

        rvTv = findViewById(R.id.rv_search_tv);
        pbTv = findViewById(R.id.progressbar_search_tv);
        gridTvAdapter = new GridTvAdapter(this);
        tvs = new ArrayList<>();
        String query = getIntent().getStringExtra(EXTRA_QUERY);
        Log.e("query",query);

        showRecyclerGridTvShow();
        if (savedInstanceState == null){
            URL url = TvShowApi.getSearch(query);
            Log.e("url",url.toString());
            new TvAsyncTask().execute(url);
        } else{
            tvs = savedInstanceState.getParcelableArrayList("tv_list");
            if (tvs != null) {
                gridTvAdapter.setTvs(tvs);
            }
        }
    }

    private void showRecyclerGridTvShow(){
        rvTv.setLayoutManager(new GridLayoutManager(this,3));
        rvTv.setAdapter(gridTvAdapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("tv_list",tvs);
        super.onSaveInstanceState(outState);
    }

    private class TvAsyncTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rvTv.setVisibility(View.GONE);
            pbTv.setVisibility(View.VISIBLE);
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
            rvTv.setVisibility(View.VISIBLE);
            pbTv.setVisibility(View.GONE);
            Log.e("TV_DATA_UP", s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    Tv tv = new Tv(object);
                    tvs.add(tv);
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
            gridTvAdapter.setTvs(tvs);
        }
    }

}
