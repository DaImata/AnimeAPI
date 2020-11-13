package com.example.marvelapi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AnimesMangaBackup extends AppCompatActivity {
    private RecyclerView recycler;
    private BackupAdapter backupAdapter;
    private List<BackupsFeitos> listaBackups = new ArrayList<>();
    private BackupsFeitos backupSelecionado;

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
                        Log.i("clique", "Clicado");
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        backupSelecionado = listaBackups.get(position);

                        AlertDialog.Builder dialog = new AlertDialog.Builder(AnimesMangaBackup.this);

                        dialog.setTitle("Tem certeza?");
                        dialog.setMessage("O backup do item " + backupSelecionado.getId() + " será excluído permanentemente");

                        dialog.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DAO dao = new DAO(getApplicationContext());
                                if (dao.deletar(backupSelecionado)) {
                                    carregarBackups();
                                    Toast.makeText(getApplicationContext(), "Sucesso ao excluir o backup", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Não foi possível excluir o backup", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        dialog.setNegativeButton("Cancelar", null);
                        dialog.create();
                        dialog.show();
                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
                )
        );
    }

    public void Voltar (View view){
        Intent intent = new Intent (AnimesMangaBackup.this, MainActivity.class);
        startActivity(intent);
    }

    public void carregarBackups() {

        DAO dao = new DAO(getApplicationContext());
        listaBackups = dao.listar();

        backupAdapter = new BackupAdapter(listaBackups);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(true);
        recycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recycler.setAdapter(backupAdapter);
    }

    @Override
    protected void onStart() {
        carregarBackups();
        super.onStart();
    }
}