package co.gov.dane.recuento.backend;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public  class DatabaseCargueInfoTablas {

    public Context context;

    public DatabaseCargueInfoTablas(Context context) {
        this.context = context;
    }

    /**
     * Metodo que cargar la informacion a las tablas
     * @param sqLiteDatabase
     */
    public void cargarIntoTablas(SQLiteDatabase sqLiteDatabase, String nombreArchivo){
        try {
            AssetManager assetManager = context.getAssets();
            InputStream stream = assetManager.open(nombreArchivo);
            BufferedReader r = new BufferedReader(new InputStreamReader(stream));
            StringBuilder total = new StringBuilder();
            String line;
            int i= 0;
            while ((line = r.readLine()) != null) {
                if(!line.isEmpty()){
                    //Log.e("sqLiteDatabase ---->", "Load assets----"+i+"-----"+line);
                    sqLiteDatabase.execSQL(line);
                }
                i++;
            }
        } catch (Exception ex) {
            Log.e("Error ---->", "Load assets/"+nombreArchivo, ex);
        }
    }
}
