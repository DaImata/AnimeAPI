package com.example.marvelapi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.concurrent.ExecutionException;

public class dbHelper extends SQLiteOpenHelper {

    public static int VERSAO = 1;
    public static String NOME_DB = "DB_OBRAS", TB_BACKUP = "tbBackup";

    public dbHelper(Context context) {
        super(context, NOME_DB, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TB_BACKUP
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " dadosBackup TEXT NOT NULL ); ";
        try{
            sqLiteDatabase.execSQL(sql);
            Log.i("SUCESSO DB", "Sucesso ao criar");
        }catch(Exception e){
            Log.i("ERRO DB", "Erro ao criar" + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = " DROP TABLE IF EXISTS " + TB_BACKUP + " ;" ;
        try{
            sqLiteDatabase.execSQL(sql);
            Log.i("SUCESSO APP", "Sucesso ao atualizar");
        }catch(Exception e){
            Log.i("ERRO APP", "Erro ao atualizar" + e.getMessage());
        }
    }
}
