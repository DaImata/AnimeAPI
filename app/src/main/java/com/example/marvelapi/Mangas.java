package com.example.marvelapi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.os.Environment.getExternalStorageDirectory;

public class Mangas extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final String FILE_NAME = "InformacoesManga.txt";
    private EditText txtQuadrinhos;
    private TextView txtInformacao1, txtInformacao3;
    private Button btnMaisInformacoes, btnSalvarInfoManga, btnCarregarInfoManga;
    String Score, Chapters, Url, Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mangas);
        getSupportActionBar().hide();

        ArmazenamentoExternoWrite();
        ArmazenamentoExternoRead();

        setTitle("Pesquisar Mangás");
        txtQuadrinhos = findViewById(R.id.txtQuadrinhos);
        txtInformacao1 = findViewById(R.id.txtInformacao1Q);
        txtInformacao3 = findViewById(R.id.txtInformacao3Q);
        btnMaisInformacoes = findViewById(R.id.btnPesquisarQ2);
        btnCarregarInfoManga = findViewById(R.id.btnCarregarInfoManga);
        btnSalvarInfoManga = findViewById(R.id.btnSalvarInfoManga);
        btnMaisInformacoes.setEnabled(false);
        btnSalvarInfoManga.setEnabled(false);
        btnCarregarInfoManga.setEnabled(false);

        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    public void pesquisarMangas(View view) {
        String queryString = txtQuadrinhos.getText().toString();

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
            txtInformacao1.setText(R.string.carregando);
        } else {
            if (queryString.length() == 0) {
                txtInformacao1.setText(R.string.sem_termodebusca);
            } else {
                txtInformacao1.setText(R.string.sem_internet);
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
        return new CarregaMangas(this, queryString);
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
            JSONObject obj = resultados.getJSONObject(0);
            Score = obj.getString("score");
            Chapters = obj.getString("chapters");
            Url = obj.getString("url");
            Name = obj.getString("title");

            //mostra o resultado qdo possivel.
            if (Score != null || Chapters != null) {
                txtInformacao1.setText("Mangá Encontrado: " + Name + "\nNota do mangá: " + Score + "/10" + "\nNúmero de capítulos: " + Chapters + " capítulos");
                txtInformacao1.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                btnMaisInformacoes.setEnabled(true);
                btnSalvarInfoManga.setEnabled(true);
                btnCarregarInfoManga.setEnabled(true);
                txtInformacao3.setText(R.string.mais_informacoes);
            } else {
                // If none are found, update the UI to show failed results.
                txtInformacao1.setText(R.string.sem_resultados);
            }
        } catch (Exception e) {
            // Se não receber um JSOn valido, informa ao usuário
            txtInformacao1.setText(R.string.sem_resultados);
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // obrigatório implementar, nenhuma ação executada
    }

    public void MaisInformacoes(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(Url));
        startActivity(intent);
    }

    private boolean ArmazenamentoExternoWrite() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("State", "Está disponível para write");
            return true;
        } else {
            Log.i("State", "Não está disponível para write");
            return false;
        }
    }

    private boolean ArmazenamentoExternoRead() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())) {
            Log.i("State", "Está disponível para read");
            return true;
        } else {
            Log.i("State", "Não está disponível para read");
            return false;
        }
    }

    public void SalvarInfoManga(View view) {
        String texto = txtInformacao1.getText().toString();
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fileOutputStream.write(texto.getBytes());

            txtInformacao1.setText("");
            txtInformacao3.setText("");
            txtQuadrinhos.getText().clear();
            btnMaisInformacoes.setEnabled(true);
            btnCarregarInfoManga.setEnabled(true);
            btnSalvarInfoManga.setEnabled(false);
            Toast.makeText(this, "Salvo no armazenamento externo em " + getExternalStorageDirectory() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
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

    public void CarregarInfoManga(View view) {
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
            txtInformacao1.setText(stringBuilder.toString());
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