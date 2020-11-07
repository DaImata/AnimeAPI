package com.example.marvelapi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BackupAdapter extends RecyclerView.Adapter<BackupAdapter.ViewHolder> {

    private List<BackupsFeitos> listaBackups;

    public BackupAdapter(List<BackupsFeitos> lista) {
        this.listaBackups = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_backup_adapter, parent, false);
        return new ViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BackupsFeitos backupsFeitos = listaBackups.get(position);
        holder.item.setText(backupsFeitos.getDadosBackup());
    }

    @Override
    public int getItemCount() {
        return this.listaBackups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.txtItem);
        }
    }
}
