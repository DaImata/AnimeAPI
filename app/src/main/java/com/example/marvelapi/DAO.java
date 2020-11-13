package com.example.marvelapi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class DAO implements I_DAO {
    private SQLiteDatabase write, read;

    public DAO(Context context) {
        dbHelper db = new dbHelper(context);
        write = db.getWritableDatabase();
        read = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(BackupsFeitos backupsFeitos) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("dadosBackup", backupsFeitos.getDadosBackup());
        try {
            read.insert(dbHelper.TB_BACKUP, null, contentValues);
            Log.i("SucessoSalvar", "Backup salvo!");
        } catch (Exception e) {
            Log.i("ErroSalvar", "Erro");
            return false;
        }

        return true;
    }

    @Override
    public boolean deletar(BackupsFeitos backupsFeitos) {
        try {
            String[] args = {backupsFeitos.getId().toString()};
            write.delete(dbHelper.TB_BACKUP, "id=?", args);
        } catch (Exception e) {
            Log.i("ErroDelete", "Erro ao deletar" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public List<BackupsFeitos> listar() {
        List<BackupsFeitos> backups = new ArrayList<>();
        String sql = "SELECT * FROM " + dbHelper.TB_BACKUP + ";";
        Cursor cursor = read.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            BackupsFeitos backup = new BackupsFeitos();
            Long id = cursor.getLong(cursor.getColumnIndex("id"));
            String dadosBackup = cursor.getString(cursor.getColumnIndex("dadosBackup"));
            backup.setId(id);
            backup.setDadosBackup(dadosBackup);

            backups.add(backup);
        }

        return backups;
    }
}
