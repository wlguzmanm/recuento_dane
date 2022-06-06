package co.gov.dane.reconteo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import co.gov.dane.reconteo.assets.utilidades.ViewComponent;
import co.gov.dane.reconteo.backend.Database;
import co.gov.dane.reconteo.model.EnvioFormularioViewModel;
import co.gov.dane.reconteo.model.Offline;

public class ConfiguracionLocal extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 101;
    private Mensajes msg;
    String[] permissions = new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    public Switch swTransmisionLocal;

    private boolean isCheckedGlobal;
    private boolean visibleMensajeGlobal;
    private Database localPersistence;
    ViewComponent viewComponent = new ViewComponent(this,"CONFIGURACION_LOCAL",null);
    private TextView textActive;
    private Session session;
    private Controlador controlador;
    private EnvioFormularioViewModel envioSincronizacionNube = new EnvioFormularioViewModel();
    private Offline offline = new Offline();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_offline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new Session(ConfiguracionLocal.this);
        controlador = new Controlador(this);
        localPersistence = new Database(ConfiguracionLocal.this);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST_ACCOUNTS);
        } else {

        }
        msg = new Mensajes(ConfiguracionLocal.this);
        textActive = (TextView) findViewById(R.id.textActive);

        swTransmisionLocal = (Switch) findViewById(R.id.swTransmisionLocal);
        offline = localPersistence.getOffline("OFFLINE", session.getusename());

        swTransmisionLocal.setChecked(offline.isActivo());
        if(offline!= null && offline.isActivo()){
            textActive.setText("\n\nEl sistema actualmente esta configurado para trabajar sin conexión de Internet");
        }else{
            textActive.setText("\n\nEl sistema actualmente esta configurado para trabajar con conexión de Internet");
        }
        swTransmisionLocal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedGlobal = isChecked;
                viewComponent.progressBarProcess(R.id.loading,true);
                Boolean paso = false;
                envioSincronizacionNube = localPersistence.getCargarSincronizar(session.getusename());
                Gson gson = new Gson();
                String jsonNew = gson.toJson(envioSincronizacionNube);
                Log.w("jsonNew----> ", jsonNew);

                if(!isChecked ){
                    if(envioSincronizacionNube.getEsquemaManzana().size() > 0)
                        createAlert2("Proceso Actualización","El sistema comenzará a sincronizar con la nube por que ha detectado ajustes en la información OFFLINE" );

                    swTransmisionLocal.setChecked(offline.isActivo());
                    Boolean hay_internet=controlador.isNetworkAvailable();
                    if(hay_internet){
                        viewComponent.progressBarProcess(R.id.loading,true);

                        if(envioSincronizacionNube.getEsquemaManzana().size() > 0 ){
                            controlador.uploadData(envioSincronizacionNube, new VolleyCallBack() {
                                @Override
                                public void onSuccess() {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            viewComponent.progressBarProcess(R.id.loading,false);
                                            localPersistence.actualizarBaseDatosSincronizacion(session.getusename(),envioSincronizacionNube.getEsquemaManzana());
                                            Offline offline = localPersistence.getOffline("OFFLINE", session.getusename());
                                            offline.setActivo(isChecked);
                                            localPersistence.putOffLine(offline);
                                            visibleMensajeGlobal = false;
                                            viewComponent.progressBarProcess(R.id.loading,false);
                                            createAlert("Configuración Local","Se ha cambiado el modo a ONLINE" );
                                            createAlert2("Proceso Actualización","Sincronización en la nube exitoso");
                                        }
                                    }, 1000);

                                }
                            });
                        }else{
                            Offline offline = localPersistence.getOffline("OFFLINE", session.getusename());
                            offline.setActivo(isChecked);
                            localPersistence.putOffLine(offline);
                            viewComponent.progressBarProcess(R.id.loading,false);
                            createAlert("Configuración Local","Se ha cambiado el modo a ONLINE" );
                        }

                    }else{
                        createAlert("Error","Antes de cambiar el estado a offline. Para actualizar la sincronización de la nube, debe tener acceso a internet la aplicación por tal seguirá en modo OFFLINE." );
                        paso = false;
                    }
                }else{
                    Offline offline = localPersistence.getOffline("OFFLINE",session.getusename());
                    offline.setActivo(isChecked);
                    localPersistence.putOffLine(offline);
                    viewComponent.progressBarProcess(R.id.loading,false);
                    createAlert("Configuración Local","Se ha cambiado el modo a OFFLINE" );
                }
                viewComponent.progressBarProcess(R.id.loading,false);
            }
        });

        ImageView atras = (ImageView) findViewById(R.id.atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retornar();
            }
        });

    }

    public void createAlert(String title, String message ) {
        new AlertDialog.Builder(ConfiguracionLocal.this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent inte = new Intent(ConfiguracionLocal.this, MainActivity.class);
                        finish();
                        startActivity(inte);
                    }
                })
                .show();
    }

    public void createAlert2(String title, String message ) {
        new AlertDialog.Builder(ConfiguracionLocal.this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    /**
     * Metodo que devuelve la fecha
     * @return
     */
    public String getFechaActual(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    /**
     * Metodo que realiza el retorno
     */
    public void retornar(){
        Intent intent = new Intent(ConfiguracionLocal.this, MainActivity.class);
        startActivity(intent);
        ConfiguracionLocal.this.finish();
    }

    @Override
    public void onBackPressed() {
        retornar();
    }

}
