package com.example.marvelapi;
import android.content.Context;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class CarregaAnimes extends AsyncTaskLoader<String> {
    private String QueryString;
    CarregaAnimes(Context context, String queryString) {
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
        return Network.buscaInfo(QueryString);
    }
}
