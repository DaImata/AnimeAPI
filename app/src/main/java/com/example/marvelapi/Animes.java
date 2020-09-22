package com.example.marvelapi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Animes extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static final String FILE_NAME = "InformacoesAnime.txt";
    private EditText txtHeroi;
    private TextView txtInformacaoHeroi, txtInformacaoHeroi3, btnSalvarInfoAnime, btnCarregarInfoAnime;
    private Button btnMaisInformacoes;
    String Score, Episodes, Url, Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animes);

        setTitle("Pesquisar Animes");
        txtHeroi = findViewById(R.id.txtNomeHeroi);
        txtInformacaoHeroi = findViewById(R.id.txtInformacaoHeroi);
        txtInformacaoHeroi3 = findViewById(R.id.txtInformacaoHeroi3);
        btnMaisInformacoes = findViewById(R.id.btnPesquisarH2);
        btnSalvarInfoAnime = findViewById(R.id.btnSalvarInfoAnime);
        btnCarregarInfoAnime = findViewById(R.id.btnCarregarInfoAnime);
        btnMaisInformacoes.setEnabled(false);
        btnCarregarInfoAnime.setEnabled(false);
        btnSalvarInfoAnime.setEnabled(false);

        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    public void pesquisarHerois(View view) {
        String queryString = txtHeroi.getText().toString();

        InputMethodManager escondeTeclado = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (escondeTeclado != null) {
            escondeTeclado.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        ConnectivityManager statusConexao = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (statusConexao != null) {
            networkInfo = statusConexao.getActiveNetworkInfo();
        }
        if (networkInfo != null && networkInfo.isConnected()
                && queryString.length() != 0) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
            txtInformacaoHeroi.setText(R.string.carregando);
        }
        else {
            if (queryString.length() == 0) {
                txtInformacaoHeroi.setText(R.string.sem_termodebusca);
            } else {
                txtInformacaoHeroi.setText(R.string.sem_internet);
            }
        }
    }
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";
        if (args != null) {
            queryString = args.getString("queryString");
        }
        return new CarregaAnimes(this, queryString);
    }
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            // Converte a resposta em Json
            JSONObject jsonObject = new JSONObject(data);
            // Obtem o JSONArray dos itens de livros
            JSONArray resultados = jsonObject.getJSONArray("results");
            // Procura pro resultados nos itens do array
                // Obtem a informação
                JSONObject obj= resultados.getJSONObject(0);
                Score = obj.getString("score");
                Episodes = obj.getString("episodes");
                Url = obj.getString("url");
                Name = obj.getString("title");

            //mostra o resultado qdo possivel.
            if (Score != null || Episodes != null) {
                txtInformacaoHeroi.setText("Anime encontrado: " + Name + "\nNota do anime: " + Score + "/10" + "\nNº de Episódios: " + Episodes + " episódios");
                txtInformacaoHeroi.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                btnMaisInformacoes.setEnabled(true);
                btnCarregarInfoAnime.setEnabled(true);
                btnSalvarInfoAnime.setEnabled(true);
                txtInformacaoHeroi3.setText(R.string.mais_informacoes);
            } else {
                // If none are found, update the UI to show failed results.
                txtInformacaoHeroi.setText(R.string.sem_resultados);
            }
        } catch (Exception e) {
            // Se não receber um JSOn valido, informa ao usuário
            txtInformacaoHeroi.setText(R.string.sem_resultados);
            e.printStackTrace();
        }
    }
    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // obrigatório implementar, nenhuma ação executada
    }
    public void MaisInformacoes (View view){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(Url));
        startActivity(intent);
    }

    public void SalvarInfoAnime(View view){
        String texto = txtInformacaoHeroi.getText().toString();
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fileOutputStream.write(texto.getBytes());

            txtInformacaoHeroi.setText("");
            txtInformacaoHeroi3.setText("");
            txtHeroi.getText().clear();
            btnMaisInformacoes.setEnabled(true);
            btnCarregarInfoAnime.setEnabled(true);
            btnSalvarInfoAnime.setEnabled(false);
            Toast.makeText(this, "Salvo no armazenamento interno em " + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if (fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void CarregarInfoAnime(View view){
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = openFileInput(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String texto;

            while((texto = bufferedReader.readLine()) !=null){
                stringBuilder.append(texto).append("\n");
            }
            txtInformacaoHeroi.setText(stringBuilder.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}