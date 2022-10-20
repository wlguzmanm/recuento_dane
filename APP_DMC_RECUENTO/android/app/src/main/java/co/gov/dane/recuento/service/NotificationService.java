package co.gov.dane.recuento.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import co.gov.dane.recuento.Helper.RetrofitClientInstance;
import co.gov.dane.recuento.MainActivity;
import co.gov.dane.recuento.R;
import co.gov.dane.recuento.Session;
import co.gov.dane.recuento.backend.Database;
import co.gov.dane.recuento.backend.EsquemaManzana;
import co.gov.dane.recuento.backend.Usuario;
import co.gov.dane.recuento.interfaces.IAuthentication;
import co.gov.dane.recuento.login;
import co.gov.dane.recuento.model.JwtViewModel;
import co.gov.dane.recuento.model.NotificacionesViewModel;
import co.gov.dane.recuento.model.RequestAuthentication;
import co.gov.dane.recuento.model.RequestStatusViewModel;
import co.gov.dane.recuento.model.ResponseToken;
import co.gov.dane.recuento.model.UserTokenViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService extends Service {

    public static final long NOTIFY_INTERVAL = 60 * 1000; // 60 seconds
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    private String token;
    private String username;
    private String rol;
    private String clave;

    private Session session;

    public String channelId = "MyApp_Conteo_DANE";

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

        session = new Session(NotificationService.this);

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
                    callConsultaStatus();
                   // callActualizarLogin();
                }
            });
        }

        /**
         * Metodo que actualiza el token con el usuario y clave
         */
        private void callActualizarLogin() {
            try{
                if(session!= null && session.getFechaExp()!= null && !session.getFechaExp().equals("")){
                    long fechaExp = 0;
                    long yourmilliseconds = System.currentTimeMillis();
                    if(isNumeric(session.getFechaExp())){
                        fechaExp = Long.valueOf(session.getFechaExp());
                    }else{
                        fechaExp = System.currentTimeMillis();
                    }

                    if(fechaExp <= yourmilliseconds){
                        RequestAuthentication authentication = new RequestAuthentication();
                        authentication.setUsername(session.getusename());
                        authentication.setPassword(session.getPassword());
                        authentication.setApp_name("SMC_BARRIDO");

                        IAuthentication service = RetrofitClientInstance.getRetrofitInstance().create(IAuthentication.class);
                        Call<ResponseToken> call = service.login("application/json",authentication);
                        call.enqueue(new Callback<ResponseToken>() {
                            @Override
                            public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                                if(response.errorBody()!= null){
                                    if(response.code()!=200){
                                        Toast.makeText(getApplicationContext(), "Para la actualización del token, el server generó error, se cerrará la sesion automaticamente...", Toast.LENGTH_LONG).show();
                                        session.borrarSession();
                                        Intent mainIntent = new Intent(NotificationService.this, login.class);
                                        startActivity(mainIntent);
                                    }
                                }else{
                                    JwtViewModel jwtViewModel =  getDecodedJwt(response.body().getToken());
                                    session.setTokenVigencia(response.body().getToken(), jwtViewModel.getExp()+"000");
                                    setActualizarDbToken(response.body().getToken(), jwtViewModel.getExp()+"000",session.getusename(),session.getPassword());
                                }
                            }
                            @Override
                            public void onFailure(Call<ResponseToken> call, Throwable t) {
                                Log.w("error proyecto AAAAAAA:" , t.getMessage().toString());
                                Toast.makeText(getApplicationContext(), "Error en el servicor : "+t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }catch (Exception e){
            }
        }

        /**
         * Metodo que dice si es numerico o
         *
         * @param cadena
         * @return
         */
        private boolean isNumeric(String cadena){
            try {
                Long.valueOf(cadena);
                return true;
            } catch (NumberFormatException nfe){
                return false;
            }
        }

        /**
         * Metodo que se actualiza el token y la vigencia
         *
         * @param token
         * @param exp
         * @param username
         * @param clave
         */
        private void setActualizarDbToken(String token, String exp, String username, String clave){
            try{
                Database dbDatabase = new Database(NotificationService.this);

                SQLiteDatabase spLiteDatabase=dbDatabase.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Usuario.TOKEN,token);
                values.put(Usuario.VIGENCIA,exp);

                spLiteDatabase.update(Usuario.TABLE_NAME, values,
                        Usuario.USUARIO+"= ? AND "+
                                Usuario.CLAVE+"= ? "
                        , new String[]{username,clave});
            }catch (Exception e){
            }
        }


        /**
         * Metodo de invocacion
         */
        private void callConsultaStatus(){
            NotificacionesViewModel objetoEnviar = new NotificacionesViewModel();
            objetoEnviar = cargarManzanasValidarSincronizacionSqlLite();

            if(objetoEnviar.getRequestStatus().size()>0){
                IAuthentication service = RetrofitClientInstance.getRetrofitInstance().create(IAuthentication.class);
                Call<NotificacionesViewModel> call = service.consultaStatus("application/json",token, objetoEnviar);

                call.enqueue(new Callback<NotificacionesViewModel>() {
                    @Override
                    public void onResponse(Call<NotificacionesViewModel> call, Response<NotificacionesViewModel> response) {
                        if(response.errorBody()!= null){
                        }else{
                            String mensaje = "";
                            int i = 0;
                            for(RequestStatusViewModel list: response.body().getRequestStatus()){
                                actualizarInformacionSqlLite(list);
                                mensaje = " IdManzana : "+ list.getFkManzana() + ", con estado : "+ list.getMessage();
                                i++;
                                createNotification("Sincronización con DANE-CENTRAL", "Sincronizacion: "+ mensaje, ""+i, i);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<NotificacionesViewModel> call, Throwable t) {
                        Log.d("Error:", t.getMessage());
                    }
                });
            }
        }

        /**
         * Metodo que carga la informacion de la sincronizacion a enviar para validar
         * @return
         */
        private NotificacionesViewModel cargarManzanasValidarSincronizacionSqlLite() {
            NotificacionesViewModel retorno  = new NotificacionesViewModel();
            retorno.setRequestStatus(cargarListadoManzanasValidar());
            return retorno;
        }

        /**
         * Metodo que busca en la sqllite
         * @return
         */
        private List<RequestStatusViewModel> cargarListadoManzanasValidar() {
            List<RequestStatusViewModel> retorno = new ArrayList<>();
            try{
                RequestStatusViewModel status = new RequestStatusViewModel();
                Database dbDatabase=new Database(NotificationService.this);
                SQLiteDatabase spLiteDatabase=dbDatabase.getWritableDatabase();

                Cursor resCursor =  spLiteDatabase.rawQuery( "SELECT DISTINCT id_manzana, imei, encuestador FROM manzana " +
                        "where finalizado = 'Si' and sincronizado = 'Si' and doble_sincronizado = 'Si' and sincronizado_nube is null", null );

                while (resCursor.moveToNext()) {
                    status = new RequestStatusViewModel();
                    status.setFkManzana(resCursor.getString(0));
                    status.setImei(resCursor.getString(1));
                    status.setUsuario(resCursor.getString(2));
                    retorno.add(status);
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
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationService.this);
            NotificationCompat.Builder builder =  new NotificationCompat.Builder(NotificationService.this, channelId + canal)
                    .setSmallIcon(R.drawable.ic_databse)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(text))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            Intent notificationIntent = new Intent(NotificationService.this, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent intent = PendingIntent.getActivity(NotificationService.this, 0,
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
                CharSequence name = "channel";
                String description = "channel DANE-CONTEO";
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

        /**
         * Metodo que actualiza la informacion de la db sqllite
         * @param requestStatus
         */
        private void actualizarInformacionSqlLite(RequestStatusViewModel requestStatus){
            Database dbDatabase=new Database(NotificationService.this);
            SQLiteDatabase spLiteDatabase=dbDatabase.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(EsquemaManzana.SINCRONIZADO_NUBE,(requestStatus.getValidar())? "Si":null);
            values.put(EsquemaManzana.FECHA_SINCRONIZACION,getFechaActual());
            values.put(EsquemaManzana.USUARIO_SINCRONIZO,requestStatus.getUsuario());
            values.put(EsquemaManzana.MENSAJE_SINCRONIZADO,requestStatus.getMessage());
            values.put(EsquemaManzana.STATUS_SINCRONIZO,requestStatus.getStatus());

            spLiteDatabase.update(EsquemaManzana.TABLE_NAME, values,
                    EsquemaManzana.ID_MANZANA+"= ? AND "+
                            EsquemaManzana.IMEI+"= ? AND "+
                            EsquemaManzana.ENCUESTADOR+"= ? "
                    , new String[]{requestStatus.getFkManzana(),requestStatus.getImei(),requestStatus.getUsuario()});
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
         * Metodo que actualiza el decode
         *
         * @param jwt
         * @return
         */
        public JwtViewModel getDecodedJwt(String jwt){
            String result = "";
            String[] parts = jwt.split("[.]");
            try
            {
                result += decoded(parts[1]);
                Gson gson = new GsonBuilder().create();
                JwtViewModel jwtViewModel = gson.fromJson(result, JwtViewModel.class);
                return jwtViewModel;
            }
            catch(Exception e)
            {
                JwtViewModel jwtViewModel = new JwtViewModel();
                return jwtViewModel;
            }
        }

        public String decoded(String JWTEncoded) throws Exception {
            try {
                String[] split = JWTEncoded.split("\\.");
                return  getJson(JWTEncoded);
            } catch (UnsupportedEncodingException e) {
                //Error
            }
            return "";
        }

        private String getJson(String strEncoded) throws UnsupportedEncodingException{
            byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
            return new String(decodedBytes, "UTF-8");
        }

    }
}
