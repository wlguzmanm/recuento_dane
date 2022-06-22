package co.gov.dane.recuento;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {


    /**
     * Metodo que retorna true si es diferente de vacio y null
     * @param valor
     * @return
     */
    public static boolean stringNullEmptys(String valor){
        if(valor != null && !valor.equals("")){
            return true;
        }
        return false;
    }


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

    /**
     * Metodo que solo deja los acronimos de la direccion.
     *
     * @param datoDireccion
     * @return
     */
    public static String getLimpiarAcronimoDireccion(String datoDireccion){
        if(stringNullEmptys(datoDireccion)){
            if(datoDireccion.contains("(") && datoDireccion.contains(")")){
                String derecho[] = datoDireccion.split(" ");
                return derecho[0];
            }
        }
        return datoDireccion;
    }

    /**
     * Metodo que realiza em mensaje informativo
     * @param activity
     * @param context
     * @param texto
     */
    public static  void getMensajeInformativo(Activity activity, Context context, String texto){
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        final View mView = inflater.inflate(R.layout.dialog_informativo_general, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        LinearLayout dialog_confirmacion = (LinearLayout) mView.findViewById(R.id.dialog_aceptar_confirmacion_mensaje_informativo_general);
        TextView textoV = (TextView) mView.findViewById(R.id.text_select_mensaje_informativo_general);
        textoV.setText(texto);

        dialog_confirmacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }

    /**
     * Metodo que retorno un entero
     * @param texto
     * @return
     */
    public int StringToInt(String texto){
        int valor=0;
        if(texto!= null){
            if(texto.equals("")){
                return 0;
            }else{
                return Integer.parseInt(texto);
            }
        }else{
            return 0;
        }
    }





}
