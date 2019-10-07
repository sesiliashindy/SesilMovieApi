package com.example.user.sesilmovieapi.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class StackMovieWidgetService extends RemoteViewsService {

    public StackMovieWidgetService(){
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackMovieRemoteViewsFactory(this.getApplicationContext());
    }
}
