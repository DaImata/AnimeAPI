package com.example.marvelapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AnimesMangaBackup extends AppCompatActivity {
    private RecyclerView recycler;
    private BackupAdapter backupAdapter;
    private List<BackupsFeitos> listaBackups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animes_manga_backup);
        getSupportActionBar().hide();

        recycler = findViewById(R.id.recycler);
        recycler.addOnItemTouchListener(
                new RecyclerListener(
                        getApplicationContext(), recycler, new RecyclerListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
                )
        );
    }

    public void carregarBackups() {

        BackupsFeitos backup1 = new BackupsFeitos();
        backup1.setDadosBackup("Anime X");
        listaBackups.add(backup1);

        backupAdapter = new BackupAdapter(listaBackups);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(false);
        recycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recycler.setAdapter(backupAdapter);
    }

    @Override
    protected void onStart() {
        carregarBackups();
        super.onStart();
    }
}