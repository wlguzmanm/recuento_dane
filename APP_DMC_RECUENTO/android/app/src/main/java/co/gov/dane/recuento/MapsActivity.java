package co.gov.dane.recuento;


import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.maps.android.ui.IconGenerator;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Map;

import co.gov.dane.recuento.Preguntas.Edificacion;
import co.gov.dane.recuento.Preguntas.Manzana;
import co.gov.dane.recuento.backend.Database;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String id_manzana,id_edificacion;
    private Mensajes msg;
     private Marker ubicacion_actual;
    private Database db;
    private SpatiaLite sp;
    private String idDevice;
    private Session session;

    LocationManager mLocationManager;
    private com.google.android.gms.maps.model.Polygon poly_manzana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idDevice = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        setContentView(R.layout.formulario_mapa);

        msg=new Mensajes(MapsActivity.this);
        db=new Database(MapsActivity.this);
        sp=new SpatiaLite(MapsActivity.this);
        session=new Session(MapsActivity.this);

        id_manzana = getIntent().getStringExtra("id_manzana");
        id_edificacion = getIntent().getStringExtra("id_edificacion");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageView atras = (ImageView) findViewById(R.id.atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapsActivity.this.finish();
            }
        });

        LinearLayout guardar_formulario_mapa_punto = (LinearLayout) findViewById(R.id.guardar_formulario_mapa_punto);
        guardar_formulario_mapa_punto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(ubicacion_actual!=null)
                    {
                        if(puntoDentroPoligono()){
                            Edificacion edificacion=saveMapa();
                            edificacion.setImei(idDevice);
                            if(db.guardarMapa(edificacion,id_manzana,id_edificacion)){
                                msg.generarToast("Ubicación almacenada");
                            }
                            MapsActivity.this.finish();
                        }else{
                            msg.generarToast("Punto por fuera de la Manzana");
                        }
                    }else{
                        msg.generarToast("Ubique un Punto","error");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Manzana manzana =new Manzana();
        manzana=db.getManzana(id_manzana);
            if(manzana.getFinalizado().equals("Si")){
                guardar_formulario_mapa_punto.setVisibility(View.GONE);
        }
    }

    public Edificacion saveMapa(){
        Edificacion edificacion=new Edificacion();
        if(ubicacion_actual!=null){
            edificacion.setLatitud(String.valueOf(ubicacion_actual.getPosition().latitude));
            edificacion.setLongitud(String.valueOf(ubicacion_actual.getPosition().longitude));
        }
        return edificacion;
    }

    public Boolean puntoDentroPoligono(){
        Util util=new Util();
        String punto=util.puntoWKT(ubicacion_actual);
        String poligono=util.PoligonoWKT(poly_manzana);
        return sp.puntoDentro(punto,poligono);
    }

    public void llenarMapa(){
        Edificacion edificacion=new Edificacion();
        try{
            edificacion=db.getMapa(id_manzana,id_edificacion);
            if(edificacion.getLatitud()!=null || edificacion.getLongitud()!=null ){
                LatLng ubicacion = new LatLng(Double.parseDouble(edificacion.getLatitud()), Double.parseDouble(edificacion.getLongitud()));
                mMap.addMarker(new MarkerOptions().position(ubicacion).title("Ubicación Actual"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacion));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        llenarMapa();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(ubicacion_actual!=null){
                    ubicacion_actual.remove();
                }
                ubicacion_actual = mMap.addMarker(new MarkerOptions()
                        .position(latLng).title("Ubicación Nueva").icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            }
        });

        Location location = getLastKnownLocation();
        if(location!= null){
            LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.setMyLocationEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,18));
        }else{
            msg.generarToast("GPS Desactivado");
        }
        Manzana manzana = db.getManzana(id_manzana);
        PolygonOptions opts=sp.getManzana(manzana.getId_manzana());
        try{
            poly_manzana=mMap.addPolygon(opts);
        }catch (Exception e){
            msg.generarToast("Error al cargar el polígono. Error "+e.getMessage(),"error");
        }

        if(poly_manzana!= null){
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    try{
                        getPolygonLatLngBounds(poly_manzana.getPoints());
                    }catch (Exception e){
                        msg.generarToast("Error al cargar el mapa. Error "+e.getMessage(),"error");
                    }
                }
            });
            List<LatLng> puntos=db.getPuntosEdificaciones(id_manzana,id_edificacion);
            for(LatLng pto: puntos){
                mMap.addMarker(new MarkerOptions()
                        .position(pto).title("").icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            }

            //parte del label de las edificaciones
            IconGenerator iconFactory = new IconGenerator(this);
            iconFactory.setTextAppearance(R.style.iconGenTextENA);
            iconFactory.setBackground(this.getResources().getDrawable(R.drawable.borde_boton));
            Map<String,LatLng> labels=db.getLabelsEdificaciones(id_manzana);

            for (Map.Entry<String, LatLng> entry : labels.entrySet()) {
                MarkerOptions opts1=new MarkerOptions();
                opts1.position(entry.getValue());
                opts1.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(String.valueOf(entry.getKey()))));
                opts1.anchor(iconFactory.getAnchorU(), (float) (iconFactory.getAnchorV()+0.4));
                mMap.addMarker(opts1);
            }
        }
        try{
            // mbtiles
            String ruta_mbtiles = Environment.getExternalStorageDirectory() + File.separator + "Censo_Economico"+ File.separator+"mbtiles";
            File dir = new File(ruta_mbtiles);
            if (dir.exists()) {
                String[] files = dir.list(
                        new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                return name.endsWith(".mbtiles");
                            }
                        });
                for (String s : files) {
                    String nombre_capa = s.split("\\.")[0];
                    String url = ruta_mbtiles + "/" + s;
                    TileProvider tileProvider = new ExpandedMBTilesTileProvider(new File(url), 256, 256);
                    TileOverlay tileOverlay = mMap.addTileOverlay(
                            new TileOverlayOptions()
                                    .tileProvider(tileProvider).zIndex(-1));
                }
            }
        }catch (Exception e){
            msg.generarToast("Error cargando los mbtiles para visualizarlos en el mapa","error");
        }
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

    public void getPolygonLatLngBounds(List<LatLng> polygon) {
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (LatLng point : polygon) {
            builder.include(point);
        }
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
