package com.example.marvelapi;

import java.io.Serializable;

public class BackupsFeitos implements Serializable {

    private int id;
    private String dadosBackup;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDadosBackup() {
        return dadosBackup;
    }

    public void setDadosBackup(String dadosBackup) {
        this.dadosBackup = dadosBackup;
    }
}
