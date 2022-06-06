package co.gov.dane.reconteo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusename(String usename,String nombre,String rol, String token, String clave, String exp) {
        prefs.edit().putString("username", usename).commit();
        prefs.edit().putString("nombre", nombre).commit();
        prefs.edit().putString("rol", rol).commit();
        prefs.edit().putString("token", token).commit();
        prefs.edit().putString("clave", clave).commit();
        prefs.edit().putString("exp", exp).commit();
    }

    public void setTokenVigencia(String token, String exp){
        prefs.edit().putString("token", token).commit();
        prefs.edit().putString("exp", exp).commit();
    }

    public String getFechaExp() {
        String exp = prefs.getString("exp","");
        return exp;
    }
    public String getusename() {
        String usename = prefs.getString("username","");
        return usename;
    }
    public String getnombre() {
        String nombre = prefs.getString("nombre","");
        return nombre;
    }

    public String getToken() {
        String token = prefs.getString("token","");
        return token;
    }

    public String getPassword() {
        String clave = prefs.getString("clave","");
        return clave;
    }

    public String getrol() {
        String rol = prefs.getString("rol","");
        return rol;
    }


    public void borrarSession(){
        prefs.edit().clear().commit();
    }
}
