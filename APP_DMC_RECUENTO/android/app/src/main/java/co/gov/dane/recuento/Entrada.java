package co.gov.dane.recuento;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import co.gov.dane.recuento.backend.Database;
import co.gov.dane.recuento.model.Offline;
import co.gov.dane.recuento.service.NotificationService;
import co.gov.dane.recuento.service.SincronizacionService;

public class Entrada extends AppCompatActivity {

    private LinearLayout conteo,novedad,salir_app;
    private Session session;
    private Database db;
    private Offline offline = new Offline();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrada);

        conteo=(LinearLayout) findViewById(R.id.conteo);
        novedad=(LinearLayout) findViewById(R.id.novedad);
        salir_app=(LinearLayout) findViewById(R.id.salir_app);

        session=new Session(Entrada.this);
        db = new Database(Entrada.this);


        if(session.getusename()==null){
            session.borrarSession();

            Intent mainIntent = new Intent(Entrada.this,login.class);
            startActivity(mainIntent);
            finish();
        }

        Bundle extras = getIntent().getExtras();
        Intent instenService = new Intent(this, NotificationService.class);
        instenService.putExtra("token",session.getToken());
        instenService.putExtra("username",session.getusename());
        instenService.putExtra("clave",session.getPassword());
        instenService.putExtra("rol",session.getrol());
        startService(instenService);

        Intent instenServiceSincronizacion = new Intent(this, SincronizacionService.class);
        instenServiceSincronizacion.putExtra("token",session.getToken());
        instenServiceSincronizacion.putExtra("username",session.getusename());
        instenServiceSincronizacion.putExtra("clave",session.getPassword());
        instenServiceSincronizacion.putExtra("rol",session.getrol());
        startService(instenServiceSincronizacion);

        conteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mensajes mitoast =new Mensajes(Entrada.this);
                if(session.getFechaExp()!= null && !session.getFechaExp().isEmpty()){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                    String fechaInicio = sdf.format(new Date());

                    Date date =new Date(Long.valueOf(session.getFechaExp()));
                    String fechaFin = sdf.format(date);

                    int resta = Long.valueOf(fechaFin).intValue() - Long.valueOf(fechaInicio).intValue();
                    if(resta > 0 && resta <= 3 ){
                        mitoast.dialogoMensajeError("Error Acceso","El sistema ha detectado que el Token que esta usando, debe ser renovado por uno nuevo prontamente, por favor, cambiar la configuración local a Internet, cerrar la sesión y garantizar que el dispositivo tenga acceso a Internet, para la renovación del Token.");
                    }

                    if(resta <= 0 ){
                        mitoast.dialogoMensajeError("Error Acceso", "El Token se encuentra vencido. Por favor,  cambiar la configuración local a Internet, cerrar la sesión y garantizar que el dispositivo tenga acceso a Internet, para la renovación del Token. ");
                        //mitoast.dialogoMensajeError("Error Acceso", "El sistema no lo dejara avanzar hasta que actualice el token.");
                    }
                    Intent mainIntent = new Intent(Entrada.this, MainActivity.class);
                    Entrada.this.startActivity(mainIntent);
                    //Entrada.this.finish();
                }else{
                    Intent mainIntent = new Intent(Entrada.this, MainActivity.class);
                    Entrada.this.startActivity(mainIntent);
                    //Entrada.this.finish();
                }
            }
        });

        novedad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                PackageManager manager = getPackageManager();
                try {
                    i = manager.getLaunchIntentForPackage("co.gov.dane.novedades");
                    if (i == null){
                        Toast.makeText(getApplicationContext(),
                                "Aplicación no instalada", Toast.LENGTH_SHORT).show();
                        throw new PackageManager.NameNotFoundException();
                    }else{
                        i.addCategory(Intent.CATEGORY_LAUNCHER);
                        i.putExtra("username",session.getusename());
                        startActivity(i);
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Aplicación no instalada", Toast.LENGTH_SHORT).show();
                }
            }
        });

        salir_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Entrada.this);
                builder.setTitle("Confirmación");
                builder.setMessage("¿ Desea salir del aplicativo ?");
                builder.setIcon(R.drawable.ic_menu_salir);
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        offline = db.getOffline("OFFLINE", session.getusename());
                        if(offline!= null && !offline.isActivo()){
                            Mensajes mitoast =new Mensajes(Entrada.this);
                            mitoast.generarToast("Recuerde que esta en modo ONLINE y requiere nuevamente autenticación.");
                            session.borrarSession();
                        }
                        Intent mainIntent = new Intent(Entrada.this,login.class);
                        startActivity(mainIntent);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                try{
                    AlertDialog alert = builder.create();
                    alert.show();
                }catch (Exception e){
                }
            }
        });

    }
}