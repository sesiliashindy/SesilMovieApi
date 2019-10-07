package com.example.user.sesilmovieapi.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.user.sesilmovieapi.fragment.FavoriteFragment;
import com.example.user.sesilmovieapi.fragment.MovieFragment;
import com.example.user.sesilmovieapi.R;
import com.example.user.sesilmovieapi.fragment.TvShowFragment;

public class BottomNavActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
     private Fragment fragment;
     private final String TAG_FRAGMENT = "FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        BottomNavigationView bottomNavigationMain;

        bottomNavigationMain = findViewById(R.id.btn_navigation);
        bottomNavigationMain.setOnNavigationItemSelectedListener(this);
        if(savedInstanceState == null){
            bottomNavigationMain.setSelectedItemId(R.id.navigation_movie);
            fragment = new MovieFragment();
        } else {
            fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
            loadFragment(fragment);
        }

    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void loadFragment(Fragment fragment) {
        if(fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, fragment, TAG_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.navigation_movie:
                fragment = new MovieFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame,fragment,TAG_FRAGMENT)
                        .commit();
                return true;
            case R.id.navigation_tv_show:
                fragment = new TvShowFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame,fragment,TAG_FRAGMENT)
                        .commit();
                return true;
            case R.id.navigation_favorite:
                fragment = new FavoriteFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame,fragment,TAG_FRAGMENT)
                        .commit();
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager !=null){
            SearchView searchView = (SearchView) (menu.findItem(R.id.menu_search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
                    if (fragment instanceof MovieFragment){
                        Intent intent = new Intent(BottomNavActivity.this, SearchMovieActivity.class);
                        intent.putExtra(SearchMovieActivity.EXTRA_QUERY, query);
                        startActivity(intent);
                        return true;
                    }
                    else if (fragment instanceof TvShowFragment){
                        Intent intent = new Intent(BottomNavActivity.this,SearchTvActivity.class);
                        intent.putExtra(SearchTvActivity.EXTRA_QUERY,query);
                        startActivity(intent);
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.language_settings:
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
                break;

            case R.id.menu_reminder:
                Intent intent1 = new Intent(BottomNavActivity.this, RemainderActivity.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);

    }


}
