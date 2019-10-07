package com.example.user.sesilmovieapi.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.user.sesilmovieapi.adapter.GridTvAdapter;
import com.example.user.sesilmovieapi.favorite.TvFavorite;
import com.example.user.sesilmovieapi.model.Tv;
import com.example.user.sesilmovieapi.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteTvFragment extends Fragment {
    private RecyclerView rvTv;
    private ProgressBar pbTv;
    private ArrayList<Tv> tvs;
    private GridTvAdapter gridTvAdapter;
    RealmResults<TvFavorite> listTvFav;
    private Realm realm;

    public FavoriteTvFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_tv, container, false);
        rvTv = view.findViewById(R.id.rv_favorit_tv);
        pbTv = view.findViewById(R.id.progressbar_favorit_tv);
        return view;
    }

    private void showRecyclerGridTvShow(){
        rvTv.setLayoutManager(new GridLayoutManager(getActivity(),3));
        gridTvAdapter = new GridTvAdapter(getActivity());
        gridTvAdapter.setTvs(tvs);
        rvTv.setAdapter(gridTvAdapter);
        listTvFav = realm.where(TvFavorite.class).findAll();
    }

    private void loadData(){
        listTvFav = realm.where(TvFavorite.class).findAll();
        pbTv.setVisibility(View.VISIBLE);
        if (!listTvFav.isEmpty()){
            for (int i=0; i<listTvFav.size(); i++){
                Tv dummy = new Tv();
                dummy.setId(listTvFav.get(i).getId());
                dummy.setName(listTvFav.get(i).getName());
                dummy.setOverview(listTvFav.get(i).getOverview());
                dummy.setPoster(listTvFav.get(i).getPoster());
                dummy.setVote_average(listTvFav.get(i).getVote_average());
                dummy.setDate(listTvFav.get(i).getDate());
                dummy.setLanguage(listTvFav.get(i).getLanguage());
                tvs.add(dummy);
            }
        }
        pbTv.setVisibility(View.GONE);
        gridTvAdapter.setTvs(tvs);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            Realm.init(getActivity());
            realm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException e){
            Realm.deleteRealm(realm.getDefaultConfiguration());
            realm = Realm.getDefaultInstance();
        }
        tvs = new ArrayList<>();
        showRecyclerGridTvShow();
        loadData();
    }

}
