package com.example.marvelapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AnimesMangaBackup extends AppCompatActivity {
    private RecyclerView recycler;
    private BackupAdapter backupAdapter;
    private TextView itens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animes_manga_backup);
        getSupportActionBar().hide();

//        recycler = findViewById(R.id.recycler);
        itens = findViewById(R.id.txtItensSalvos);

    }
    public void carregarBackups(){

        backupAdapter = new BackupAdapter();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(false);
        recycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
    }
}