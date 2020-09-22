package com.example.marvelapi;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class EnderecoAsync extends AsyncTask<Location, Void, String[]> {

    private Context Context;
    private OnTaskCompleted Listener;

    EnderecoAsync(Context appContext, OnTaskCompleted listener){
    Context = appContext;
    Listener = listener;
    }

    private final String TAG = EnderecoAsync.class.getSimpleName();

    @Override
    protected String[] doInBackground(Location... params) {
        // Definindo local do GEOCODER
        Geocoder geocoder = new Geocoder(Context, Locale.getDefault());

        // Pegando endereço, latitude e longitude
        Location location = params[0];
        List<Address> endereco = null;
        String[] mensagem = new String[3];
        mensagem[0] = "";
        mensagem[1] = "";
        mensagem[2] = "";

        try {
            endereco = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), /* Pegando um único endereço */1);
            mensagem[1] = String.valueOf(location.getLatitude());
            mensagem[2] = String.valueOf(location.getLongitude());
        } catch (IOException exception) {
            // Recuperar problemas de network e entrada e saída
            mensagem[0] = Context.getString(R.string.ServicoIndisponivel);
            Log.e(TAG, mensagem[0], exception);
        } catch (IllegalArgumentException argumentException) {
            // Recuperar problemas de latitude ou longitude
            mensagem[0] = Context.getString(R.string.CoordenadaIndisponivel);
            Log.e(TAG, mensagem[0] + ", Latitude = " + location.getLatitude() + ", Longitude = " + location.getLongitude(), argumentException);
        }

        // Mensagem caso não forem encontrados endereços
        if (endereco == null || endereco.size() == 0) {
            if (mensagem[0].isEmpty()) {
                mensagem[0] = Context.getString(R.string.EnderecoIndisponivel);
                Log.e(TAG, mensagem[0]);
            }
        } else {
            // Se um endereço for encontrado, ler na mensagem
            Address enderecos = endereco.get(0);
            ArrayList<String> parteEndereco = new ArrayList<>();

            // Busca as linhas de endereço e as enviam à thread
            for (int i = 0; i <= enderecos.getMaxAddressLineIndex(); i++) {
                parteEndereco.add(enderecos.getAddressLine(i));
            }
            mensagem[0] = TextUtils.join("\n", parteEndereco);
        }
        return mensagem;
    }

    @Override
    protected void onPostExecute(String[] enderecos) {
        Listener.onTaskCompleted(enderecos);
        super.onPostExecute(enderecos);
    }

    interface OnTaskCompleted {
        void onTaskCompleted(String[] resultado);
    }
}