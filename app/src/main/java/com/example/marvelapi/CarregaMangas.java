package com.example.marvelapi;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class CarregaMangas  extends AsyncTaskLoader<String> {
    private String QueryString;
    CarregaMangas(Context context, String queryString) {
        super(context);
        QueryString = queryString;
    }
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
    @Nullable
    @Override
    public String loadInBackground() {
        return Network2.buscaInfo(QueryString);
    }
}
