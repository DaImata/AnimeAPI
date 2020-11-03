package com.example.marvelapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        setTitle("API de Animes");
    }
    public void Animes (View view){
        Intent intentAnimes = new Intent (MainActivity.this, Animes.class);
        startActivity(intentAnimes);
        Toast.makeText(this, "Arquivos de Animes serão salvos em armazenamento interno", Toast.LENGTH_LONG).show();
    }
    public void Mangas (View view){
        Intent intentMangas = new Intent (MainActivity.this, Mangas.class);
        startActivity(intentMangas);
        Toast.makeText(this, "Arquivos de Mangá serão salvos em armazenamento externo", Toast.LENGTH_LONG).show();
    }
    public void Localizacao (View view){
        Intent intentLocalizacao = new Intent (MainActivity.this, Localizacao.class);
        startActivity(intentLocalizacao);
    }

    public void Backup (View view){
        Intent intentBackup = new Intent (MainActivity.this, AnimesMangaBackup.class);
        startActivity(intentBackup);
    }
}