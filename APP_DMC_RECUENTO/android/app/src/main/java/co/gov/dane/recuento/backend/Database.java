package co.gov.dane.recuento.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import co.gov.dane.recuento.model.ConteoManzana;
import co.gov.dane.recuento.model.ConteoEdificacion;
import co.gov.dane.recuento.model.ConteoUnidad;
import co.gov.dane.recuento.Preguntas.Edificacion;
import co.gov.dane.recuento.Preguntas.Manzana;
import co.gov.dane.recuento.Preguntas.UnidadEconomica;
import co.gov.dane.recuento.Util;
import co.gov.dane.recuento.model.EnvioFormularioViewModel;
import co.gov.dane.recuento.model.EsquemaEdificacionEnvioViewModel;
import co.gov.dane.recuento.model.EsquemaManzanaEnvioViewModel;
import co.gov.dane.recuento.model.EsquemaUnidadesEnvioViewModel;
import co.gov.dane.recuento.model.Offline;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Database extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 17;
    public static final String DATABASE_NAME = "Re_ConteoFormularioV_1_0_0.db";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Util util=new Util();

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + EsquemaManzana.TABLE_NAME + " ("
                + EsquemaManzana.ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + EsquemaManzana.ID_MANZANA + " TEXT,"
                + EsquemaManzana.FECHA_CONTEO + " TEXT, "
                + EsquemaManzana.DEPARTAMENTO + " TEXT, "
                + EsquemaManzana.MUNICIPIO + " TEXT, "
                + EsquemaManzana.CLASE + " TEXT, "
                + EsquemaManzana.CENTRO_POBLADO + " TEXT, "
                + EsquemaManzana.SECTOR_URBANO + " TEXT, "
                + EsquemaManzana.SECCION_URBANA + " TEXT, "
                + EsquemaManzana.MANZANA + " TEXT, "
                + EsquemaManzana.EXISTE_UNIDAD_ECONOMICA + " TEXT, "
                + EsquemaManzana.LATITUD + " TEXT, "
                + EsquemaManzana.LONGITUD + " TEXT, "
                + EsquemaManzana.ALTURA + " TEXT ,"
                + EsquemaManzana.PRECISION + " TEXT ,"
                + EsquemaManzana.BARRIO + " TEXT ,"
                + EsquemaManzana.IMEI + " TEXT ,"
                + EsquemaManzana.OBSERVACIONES + " TEXT ,"
                + EsquemaManzana.CODIGO_AG + " TEXT ,"
                + EsquemaManzana.NOVEDADES_CARTOGRAFICAS + " TEXT ,"
                + EsquemaManzana.ENCUESTADOR + " TEXT ,"
                + EsquemaManzana.SUPERVISOR + " TEXT ,"
                + EsquemaManzana.FINALIZADO + " TEXT ,"
                + EsquemaManzana.SINCRONIZADO + " TEXT ,"
                + EsquemaManzana.DOBLE_SINCRONIZADO + " TEXT ,"
                + EsquemaManzana.SINCRONIZADO_NUBE + " TEXT ,"
                + EsquemaManzana.FECHA_MODIFICACION + " TEXT,"
                + EsquemaManzana.FECHA_CARGUE + " TEXT,"
                + EsquemaManzana.FECHA_SINCRONIZACION + " TEXT,"
                + EsquemaManzana.USUARIO_SINCRONIZO + " TEXT,"
                + EsquemaManzana.MENSAJE_SINCRONIZADO + " TEXT,"
                + EsquemaManzana.STATUS_SINCRONIZO + " TEXT,"
                + EsquemaManzana.PENDIENTE_SINCRONIZAR + " TEXT"
                + ")");

        db.execSQL("CREATE TABLE " + EsquemaEdificacion.TABLE_NAME + " ("
                + EsquemaEdificacion.ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + EsquemaEdificacion.ID_MANZANA + " TEXT,"
                + EsquemaEdificacion.ID_EDIFICACION + " TEXT, "
                + EsquemaEdificacion.LATITUD + " TEXT, "
                + EsquemaEdificacion.LONGITUD + " TEXT, "
                + EsquemaEdificacion.IMEI + " TEXT ,"
                + EsquemaEdificacion.CHECK_BASE_DATOS + " TEXT, "
                + EsquemaEdificacion.FECHA_MODIFICACION + " TEXT "
                + ")");

        db.execSQL("CREATE TABLE " + EsquemaUnidadEconomica.TABLE_NAME + " ("
                + EsquemaUnidadEconomica.ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + EsquemaUnidadEconomica.ID_MANZANA + " TEXT,"
                + EsquemaUnidadEconomica.ID_EDIFICACION + " TEXT, "
                + EsquemaUnidadEconomica.ID_UNIDAD_ECONOMICA + " TEXT, "
                + EsquemaUnidadEconomica.IMEI + " TEXT ,"
                + EsquemaUnidadEconomica.TIPO_VIA_PRINCIPAL + " TEXT, "
                + EsquemaUnidadEconomica.VIA_PRINCIPAL + " TEXT, "
                + EsquemaUnidadEconomica.VIA_SECUNDARIA + " TEXT, "
                + EsquemaUnidadEconomica.PLACA_CUADRANTE + " TEXT, "
                + EsquemaUnidadEconomica.COMPLEMENTO_DIRECCION + " TEXT, "
                + EsquemaUnidadEconomica.UNIDADES_OCUPADAS + " TEXT, "
                + EsquemaUnidadEconomica.ESTADO_OCUPADO + " TEXT, "
                + EsquemaUnidadEconomica.ESTADO_DESOCUPADO + " TEXT, "
                + EsquemaUnidadEconomica.OBSERVACIONES_SN + " TEXT, "
                + EsquemaUnidadEconomica.ESTADO_OBRA + " TEXT, "
                + EsquemaUnidadEconomica.ESTABLECIMIENTO_FIJO + " TEXT, "
                + EsquemaUnidadEconomica.ESTABLECIMIENTO_SEMIFIJO + " TEXT, "
                + EsquemaUnidadEconomica.PUESTO_MOVIL + " TEXT, "
                + EsquemaUnidadEconomica.VIVIENDA_ACTITVIDAD_ECONOMICA + " TEXT, "
                + EsquemaUnidadEconomica.OBRA_EDIFICACION + " TEXT, "
                + EsquemaUnidadEconomica.SECTOR_COMERCIO + " TEXT, "
                + EsquemaUnidadEconomica.SECTOR_INDUSTRIA + " TEXT, "
                + EsquemaUnidadEconomica.SECTOR_SERVICIOS + " TEXT, "
                + EsquemaUnidadEconomica.SECTOR_TRANSPORTE + " TEXT, "
                + EsquemaUnidadEconomica.SECTOR_CONSTRUCCION + " TEXT, "
                + EsquemaUnidadEconomica.SECTOR_NO_APLICA + " TEXT, "
                + EsquemaUnidadEconomica.NOMBRE_UNIDAD_OBSERVACION + " TEXT, "
                + EsquemaUnidadEconomica.OBSERVACIONES_UNIDAD_OBSERVACION + " TEXT, "
                + EsquemaUnidadEconomica.FECHA_MODIFICACION + " TEXT "
                + ")");

        db.execSQL("CREATE TABLE " + Usuario.TABLE_NAME + " ("
                + Usuario.ID +" INTEGER PRIMARY KEY,"
                + Usuario.USUARIO + " TEXT NOT NULL,"
                + Usuario.CLAVE + " TEXT NOT NULL,"
                + Usuario.IMEI + " TEXT NOT NULL,"
                + Usuario.NOMBRE + " TEXT NOT NULL,"
                + Usuario.CORREO + " TEXT ,"
                + Usuario.VIGENCIA + " TEXT ,"
                + Usuario.TOKEN + " TEXT ,"
                + Usuario.ROL + " INTEGER,"
                + Usuario.ID_SUPERVISOR + " INTEGER"
                + ")");

        db.execSQL("CREATE TABLE " + EsquemaAsignacion.TABLE_NAME + " ("
                + EsquemaAsignacion.MANZANA +" TEXT PRIMARY KEY,"
                + EsquemaAsignacion.USUARIO + " TEXT NOT NULL"
                + ")");

        db.execSQL("CREATE TABLE " + EsquemaOffline.TABLE_NAME + " ("
                + EsquemaOffline.ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + EsquemaOffline.USERNAME + " TEXT,"
                + EsquemaOffline.ACTIVO + " TEXT "
                + ")");

        db.execSQL("CREATE TABLE " + EsquemaMapa.TABLE_NAME + " ("
                + EsquemaMapa.ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + EsquemaMapa.NOMBRE + " TEXT,"
                + EsquemaMapa.RUTA + " TEXT,"
                + EsquemaMapa.ACTIVO + " TEXT "
                + ")");

        db.execSQL("CREATE TABLE " + EsquemaMapaUsuario.TABLE_NAME + " ("
                + EsquemaMapaUsuario.ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + EsquemaMapaUsuario.MAPA + " TEXT,"
                + EsquemaMapaUsuario.USERNAME + " TEXT,"
                + EsquemaMapaUsuario.ACTIVO + " TEXT "
                + ")");




    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Usuario.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EsquemaUnidadEconomica.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EsquemaEdificacion.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EsquemaManzana.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EsquemaAsignacion.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EsquemaOffline.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    public Boolean guardarManzana(Manzana manzana, String id_formulario) {
        try {
            ContentValues values = new ContentValues();
            //Valido Existencia
            Manzana existe = getManzanaValidar(id_formulario);

            values.put(EsquemaManzana.ID_MANZANA, id_formulario);
            values.put(EsquemaManzana.FECHA_CONTEO, manzana.getFecha());
            values.put(EsquemaManzana.FECHA_MODIFICACION, util.getFechaActual());
            values.put(EsquemaManzana.DEPARTAMENTO, manzana.getDepartamento());
            values.put(EsquemaManzana.MUNICIPIO, manzana.getMunicipio());
            values.put(EsquemaManzana.CLASE, manzana.getClase());
            values.put(EsquemaManzana.CENTRO_POBLADO, manzana.getCentroPoblado());
            values.put(EsquemaManzana.SECTOR_URBANO, manzana.getSectorUrbano());
            values.put(EsquemaManzana.SECCION_URBANA, manzana.getSeccionUrbana());
            values.put(EsquemaManzana.MANZANA, manzana.getManzana());
            values.put(EsquemaManzana.EXISTE_UNIDAD_ECONOMICA, manzana.getPresenciaUnidades());
            values.put(EsquemaManzana.LATITUD, manzana.getLatitud());
            values.put(EsquemaManzana.LONGITUD, manzana.getLongitud());
            values.put(EsquemaManzana.ALTURA, manzana.getAltura());
            values.put(EsquemaManzana.PRECISION, manzana.getPrecision());
            values.put(EsquemaManzana.CODIGO_AG, manzana.getCodigoAG());
            values.put(EsquemaManzana.IMEI, manzana.getImei());
            values.put(EsquemaManzana.OBSERVACIONES, manzana.getObservaciones());
            values.put(EsquemaManzana.NOVEDADES_CARTOGRAFICAS, manzana.getNovedadesCartografias());
            values.put(EsquemaManzana.BARRIO, manzana.getBarrio());
            values.put(EsquemaManzana.FINALIZADO, manzana.getFinalizado());
            values.put(EsquemaManzana.ENCUESTADOR, manzana.getCod_enumerador());
            values.put(EsquemaManzana.SUPERVISOR, manzana.getSupervisor());
            values.put(EsquemaManzana.PENDIENTE_SINCRONIZAR, "No");

            if(existe== null){
                SQLiteDatabase db = getWritableDatabase();
                // int id = getMaxFormularios() + 1;
                // values.put(EsquemaManzana.ID, id);
                db.insert(EsquemaManzana.TABLE_NAME,null,values);
            }else{
                SQLiteDatabase db = getWritableDatabase();
                String[] args = new String[]{String.valueOf(id_formulario)};
                db.update(EsquemaManzana.TABLE_NAME, values,"ID_MANZANA=?",args);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        } finally {
        }
        //return db.insertWithOnConflict(EsquemaManzana.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE)>0;
    }

    public Boolean guardarUnidadEconomica(UnidadEconomica unidad, String id_manzana, String id_edificacion, String id_unidad){

        try{
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(EsquemaUnidadEconomica.TIPO_VIA_PRINCIPAL,unidad.getTipo_via_principal());
            values.put(EsquemaUnidadEconomica.VIA_PRINCIPAL,unidad.getVia_principal());
            values.put(EsquemaUnidadEconomica.VIA_SECUNDARIA,unidad.getVia_secundaria());
            values.put(EsquemaUnidadEconomica.PLACA_CUADRANTE,unidad.getPlaca_cuadrante());
            values.put(EsquemaUnidadEconomica.COMPLEMENTO_DIRECCION,unidad.getComplemento_direccion());
            values.put(EsquemaUnidadEconomica.UNIDADES_OCUPADAS,unidad.getUnidades_ocupadas());
            values.put(EsquemaUnidadEconomica.ESTADO_OCUPADO,unidad.getEstado_ocupado());
            values.put(EsquemaUnidadEconomica.OBSERVACIONES_SN,unidad.getObservaciones_sn());
            values.put(EsquemaUnidadEconomica.ESTADO_DESOCUPADO,unidad.getEstado_desocupado());
            values.put(EsquemaUnidadEconomica.ESTADO_OBRA,unidad.getEstado_obra());
            values.put(EsquemaUnidadEconomica.ESTABLECIMIENTO_FIJO,unidad.getEstablecimiento_fijo());
            values.put(EsquemaUnidadEconomica.ESTABLECIMIENTO_SEMIFIJO,unidad.getEstablecimiento_semifijo());
            values.put(EsquemaUnidadEconomica.PUESTO_MOVIL,unidad.getPuesto_movil());
            values.put(EsquemaUnidadEconomica.VIVIENDA_ACTITVIDAD_ECONOMICA,unidad.getVivienda_actividad_economica());
            values.put(EsquemaUnidadEconomica.OBRA_EDIFICACION,unidad.getObra_edificacion());
            values.put(EsquemaUnidadEconomica.SECTOR_COMERCIO,unidad.getComercio());
            values.put(EsquemaUnidadEconomica.SECTOR_INDUSTRIA,unidad.getIndustria());
            values.put(EsquemaUnidadEconomica.SECTOR_SERVICIOS,unidad.getServicios());
            values.put(EsquemaUnidadEconomica.SECTOR_TRANSPORTE,unidad.getTransporte());
            values.put(EsquemaUnidadEconomica.SECTOR_CONSTRUCCION,unidad.getConstruccion());
            values.put(EsquemaUnidadEconomica.SECTOR_NO_APLICA,unidad.getNo_aplica());
            values.put(EsquemaUnidadEconomica.NOMBRE_UNIDAD_OBSERVACION,unidad.getNombre_unidad_observacion());
            values.put(EsquemaUnidadEconomica.OBSERVACIONES_UNIDAD_OBSERVACION,unidad.getObservaciones_unidad_observacion());
            values.put(EsquemaUnidadEconomica.IMEI, unidad.getImei());

            return db.update(EsquemaUnidadEconomica.TABLE_NAME, values,
                    EsquemaUnidadEconomica.ID_MANZANA+"= ? AND "+
                            EsquemaUnidadEconomica.ID_EDIFICACION+"= ? AND "+
                            EsquemaUnidadEconomica.ID_UNIDAD_ECONOMICA+"= ? "
                    , new String[]{id_manzana,id_edificacion,id_unidad})>0;
        }catch (Exception e){
            return false;
        }
    }

    public Boolean guardarMapa(Edificacion edificacion, String id_manzana, String id_edificacion){

        try{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(EsquemaEdificacion.LATITUD,edificacion.getLatitud());
            values.put(EsquemaEdificacion.LONGITUD,edificacion.getLongitud());
            values.put(EsquemaEdificacion.IMEI, edificacion.getImei());

            return db.update(EsquemaEdificacion.TABLE_NAME, values,
                    EsquemaEdificacion.ID_MANZANA+"= ? AND "+
                            EsquemaEdificacion.ID_EDIFICACION+"= ?"

                    , new String[]{id_manzana,id_edificacion})>0;
        }catch (Exception e){
            return false;
        }
    }

    public void crearEdificacion(String id_manzana,String id_edificacion) {
        try{
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(EsquemaEdificacion.ID_MANZANA, id_manzana);
            values.put(EsquemaEdificacion.ID_EDIFICACION, id_edificacion);
            values.put(EsquemaEdificacion.FECHA_MODIFICACION, util.getFechaActual());

            db.insertWithOnConflict(EsquemaEdificacion.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            db.close();
        }catch (Exception e){
        }
    }

    public void crearUnidad(String id_manzana,String id_edificacion,String id_unidad) {
        try{
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(EsquemaUnidadEconomica.ID_MANZANA, id_manzana);
            values.put(EsquemaUnidadEconomica.ID_EDIFICACION, id_edificacion);
            values.put(EsquemaUnidadEconomica.ID_UNIDAD_ECONOMICA, id_unidad);
            values.put(EsquemaUnidadEconomica.FECHA_MODIFICACION, util.getFechaActual());

            db.insertWithOnConflict(EsquemaUnidadEconomica.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            db.close();
        }catch (Exception e){
        }
    }

    public int getMaxFormularios(){
        try{
            SQLiteDatabase db = getWritableDatabase();
            Cursor c = db.query(
                    EsquemaManzana.TABLE_NAME,  // Nombre de la tabla
                    null,  // Lista de Columnas a consultar
                    null,  // Columnas para la cláusula WHERE
                    null,  // Valores a comparar con las columnas del WHERE
                    null,  // Agrupar con GROUP BY
                    null,  // Condición HAVING para GROUP BY
                    null  // Cláusula ORDER BY
            );
            int count = c.getCount();

            return count;
        }catch (Exception e){
            return 0;
        }
    }

    public int getMaxEdificacion(String id_manzana){

        try{
            SQLiteDatabase db = getWritableDatabase();

            String whereClause = EsquemaEdificacion.ID_MANZANA+" = ? ";
            String[] whereArgs = new String[] {
                    id_manzana
            };

            Cursor c = db.query(EsquemaEdificacion.TABLE_NAME, new String[]{"MAX( cast("+EsquemaEdificacion.ID_EDIFICACION+" as integer))"}, whereClause, whereArgs, null, null, null);

            if(c.getCount()>0){
                c.moveToFirst();
                String max_id=c.getString(0);
                if(max_id ==null){
                    return 0;
                }else{
                    return Integer.parseInt(max_id);
                }
            }else{
                db.close();
                return 0;
            }
        }catch (Exception e){
            return 0;
        }
    }

    public int getMaxUnidad(String id_manzana,String id_edificacion){
        try{
            SQLiteDatabase db = getWritableDatabase();

            String whereClause = EsquemaUnidadEconomica.ID_MANZANA+" = ? AND "+EsquemaUnidadEconomica.ID_EDIFICACION+" = ? ";
            String[] whereArgs = new String[] {
                    id_manzana,
                    id_edificacion
            };

            Cursor c = db.query(EsquemaUnidadEconomica.TABLE_NAME, new String[]{"MAX(cast("+EsquemaUnidadEconomica.ID_UNIDAD_ECONOMICA+" as integer))"}, whereClause, whereArgs, null, null, null);

            if(c.getCount()>0){
                c.moveToFirst();
                String max_id=c.getString(0);
                if(max_id ==null){
                    return 0;
                }else{
                    return Integer.parseInt(max_id);
                }

            }else{
                db.close();
                return 0;
            }
        }catch (Exception e){
            return 0;
        }
    }



    public Boolean existeFormulario(String id_manzana){
        try{
            Boolean existe=false;
            String whereClause = EsquemaManzana.ID_MANZANA+" = ? ";
            String[] whereArgs = new String[] {
                    id_manzana
            };

            SQLiteDatabase db = getWritableDatabase();
            Cursor c = db.query(
                    EsquemaManzana.TABLE_NAME,  // Nombre de la tabla
                    null,  // Lista de Columnas a consultar
                    whereClause,  // Columnas para la cláusula WHERE
                    whereArgs,  // Valores a comparar con las columnas del WHERE
                    null,  // Agrupar con GROUP BY
                    null,  // Condición HAVING para GROUP BY
                    null  // Cláusula ORDER BY
            );
            int count = c.getCount();

            if(count>=1){
                existe=true;
            }

            return existe;
        }catch (Exception e){
            return false;
        }
    }

    public Boolean finalizarFormulario(String id_manzana){

        try{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(EsquemaManzana.FINALIZADO, "Si");
            return db.update(EsquemaManzana.TABLE_NAME, values,
                    EsquemaManzana.ID_MANZANA+"= ? "
                    , new String[]{id_manzana})>0;
        }catch (Exception e){
            return false;
        }
    }

    public Boolean pendienteSincronizarFormulario(String id_manzana, String valor){

        try{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(EsquemaManzana.PENDIENTE_SINCRONIZAR, valor);
            return db.update(EsquemaManzana.TABLE_NAME, values,
                    EsquemaManzana.ID_MANZANA+"= ? "
                    , new String[]{id_manzana})>0;
        }catch (Exception e){
            return false;
        }
    }

    public Boolean habilitarFormulario(String id_manzana){

        try{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(EsquemaManzana.FINALIZADO, "No");
            return db.update(EsquemaManzana.TABLE_NAME, values,
                    EsquemaManzana.ID_MANZANA+"= ? "
                    , new String[]{id_manzana})>0;
        }catch (Exception e){
            return false;
        }
    }

    public Boolean sincronizarFormulario(String id_manzana){

        try{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(EsquemaManzana.SINCRONIZADO, "Si");
            return db.update(EsquemaManzana.TABLE_NAME, values,
                    EsquemaManzana.ID_MANZANA+"= ? "
                    , new String[]{id_manzana})>0;
        }catch (Exception e){
            return false;
        }
    }

    public Boolean dobleSincronizacioFormulario(String id_manzana){

        try{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(EsquemaManzana.DOBLE_SINCRONIZADO, "Si");
            values.put(EsquemaManzana.FECHA_CARGUE, new Date().toString());
            return db.update(EsquemaManzana.TABLE_NAME, values,
                    EsquemaManzana.ID_MANZANA+"= ? "
                    , new String[]{id_manzana})>0;
        }catch (Exception e){
            return false;
        }

    }

    public Boolean seGuardoEnDatabaseFormulario(String id_manzana){

        try{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(EsquemaManzana.SINCRONIZADO_NUBE, "Si");
            values.put(EsquemaManzana.FECHA_SINCRONIZACION, new Date().toString());

            return db.update(EsquemaManzana.TABLE_NAME, values,
                    EsquemaManzana.ID_MANZANA+"= ? "
                    , new String[]{id_manzana})>0;
        }catch (Exception e){
            return false;
        }
    }


    public List<LatLng> getPuntosEdificaciones(String id_manzana, String id_edificacion){
        List<LatLng> puntos=new ArrayList<>();

        try{
            String whereClause = EsquemaEdificacion.ID_MANZANA+" = ? AND "+
                    EsquemaEdificacion.ID_EDIFICACION+" != ? ";

            String[] whereArgs = new String[] {
                    id_manzana,id_edificacion
            };

            SQLiteDatabase db = getWritableDatabase();
            Cursor c = db.query(
                    EsquemaEdificacion.TABLE_NAME,  // Nombre de la tabla
                    null,  // Lista de Columnas a consultar
                    whereClause,  // Columnas para la cláusula WHERE
                    whereArgs,  // Valores a comparar con las columnas del WHERE
                    null,  // Agrupar con GROUP BY
                    null,  // Condición HAVING para GROUP BY
                    null  // Cláusula ORDER BY
            );

            while (c.moveToNext()) {
                int index;

                index = c.getColumnIndexOrThrow(EsquemaEdificacion.LATITUD);
                String latitud=c.getString(index);

                index = c.getColumnIndexOrThrow(EsquemaEdificacion.LONGITUD);
                String longitud=c.getString(index);
                if(latitud !=null && longitud !=null){
                    LatLng punto=new LatLng(Double.parseDouble(latitud),Double.parseDouble(longitud));
                    puntos.add(punto);
                }


            }
            db.close();
            return puntos;
        }catch (Exception e){
            List<LatLng> retorno=new ArrayList<>();
            return retorno;
        }
    }


    public Map<String,LatLng> getLabelsEdificaciones(String id_manzana){

        Map<String,LatLng> puntos=new HashMap<>();

        String whereClause = EsquemaEdificacion.ID_MANZANA+" = ? ";

        String[] whereArgs = new String[] {
                id_manzana
        };

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(
                EsquemaEdificacion.TABLE_NAME,  // Nombre de la tabla
                null,  // Lista de Columnas a consultar
                whereClause,  // Columnas para la cláusula WHERE
                whereArgs,  // Valores a comparar con las columnas del WHERE
                null,  // Agrupar con GROUP BY
                null,  // Condición HAVING para GROUP BY
                null  // Cláusula ORDER BY
        );

        while (c.moveToNext()) {
            int index;

            index = c.getColumnIndexOrThrow(EsquemaEdificacion.ID_EDIFICACION);
            String edificacion=c.getString(index);

            index = c.getColumnIndexOrThrow(EsquemaEdificacion.LATITUD);
            String latitud=c.getString(index);

            index = c.getColumnIndexOrThrow(EsquemaEdificacion.LONGITUD);
            String longitud=c.getString(index);

            if(latitud !=null && longitud !=null){
                LatLng punto=new LatLng(Double.parseDouble(latitud),Double.parseDouble(longitud));
                puntos.put(edificacion,punto);
            }

        }
        db.close();
        return puntos;
    }


    public Manzana getManzana(String id_manzana){
        Manzana manzana=new Manzana();

        try{
            String whereClause = EsquemaManzana.ID_MANZANA+" = ? ";
            String[] whereArgs = new String[] {
                    id_manzana
            };

            SQLiteDatabase db = getWritableDatabase();
            Cursor c = db.query(
                    EsquemaManzana.TABLE_NAME,  // Nombre de la tabla
                    null,  // Lista de Columnas a consultar
                    whereClause,  // Columnas para la cláusula WHERE
                    whereArgs,  // Valores a comparar con las columnas del WHERE
                    null,  // Agrupar con GROUP BY
                    null,  // Condición HAVING para GROUP BY
                    null  // Cláusula ORDER BY
            );

            while (c.moveToNext()) {
                int index;

                index = c.getColumnIndexOrThrow(EsquemaManzana.FECHA_CONTEO);
                manzana.setFecha(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.DEPARTAMENTO);
                manzana.setDepartamento(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.MUNICIPIO);
                manzana.setMunicipio(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.CLASE);
                manzana.setClase(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.CENTRO_POBLADO);
                manzana.setCentroPoblado(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.SECTOR_URBANO);
                manzana.setSectorUrbano(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.SECCION_URBANA);
                manzana.setSeccionUrbana(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.MANZANA);
                manzana.setManzana(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.EXISTE_UNIDAD_ECONOMICA);
                manzana.setMPresenciaUnidades(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.LATITUD);
                manzana.setLatitud(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.LONGITUD);
                manzana.setLongitud(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.ALTURA);
                manzana.setAltura(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.PRECISION);
                manzana.setPrecision(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.ID_MANZANA);
                manzana.setId_manzana(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.FINALIZADO);
                manzana.setFinalizado(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.BARRIO);
                manzana.setBarrio(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.CODIGO_AG);
                manzana.setCodigoAG(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.OBSERVACIONES);
                manzana.setObservaciones(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.NOVEDADES_CARTOGRAFICAS);
                manzana.setNovedadesCartografias(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.ENCUESTADOR);
                manzana.setCod_enumerador(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.FECHA_MODIFICACION);
                manzana.setFechaModificacion(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.IMEI);
                manzana.setImei(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.SUPERVISOR);
                manzana.setSupervisor(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.DOBLE_SINCRONIZADO);
                manzana.setDoble_sincronizado(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.SINCRONIZADO_NUBE);
                manzana.setSincronizado_nube(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.PENDIENTE_SINCRONIZAR);
                manzana.setPendiente_sincronizar(c.getString(index));

            }
            db.close();
            return   manzana;
        }catch (Exception e){
            return manzana;
        }
    }

    public Manzana getManzanaValidar(String id_manzana){
        Manzana manzana=new Manzana();
        Boolean encontro = false;
        String whereClause = EsquemaManzana.ID_MANZANA+" = ? ";
        String[] whereArgs = new String[] {
                id_manzana
        };

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(
                EsquemaManzana.TABLE_NAME,  // Nombre de la tabla
                null,  // Lista de Columnas a consultar
                whereClause,  // Columnas para la cláusula WHERE
                whereArgs,  // Valores a comparar con las columnas del WHERE
                null,  // Agrupar con GROUP BY
                null,  // Condición HAVING para GROUP BY
                null  // Cláusula ORDER BY
        );

        while (c.moveToNext()) {
            int index;

            index = c.getColumnIndexOrThrow(EsquemaManzana.FECHA_CONTEO);
            manzana.setFecha(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.DEPARTAMENTO);
            manzana.setDepartamento(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.MUNICIPIO);
            manzana.setMunicipio(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.CLASE);
            manzana.setClase(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.CENTRO_POBLADO);
            manzana.setCentroPoblado(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.SECTOR_URBANO);
            manzana.setSectorUrbano(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.SECCION_URBANA);
            manzana.setSeccionUrbana(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.MANZANA);
            manzana.setManzana(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.EXISTE_UNIDAD_ECONOMICA);
            manzana.setMPresenciaUnidades(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.LATITUD);
            manzana.setLatitud(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.LONGITUD);
            manzana.setLongitud(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.ALTURA);
            manzana.setAltura(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.PRECISION);
            manzana.setPrecision(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.ID_MANZANA);
            manzana.setId_manzana(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.FINALIZADO);
            manzana.setFinalizado(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.BARRIO);
            manzana.setBarrio(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.CODIGO_AG);
            manzana.setCodigoAG(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.OBSERVACIONES);
            manzana.setObservaciones(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.NOVEDADES_CARTOGRAFICAS);
            manzana.setNovedadesCartografias(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.PENDIENTE_SINCRONIZAR);
            manzana.setPendiente_sincronizar(c.getString(index));
            encontro = true;
        }
        db.close();

        if(!encontro){
            return   null;
        }
        return   manzana;
    }

    public UnidadEconomica  getUnidadEconomica(String id_manzana,String id_edificacion,String id_unidad){
        UnidadEconomica unidad=new UnidadEconomica();

        String whereClause = EsquemaUnidadEconomica.ID_MANZANA+" = ? AND "+
                EsquemaUnidadEconomica.ID_EDIFICACION+" = ? AND "+
                EsquemaUnidadEconomica.ID_UNIDAD_ECONOMICA+" = ? ";

        String[] whereArgs = new String[] {
                id_manzana,id_edificacion,id_unidad
        };

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(
                EsquemaUnidadEconomica.TABLE_NAME,  // Nombre de la tabla
                null,  // Lista de Columnas a consultar
                whereClause,  // Columnas para la cláusula WHERE
                whereArgs,  // Valores a comparar con las columnas del WHERE
                null,  // Agrupar con GROUP BY
                null,  // Condición HAVING para GROUP BY
                null  // Cláusula ORDER BY
        );

        while (c.moveToNext()) {
            int index;

            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.TIPO_VIA_PRINCIPAL);
            unidad.setTipo_via_principal(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.VIA_PRINCIPAL);
            unidad.setVia_principal(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.VIA_SECUNDARIA);
            unidad.setVia_secundaria(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.PLACA_CUADRANTE);
            unidad.setPlaca_cuadrante(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.COMPLEMENTO_DIRECCION);
            unidad.setComplemento_direccion(c.getString(index));

            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.UNIDADES_OCUPADAS);
            unidad.setUnidades_ocupadas(c.getString(index));

            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.ESTADO_OCUPADO);
            unidad.setEstado_ocupado(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.ESTADO_DESOCUPADO);
            unidad.setEstado_desocupado(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.ESTADO_OBRA);
            unidad.setEstado_obra(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.ESTABLECIMIENTO_FIJO);
            unidad.setEstablecimiento_fijo(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.ESTABLECIMIENTO_SEMIFIJO);
            unidad.setEstablecimiento_semifijo(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.PUESTO_MOVIL);
            unidad.setPuesto_movil(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.VIVIENDA_ACTITVIDAD_ECONOMICA);
            unidad.setVivienda_actividad_economica(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.OBRA_EDIFICACION);
            unidad.setObra_edificacion(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.SECTOR_COMERCIO);
            unidad.setComercio(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.SECTOR_INDUSTRIA);
            unidad.setIndustria(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.SECTOR_SERVICIOS);
            unidad.setServicios(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.SECTOR_TRANSPORTE);
            unidad.setTransporte(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.SECTOR_CONSTRUCCION);
            unidad.setConstruccion(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.SECTOR_NO_APLICA);
            unidad.setNo_aplica(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.NOMBRE_UNIDAD_OBSERVACION);
            unidad.setNombre_unidad_observacion(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.OBSERVACIONES_UNIDAD_OBSERVACION);
            unidad.setObservaciones_unidad_observacion(c.getString(index));

            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.ID_MANZANA);
            unidad.setId_manzana(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.ID_EDIFICACION);
            unidad.setId_edificacion(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.ID_UNIDAD_ECONOMICA);
            unidad.setId_unidad(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.OBSERVACIONES_SN);
            unidad.setObservaciones_sn(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.FECHA_MODIFICACION);
            unidad.setFechaModificacion(c.getString(index));

        }
        db.close();

        return   unidad;
    }



    public Edificacion getMapa(String id_manzana,String id_edificacion){
        Edificacion edificacion=new Edificacion();

        String whereClause = EsquemaEdificacion.ID_MANZANA+" = ? AND "+
                EsquemaEdificacion.ID_EDIFICACION+" = ? ";

        String[] whereArgs = new String[] {
                id_manzana,id_edificacion
        };

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(
                EsquemaEdificacion.TABLE_NAME,  // Nombre de la tabla
                null,  // Lista de Columnas a consultar
                whereClause,  // Columnas para la cláusula WHERE
                whereArgs,  // Valores a comparar con las columnas del WHERE
                null,  // Agrupar con GROUP BY
                null,  // Condición HAVING para GROUP BY
                null  // Cláusula ORDER BY
        );

        while (c.moveToNext()) {
            int index;

            index = c.getColumnIndexOrThrow(EsquemaEdificacion.LATITUD);
            edificacion.setLatitud(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaEdificacion.LONGITUD);
            edificacion.setLongitud(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaEdificacion.ID_MANZANA);
            edificacion.setId_manzana(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaEdificacion.ID_EDIFICACION);
            edificacion.setId_edificacion(c.getString(index));


        }
        db.close();

        return   edificacion;
    }


    /* primer acordeon en la vista inicial*/
    public List<ConteoManzana> getFormulario(Boolean isCompleto, String encuestador) {
        List<ConteoManzana> formularios = new ArrayList<>();
        String sql = "SELECT id_manzana, id_manzana, fecha_modificado, finalizado, sincronizado, doble_sincronizado, sincronizado_nube " +
                "FROM manzana WHERE " ;

        if(isCompleto){  ///Procesadas
            sql = sql +
                    "(finalizado = 'Si') " +
                    "AND ( sincronizado is null or sincronizado = 'No' or sincronizado = 'Si' ) " +
                    "AND ( doble_sincronizado is null or doble_sincronizado = 'No'  or doble_sincronizado = 'Si' ) " +
                    "AND ( pendiente_sincronizar = 'No'  or  pendiente_sincronizar= 'Si' ) " +
                    "AND encuestador= '"+encuestador+"' ";

            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.getCount()>0) {
                cursor.moveToFirst();
                do {

                    String id = cursor.getString(0);
                    String nombre = cursor.getString(1);
                    String fecha = cursor.getString(2);
                    String finalizado = cursor.getString(3);
                    String sincronizado = cursor.getString(4);
                    String doble_sincronizado = cursor.getString(5);
                    String sincronizado_nube = cursor.getString(6);

                    ConteoManzana formulario = new ConteoManzana(id, nombre, fecha,finalizado,sincronizado,doble_sincronizado, sincronizado_nube);
                    formularios.add(formulario);

                } while (cursor.moveToNext());
            }
            cursor.close();

        }else{
            ///No Procesadas
            try {
                sql = sql +
                        "(finalizado is null OR finalizado = 'No'  OR finalizado = 'Si' ) " +
                        "AND ( sincronizado is null or sincronizado = 'No' ) " +
                        "AND ( doble_sincronizado is null or doble_sincronizado = 'No' ) " +
                        "AND pendiente_sincronizar= 'No' " +
                        "AND encuestador= '"+encuestador+"' ";

                SQLiteDatabase db = getWritableDatabase();
                Cursor cursor = db.rawQuery(sql, null);
                if (cursor != null && cursor.getCount()>0) {
                    cursor.moveToFirst();
                    do {

                        String id = cursor.getString(0);
                        String nombre = cursor.getString(1);
                        String fecha = cursor.getString(2);
                        String finalizado = cursor.getString(3);
                        String sincronizado = cursor.getString(4);
                        String doble_sincronizado = cursor.getString(5);
                        String sincronizado_nube = cursor.getString(6);

                        ConteoManzana formulario = new ConteoManzana(id, nombre, fecha,finalizado,sincronizado,doble_sincronizado, sincronizado_nube);
                        formularios.add(formulario);

                    } while (cursor.moveToNext());
                }
                cursor.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }

        return formularios;

    }

    public List<ConteoEdificacion> getAcordeonEdificacion(String id_manzana) {

        String whereClause = EsquemaEdificacion.ID_MANZANA+" = ? ";
        String[] whereArgs = new String[] {
                id_manzana
        };

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(
                EsquemaEdificacion.TABLE_NAME,  // Nombre de la tabla
                null,  // Lista de Columnas a consultar
                whereClause,  // Columnas para la cláusula WHERE
                whereArgs,  // Valores a comparar con las columnas del WHERE
                null,  // Agrupar con GROUP BY
                null,  // Condición HAVING para GROUP BY
                EsquemaEdificacion.ID +" DESC",  // Cláusula ORDER BY,
                null
        );

        List<ConteoEdificacion> formularios = new ArrayList<>();

        while (c.moveToNext()) {
            int index;

            index = c.getColumnIndexOrThrow(EsquemaEdificacion.ID_MANZANA);
            String id_manzana_get = c.getString(index);

            index = c.getColumnIndexOrThrow(EsquemaEdificacion.ID_EDIFICACION);
            String id_edificacion = c.getString(index);
            Log.d("id:",id_edificacion);

            index = c.getColumnIndexOrThrow(EsquemaEdificacion.ID_EDIFICACION);
            String nombre = c.getString(index);

            index = c.getColumnIndexOrThrow(EsquemaEdificacion.FECHA_MODIFICACION);
            String fecha = c.getString(index);

            index = c.getColumnIndexOrThrow(EsquemaEdificacion.ID);
            Long id = c.getLong(index);


            ConteoEdificacion formulario = new ConteoEdificacion(id_manzana_get,id_edificacion, nombre, fecha, id);
            formularios.add(formulario);

        }
        db.close();

        return formularios;

    }

    public List<ConteoUnidad> getAcordeonUnidades(String id_manzana, String id_edificacion) {

        String whereClause = EsquemaUnidadEconomica.ID_MANZANA+" = ? AND "+EsquemaUnidadEconomica.ID_EDIFICACION+" = ? ";
        String[] whereArgs = new String[] {
                id_manzana,
                id_edificacion
        };

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(
                EsquemaUnidadEconomica.TABLE_NAME,  // Nombre de la tabla
                null,  // Lista de Columnas a consultar
                whereClause,  // Columnas para la cláusula WHERE
                whereArgs,  // Valores a comparar con las columnas del WHERE
                null,  // Agrupar con GROUP BY
                null,  // Condición HAVING para GROUP BY
                EsquemaUnidadEconomica.ID +" DESC"  // Cláusula ORDER BY
        );

        List<ConteoUnidad> formularios = new ArrayList<>();

        while (c.moveToNext()) {
            int index;

            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.ID_MANZANA);
            String id_manzana_get = c.getString(index);

            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.ID_EDIFICACION);
            String id_edificacion_get = c.getString(index);

            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.ID_UNIDAD_ECONOMICA);
            String id_unidad = c.getString(index);

            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.ID_UNIDAD_ECONOMICA);
            String nombre = c.getString(index);

            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.FECHA_MODIFICACION);
            String fecha = c.getString(index);

            ConteoUnidad formulario = new ConteoUnidad(id_manzana_get,id_edificacion_get,id_unidad, nombre, fecha);
            formularios.add(formulario);

        }
        db.close();

        return formularios;

    }


    public boolean borrarManzana(String id_manzana)
    {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(EsquemaUnidadEconomica.TABLE_NAME, EsquemaUnidadEconomica.ID_MANZANA + "=? ",
                new String[]{id_manzana});

        db.delete(EsquemaEdificacion.TABLE_NAME, EsquemaEdificacion.ID_MANZANA + "=? ", new String[]{id_manzana});

        return db.delete(EsquemaManzana.TABLE_NAME, EsquemaManzana.ID_MANZANA + "=?", new String[]{id_manzana}) > 0;
    }

    public boolean borrarEdificacion(String id_manzana,String id_edificacion)
    {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(EsquemaUnidadEconomica.TABLE_NAME, EsquemaUnidadEconomica.ID_MANZANA + "=? AND "+
                        EsquemaUnidadEconomica.ID_EDIFICACION+"=? ",
                new String[]{id_manzana,id_edificacion});

        return db.delete(EsquemaEdificacion.TABLE_NAME, EsquemaEdificacion.ID_MANZANA + "=? AND "+
                EsquemaEdificacion.ID_EDIFICACION+"=?", new String[]{id_manzana,id_edificacion}) > 0;
    }

    public boolean borrarUnidad(String id_manzana,String id_edificacion,String id_unidad)
    {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(EsquemaUnidadEconomica.TABLE_NAME, EsquemaUnidadEconomica.ID_MANZANA + "=? AND "+
                        EsquemaUnidadEconomica.ID_EDIFICACION+"=? AND "+EsquemaUnidadEconomica.ID_UNIDAD_ECONOMICA+"=? ",
                new String[]{id_manzana,id_edificacion, id_unidad}) > 0;
    }

    public List<Manzana> getListadofinalizados(){
        List<Manzana> retorno = new ArrayList<>();

        String whereClause = EsquemaManzana.FINALIZADO+" = ? ";
        String[] whereArgs = new String[] {
                "Si"
        };

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(
                EsquemaManzana.TABLE_NAME,  // Nombre de la tabla
                null,  // Lista de Columnas a consultar
                whereClause,  // Columnas para la cláusula WHERE
                whereArgs,  // Valores a comparar con las columnas del WHERE
                null,  // Agrupar con GROUP BY
                null,  // Condición HAVING para GROUP BY
                null  // Cláusula ORDER BY
        );

        while (c.moveToNext()) {
            int index;
            Manzana manzana = new Manzana();
            index = c.getColumnIndexOrThrow(EsquemaManzana.FECHA_CONTEO);
            manzana.setFecha(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.DEPARTAMENTO);
            manzana.setDepartamento(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.MUNICIPIO);
            manzana.setMunicipio(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.CLASE);
            manzana.setClase(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.CENTRO_POBLADO);
            manzana.setCentroPoblado(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.SECTOR_URBANO);
            manzana.setSectorUrbano(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.SECCION_URBANA);
            manzana.setSeccionUrbana(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.MANZANA);
            manzana.setManzana(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.EXISTE_UNIDAD_ECONOMICA);
            manzana.setMPresenciaUnidades(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.LATITUD);
            manzana.setLatitud(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.LONGITUD);
            manzana.setLongitud(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.ALTURA);
            manzana.setAltura(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.PRECISION);
            manzana.setPrecision(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.ID_MANZANA);
            manzana.setId_manzana(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.FINALIZADO);
            manzana.setFinalizado(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.BARRIO);
            manzana.setBarrio(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.CODIGO_AG);
            manzana.setCodigoAG(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.OBSERVACIONES);
            manzana.setObservaciones(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.NOVEDADES_CARTOGRAFICAS);
            manzana.setNovedadesCartografias(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.ENCUESTADOR);
            manzana.setCod_enumerador(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.FECHA_MODIFICACION);
            manzana.setFechaModificacion(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.IMEI);
            manzana.setImei(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.SUPERVISOR);
            manzana.setSupervisor(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.DOBLE_SINCRONIZADO);
            manzana.setDoble_sincronizado(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.SINCRONIZADO_NUBE);
            manzana.setSincronizado_nube(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.PENDIENTE_SINCRONIZAR);
            manzana.setPendiente_sincronizar(c.getString(index));
            retorno.add(manzana);
        }
        db.close();

        return  retorno;
    }



    public List<Manzana> getAllManzanas(){

        List<Manzana> listado= new ArrayList<>();

        String whereClause = EsquemaManzana.FINALIZADO+" = ? ";
        String[] whereArgs = new String[] {
                "Si"
        };

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(
                EsquemaManzana.TABLE_NAME,  // Nombre de la tabla
                null,  // Lista de Columnas a consultar
                whereClause,  // Columnas para la cláusula WHERE
                whereArgs,  // Valores a comparar con las columnas del WHERE
                null,  // Agrupar con GROUP BY
                null,  // Condición HAVING para GROUP BY
                null  // Cláusula ORDER BY
        );


        while (c.moveToNext()) {
            int index;

            index = c.getColumnIndexOrThrow(EsquemaManzana.ID_MANZANA);
            String id_manzana_get = c.getString(index);

            Manzana mz = new Manzana(id_manzana_get);
            listado.add(mz);

        }


        db.close();
        return listado;
    }

    public Boolean getFinalizadaManzana(String id_manzana){

        String whereClause = EsquemaManzana.ID_MANZANA+" = ? ";
        String[] whereArgs = new String[] {
                id_manzana
        };

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(
                EsquemaManzana.TABLE_NAME,  // Nombre de la tabla
                null,  // Lista de Columnas a consultar
                whereClause,  // Columnas para la cláusula WHERE
                whereArgs,  // Valores a comparar con las columnas del WHERE
                null,  // Agrupar con GROUP BY
                null,  // Condición HAVING para GROUP BY
                null  // Cláusula ORDER BY
        );


        while (c.moveToNext()) {
            int index;

            index = c.getColumnIndexOrThrow(EsquemaManzana.ID_MANZANA);
            String id_manzana_get = c.getString(index);

            index = c.getColumnIndexOrThrow(EsquemaManzana.FINALIZADO);
            String finalizado = c.getString(index);

            if(finalizado.equals("Si")){
                db.close();
                return true;
            }else{
                db.close();
                return false;
            }
        }
        return false;
    }

    /**
     * Metodo si tiene edificaciones
     *
     * @param id_manzana
     * @return
     */
    public Boolean tieneEdificaciones(String id_manzana){
        List<Edificacion> edificacions = getAllEdificaciones(id_manzana);
        if(edificacions!= null && edificacions.size()>0){
            return true;
        }else{
            return  false;
        }
    }

    /**
     * Metodo de eliminacion de edificaciones y unidades
     *
     * @param id_manzana
     */
    public void eliminarEdificacionesUnidades(String id_manzana){

        List<Edificacion> edificacions = getAllEdificaciones(id_manzana);

        try{
            for (Edificacion lista: edificacions) {
                List<UnidadEconomica> unidades = getAllUnidades(id_manzana,lista.getId_edificacion());
                for (UnidadEconomica listaUni : unidades) {
                    SQLiteDatabase db = getWritableDatabase();
                    db.delete(EsquemaUnidadEconomica.TABLE_NAME, EsquemaUnidadEconomica.ID_MANZANA + "=? AND "+
                                    EsquemaUnidadEconomica.ID_EDIFICACION+"=? AND "+EsquemaUnidadEconomica.ID_UNIDAD_ECONOMICA + "=? ",
                            new String[]{id_manzana,lista.getId_edificacion(),listaUni.getId_unidad()});
                    db.close();
                }
                SQLiteDatabase db = getWritableDatabase();
                db.delete(EsquemaEdificacion.TABLE_NAME, EsquemaEdificacion.ID_MANZANA + "=? AND "+
                                EsquemaEdificacion.ID_EDIFICACION+"=? ",
                        new String[]{id_manzana,lista.getId_edificacion()});
                db.close();
            }
        }catch (Exception e){
            Log.d("id:",e.getMessage());
        }

    }

    /**
     * Metodo que carga todas las edificaciones
     *
     * @param id_manzana
     * @return
     */
    public List<Edificacion> getAllEdificaciones(String id_manzana){

        List<Edificacion> listado= new ArrayList<>();

        String whereClause = EsquemaEdificacion.ID_MANZANA+" = ? ";
        String[] whereArgs = new String[] {
                id_manzana
        };

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(
                EsquemaEdificacion.TABLE_NAME,  // Nombre de la tabla
                null,  // Lista de Columnas a consultar
                whereClause,  // Columnas para la cláusula WHERE
                whereArgs,  // Valores a comparar con las columnas del WHERE
                null,  // Agrupar con GROUP BY
                null,  // Condición HAVING para GROUP BY
                null  // Cláusula ORDER BY
        );


        while (c.moveToNext()) {
            int index;

            index = c.getColumnIndexOrThrow(EsquemaEdificacion.ID_EDIFICACION);
            String id_edi_get = c.getString(index);

            Edificacion ed = new Edificacion(id_edi_get);

            index = c.getColumnIndexOrThrow(EsquemaEdificacion.IMEI);
            ed.setImei(c.getString(index));

            index = c.getColumnIndexOrThrow(EsquemaEdificacion.LATITUD);
            ed.setLatitud(c.getString(index));

            index = c.getColumnIndexOrThrow(EsquemaEdificacion.LONGITUD);
            ed.setLongitud(c.getString(index));

            index = c.getColumnIndexOrThrow(EsquemaEdificacion.ID_EDIFICACION);
            ed.setId_edificacion(c.getString(index));

            index = c.getColumnIndexOrThrow(EsquemaEdificacion.ID_MANZANA);
            ed.setId_manzana(c.getString(index));

            index = c.getColumnIndexOrThrow(EsquemaEdificacion.FECHA_MODIFICACION);
            ed.setFechaModificacion(c.getString(index));

            listado.add(ed);
        }
        db.close();
        return listado;
    }

    public List<UnidadEconomica> getAllUnidades(String id_manzana,String id_edificacion){

        List<UnidadEconomica> listado= new ArrayList<>();

        String whereClause = EsquemaUnidadEconomica.ID_MANZANA+" = ? AND "+EsquemaUnidadEconomica.ID_EDIFICACION+" = ? ";
        String[] whereArgs = new String[] {
                id_manzana,
                id_edificacion
        };

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(
                EsquemaUnidadEconomica.TABLE_NAME,  // Nombre de la tabla
                null,  // Lista de Columnas a consultar
                whereClause,  // Columnas para la cláusula WHERE
                whereArgs,  // Valores a comparar con las columnas del WHERE
                null,  // Agrupar con GROUP BY
                null,  // Condición HAVING para GROUP BY
                null  // Cláusula ORDER BY
        );


        while (c.moveToNext()) {
            int index;

            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.ID_UNIDAD_ECONOMICA);
            String id_edi_get = c.getString(index);

            UnidadEconomica ed = new UnidadEconomica(id_edi_get);
            listado.add(ed);

        }


        db.close();
        return listado;
    }

    public String getResumen(String id_manzana){

        String resumen="";
        SQLiteDatabase db = getWritableDatabase();

        Cursor c =db.rawQuery("select \n" +
                "\"Total Edificaciones: \" ||\n" +
                "(select count(cast(id_manzana as integer)) from edificacion where id_manzana=?) || \",\" ||\n" +
                "\"Total Unidades: \" || (\n" +
                "sum(cast(estado_ocupado as integer))+\n" +
                "sum(cast(estado_desocupado as integer))+\n" +
                "sum(cast(estado_obra as integer))) ||\n" +
                "\",,Total Ocupados: \" || sum(cast(estado_ocupado as integer)) || \",Total Desocupados: \" ||\n" +
                "sum(cast(estado_desocupado as integer)) || \",Total Obras: \" ||\n" +
                "sum(cast(estado_obra as integer))\n" +
                "from unidad_economica where id_manzana=?", new String[] {id_manzana,id_manzana});


        while (c.moveToNext()) {
            if(c.getString(0)==null){
                resumen="Manzana sin unidades";
            }else{
                resumen = c.getString(0);
            }

        }


        db.close();
        return resumen;
    }


    public Boolean getAsignacion(String cod_manzana){
        Boolean resultado = false;
        try{
            String whereClause = EsquemaAsignacion.MANZANA + " = ?";
            String[] whereArgs = new String[] {
                    cod_manzana
            };

            SQLiteDatabase db = getWritableDatabase();
            Cursor c = db.query(
                    EsquemaAsignacion.TABLE_NAME,  // Nombre de la tabla
                    null,  // Lista de Columnas a consultar
                    whereClause,  // Columnas para la cláusula WHERE
                    whereArgs,  // Valores a comparar con las columnas del WHERE
                    null,  // Agrupar con GROUP BY
                    null,  // Condición HAVING para GROUP BY
                    null  // Cláusula ORDER BY
            );
            if(c.getCount() > 0){
                resultado = true;
            }
            db.close();
        }catch (Exception e){
            return false;
        }
        return resultado;

    }

    public Boolean getAsignacionManzanaUsuario(String usuario){
        Boolean resultado = false;
        try{
            String whereClause = EsquemaAsignacion.USUARIO + " = ?";
            String[] whereArgs = new String[] {
                    usuario
            };

            SQLiteDatabase db = getWritableDatabase();
            Cursor c = db.query(
                    EsquemaAsignacion.TABLE_NAME,  // Nombre de la tabla
                    null,  // Lista de Columnas a consultar
                    whereClause,  // Columnas para la cláusula WHERE
                    whereArgs,  // Valores a comparar con las columnas del WHERE
                    null,  // Agrupar con GROUP BY
                    null,  // Condición HAVING para GROUP BY
                    null  // Cláusula ORDER BY
            );
            if(c.getCount() > 0){
                resultado = true;
            }
            db.close();
        }catch (Exception e){
            return false;
        }
        return resultado;
    }


    /**
     *
     * @param offline
     */
    public void putOffLine(Offline offline){
        String[] args = new String[]{ offline.getUsername()};
        ContentValues cv = new ContentValues();
        cv.put("USERNAME", offline.getUsername());
        cv.put("ACTIVO", offline.isActivo());

        SQLiteDatabase db = getWritableDatabase();
        db.update("OFFLINE",cv ," USERNAME=? ",args);
    }

    public synchronized Offline getOffline(String tipo, String username) {
        Offline offline = null;
        try {
            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery("select ID,  USERNAME, ACTIVO from OFFLINE where USERNAME = '"+username+"'", null);
            int i = 0;
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    offline =  new Offline( //
                            cursor.isNull(0) ? null : cursor.getLong(0), // id
                            cursor.isNull(1) ? null : cursor.getString(1), // username
                            cursor.isNull(2) ? null : cursor.getShort(2) != 0 // activo
                    );

                } while (cursor.moveToNext());
            }else{
                ContentValues contentValues = new ContentValues();
                contentValues.put("USERNAME",username);
                contentValues.put("ACTIVO",0);
                db.insert("OFFLINE", null, contentValues);

                offline =  new Offline( 0L, username,false);
            }
            cursor.close();

        } catch (Exception e) {
//            e.printStackTrace(localFilesManager.get);
        } finally {
        }
        return offline;
    }

    public EnvioFormularioViewModel getCargarSincronizar(String username) {
        EnvioFormularioViewModel envio = new EnvioFormularioViewModel();
        envio.setFechaInicial(getFechaActual());

        List<EsquemaManzanaEnvioViewModel> lstManzana = new ArrayList<>();

        List<Manzana> listado_manzanas =  getValidarPendientesSincronizarOffline(username);

        for (Manzana manzana : listado_manzanas) {
            Manzana mz = getManzana(manzana.getId_manzana());
            EsquemaManzanaEnvioViewModel retornoManzana = new EsquemaManzanaEnvioViewModel();
            retornoManzana  = getManzana(mz);
            retornoManzana.setCod_enumerador(username);

            List<Edificacion> listado_edificaciones = getAllEdificaciones(manzana.getId_manzana());

            List<EsquemaEdificacionEnvioViewModel> retornoEdificacion = new ArrayList<>();
            for (Edificacion edificacion : listado_edificaciones) {
                EsquemaEdificacionEnvioViewModel objetoEdifi = new EsquemaEdificacionEnvioViewModel();
                objetoEdifi = getEdificacion(edificacion);

                List<UnidadEconomica> listado_unidades = getAllUnidades(manzana.getId_manzana(),edificacion.getId_edificacion());
                List<EsquemaUnidadesEnvioViewModel> lstUnidadEconomica = new ArrayList<>();
                for (UnidadEconomica unidadEconomica : listado_unidades) {
                    EsquemaUnidadesEnvioViewModel objetoUnidad = new EsquemaUnidadesEnvioViewModel();
                    UnidadEconomica unidad = getUnidadEconomica(manzana.getId_manzana(), edificacion.getId_edificacion(),unidadEconomica.getId_unidad());
                    objetoUnidad = getUnidadEconomica(unidad);
                    lstUnidadEconomica.add(objetoUnidad);
                }
                objetoEdifi.setUnidades(lstUnidadEconomica);
                retornoEdificacion.add(objetoEdifi);
            }
            retornoManzana.setEsquemaEdificacion(retornoEdificacion);
            lstManzana.add(retornoManzana);

        }
        envio.setEsquemaManzana(lstManzana);

        return envio;
    }

    public List<Manzana> getValidarPendientesSincronizarOffline(String username){
        List<Manzana> retorno = new ArrayList<>();

        try {
            String sql = "SELECT id, id_manzana  FROM manzana " +
                    "WHERE encuestador = '"+username+"' AND finalizado = 'Si' and pendiente_sincronizar = 'Si' ";
            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.getCount()>0) {
                cursor.moveToFirst();
                do {
                    Manzana manzana =  new Manzana( //
                            cursor.isNull(0) ? null : cursor.getString(0), // id
                            cursor.isNull(1) ? null : cursor.getString(1) // id_manzana
                    );
                    retorno.add(manzana);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return retorno;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return retorno;
    }


    /**
     * Metodo que valida l username y el password a modo offline
     * @param username
     * @param password
     * @return
     */
    public Boolean validarUserPassword(String username, String password){
        Boolean retorno = false;

        try {
            String sql = "SELECT id, usuario, clave FROM usuarios " +
                    "WHERE usuario = '"+username+"' AND clave = '"+password+"' ";
            SQLiteDatabase db = getWritableDatabase();
            Cursor c = db.rawQuery(sql, null);
            if (c != null && c.getCount()>0) {
                c.moveToFirst();
                do {
                    retorno = true;
                } while (c.moveToNext());
            }
            c.close();
            return retorno;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return retorno;
    }

    /**
     * Metodo que valida l username
     * @param username
     * @return
     */
    public Boolean validarUser(String username){
        Boolean retorno = false;

        try {
            String sql = "SELECT id, usuario, clave FROM usuarios " +
                    "WHERE usuario = '"+username+"' ";
            SQLiteDatabase db = getWritableDatabase();
            Cursor c = db.rawQuery(sql, null);
            if (c != null && c.getCount()>0) {
                c.moveToFirst();
                do {
                    retorno = true;
                } while (c.moveToNext());
            }
            c.close();
            return retorno;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return retorno;
    }

    /**
     * Metodo que valida la asignacion
     *
     * @param idManzana
     * @param recuentista
     * @return
     */
    public Boolean validarAsignacion(String idManzana, String recuentista){
        Boolean retorno = false;

        try {
            String sql = "SELECT manzana, usuario FROM asignacion " +
                    "WHERE manzana = '"+idManzana+"' and usuario = '"+recuentista+"'";
            SQLiteDatabase db = getWritableDatabase();
            Cursor c = db.rawQuery(sql, null);
            if (c != null && c.getCount()>0) {
                c.moveToFirst();
                do {
                    retorno = true;
                } while (c.moveToNext());
            }
            c.close();
            return retorno;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return retorno;
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
     * Metodo que setea valores de manzana
     *
     * @param manzana
     * @return
     */
    private EsquemaManzanaEnvioViewModel getManzana(Manzana manzana){
        EsquemaManzanaEnvioViewModel retorno = new EsquemaManzanaEnvioViewModel();
        retorno.setAltura(manzana.getAltura());
        retorno.setBarrio(manzana.getBarrio());
        retorno.setCentro_poblado(manzana.getCentroPoblado());
        retorno.setClase(manzana.getClase());
        retorno.setCodigoAG(manzana.getCodigoAG());
        retorno.setDepartamento(manzana.getDepartamento());
        retorno.setFechaConteo(manzana.getFecha());
        retorno.setFechaModificacion(manzana.getFechaModificacion());
        retorno.setFinalizado(manzana.getFinalizado());
        retorno.setId_manzana(manzana.getId_manzana());
        retorno.setImei(manzana.getImei());
        retorno.setLatitud(manzana.getLatitud());
        retorno.setLongitud(manzana.getLongitud());
        retorno.setManzana(manzana.getManzana());
        retorno.setMunicipio(manzana.getMunicipio());
        retorno.setNovedadesCartograficas(manzana.getNovedadesCartografias());
        retorno.setObservaciones(manzana.getObservaciones());
        retorno.setPrecision(manzana.getPrecision());
        retorno.setPresencia_und(manzana.getPresenciaUnidades());
        retorno.setSeccion_urbana(manzana.getSeccionUrbana());
        retorno.setSector_urbano(manzana.getSectorUrbano());
        retorno.setSupervisor(manzana.getSupervisor());
        return retorno;
    }

    /**
     * Metodo que setea valores de edificacion
     *
     * @param edificacion
     * @return
     */
    private EsquemaEdificacionEnvioViewModel getEdificacion(Edificacion edificacion){
        EsquemaEdificacionEnvioViewModel retorno = new EsquemaEdificacionEnvioViewModel();
        retorno.setFechaModificacion(edificacion.getFechaModificacion());
        retorno.setId_edificacion(edificacion.getId_edificacion());
        retorno.setId_manzana(edificacion.getId_manzana());
        retorno.setId_manzana_edificacion(edificacion.getId_manzana()+edificacion.getId_edificacion());
        retorno.setLatitud(edificacion.getLatitud());
        retorno.setLongitud(edificacion.getLongitud());

        return retorno;
    }

    /**
     * Metodo que setea valores de unidades
     *
     * @param unidad
     * @return
     */
    private EsquemaUnidadesEnvioViewModel getUnidadEconomica(UnidadEconomica unidad){
        EsquemaUnidadesEnvioViewModel retorno = new EsquemaUnidadesEnvioViewModel();
        retorno.setComplemento_direccion(unidad.getComplemento_direccion());
        retorno.setUnidad_ocupada(unidad.getUnidades_ocupadas());
        retorno.setEstablecimiento_fijo(unidad.getEstablecimiento_fijo());
        retorno.setEstablecimiento_semifijo(unidad.getEstablecimiento_semifijo());
        retorno.setEstado_desocupado(unidad.getEstado_desocupado());
        retorno.setEstado_obra(unidad.getEstado_obra());
        retorno.setEstado_ocupado(unidad.getEstado_ocupado());
        retorno.setFecha_modificacion(unidad.getFechaModificacion());
        retorno.setId_edificacion(unidad.getId_edificacion());
        retorno.setId_manzana(unidad.getId_manzana());
        retorno.setId_manzana_edificio_unidad(unidad.getId_manzana()+unidad.getId_edificacion()+unidad.getId_unidad());
        retorno.setId_unidad_economica(unidad.getId_unidad());
        retorno.setNombre_unidad_observacion(unidad.getNombre_unidad_observacion());
        retorno.setObra_edificacion(unidad.getObra_edificacion());
        retorno.setObservaciones_sn(unidad.getObservaciones_sn());
        retorno.setObservaciones_unidad_observ(unidad.getObservaciones_unidad_observacion());
        retorno.setPlaca_cuadrante(unidad.getPlaca_cuadrante());
        retorno.setPuesto_movil(unidad.getPuesto_movil());
        retorno.setSector_comercio(unidad.getComercio());
        retorno.setSector_construccion(unidad.getConstruccion());
        retorno.setSector_industria(unidad.getIndustria());
        retorno.setSector_no_aplica(unidad.getNo_aplica());
        retorno.setSector_servicios(unidad.getServicios());
        retorno.setSector_transporte(unidad.getTransporte());
        retorno.setTipo_via_principal(unidad.getTipo_via_principal());
        retorno.setVia_principal(unidad.getVia_principal());
        retorno.setVia_secundaria(unidad.getVia_secundaria());
        retorno.setVivienda_actividad_eco(unidad.getVivienda_actividad_economica());

        return retorno;
    }


    /**
     * Metodoq que actualiza la información de la base de datos.
     *
     * @param respuestaSincronizacion
     */
    public void actualizarBaseDatosSincronizacion(String username, List<EsquemaManzanaEnvioViewModel> respuestaSincronizacion){
        for (EsquemaManzanaEnvioViewModel lista : respuestaSincronizacion){
            String[] args = new String[]{lista.getId_manzana().toString(), username};

            ContentValues cv = new ContentValues();
            cv.put("pendiente_sincronizar", "No");

            SQLiteDatabase db = getWritableDatabase();
            db.update("manzana",  cv  ,"id_manzana = ? and encuestador = ?",args);
        }
    }

    /**
     *
     * @param idManzana
     */
    public void putManzanaPendienteSincronizar(String idManzana, String siNo){
        String[] args = new String[]{ idManzana };
        ContentValues cv = new ContentValues();
        cv.put("pendiente_sincronizar", siNo );

        SQLiteDatabase db = getWritableDatabase();
        db.update("manzana",cv ," id_manzana =? ",args);
    }



    /**
     * Metodo que valida l username y el password a modo offline
     * @param username
     * @param password
     * @return
     */
    public Boolean getUsuarioId(String username, String password){
        Boolean retorno = false;

        try {
            String sql = "SELECT id, usuario, clave FROM usuarios " +
                    "WHERE usuario = '"+username+"' AND clave = '"+password+"' ";
            SQLiteDatabase db = getWritableDatabase();
            Cursor c = db.rawQuery(sql, null);
            if (c != null && c.getCount()>0) {
                c.moveToFirst();
                do {
                    retorno = true;
                } while (c.moveToNext());
            }
            c.close();
            return retorno;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return retorno;
    }

}
