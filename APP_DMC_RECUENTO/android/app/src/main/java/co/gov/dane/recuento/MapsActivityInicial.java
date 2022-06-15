package co.gov.dane.recuento;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import co.gov.dane.recuento.backend.Database;
import co.gov.dane.recuento.backend.EsquemaMapa;
import co.gov.dane.recuento.dtos.MapaOffline;
import co.gov.dane.recuento.model.Offline;

public class MapsActivityInicial extends AppCompatActivity implements OnMapReadyCallback {

    public static  GoogleMap mMap;
    private String id_manzana,id_edificacion;
    private Mensajes msg;
    private Database db;
    private Session session;
    private Offline offline = new Offline();

    private List<Polygon> poligonos =new ArrayList<>();
    private String cod_manzana;
    private SpatiaLite sp;

    private TextView id_manzana_touch;
    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.formulario_mapa_inicial);

        msg = new Mensajes(MapsActivityInicial.this);
        db = new Database(MapsActivityInicial.this);
        sp = new SpatiaLite(MapsActivityInicial.this);
        session = new Session(MapsActivityInicial.this);

        id_manzana_touch=(TextView) findViewById(R.id.id_manzana_touch);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        ImageView atras = (ImageView) findViewById(R.id.atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivityInicial.this, MainActivity.class);
                startActivity(intent);
                MapsActivityInicial.this.finish();
            }
        });

        LinearLayout guardar_formulario_mapa_siguiente = (LinearLayout) findViewById(R.id.guardar_formulario_mapa_siguiente);
        guardar_formulario_mapa_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: QUITAR
                /*if(session.getTipoFormulario().equals("Movil")){
                    cod_manzana = "1100110011000131010114864199";
                }else{
                    cod_manzana = "1100110011000131010114918999";
                }*/
                if(cod_manzana!=null){
                    Intent formulario = new Intent(MapsActivityInicial.this, Formulario.class);
                    formulario.putExtra("id_manzana",cod_manzana);
                    formulario.putExtra("mapa","yes");
                    startActivity(formulario);
                    MapsActivityInicial.this.finish();
                }else{
                    msg.generarToast("Seleccione una manzana","error");
                }
            }
        });


    }



    public void llenarMapa(){

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        llenarMapa();

        Location location = getLastKnownLocation();


        if(location!= null){
            LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.setMyLocationEnabled(true);

            Log.d("lat:Lon", String.valueOf(userLocation));

            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,17));

            Map<String,PolygonOptions> pol_get=sp.getManzanas(userLocation);

            if(pol_get!= null){
                for (Map.Entry<String, PolygonOptions> entry : pol_get.entrySet()) {
                    if(db.existeFormulario(entry.getKey())){
                        Polygon pol=mMap.addPolygon(entry.getValue());
                        pol.setFillColor(getResources().getColor(R.color.poligono_cerrado));
                        pol.setStrokeColor(getResources().getColor(R.color.poligono_cerrado_boundary));

                        offline = db.getOffline("OFFLINE", session.getusename());
                        if(offline!= null && offline.isActivo()) {
                            Boolean asignada = db.getAsignacion(entry.getKey());
                            if (asignada) {
                                pol.setFillColor(getResources().getColor(R.color.poligono_cerrado_seleccionado));
                                pol.setFillColor(getResources().getColor(R.color.poligono_cerrado_seleccionado));
                            }
                        }
                    }else{
                        poligonos.add(mMap.addPolygon(entry.getValue()));
                        poligonos.get(poligonos.size()-1).setClickable(true);
                        poligonos.get(poligonos.size()-1).setTag(entry.getKey());

                        offline = db.getOffline("OFFLINE", session.getusename());
                        if(offline!= null && offline.isActivo()){
                            Boolean asignada = db.getAsignacion(entry.getKey());
                            if(asignada){
                                Polygon pol=mMap.addPolygon(entry.getValue());
                                pol.setFillColor(getResources().getColor(R.color.poligono_cerrado));
                                pol.setStrokeColor(getResources().getColor(R.color.poligono_cerrado_seleccionado));
                            }
                        }
                    }
                }
            }else{
                Intent intent = new Intent(MapsActivityInicial.this, MainActivity.class);
                startActivity(intent);
                this.finish();
                msg.generarToast("Por favor, completar la instalación de la aplicación con los mbtiles y con la base de datos geom.db", "error");
            }
        }else{
            Intent intent = new Intent(MapsActivityInicial.this, MainActivity.class);
            startActivity(intent);
            this.finish();
            msg.generarToast("GPS Desactivado","error");
        }


        try{
            // mbtiles
            MapaOffline mapa = getActivoMapaOffline();
            if(mapa!= null && mapa.getRuta()!= null && !mapa.getRuta().isEmpty()){
                String url = mapa.getRuta();
                TileProvider tileProvider = new ExpandedMBTilesTileProvider(new File(url), 256, 256);
                TileOverlay  tileOverlay = mMap.addTileOverlay(
                        new TileOverlayOptions()
                                .tileProvider(tileProvider).zIndex(-1));
            }else{
                msg.generarToast("Debe seleccionar una configuración del archivo mbtiles","error");
                Intent intent = new Intent(MapsActivityInicial.this, MainActivity.class);
                startActivity(intent);
                MapsActivityInicial.this.finish();
            }
        }catch (Exception e){
            msg.generarToast("No se encuentra la ubicación del archivo mbtiles","error");
        }


        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
           @Override
           public void onPolygonClick(Polygon polygon) {

               for (Polygon pol : poligonos) {
                   pol.setFillColor(getResources().getColor(R.color.transparent));
                   pol.setStrokeColor(getResources().getColor(R.color.negro));
               }

               polygon.setFillColor(getResources().getColor(R.color.poligono_touch));
               polygon.setStrokeColor(getResources().getColor(R.color.poligono_touch_boundary));
               cod_manzana=polygon.getTag().toString();
               id_manzana_touch.setText(cod_manzana);

           }
       }
        );


    }


    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MapsActivityInicial.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }


    /**
     * Metodo que carga el mapa activo
     *
     * @return
     */
    private MapaOffline getActivoMapaOffline(){
        MapaOffline mapa  = new MapaOffline();
        try{
            Database db=new Database(MapsActivityInicial.this);
            SQLiteDatabase sp=db.getWritableDatabase();
            String whereClause = EsquemaMapa.ACTIVO+" = ?";

            String[] whereArgs = new String[] {
                    "1"
            };

            Cursor c = sp.query(
                    EsquemaMapa.TABLE_NAME,  // Nombre de la tabla
                    null,  // Lista de Columnas a consultar
                    whereClause,  // Columnas para la cláusula WHERE
                    whereArgs,  // Valores a comparar con las columnas del WHERE
                    null,  // Agrupar con GROUP BY
                    null,  // Condición HAVING para GROUP BY
                    null  // Cláusula ORDER BY
            );
            while (c.moveToNext()) {
                int index;

                index = c.getColumnIndexOrThrow(EsquemaMapa.ID);
                int id = c.getInt(index);

                index = c.getColumnIndexOrThrow(EsquemaMapa.NOMBRE);
                String nombre = c.getString(index);

                index = c.getColumnIndexOrThrow(EsquemaMapa.RUTA);
                String ruta = c.getString(index);

                index = c.getColumnIndexOrThrow(EsquemaMapa.ACTIVO);
                Boolean activo = ((c.getString(index) != null && c.getString(index).equals("1") ) ?  true  :  false );

                mapa = new MapaOffline(id, nombre, ruta, activo);
            }
            db.close();
            return mapa;
        }catch (Exception e){
            MapaOffline retorno=new MapaOffline();
            return retorno;
        }
    }


}
