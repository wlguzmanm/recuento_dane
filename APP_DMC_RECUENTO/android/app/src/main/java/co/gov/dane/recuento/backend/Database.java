package co.gov.dane.recuento.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import co.gov.dane.recuento.dtos.ComplementoNormalizadorDTO;
import co.gov.dane.recuento.dtos.NormalizadorDireccionDTO;
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

    private Context contexto ;
    public static final int DATABASE_VERSION = 17;
    public static final String DATABASE_NAME = "Re_ConteoFormularioV_1_0_0_PROD.db";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        contexto = context;
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
                + EsquemaManzana.LOCALIDAD + " TEXT, "
                + EsquemaManzana.CENTRO_POBLADO + " TEXT, "
                + EsquemaManzana.TERRITORIO_ETNICO + " TEXT, "
                + EsquemaManzana.SEL_TERR_ETNICO + " TEXT, "
                + EsquemaManzana.COD_RESG_ETNICO + " TEXT, "
                + EsquemaManzana.COD_COMUN_ETNICO + " TEXT, "
                + EsquemaManzana.CO + " TEXT, "
                + EsquemaManzana.AO + " TEXT, "
                + EsquemaManzana.AG + " TEXT, "
                + EsquemaManzana.ACER + " TEXT, "
                + EsquemaManzana.DIREC_BARRIO + " TEXT ,"
                + EsquemaManzana.NOV_CARTO + " TEXT, "
                + EsquemaManzana.OBSMZ + " TEXT, "
                + EsquemaManzana.UC + " TEXT, "
                + EsquemaManzana.EXISTE_UNIDAD + " TEXT, "
                + EsquemaManzana.TIPO_NOVEDAD + " TEXT, "
                + EsquemaManzana.MANZANA + " TEXT, "
                + EsquemaManzana.PTO_LAT_GPS + " TEXT, "
                + EsquemaManzana.PTO_LON_GPS + " TEXT, "
                + EsquemaManzana.PTO_ALT_GPS + " TEXT ,"
                + EsquemaManzana.PTO_PRE_GPS + " TEXT ,"
                + EsquemaManzana.IMEI + " TEXT ,"
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
                + EsquemaUnidadEconomica.DIREC_PREVIA + " TEXT, "
                + EsquemaUnidadEconomica.DIREC_P_TIPO + " TEXT, "
                + EsquemaUnidadEconomica.DIRECC + " TEXT, "
                + EsquemaUnidadEconomica.ESTADO_UNIDAD_OBSERVACION + " TEXT, "
                + EsquemaUnidadEconomica.TIPO_UNIDAD_OBSERVACION + " TEXT, "
                + EsquemaUnidadEconomica.TIPO_VENDEDOR + " TEXT, "
                + EsquemaUnidadEconomica.SECTOR_ECONOMICO + " TEXT, "
                + EsquemaUnidadEconomica.UNIDAD_OSBSERVACION + " TEXT, "
                + EsquemaUnidadEconomica.OBSERVACION + " TEXT, "
                + EsquemaUnidadEconomica.FECHA_MODIFICACION + " TEXT "
                + ")");

        db.execSQL("CREATE TABLE " + Usuario.TABLE_NAME + " ("
                + Usuario.ID +" TEXT PRIMARY KEY,"
                + Usuario.USUARIO + " TEXT NOT NULL,"
                + Usuario.CLAVE + " TEXT NOT NULL,"
                + Usuario.IMEI + " TEXT NOT NULL,"
                + Usuario.NOMBRE + " TEXT NOT NULL,"
                + Usuario.CORREO + " TEXT ,"
                + Usuario.VIGENCIA + " TEXT ,"
                + Usuario.TOKEN + " TEXT ,"
                + Usuario.ROL + " TEXT,"
                + Usuario.ID_SUPERVISOR + " TEXT"
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

        //NORMALIZADOR
        db.execSQL("CREATE TABLE " + Normalizador.TABLE_NAME + " ("
                + Normalizador.ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Normalizador.ID_MANZANA + " TEXT,"
                + Normalizador.ID_EDIFICACION + " TEXT,"
                + Normalizador.ID_UNIDAD_ECONOMICA + " TEXT,"
                + Normalizador.DIREC_VP + " TEXT,"
                + Normalizador.DIREC_NNOM_VP + " TEXT,"
                + Normalizador.DIREC_NUM_VP + " TEXT,"
                + Normalizador.DIREC_LET_VP + " TEXT, "
                + Normalizador.DIREC_LET_VP_OTRO + " TEXT, "
                + Normalizador.DIREC_SF_VP + " TEXT, "
                + Normalizador.DIREC_LET_SVP + " TEXT,"
                + Normalizador.DIREC_LET_SVP_OTRO + " TEXT,"
                + Normalizador.DIREC_CUAD_VP + " TEXT, "
                + Normalizador.DIREC_NUM_VG + " TEXT, "
                + Normalizador.DIREC_LET_VG + " TEXT ,"
                + Normalizador.DIREC_LET_VG_OTRO + " TEXT ,"
                + Normalizador.DIREC_SF_VG + " TEXT ,"
                + Normalizador.DIREC_LET_SVG + " TEXT ,"
                + Normalizador.DIREC_LET_SVG_OTRO + " TEXT ,"
                + Normalizador.DIREC_NUM_PLACA + " TEXT ,"
                + Normalizador.DIREC_CUAD_VG + " TEXT ,"
                + Normalizador.DIREC_P_COMP + " TEXT ,"
                + Normalizador.IMEI + " TEXT ,"
                + Normalizador.USUARIO + " TEXT ,"
                + Normalizador.FECHA_CREACION + " TEXT"
                + ")");

        db.execSQL("CREATE TABLE " + ComplementoNormalizador.TABLE_NAME + " ("
                + ComplementoNormalizador.ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ComplementoNormalizador.ID_NORMALIZADOR +" TEXT NOT NULL,"
                + ComplementoNormalizador.DIREC_COMP +" TEXT NOT NULL,"
                + ComplementoNormalizador.DIREC_TEX_COM +" TEXT NOT NULL,"
                + ComplementoNormalizador.DIREC_COMPLEMENTO + " TEXT NOT NULL"
                + ")");


        db.execSQL("CREATE TABLE " + ResguardoIndigena.TABLE_NAME + " ("
                + ResguardoIndigena.ID +" INTEGER PRIMARY KEY,"
                + ResguardoIndigena.TERRITORIO_ETNICO + " TEXT ,"
                + ResguardoIndigena.COD_RESG_ETNICO + " TEXT ,"
                + ResguardoIndigena.NOMBRE_RESGUARDO_INDIGENA + " TEXT ,"
                + ResguardoIndigena.DIV_DPTO + " TEXT ,"
                + ResguardoIndigena.DIV_NOM_DPTO + " TEXT ,"
                + ResguardoIndigena.DIV_MPIO + " TEXT ,"
                + ResguardoIndigena.DIV_NOM_MPIO + " TEXT "
                + ")");

        db.execSQL("CREATE TABLE " + DatosTierraComunidadNegra.TABLE_NAME + " ("
                + DatosTierraComunidadNegra.ID +" INTEGER PRIMARY KEY,"
                + DatosTierraComunidadNegra.TERRITORIO_ETNICO + " TEXT ,"
                + DatosTierraComunidadNegra.COD_COMUN_ETNICO + " TEXT ,"
                + DatosTierraComunidadNegra.NOMBRE_TIERRA_COMUNIDAD_NEGRA + " TEXT ,"
                + DatosTierraComunidadNegra.DIV_DPTO + " TEXT ,"
                + DatosTierraComunidadNegra.DIV_NOM_DPTO + " TEXT ,"
                + DatosTierraComunidadNegra.DIV_MPIO + " TEXT ,"
                + DatosTierraComunidadNegra.DIV_NOM_MPIO + " TEXT "
                + ")");


        db.execSQL("CREATE TABLE " + PuebloIndigena.TABLE_NAME + " ("
                + PuebloIndigena.ID +" INTEGER PRIMARY KEY,"
                + PuebloIndigena.CODIGO + " TEXT ,"
                + PuebloIndigena.NOMBRE + " TEXT "
                + ")");

        db.execSQL("CREATE TABLE " + Divipola.TABLE_NAME + " ("
                + Divipola.ID +" INTEGER PRIMARY KEY,"
                + Divipola.CODIGO_DEPARTAMENTO + " TEXT ,"
                + Divipola.CODIGO_MUNICIPIO + " TEXT ,"
                + Divipola.CODIGO_CENTRO_POBLADO + " TEXT ,"
                + Divipola.DEPARTAMENTO + " TEXT ,"
                + Divipola.MUNICIPIO + " TEXT ,"
                + Divipola.CENTRO_POBLADO + " TEXT ,"
                + Divipola.TIPO_CENTRO_POBLADO + " TEXT ,"
                + Divipola.LONGITUD + " TEXT ,"
                + Divipola.LATITUD + " TEXT ,"
                + Divipola.DISTRITO + " TEXT ,"
                + Divipola.TIPO_MUNICIPIO + " TEXT ,"
                + Divipola.AREA_METROPOLITANA + " TEXT "
                + ")");

        db.execSQL("CREATE TABLE " + ComunasLocalidades.TABLE_NAME + " ("
                + ComunasLocalidades.ID +" INTEGER PRIMARY KEY,"
                + ComunasLocalidades.COD_DPTO + " TEXT ,"
                + ComunasLocalidades.NOM_DPTO + " TEXT ,"
                + ComunasLocalidades.COD_MPIO + " TEXT ,"
                + ComunasLocalidades.NOM_MPIO + " TEXT ,"
                + ComunasLocalidades.COD_CPOB + " TEXT ,"
                + ComunasLocalidades.COD_LOCALIDAD + " TEXT ,"
                + ComunasLocalidades.NOM_LOCALIDAD + " TEXT ,"
                + ComunasLocalidades.TIPO_LOC_COM_CORR + " TEXT "
                + ")");

        db.execSQL("CREATE TABLE " + CentroPoblado.TABLE_NAME + " ("
                + CentroPoblado.ID +" INTEGER PRIMARY KEY,"
                + CentroPoblado.DIV_DPTO + " TEXT ,"
                + CentroPoblado.DIV_NOM_DPTO + " TEXT ,"
                + CentroPoblado.DIV_MPIO + " TEXT ,"
                + CentroPoblado.DIV_NOM_MPIO + " TEXT ,"
                + CentroPoblado.DIV_CLASE + " TEXT ,"
                + CentroPoblado.DIV_CPOB + " TEXT ,"
                + CentroPoblado.DIV_NOM_CPOB + " TEXT "
                + ")");


        //CARGAR DATOS A TABLAS PARAMETRICAS
        DatabaseCargueInfoTablas cargue = new DatabaseCargueInfoTablas(contexto);

        cargue.cargarIntoTablas(db , "infoCentroPoblado");
        cargue.cargarIntoTablas(db , "infoTablaComunas");
        cargue.cargarIntoTablas(db , "infoTablaDivipola");
        cargue.cargarIntoTablas(db , "infoTablaPuebloIndigena");
        cargue.cargarIntoTablas(db , "infoTablaResguardoIndigena");
        cargue.cargarIntoTablas(db , "infoTablaTierraComunidadNegra");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Usuario.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EsquemaUnidadEconomica.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EsquemaEdificacion.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EsquemaManzana.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EsquemaAsignacion.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EsquemaOffline.TABLE_NAME);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Normalizador.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ResguardoIndigena.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatosTierraComunidadNegra.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PuebloIndigena.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Divipola.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ComunasLocalidades.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CentroPoblado.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    /**
     * Metodo que guarda la manzana
     * @param manzana
     * @param id_formulario
     * @return
     */
    public Boolean  guardarManzana(Manzana manzana, String id_formulario) {
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
            values.put(EsquemaManzana.CENTRO_POBLADO, manzana.getCentro_poblado());
            values.put(EsquemaManzana.LOCALIDAD, manzana.getLocalidad());
            values.put(EsquemaManzana.MANZANA, manzana.getManzana());
            values.put(EsquemaManzana.TERRITORIO_ETNICO, manzana.getTerritorioEtnico());
            values.put(EsquemaManzana.SEL_TERR_ETNICO, manzana.getSelTerritorioEtnico());
            values.put(EsquemaManzana.PTO_LAT_GPS, manzana.getLatitud());
            values.put(EsquemaManzana.PTO_LON_GPS, manzana.getLongitud());
            values.put(EsquemaManzana.PTO_ALT_GPS, manzana.getAltura());
            values.put(EsquemaManzana.PTO_PRE_GPS, manzana.getPrecision());
            values.put(EsquemaManzana.COD_RESG_ETNICO, manzana.getResguardoEtnico());
            values.put(EsquemaManzana.COD_COMUN_ETNICO, manzana.getComunidadEtnica());
            values.put(EsquemaManzana.CO, manzana.getCoordinacionOperativa());
            values.put(EsquemaManzana.AO, manzana.getAreaOperativa());
            values.put(EsquemaManzana.AG, manzana.getUnidad_cobertura());
            values.put(EsquemaManzana.ACER, manzana.getACER());
            values.put(EsquemaManzana.DIREC_BARRIO, manzana.getBarrio());
            values.put(EsquemaManzana.EXISTE_UNIDAD, manzana.getExisteUnidad());
            values.put(EsquemaManzana.TIPO_NOVEDAD, manzana.getTipoNovedad());
            values.put(EsquemaManzana.OBSMZ, manzana.getObsmz());
            values.put(EsquemaManzana.UC, manzana.getUc());
            values.put(EsquemaManzana.NOV_CARTO, manzana.getNov_carto());

            values.put(EsquemaManzana.IMEI, manzana.getImei());
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

            values.put(EsquemaUnidadEconomica.DIREC_PREVIA,unidad.getDirec_previa());
            values.put(EsquemaUnidadEconomica.DIREC_P_TIPO,unidad.getDirec_p_tipo());
            values.put(EsquemaUnidadEconomica.DIRECC,unidad.getDirecc());
            values.put(EsquemaUnidadEconomica.ESTADO_UNIDAD_OBSERVACION,unidad.getEstado_unidad_observacion());
            values.put(EsquemaUnidadEconomica.TIPO_UNIDAD_OBSERVACION,unidad.getTipo_unidad_observacion());
            values.put(EsquemaUnidadEconomica.TIPO_VENDEDOR,unidad.getTipo_vendedor());
            values.put(EsquemaUnidadEconomica.SECTOR_ECONOMICO,unidad.getSector_economico());
            values.put(EsquemaUnidadEconomica.UNIDAD_OSBSERVACION,unidad.getUnidad_observacion());
            values.put(EsquemaUnidadEconomica.OBSERVACION,unidad.getObservacion());
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
                manzana.setCentro_poblado(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.LOCALIDAD);
                manzana.setLocalidad(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.TERRITORIO_ETNICO);
                manzana.setTerritorioEtnico(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.SEL_TERR_ETNICO);
                manzana.setSelTerritorioEtnico(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.COD_RESG_ETNICO);
                manzana.setResguardoEtnico(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.COD_COMUN_ETNICO);
                manzana.setComunidadEtnica(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.CO);
                manzana.setCoordinacionOperativa(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.AO);
                manzana.setAreaOperativa(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.AG);
                manzana.setUnidad_cobertura(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.ACER);
                manzana.setACER(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.TIPO_NOVEDAD);
                manzana.setTipoNovedad(c.getString(index));


                index = c.getColumnIndexOrThrow(EsquemaManzana.UC);
                manzana.setUc(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.OBSMZ);
                manzana.setObsmz(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.NOV_CARTO);
                manzana.setNov_carto(c.getString(index));


                index = c.getColumnIndexOrThrow(EsquemaManzana.MANZANA);
                manzana.setManzana(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.EXISTE_UNIDAD);
                manzana.setExisteUnidad(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.PTO_LAT_GPS);
                manzana.setLatitud(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.PTO_LON_GPS);
                manzana.setLongitud(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.PTO_ALT_GPS);
                manzana.setAltura(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.PTO_PRE_GPS);
                manzana.setPrecision(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.ID_MANZANA);
                manzana.setId_manzana(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.FINALIZADO);
                manzana.setFinalizado(c.getString(index));
                index = c.getColumnIndexOrThrow(EsquemaManzana.DIREC_BARRIO);
                manzana.setBarrio(c.getString(index));



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
            manzana.setCentro_poblado(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.LOCALIDAD);
            manzana.setLocalidad(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.TERRITORIO_ETNICO);
            manzana.setTerritorioEtnico(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.SEL_TERR_ETNICO);
            manzana.setSelTerritorioEtnico(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.COD_RESG_ETNICO);
            manzana.setResguardoEtnico(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.COD_COMUN_ETNICO);
            manzana.setComunidadEtnica(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.CO);
            manzana.setCoordinacionOperativa(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.AO);
            manzana.setAreaOperativa(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.AG);
            manzana.setUnidad_cobertura(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.ACER);
            manzana.setACER(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.TIPO_NOVEDAD);
            manzana.setTipoNovedad(c.getString(index));

            index = c.getColumnIndexOrThrow(EsquemaManzana.UC);
            manzana.setUc(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.OBSMZ);
            manzana.setObsmz(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.NOV_CARTO);
            manzana.setNov_carto(c.getString(index));

            index = c.getColumnIndexOrThrow(EsquemaManzana.MANZANA);
            manzana.setManzana(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.EXISTE_UNIDAD);
            manzana.setExisteUnidad(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.PTO_LAT_GPS);
            manzana.setLatitud(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.PTO_LON_GPS);
            manzana.setLongitud(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.PTO_ALT_GPS);
            manzana.setAltura(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.PTO_PRE_GPS);
            manzana.setPrecision(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.ID_MANZANA);
            manzana.setId_manzana(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.FINALIZADO);
            manzana.setFinalizado(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.DIREC_BARRIO);
            manzana.setBarrio(c.getString(index));



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

            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.DIREC_PREVIA);
            unidad.setDirec_previa(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.DIREC_P_TIPO);
            unidad.setDirec_p_tipo(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.DIRECC);
            unidad.setDirecc(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.ESTADO_UNIDAD_OBSERVACION);
            unidad.setEstado_unidad_observacion(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.TIPO_UNIDAD_OBSERVACION);
            unidad.setTipo_unidad_observacion(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.TIPO_VENDEDOR);
            unidad.setTipo_vendedor(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.SECTOR_ECONOMICO);
            unidad.setSector_economico(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.UNIDAD_OSBSERVACION);
            unidad.setUnidad_observacion(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.OBSERVACION);
            unidad.setObservacion(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.IMEI);
            unidad.setImei(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.ID);
            unidad.setId(c.getString(index));

            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.ID_MANZANA);
            unidad.setId_manzana(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.ID_EDIFICACION);
            unidad.setId_edificacion(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaUnidadEconomica.ID_UNIDAD_ECONOMICA);
            unidad.setId_unidad(c.getString(index));
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
            manzana.setCentro_poblado(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.LOCALIDAD);
            manzana.setLocalidad(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.TERRITORIO_ETNICO);
            manzana.setTerritorioEtnico(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.SEL_TERR_ETNICO);
            manzana.setSelTerritorioEtnico(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.COD_RESG_ETNICO);
            manzana.setResguardoEtnico(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.COD_COMUN_ETNICO);
            manzana.setComunidadEtnica(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.CO);
            manzana.setCoordinacionOperativa(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.AO);
            manzana.setAreaOperativa(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.AG);
            manzana.setUnidad_cobertura(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.ACER);
            manzana.setACER(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.TIPO_NOVEDAD);
            manzana.setTipoNovedad(c.getString(index));

            index = c.getColumnIndexOrThrow(EsquemaManzana.UC);
            manzana.setUc(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.OBSMZ);
            manzana.setObsmz(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.NOV_CARTO);
            manzana.setNov_carto(c.getString(index));

            index = c.getColumnIndexOrThrow(EsquemaManzana.MANZANA);
            manzana.setManzana(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.EXISTE_UNIDAD);
            manzana.setExisteUnidad(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.PTO_LAT_GPS);
            manzana.setLatitud(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.PTO_LON_GPS);
            manzana.setLongitud(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.PTO_ALT_GPS);
            manzana.setAltura(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.PTO_PRE_GPS);
            manzana.setPrecision(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.ID_MANZANA);
            manzana.setId_manzana(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.FINALIZADO);
            manzana.setFinalizado(c.getString(index));
            index = c.getColumnIndexOrThrow(EsquemaManzana.DIREC_BARRIO);
            manzana.setBarrio(c.getString(index));



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

    /**
     * Resumen por manzana
     *
     * @param id_manzana
     * @return
     */
    public String getResumen(String id_manzana){

        String resumen="";
        SQLiteDatabase db = getWritableDatabase();

        String sql = "";
        sql = "select DISTINCT \"Total Edificaciones: \" ||  (select count(cast(id_manzana as integer)) from edificacion where id_manzana=?) || \",\" ||\n" +
                "\"Total Unidades: \" || " +
                "((cast((select count(id) from  unidad_economica where id_manzana =  ? ) as integer)))  ||\n" +
                "\",Total Ocupados: \" || cast((select count(id) from  unidad_economica where ESTADO_UNIDAD_OBSERVACION= '1'and id_manzana=? ) as integer) || \n" +
                "\",Total Desocupados: \" || cast((select count(id) from  unidad_economica where ESTADO_UNIDAD_OBSERVACION= '2'and id_manzana=? ) as integer) || \n" +
                "\",Total Obras: \" || cast((select count(id) from  unidad_economica where ESTADO_UNIDAD_OBSERVACION= '3' and id_manzana=? ) as integer) as texto \n" +
                "from unidad_economica  where id_manzana=?";

        Cursor c =db.rawQuery(sql, new String[] {id_manzana,id_manzana,id_manzana,id_manzana,id_manzana,id_manzana});


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
        retorno.setPto_alt_gps(manzana.getAltura());
        retorno.setPto_lat_gps(manzana.getLatitud());
        retorno.setPto_lon_gps(manzana.getLongitud());
        retorno.setPto_pre_gp(manzana.getPrecision());
        retorno.setMpio(manzana.getMunicipio());
        retorno.setClase(manzana.getClase());
        retorno.setCom_loc(manzana.getLocalidad());
        retorno.setC_pob(manzana.getCentro_poblado());
        retorno.setTerritorio_etnico(manzana.getTerritorioEtnico());
        retorno.setSel_terr_etnico(manzana.getSelTerritorioEtnico());
        retorno.setCod_resg_etnico(manzana.getResguardoEtnico());
        retorno.setCod_comun_etnico(manzana.getComunidadEtnica());
        retorno.setCo(manzana.getCoordinacionOperativa());
        retorno.setAo(manzana.getAreaOperativa());
        retorno.setAg(manzana.getUnidad_cobertura());
        retorno.setAcer(manzana.getACER());
        retorno.setDirec_barrio(manzana.getBarrio());
        retorno.setExiste_unidad(manzana.getExisteUnidad());
        retorno.setTipo_novedad(manzana.getTipoNovedad());
        retorno.setUc(manzana.getUc());
        retorno.setObsmz(manzana.getObsmz());
        retorno.setNov_carto(manzana.getNov_carto());

        retorno.setFechaConteo(manzana.getFecha());
        retorno.setFechaModificacion(manzana.getFechaModificacion());
        retorno.setFinalizado(manzana.getFinalizado());
        retorno.setId_manzana(manzana.getId_manzana());
        retorno.setImei(manzana.getImei());
        retorno.setManzana(manzana.getManzana());
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
        retorno.setDirec_previa(unidad.getDirec_previa());
        retorno.setDirec_p_tipo(unidad.getDirec_p_tipo());
        retorno.setDirecc(unidad.getDirecc());
        retorno.setEstado_unidad_observacion(unidad.getEstado_unidad_observacion());
        retorno.setTipo_unidad_observacion(unidad.getTipo_unidad_observacion());
        retorno.setTipo_vendedor(unidad.getTipo_vendedor());
        retorno.setSector_economico(unidad.getSector_economico());
        retorno.setUnidad_osbservacion(unidad.getUnidad_observacion());
        retorno.setObservacion(unidad.getObservacion());
        retorno.setId_edificacion(unidad.getId_edificacion());
        retorno.setId_manzana(unidad.getId_manzana());
        retorno.setId_unidad_economica(unidad.getId_unidad());
        retorno.setId_manzana_edificio_unidad(unidad.getId_manzana()+""+unidad.getId_edificacion()+""+unidad.getId_unidad());

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



    /**
     * Metodo de consulta la informacion de los resguardo indigena
     * @return
     */
    public synchronized List<String> getResguardoIndigena(String departamento, String ciudad) {
        List<String> objeto = new ArrayList<String>();
        objeto.add("Seleccione...");

        if(Util.stringNullEmptys(departamento) && Util.stringNullEmptys(ciudad)){
            //Log.d("SQL_departamento:", departamento);
            //Log.d("SQL_ciudad:", ciudad);
            try {
                SQLiteDatabase db = getReadableDatabase();
                String sql = "SELECT DISTINCT  NOMBRE_RESGUARDO_INDIGENA " +
                        "FROM DATOS_RESGUARDO_INDIGENA " +
                        "WHERE DIV_DPTO  = '"+departamento+"' AND DIV_MPIO = '"+ciudad+"'  " +
                        "ORDER BY NOMBRE_RESGUARDO_INDIGENA ASC";
                //Log.d("SQL sql:", sql);
                Cursor cursor = db.rawQuery(sql, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        objeto.add(cursor.getString(0));
                    } while (cursor.moveToNext());
                }else{
                    objeto = new ArrayList<String>();
                    objeto.add("Seleccione...");
                }
                cursor.close();
            } catch (Exception e) {
                Log.d("Error:", e.getMessage());
            } finally {
            }
        }

        return objeto;
    }

    /***
     * Metodo de consulta la informacion de los resguardo indigena xNombre
     *
     * @param nombre
     * @param isId
     * @return
     */
    public synchronized String getCodResguardoIndigena(String nombre, boolean isId) {
        String objeto = null;
        if(Util.stringNullEmptys(nombre)){
            try {
                SQLiteDatabase db = getReadableDatabase();
                String sql = "";
                if(isId){
                    sql = "SELECT DISTINCT  NOMBRE_RESGUARDO_INDIGENA " +
                            "FROM DATOS_RESGUARDO_INDIGENA " +
                            "WHERE ID  =   '"+nombre+"' ";
                }else{
                    sql = "SELECT DISTINCT  ID " +
                            "FROM DATOS_RESGUARDO_INDIGENA " +
                            "WHERE NOMBRE_RESGUARDO_INDIGENA  =  \""+nombre+"\" ";
                }

                Cursor cursor = db.rawQuery(sql, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        objeto = (cursor.getString(0));
                    } while (cursor.moveToNext());
                }
                cursor.close();
            } catch (Exception e) {
                Log.d("Error:", e.getMessage());
            } finally {
            }
        }

        return objeto;
    }

    /**
     * Metodo de consulta la informacion de los Comunidad negra
     * @return
     */
    public synchronized List<String> getComunidadNegra(String departamento, String ciudad) {
        List<String> objeto = new ArrayList<String>();
        objeto.add("Seleccione...");

        if(Util.stringNullEmptys(departamento) && Util.stringNullEmptys(ciudad)){
            try {
                SQLiteDatabase db = getReadableDatabase();
                String sql = "SELECT DISTINCT  NOMBRE_TIERRA_COMUNIDAD_NEGRA " +
                        "FROM DATOS_TIERRA_COMUNIDAD_NEGRA " +
                        "WHERE DIV_DPTO  = '"+departamento+"' AND DIV_MPIO = '"+ciudad+"'  " +
                        "ORDER BY NOMBRE_TIERRA_COMUNIDAD_NEGRA ASC";
                //Log.d("SQL sql:", sql);

                Cursor cursor = db.rawQuery(sql, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        objeto.add(cursor.getString(0));
                    } while (cursor.moveToNext());
                }else{
                    objeto = new ArrayList<String>();
                    objeto.add("Seleccione...");
                }
                cursor.close();
            } catch (Exception e) {
                Log.d("Error:", e.getMessage());
            } finally {
            }
        }
        return objeto;
    }

    /**
     * Metodo de consulta la informacion de los Comunidad negra xNombre
     * @return
     */
    public synchronized String getCodComunidadNegra(String nombre, boolean isId) {
        String objeto = null;

        if(Util.stringNullEmptys(nombre)){
            try {
                SQLiteDatabase db = getReadableDatabase();
                String sql = "";

                if(isId){
                    sql = "SELECT DISTINCT  NOMBRE_TIERRA_COMUNIDAD_NEGRA " +
                            "FROM DATOS_TIERRA_COMUNIDAD_NEGRA " +
                            "WHERE ID  = '"+nombre+"'  " ;
                }else{
                    sql = "SELECT DISTINCT  ID " +
                            "FROM DATOS_TIERRA_COMUNIDAD_NEGRA " +
                            "WHERE NOMBRE_TIERRA_COMUNIDAD_NEGRA  = '"+nombre+"'  " ;
                }

                Cursor cursor = db.rawQuery(sql, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        objeto = (cursor.getString(0));
                    } while (cursor.moveToNext());
                }
                cursor.close();
            } catch (Exception e) {
                Log.d("Error:", e.getMessage());
            } finally {
            }
        }
        return objeto;
    }

    /**
     * Metodo de consulta la informacion de los Directori0
     * @return
     */
    public synchronized List<String> getPuebloIndigena() {
        List<String> objeto = new ArrayList<String>();
        objeto.add("Seleccione...");

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT DISTINCT  " +
                    "NOMBRE  " +
                    "FROM DATOS_PUEBLO_INDIGENA ORDER BY NOMBRE ASC", null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    objeto.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }else{
                objeto = new ArrayList<String>();
                objeto.add("Seleccione...");
            }
            cursor.close();
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
        } finally {
        }
        return objeto;
    }

    /**
     * Metodo de consulta la informacion de los departamentos
     * @return
     */
    public synchronized List<String> getDepartamentos() {
        List<String> objeto = new ArrayList<String>();
        objeto.add("Seleccione...");

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT DISTINCT  " +
                    "DEPARTAMENTO  " +
                    "FROM DIVIPOLA ", null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    objeto.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }else{
                objeto = new ArrayList<String>();
                objeto.add("Seleccione...");
            }
            cursor.close();
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
        } finally {
        }
        return objeto;
    }

    /**
     * Metodo de consulta la informacion  un departamento
     * @param codigo
     * @return
     */
    public synchronized String getDepartamento(String codigo) {
        String objeto = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT DISTINCT  " +
                    "DEPARTAMENTO  " +
                    "FROM DIVIPOLA WHERE CODIGO_DEPARTAMENTO = '"+codigo+"'", null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    objeto = (cursor.getString(0));
                } while (cursor.moveToNext());
            }else{
                objeto = null;
            }
            cursor.close();
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
        } finally {
        }
        return objeto;
    }

    /**
     * Metodo de consulta la informacion de los municipios
     * @return
     */
    public synchronized List<String> getMunicipios(String departamento) {
        List<String> objeto = new ArrayList<String>();
        objeto.add("Seleccione...");

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT DISTINCT  " +
                    "MUNICIPIO  " +
                    "FROM DIVIPOLA " +
                    "WHERE DEPARTAMENTO = '"+departamento+"'", null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    objeto.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }else{
                objeto = new ArrayList<String>();
                objeto.add("Seleccione...");
            }
            cursor.close();
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
        } finally {
        }
        return objeto;
    }

    /**
     * Metodo de consulta la informacion de un municipios
     * @return
     */
    public synchronized String getMunicipio(String codigo) {
        String objeto = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT DISTINCT  " +
                    "MUNICIPIO  " +
                    "FROM DIVIPOLA " +
                    "WHERE CODIGO_MUNICIPIO = '"+codigo+"'", null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    objeto = (cursor.getString(0));
                } while (cursor.moveToNext());
            }else{
                objeto = null;
            }
            cursor.close();
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
        } finally {
        }
        return objeto;
    }

    /**
     * Metodo de consulta la informacion de los centro poblado por medio de los municipios
     * @return
     */
    public synchronized List<String> getCentroPoblados(String municipio) {
        List<String> objeto = new ArrayList<String>();
        objeto.add("Seleccione...");

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT DISTINCT  " +
                    "CENTRO_POBLADO  " +
                    "FROM DIVIPOLA " +
                    "WHERE MUNICIPIO = '"+municipio+"'", null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    objeto.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }else{
                objeto = new ArrayList<String>();
                objeto.add("Seleccione...");
            }
            cursor.close();
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
        } finally {
        }
        return objeto;
    }

    /**
     * Metodo de consulta la informacion de un centro poblado
     * @return
     */
    public synchronized String getCentroPoblado(String codigo) {
        String objeto = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT DISTINCT  " +
                    "DIV_NOM_CPOB  " +
                    "FROM DATOS_CENTRO_POBLADO " +
                    "WHERE DIV_CPOB = '"+codigo+"'", null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    objeto = (cursor.getString(0));
                } while (cursor.moveToNext());
            }else{
                objeto =  null;
            }
            cursor.close();
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
        } finally {
        }
        return objeto;
    }

    /**
     * Metodo de consulta la informacion de las localidades por medio de los municipios
     * @return
     */
    public synchronized List<String> getLocalidades(String municipio) {
        List<String> objeto = new ArrayList<String>();
        objeto.add("Seleccione...");

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT DISTINCT  " +
                    "NOM_LOCALIDAD  " +
                    "FROM DATOS_COMUNAS_LOCALIDADES " +
                    "WHERE NOM_MPIO = '"+municipio+"'", null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    objeto.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }else{
                objeto = new ArrayList<String>();
                objeto.add("Seleccione...");
            }
            cursor.close();
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
        } finally {
        }
        return objeto;
    }

    /**
     * Metodo de consulta la informacion de una localidad
     * @return
     */
    public synchronized String getLocalidad(String municipio, String codigo) {
        String objeto = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT DISTINCT  " +
                    "NOM_LOCALIDAD  " +
                    "FROM DATOS_COMUNAS_LOCALIDADES " +
                    "WHERE COD_MPIO = '" + municipio + "' and COD_LOCALIDAD = '"+codigo+"'", null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    objeto = (cursor.getString(0));
                } while (cursor.moveToNext());
            }else{
                objeto = null;
            }
            cursor.close();
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
        } finally {
        }
        return objeto;
    }

    /**
     * Metodo de consulta la informacion de una localidad
     * @return
     */
    public synchronized String getLocalidadDIVIPOLA(String codigo) {
        String objeto = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT DISTINCT  " +
                    "CENTRO_POBLADO  " +
                    "FROM DIVIPOLA " +
                    "WHERE CODIGO_CENTRO_POBLADO = '"+codigo+"'", null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    objeto = (cursor.getString(0));
                } while (cursor.moveToNext());
            }else{
                objeto = null;
            }
            cursor.close();
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
        } finally {
        }
        return objeto;
    }


    /**
     * Metodo de consulta la informacion Normalizador - Consulta
     *
     * @param idManzana
     * @param idEdificacion
     * @param idUnidadEconomica
     * @return
     */
    public synchronized NormalizadorDireccionDTO getNormalizador(String idManzana, String idEdificacion,String idUnidadEconomica) {
        NormalizadorDireccionDTO consulta = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("select " +
                    "ID,  " +
                    "ID_MANZANA, " +
                    "ID_EDIFICACION, " +
                    "ID_UNIDAD_ECONOMICA, " +
                    "DIREC_VP, " +
                    "DIREC_NNOM_VP, " +
                    "DIREC_NUM_VP, " +
                    "DIREC_LET_VP, " +
                    "DIREC_LET_VP_OTRO, " +
                    "DIREC_SF_VP, " +
                    "DIREC_LET_SVP, " +
                    "DIREC_LET_SVP_OTRO, " +
                    "DIREC_CUAD_VP, " +
                    "DIREC_NUM_VG, " +
                    "DIREC_LET_VG, " +
                    "DIREC_LET_VG_OTRO, " +
                    "DIREC_SF_VG, " +
                    "DIREC_LET_SVG, " +
                    "DIREC_LET_SVG_OTRO, " +
                    "DIREC_NUM_PLACA, " +
                    "DIREC_CUAD_VG, " +
                    "DIREC_P_COMP, " +
                    "IMEI, " +
                    "USUARIO, " +
                    "FECHA_CREACION " +
                    "from NORMALIZADOR " +
                    "where ID_UNIDAD_ECONOMICA = '"+idUnidadEconomica+"' AND ID_EDIFICACION = '"+idEdificacion+"'  AND ID_MANZANA = '"+idManzana+"' " , null);
            int i = 0;
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    consulta =  new NormalizadorDireccionDTO(
                            cursor.isNull(0) ? null : cursor.getString(0), // id
                            cursor.isNull(1) ? null : cursor.getString(1), // ID_MANZANA
                            cursor.isNull(2) ? null : cursor.getString(2), // ID_EDIFICACION
                            cursor.isNull(3) ? null : cursor.getString(3), // ID_UNIDAD_ECONOMICA
                            cursor.isNull(4) ? null : cursor.getString(4), // direcVp
                            cursor.isNull(5) ? null : cursor.getString(5), // direcNnomVp
                            cursor.isNull(6) ? null : cursor.getString(6), // DIREC_NUM_VP
                            cursor.isNull(7) ? null : cursor.getString(7), // direcLetVp
                            cursor.getString(8), // direcLetVpOtro
                            cursor.getString(9), // direcSfVp
                            cursor.getString(10), // direcLetSvp
                            cursor.getString(11), // direcLetSvpOtro
                            cursor.getString(12), // direcCuadVp
                            cursor.isNull(13) ? null : cursor.getString(13), // direcNumVg
                            cursor.isNull(14) ? null : cursor.getString(14), // direcLetVg
                            cursor.getString(15), // direcLetVgOtro
                            cursor.isNull(16) ? null : cursor.getString(16), // direcSfVg
                            cursor.isNull(17) ? null : cursor.getString(17), // direcLetSvg
                            cursor.getString(18), // direcLetSvgOtro
                            cursor.isNull(19) ? null : cursor.getString(19), // direcNumPlaca
                            cursor.getString(20), // direcCuadVg
                            cursor.isNull(21) ? null : cursor.getString(21), // direcPComp
                            cursor.isNull(22) ? null : cursor.getString(22), // imei
                            cursor.isNull(23) ? null : cursor.getString(23), // usuario
                            cursor.isNull(24) ? null : cursor.getString(24) // fechaCreacion
                    );
                } while (cursor.moveToNext());
            }else{
                consulta = new NormalizadorDireccionDTO();
            }
            cursor.close();
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
        } finally {
        }
        return consulta;
    }

    /**
     * Metodo que realiza el guardado Normalizador
     * @param objeto
     * @return
     */
    public Boolean postNormalizador(NormalizadorDireccionDTO objeto){
        try{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();

            //Normalizador
            values.put(Normalizador.ID_MANZANA,objeto.getIdManzana());
            values.put(Normalizador.ID_UNIDAD_ECONOMICA,objeto.getIdUnidadEconomica());
            values.put(Normalizador.ID_EDIFICACION,objeto.getIdEdificacion());
            values.put(Normalizador.USUARIO,objeto.getUsuario());
            values.put(Normalizador.DIREC_VP,objeto.getDirecVp());
            values.put(Normalizador.DIREC_NNOM_VP,objeto.getDirecNnomVp());
            values.put(Normalizador.DIREC_NUM_VP,objeto.getDirecNumVp());
            values.put(Normalizador.DIREC_LET_VP,objeto.getDirecLetVp());
            values.put(Normalizador.DIREC_LET_VP_OTRO,objeto.getDirecLetVpOtro());
            values.put(Normalizador.DIREC_SF_VP,objeto.getDirecSfVp());
            values.put(Normalizador.DIREC_LET_SVP,objeto.getDirecLetSvp());
            values.put(Normalizador.DIREC_LET_SVP_OTRO,objeto.getDirecLetSvpOtro());
            values.put(Normalizador.DIREC_CUAD_VP,objeto.getDirecCuadVp());
            values.put(Normalizador.DIREC_NUM_VG,objeto.getDirecNumVg());
            values.put(Normalizador.DIREC_LET_VG,objeto.getDirecLetVg());
            values.put(Normalizador.DIREC_LET_VG_OTRO,objeto.getDirecLetVgOtro());
            values.put(Normalizador.DIREC_SF_VG,objeto.getDirecSfVg());
            values.put(Normalizador.DIREC_LET_SVG,objeto.getDirecLetSvg());
            values.put(Normalizador.DIREC_LET_SVG_OTRO,objeto.getDirecLetSvgOtro());
            values.put(Normalizador.DIREC_NUM_PLACA,objeto.getDirecNumPlaca());
            values.put(Normalizador.DIREC_CUAD_VG,objeto.getDirecCuadVg());
            values.put(Normalizador.DIREC_P_COMP,objeto.getDirecPComp());
            values.put(Normalizador.IMEI, objeto.getImei());
            values.put(Normalizador.USUARIO, objeto.getUsuario());
            values.put(Normalizador.FECHA_CREACION, objeto.getFechaCreacion());

            NormalizadorDireccionDTO existe = getNormalizador(objeto.getIdManzana(),objeto.getIdEdificacion(),objeto.getIdUnidadEconomica());
            if(Util.stringNullEmptys(existe.getIdUnidadEconomica())){
                String[] args = new String[]{ objeto.getIdManzana(), objeto.getIdEdificacion(), objeto.getIdUnidadEconomica()};
                boolean valor = (db.update(Normalizador.TABLE_NAME,values ,"ID_MANZANA = ? AND ID_EDIFICACION = ? AND ID_UNIDAD_ECONOMICA = ? ",args)) >0;
                db.close();
                return valor;
            }else{
                boolean valor = (db.insert(Normalizador.TABLE_NAME, null, values)) >0;
                db.close();
                return valor;
            }
        }catch (Exception e){
            return false;
        }
    }


    /**
     * Metodo de consulta la informacion del Complemento del Normalizador - Consulta
     * @param idNormalizador
     * @return
     */
    public synchronized List<ComplementoNormalizadorDTO>  getComplementoNormalizador(String idNormalizador) {
        List<ComplementoNormalizadorDTO> consulta = new ArrayList<>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("select " +
                    "ID,  " +
                    "ID_NORMALIZADOR, " +
                    "DIREC_COMP, " +
                    "DIREC_TEX_COM, " +
                    "DIREC_COMPLEMENTO " +
                    "from NORMALIZADOR_COMPLEMENTO " +
                    "where ID_NORMALIZADOR = '"+idNormalizador+"'", null);
            int i = 0;
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    ComplementoNormalizadorDTO encontro =  new ComplementoNormalizadorDTO(
                            cursor.isNull(0) ? null : cursor.getString(0), // id
                            cursor.isNull(1) ? null : cursor.getString(1), // idNormalizador
                            cursor.isNull(2) ? null : cursor.getString(2), // direcComp
                            cursor.isNull(3) ? null : cursor.getString(3), // direcTextComp
                            cursor.isNull(4) ? null : cursor.getString(4) // direcComplemento
                    );

                    consulta.add(encontro);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
        } finally {
        }
        return consulta;
    }


    /**
     * Metodo que realiza el guardado el Complementa del Normalizador
     *
     * @param objeto
     * @param validarEliminacion
     * @return
     */
    public Boolean postComplementoNormalizador(ComplementoNormalizadorDTO objeto, Boolean validarEliminacion){
        try{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();

            if(validarEliminacion){
                /*db.execSQL("DELETE FROM "+ComplementoNormalizador.TABLE_NAME+ " WHERE ID_NORMALIZADOR = '"+objeto.getIdNormalizador()+"'");
                db.close();*/
                boolean valor =  (db.delete(ComplementoNormalizador.TABLE_NAME, ComplementoNormalizador.ID_NORMALIZADOR + "=? ",
                        new String[]{objeto.getIdNormalizador()})) >0 ;
                db.close();
                return valor;
            }else{
                //Normalizador Complemento
                values.put(ComplementoNormalizador.ID_NORMALIZADOR,objeto.getIdNormalizador());
                values.put(ComplementoNormalizador.DIREC_COMP,objeto.getDirecComp());
                values.put(ComplementoNormalizador.DIREC_TEX_COM,objeto.getDirecTextComp());
                values.put(ComplementoNormalizador.DIREC_COMPLEMENTO,objeto.getDirecComplemento());

                boolean valor = (db.insert(ComplementoNormalizador.TABLE_NAME, null, values)) >0;
                db.close();
                return valor;
            }
        }catch (Exception e){
            return false;
        }
    }

}
