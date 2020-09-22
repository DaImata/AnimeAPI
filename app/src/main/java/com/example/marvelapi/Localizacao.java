package com.example.marvelapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class Localizacao extends AppCompatActivity implements EnderecoAsync.OnTaskCompleted {

    //Views
    private TextView lblEndereco;
    private Button btnPesquisarLocalizacao;
    private static final String ULTIMO_ENDERECO = "endereco";
    private FusedLocationProviderClient FusedLocation;
    private LocationCallback LocationCall;
    private boolean rastreador;

    // Arquivo SharedPreferences
    public static final String PREFERENCIAS = "com.example.marvelapi";
    private static final String RASTREADOR = "tracking_location";

    // Constantes
    private static final int PERMISSAO_LOCALIZACAO = 1;
    private static final String LATITUDE = "latitude", LONGITUDE = "longitude", ULTIMA_DATA = "data";

    // SharedPreferences

    private SharedPreferences Preferences;
    private String UltimaLatitude = "", UltimoEndereco = "", UltimaLongitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao);

        lblEndereco = findViewById(R.id.lblEndereco);
        btnPesquisarLocalizacao = findViewById(R.id.btnPesquisarLocalizacao);

        // Inicializa o FusedLocation
        FusedLocation = LocationServices.getFusedLocationProviderClient(this);

        // Recupera o estado anterior do app
        if (savedInstanceState != null){
            rastreador = savedInstanceState.getBoolean(RASTREADOR);
        }

        // Listener do botão de localização
        btnPesquisarLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rastreador){
                    comecaBusca();
                } else{
                    paraBusca();
                }
            }
        });

        // Inicializa os callbacks
        LocationCall = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult){
                // Se o rastreador estiver ligado, mostrar endereço
                if (rastreador){
                    new EnderecoAsync(Localizacao.this, Localizacao.this).execute(locationResult.getLastLocation());
                }
            }
        };

        // Inicializa as SharedPreferences
        Preferences = getSharedPreferences(PREFERENCIAS, MODE_PRIVATE);
        // Recupera Preferencias
        recuperarDados();
    }

    // Começa a busca e pede as permissões
    private void comecaBusca(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSAO_LOCALIZACAO);
        } else{
            rastreador = true;
            FusedLocation.requestLocationUpdates(requisicaoLocalizacao(), LocationCall, null);
            // "Carregando..." enquanto o endereço não é retornado
            lblEndereco.setText(getString(R.string.lblEndereco, getString(R.string.carregando), getString(R.string.carregando), getString(R.string.carregando), System.currentTimeMillis()));
            btnPesquisarLocalizacao.setText(R.string.btnParar);
        }
    }

    // Retorna os parâmetros definidos no método e faz a requisição de localização
    private LocationRequest requisicaoLocalizacao(){
     LocationRequest locationRequest = new LocationRequest();
     locationRequest.setInterval(5000);
     locationRequest.setFastestInterval(2000);
     locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
     return  locationRequest;
    }

    // Para a busca
    private void paraBusca(){
        if(rastreador){
            rastreador = false;
            btnPesquisarLocalizacao.setText(R.string.btnPesquisar);
            lblEndereco.setText(R.string.lblAperteComecar);
        }
    }

    // Salva a última localização
    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putBoolean(RASTREADOR, rastreador);
        super.onSaveInstanceState(outState);
    }

    // Callback chamado pelo requestPermission
    @Override
    public void onRequestPermissionsResult(int codigoRequisicao, @NonNull String[] permissao, @NonNull int[] grantResults) {
        switch (codigoRequisicao) {
            case PERMISSAO_LOCALIZACAO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    comecaBusca();
                } else {
                    Toast.makeText(this, R.string.PermissaoNegada, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // Resposta do EnderecoAsync
    @Override
    public void onTaskCompleted(String[]resultado){
        if(rastreador){
            // Atualiza a tela
            UltimoEndereco = resultado[0];
            UltimaLatitude = resultado[1];
            UltimaLongitude = resultado[2];
            lblEndereco.setText(getString(R.string.lblEndereco, UltimoEndereco, UltimaLatitude, UltimaLongitude, System.currentTimeMillis()));
        }
    }
    @Override
    protected void onPause() {
        if (rastreador) {
            paraBusca();
            rastreador = true;
            armazenarDados(UltimoEndereco, UltimaLatitude, UltimaLongitude);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (rastreador) {
            comecaBusca();
        }
        recuperarDados();
        super.onResume();
    }

    //Armazena as preferencias do usuário
    //na aplicação será armazenada a última localicação

    private void armazenarDados(String latitude, String longitude, String lastAdress) {
        SharedPreferences.Editor preferencesEditor = Preferences.edit();
        preferencesEditor.putString(ULTIMO_ENDERECO, lastAdress);
        preferencesEditor.putString(LATITUDE, latitude);
        preferencesEditor.putString(LONGITUDE, longitude);
        preferencesEditor.putLong(ULTIMA_DATA, System.currentTimeMillis());
        preferencesEditor.apply();
    }

    private void recuperarDados() {

        UltimoEndereco = Preferences.getString(ULTIMO_ENDERECO, "");
        UltimaLatitude = Preferences.getString(LATITUDE, "");
        UltimaLongitude = Preferences.getString(LONGITUDE, "");
        long time = Preferences.getLong(ULTIMA_DATA, 0);
        Toast.makeText(this, getString(R.string.lblEndereco, UltimaLongitude, UltimaLatitude, UltimoEndereco, time), Toast.LENGTH_SHORT).show();
    }
}