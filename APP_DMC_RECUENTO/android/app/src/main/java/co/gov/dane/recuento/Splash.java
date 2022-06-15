package co.gov.dane.recuento;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.File;

public class Splash extends Activity {
    private Session session;


    private final int SPLASH_DISPLAY_LENGTH = 2000;
    public static final int MULTIPLE_PERMISSIONS = 5; // code you want.
    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 101;

    String[] permissions = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1){
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST_ACCOUNTS);
        }else{
            logica();
        }
    }

    public boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(hasAllPermissionsGranted(grantResults)){
            logica();
        }else {
            Mensajes mensaje=new Mensajes(this);
            mensaje.generarToast("Debe aceptar todos los permisos!");
        }
    }


    public void logica(){
        String ruta_db= Environment.getExternalStorageDirectory() + File.separator + "Censo_Economico"+ File.separator+"db";
        String ruta_mbtiles= Environment.getExternalStorageDirectory() + File.separator + "Censo_Economico"+ File.separator+"mbtiles";
        String ruta_backup= Environment.getExternalStorageDirectory() + File.separator + "Censo_Economico"+ File.separator+"backup";
        String reportes= Environment.getExternalStorageDirectory() + File.separator + "Censo_Economico"+ File.separator+"reportes";

        File folder_db = new File(ruta_db);
        File folder_mbtiles = new File(ruta_mbtiles);
        File folder_backup = new File(ruta_backup);
        File folder_reportes = new File(reportes);

        if (!folder_db.exists()) {
            folder_db.mkdirs();
        }
        if (!folder_mbtiles.exists()) {
            folder_mbtiles.mkdirs();
        }
        if (!folder_backup.exists()) {
            folder_backup.mkdirs();
        }
        if (!folder_reportes.exists()) {
            folder_reportes.mkdirs();
        }

        Controlador con=new Controlador(Splash.this);

        con.getUsers(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        session = new Session(Splash.this);
                        String usuario=session.getusename();
                        if(usuario!= null && usuario.equals("")){
                            Intent mainIntent = new Intent(Splash.this,login.class);
                            Splash.this.startActivity(mainIntent);
                            Splash.this.finish();
                        }else{
                            Intent mainIntent = new Intent(Splash.this,Entrada.class);
                            Splash.this.startActivity(mainIntent);
                            Splash.this.finish();
                        }
                    }
                }, SPLASH_DISPLAY_LENGTH);
            }
        });
    }
}