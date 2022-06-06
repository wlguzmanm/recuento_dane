package co.gov.dane.reconteo;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.ArrayList;

import co.gov.dane.reconteo.backend.Database;
import co.gov.dane.reconteo.backend.EsquemaMapa;

public class MapaOfflineAdapter extends ArrayAdapter<String> {

    private Activity activity;
    ArrayList<MapaOffline> mapas;

    public MapaOfflineAdapter(Activity activity, ArrayList<MapaOffline> mapas) {
        super(activity, R.layout.adapter_mapa_offline);
        this.activity = activity;
        this.mapas = mapas;
    }

    static class ViewHolder {
    }

    public int getCount() {
        return mapas.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView,
                        final ViewGroup parent) {

        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = activity.getLayoutInflater();
            view = inflator.inflate(R.layout.adapter_mapa_offline, null);
            final Switch description = (Switch) view.findViewById(R.id.swicth_mapas_offline);
            MapaOffline object = mapas.get(position);

            description.setText(object.getNombre());
            description.setId(object.getId());
            description.setChecked(object.isActivo());

            description.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                    actualizarMapas(mapas.get(position),isChecked);
                    Log.d("isChecked:", String.valueOf(isChecked));
                    Log.d("position:", String.valueOf(position));
                }
            });
        } else {
            view = (View) convertView;
        }
        return view;
    }

    public float getConvertedValue(int intVal) {
        float floatVal = (float) 0.0;
        floatVal = .1f * intVal;
        return floatVal;
    }

    /**
     * Metodo que actualiza la lista
     *
     * @param mapaOffline
     * @param isChecked
     */
    private void actualizarMapas(MapaOffline mapaOffline, boolean isChecked) {

        try{
            Database db=new Database(activity);
            SQLiteDatabase sp=db.getWritableDatabase();
            ContentValues values = new ContentValues();

            if(isChecked){
                values.put(EsquemaMapa.ACTIVO, 0);
                sp.update(EsquemaMapa.TABLE_NAME, values, null, null);
                values = new ContentValues();
                values.put(EsquemaMapa.ACTIVO, 1);
                sp.update(EsquemaMapa.TABLE_NAME, values, " id = ?", new String[]{String.valueOf(mapaOffline.getId())});
            }else{
                values = new ContentValues();
                values.put(EsquemaMapa.ACTIVO, 0);
                sp.update(EsquemaMapa.TABLE_NAME, values, " id = ?", new String[]{String.valueOf(mapaOffline.getId())});
            }
            sp.close();
        }catch (Exception e){
        }
    }

}