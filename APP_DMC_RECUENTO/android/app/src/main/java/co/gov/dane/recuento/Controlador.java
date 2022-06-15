package co.gov.dane.recuento;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import co.gov.dane.recuento.Helper.RetrofitClientInstance;
import co.gov.dane.recuento.backend.Database;
import co.gov.dane.recuento.backend.EsquemaAsignacion;
import co.gov.dane.recuento.backend.EsquemaManzana;
import co.gov.dane.recuento.interfaces.IAuthentication;
import co.gov.dane.recuento.model.EnvioFormularioViewModel;
import co.gov.dane.recuento.model.EsquemaManzanaEnvioViewModel;
import co.gov.dane.recuento.model.Offline;
import co.gov.dane.recuento.model.ResponseEnvioManzanaViewModel;
import retrofit2.Call;
import retrofit2.Callback;


public class Controlador {
    public Context context;
    private Database db;
    public Controlador(Context context){
        this.context=context;
    }

    private Offline offline = new Offline();

    String urlAsignaciones = "https://geoportal.dane.gov.co/laboratorio/serviciosjson/asignacion_cargas/servicios";

    public  void getUsers(final VolleyCallBack callBack){
        callBack.onSuccess();
    }

    public void uploadData(final EnvioFormularioViewModel envio, final VolleyCallBack callBack){

        db=new Database(context);

        Boolean hay_internet=isNetworkAvailable();
        if(hay_internet){
            Session session=new Session(context);
            final String usuario = session.getusename();
            ProgressDialog barProgressDialog = new ProgressDialog(context);
            barProgressDialog.setTitle("Subiendo Información ...");
            barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);
            barProgressDialog.setProgress(0);

            try {
                barProgressDialog.show();
                if(envio.getEsquemaManzana().size() > 0 ){
                    Gson gson = new GsonBuilder().create();
                    String json = gson.toJson(envio);
                    Log.d("json:", json);
                    IAuthentication service = RetrofitClientInstance.getRetrofitInstance().create(IAuthentication.class);
                    Call<ResponseEnvioManzanaViewModel> call = service.sincronizarFormulario("application/json;charset=UTF-8","Bearer  "+session.getToken(), envio);
                    call.enqueue(new Callback<ResponseEnvioManzanaViewModel>() {
                        @Override
                        public void onResponse(Call<ResponseEnvioManzanaViewModel> call, retrofit2.Response<ResponseEnvioManzanaViewModel> response) {
                            Mensajes mitoast =new Mensajes(context);
                            if(response.errorBody()!= null){
                                mitoast.generarToast("Error en el servidor al sincronizar","error");
                                barProgressDialog.dismiss();
                            }else{
                                String mensaje = "";
                                if(response.body().getValidar()){
                                    for (EsquemaManzanaEnvioViewModel lst : envio.getEsquemaManzana()){
                                        actualizarInformacionSqlLite(lst,true);
                                        db.putManzanaPendienteSincronizar(lst.getId_manzana(), "No");
                                    }
                                    mitoast.generarToast("Datos Enviados");
                                    barProgressDialog.dismiss();
                                }else{
                                    for (EsquemaManzanaEnvioViewModel lst : envio.getEsquemaManzana()){
                                        actualizarInformacionSqlLite(lst,false);
                                        db.putManzanaPendienteSincronizar(lst.getId_manzana(), "Si");
                                    }
                                    mitoast.generarToast("Datos Enviados, pero generaron error en la cola del servidor.","error");
                                    barProgressDialog.dismiss();
                                }
                            }
                            callBack.onSuccess();
                        }
                        @Override
                        public void onFailure(Call<ResponseEnvioManzanaViewModel> call, Throwable t) {
                            Log.d("Error:", t.getMessage());
                            callBack.onSuccess();
                        }
                    });
                }

            } catch (SQLiteConstraintException e) {
                Mensajes mitoast =new Mensajes(context);
                mitoast.generarToast("Error al subir información");
                callBack.onSuccess();
            }
        }else{
            Mensajes mitoast =new Mensajes(context);
            mitoast.generarToast("No hay conexión a internet");
        }
    }

    public  void getAsignaciones(String recuentista,final VolleyCallBack callBack){
        Boolean hay_internet=isNetworkAvailable();
        if(hay_internet){
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonRequestUusaurios = new JsonObjectRequest (
                    Request.Method.GET,
                    urlAsignaciones + "/consulta_asignacion_cargas.php?entity=asignacion&recuentista=" + recuentista,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                JSONObject object = response;
                                JSONArray keys = object.names ();

                                String key = keys.getString (1); // Here's your key
                                String value = object.getString (key); // Here's your value

                                Log.d("key:",value);
                                JSONArray jsonArray = new JSONArray(value);
                                Database db=new Database(context);
                                SQLiteDatabase sp=db.getWritableDatabase();

                                //borra la tabla de asignaciones cada vez.
                                sp.delete(EsquemaAsignacion.TABLE_NAME, null, null);

                                if(jsonArray.length()>0){
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        Log.d("Usuarios:", String.valueOf(obj));
                                        ContentValues values = new ContentValues();
                                        values.put(EsquemaAsignacion.MANZANA,obj.getString("OBJETO_ASIGNADO"));
                                        values.put(EsquemaAsignacion.USUARIO,recuentista);

                                        if(db.validarAsignacion(obj.getString("OBJETO_ASIGNADO"), recuentista)){
                                            //borra solo el usuario si en tal caso existiera
                                            sp.delete(EsquemaAsignacion.TABLE_NAME,   EsquemaAsignacion.MANZANA  +"=? AND "+EsquemaAsignacion.USUARIO+"=? " ,
                                                    new String[]{obj.getString("OBJETO_ASIGNADO"), recuentista});
                                        }
                                        //llena la tabla de usuarios.
                                        sp.insert(EsquemaAsignacion.TABLE_NAME, null, values);
                                    }
                                }else{
                                    Mensajes mitoast =new Mensajes(context);
                                    mitoast.generarToast("El usuario no tiene asignaciones de manzanas.","error");
                                }

                                sp.close();
                                callBack.onSuccess();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            Mensajes mitoast =new Mensajes(context);
                            mitoast.generarToast("Ocurrio un error en el servicio de validación de asignación de carga. Con error "+error.getMessage(),"error");
                            Log.d("error", String.valueOf(error));
                            callBack.onSuccess();
                        }
                    }
            );
            requestQueue.add(jsonRequestUusaurios);
        } else{
            Mensajes mitoast =new Mensajes(context);
            mitoast.generarToast("El dispositivo debe tener Internet, para poder realizar la validación de asignaciones.","error");
            callBack.onSuccess();
        }
    }


    /**
     * Metodo que valida conectividad con internet
     *
     * @return
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Metodo que actualiza la informacion de la db sqllite
     * @param requestStatus
     */
    private void actualizarInformacionSqlLite(EsquemaManzanaEnvioViewModel requestStatus, Boolean validar){
        Database dbDatabase = db;
        SQLiteDatabase spLiteDatabase=dbDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();

        if(validar){
            values.put(EsquemaManzana.SINCRONIZADO,"Si");
            values.put(EsquemaManzana.FECHA_CARGUE,getFechaActual());
            values.put(EsquemaManzana.DOBLE_SINCRONIZADO,"Si");
        }else{
            values.put(EsquemaManzana.SINCRONIZADO,"Si");
            values.put(EsquemaManzana.FECHA_CARGUE,getFechaActual());
        }

        spLiteDatabase.update(EsquemaManzana.TABLE_NAME, values,
                EsquemaManzana.ID_MANZANA+"= ? AND "+
                        EsquemaManzana.IMEI+"= ? AND "+
                        EsquemaManzana.ENCUESTADOR+"= ? "
                , new String[]{requestStatus.getId_manzana(),requestStatus.getImei(),requestStatus.getCod_enumerador()});
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

}
