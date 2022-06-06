package co.gov.dane.reconteo.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Timer;
import java.util.TimerTask;

import co.gov.dane.reconteo.ConfiguracionLocal;
import co.gov.dane.reconteo.R;
import co.gov.dane.reconteo.Session;
import co.gov.dane.reconteo.backend.Database;
import co.gov.dane.reconteo.model.UserTokenViewModel;

public class SincronizacionService extends Service {

    public static final long NOTIFY_INTERVAL = 3600 * 1000; // 60 seconds
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    private String token;
    private String username;
    private String rol;
    private String clave;

    private Session session;

    public String channelId = "MyApp_Conteo_DANE_Sincronizacion";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle extras = intent.getExtras();
        token = (String) extras.get("token");
        username = (String) extras.get("username");
        clave = (String) extras.get("clave");
        rol = (String) extras.get("rol");
        token = "Bearer " + token;

        session = new Session(SincronizacionService.this);

        if (mTimer != null)
            mTimer.cancel();
        mTimer = new Timer();

        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask (token, username, clave), 0, NOTIFY_INTERVAL);
        return super.onStartCommand(intent, flags, startId);
    }

    class TimeDisplayTimerTask extends TimerTask {
        private UserTokenViewModel vc;
        private String token;
        private String username;
        private String password;

        public TimeDisplayTimerTask(String token, String username, String password) {
            this.token = token;
            this.password = password;
            this.username = username;
        }

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    callConsultaSincronizacion();
                }
            });
        }

        /**
         * Metodo de invocacion
         */
        private void callConsultaSincronizacion(){
            String objetoEnviar = cargarListadoManzanasValidar();

            if(objetoEnviar!= null && !objetoEnviar.isEmpty()){
                String mensaje = "";
                if(this.username != null && !this.username.isEmpty()){
                    mensaje  =  "Hola, "+ this.username + ".\n\nEl sistema le recuerda entrar en la función de Configuración Local y cambiar la aplicación en modo Online. El sistema tiene registro(s) que deben ser sincronizado(s) en la nube - DANE Central. ";
                }else{
                    mensaje  =  "El sistema le recuerda entrar en la función de Configuración Local y cambiar la aplicación en modo Online. El sistema tiene registro(s) que deben ser sincronizado(s) en la nube - DANE Central. ";
                }
                createNotification("Sincronización con DANE-CENTRAL", mensaje + "\n\nLas manzana(s) por sincronizar a modo OFFLINE a ONLINE son : \n"+objetoEnviar + "\n\nMuchas Gracias.", "Canal Sincronizacion "+this.username, 1);
            }
        }

        /**
         * Metodo que busca en la sqllite
         * @return
         */
        private String cargarListadoManzanasValidar() {
            String retorno = "";
            try{
                Database dbDatabase=new Database(SincronizacionService.this);
                SQLiteDatabase spLiteDatabase=dbDatabase.getWritableDatabase();

                Cursor resCursor =  spLiteDatabase.rawQuery( "SELECT DISTINCT id_manzana, imei, encuestador FROM manzana " +
                        "where pendiente_sincronizar = 'Si' ", null );

                while (resCursor.moveToNext()) {
                    retorno = retorno +resCursor.getString(0)+"\n";
                }

                if(!retorno.isEmpty()){
                    retorno = retorno.substring(0, retorno.length() -2);
                }

                resCursor.close();
                dbDatabase.close();
                return retorno;
            }catch (Exception e){
                return retorno;
            }
        }

        /**
         * Creacion de la notificacion
         *
         * @param title
         * @param text
         */
        public void createNotification(String title, String text, String canal, int notificationId) {
            createNotificationChannel(channelId + canal);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(SincronizacionService.this);
            NotificationCompat.Builder builder =  new NotificationCompat.Builder(SincronizacionService.this, channelId + canal)
                    .setSmallIcon(R.drawable.ic_cloud_done)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(text))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            Intent notificationIntent = new Intent(SincronizacionService.this, ConfiguracionLocal.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent intent = PendingIntent.getActivity(SincronizacionService.this, 0,
                    notificationIntent, 0);
            builder.setContentIntent(intent);
            builder.setAutoCancel(true);
            notificationManager.notify(notificationId, builder.build());
        }

        /**
         * Creacion del canal
         */
        private void createNotificationChannel(String canal) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "channel Sincronizacion";
                String description = "channel DANE-CONTEO Sincronizacion";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                //IMPORTANCE_MAX MUESTRA LA NOTIFICACIÓN ANIMADA
                NotificationChannel channel = new NotificationChannel(canal, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
