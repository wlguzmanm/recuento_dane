package co.gov.dane.recuento;

import android.content.Context;
import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.spatialite.database.SQLiteDatabase;
import org.spatialite.database.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

import co.gov.dane.recuento.backend.Database;
import co.gov.dane.recuento.Preguntas.*;
import co.gov.dane.recuento.model.Offline;

public class SpatiaLite extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "geom.db";

    private Context context;
    private Database db;
    private Session session;
    private Offline offline = new Offline();

    public SpatiaLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
        db = new Database(context);
        session = new Session(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

     public Map<String,PolygonOptions> getManzanas(LatLng userLocation){

         Map<String,PolygonOptions> poligonos=new HashMap<>();

         SpatiaLite db1=new SpatiaLite(context);
         org.spatialite.database.SQLiteDatabase sp1=db1.getWritableDatabase();

         Coordinate c_punto=new Coordinate(userLocation.longitude,userLocation.latitude);

         GeometryFactory gf = new GeometryFactory();

         Point p= gf.createPoint(c_punto);

         try {

             String sql = "select manz_ccnct,AsWKT(CastToPolygon(geometry)),Intersects(ST_Transform(ST_Buffer(ST_Transform(SetSRID(GeomFromText('"+p+"'),4326),3116),500),4326),geometry)as geom from manzanas where geom=1";

             Cursor c = sp1.rawQuery(
             sql,null);

             while(c.moveToNext()){

                 String cod_manzana=c.getString(0);
                 String geometria_ini = c.getString(1);

                 offline = db.getOffline("OFFLINE", session.getusename());
                 if(offline!= null && !offline.isActivo()){
                     WKTReader wkt=new WKTReader();
                     try {
                         if(wkt.read(geometria_ini).isValid()){

                             Coordinate[] coord=wkt.read(geometria_ini).getCoordinates();
                             PolygonOptions opts=new PolygonOptions();
                             for(int j=0;j<coord.length;j++){
                                 Double lat=coord[j].y;
                                 Double lon=coord[j].x;
                                 LatLng punto=new LatLng(lat,lon);
                                 opts.add(punto);

                             }
                             poligonos.put(cod_manzana,opts);
                         }
                     } catch (ParseException e) {
                         sp1.close();
                         e.printStackTrace();
                     }
                 }else{
                     WKTReader wkt=new WKTReader();
                     try {
                         if(wkt.read(geometria_ini).isValid()){

                             Coordinate[] coord=wkt.read(geometria_ini).getCoordinates();
                             PolygonOptions opts=new PolygonOptions();

                             for(int j=0;j<coord.length;j++){
                                 Double lat=coord[j].y;
                                 Double lon=coord[j].x;
                                 LatLng punto=new LatLng(lat,lon);
                                 opts.add(punto);

                             }
                             poligonos.put(cod_manzana,opts);
                         }
                     } catch (ParseException e) {
                         sp1.close();
                         e.printStackTrace();
                     }
                 }
             }
         } catch (Exception e) {
             e.printStackTrace();
             sp1.close();
             return null;
         }

         sp1.close();

         return poligonos;
     }

     public PolygonOptions getManzana (String id_manzana){
         PolygonOptions poligono=new PolygonOptions();

         SpatiaLite db1=new SpatiaLite(context);
         org.spatialite.database.SQLiteDatabase sp1=db1.getWritableDatabase();

         try {
             Cursor c = sp1.rawQuery(
                     "select AsWKT(CastToPolygon(geometry)) from manzanas where manz_ccnct='"+id_manzana+"'",null);

             while(c.moveToNext()){

                 String geometria_ini = c.getString(0);

                 WKTReader wkt=new WKTReader();
                 try {
                     if(wkt.read(geometria_ini).isValid()){

                         Coordinate[] coord=wkt.read(geometria_ini).getCoordinates();
                         PolygonOptions opts=new PolygonOptions();
                         for(int j=0;j<coord.length;j++){
                             Double lat=coord[j].y;
                             Double lon=coord[j].x;
                             LatLng punto=new LatLng(lat,lon);
                             opts.add(punto);

                         }
                         poligono=opts;
                     }
                 } catch (ParseException e) {
                     e.printStackTrace();
                 }

             }
         } catch (Exception e) {
             e.printStackTrace();
         }
         sp1.close();

         return poligono;
     }

    public Manzana getDatosCalculados(String id_manzana){

        Manzana manzana=new Manzana();
        SpatiaLite db1=new SpatiaLite(context);
        org.spatialite.database.SQLiteDatabase sp1=db1.getWritableDatabase();
        try{
            Cursor c = sp1.rawQuery(
                    "select dpto_ccdgo, mpio_ccdgo, clas_ccdgo, cpob_ccdgo as centro_poblado, " +
                            "null as localidad, null as coordinacion_operativa, null as area_operativa, null as ag, null as acer, " +
                            "null as unidad_cobertura, manz_ccnct from manzanas where manz_ccnct='"+id_manzana+"'",null);
            while (c.moveToNext()) {
                int index;
                manzana.setDepartamento(c.getString(0));
                manzana.setMunicipio(c.getString(1));
                manzana.setClase(c.getString(2));
                manzana.setCentroPoblado(c.getString(3));
                manzana.setLocalidad(c.getString(4));
                manzana.setCoordinacionOperativa(c.getString(5));
                manzana.setAreaOperativa(c.getString(6));
                manzana.setCodigoAG(c.getString(7));
                manzana.setACER(c.getString(8));
                manzana.setUnidad_cobertura(c.getString(9));
                manzana.setManzana(c.getString(10));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sp1.close();

        return manzana;

    }

     public Boolean puntoDentro(String punto, String polygono){

        SpatiaLite db1=new SpatiaLite(context);
        org.spatialite.database.SQLiteDatabase sp1=db1.getWritableDatabase();
        int inter=0;
        try{
            Cursor c = sp1.rawQuery(
                    "select Intersects(GeomFromText('"+punto+"'), GeomFromText('"+polygono+"'))as inter",null);
            while (c.moveToNext()) {
                int index;

                inter= Integer.parseInt(c.getString(0));

            }
        } catch (Exception e) {
           return false;
        }
        sp1.close();

        Boolean retorno=false;
        if(inter==1){
            retorno= true;
        }
        return retorno;
    }


}
