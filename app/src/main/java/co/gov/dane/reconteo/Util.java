package co.gov.dane.reconteo;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {



    public String getFechaActual(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public String puntoWKT(Marker marker){

        Coordinate punto=new Coordinate(marker.getPosition().longitude,marker.getPosition().latitude);

        GeometryFactory gf = new GeometryFactory();

        Point p= gf.createPoint(punto);

        return String.valueOf(p);
    }

    public String PoligonoWKT(Polygon pol){

        Coordinate[] puntos=new Coordinate[pol.getPoints().size()];

        for(int i=0;i<pol.getPoints().size();i++){

            puntos[i]=new Coordinate(pol.getPoints().get(i).longitude,pol.getPoints().get(i).latitude);
        }
        GeometryFactory gf = new GeometryFactory();


        org.locationtech.jts.geom.Polygon p=gf.createPolygon(puntos);

        return String.valueOf(p);

    }



}
