package com.example.marvelapi;

import java.util.List;

public interface I_DAO {
    public boolean salvar(BackupsFeitos backupsFeitos);
    public boolean deletar(BackupsFeitos backupsFeitos);
    public List<BackupsFeitos> listar();

}
