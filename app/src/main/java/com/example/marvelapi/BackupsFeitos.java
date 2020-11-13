package com.example.marvelapi;

import java.io.Serializable;

public class BackupsFeitos implements Serializable {

    private Long id;
    private String dadosBackup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDadosBackup() {
        return dadosBackup;
    }

    public void setDadosBackup(String dadosBackup) {
        this.dadosBackup = dadosBackup;
    }
}
