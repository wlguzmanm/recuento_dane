package co.gov.dane.recuento;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;

import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import co.gov.dane.recuento.backend.Database;
import co.gov.dane.recuento.model.Offline;
import ir.mahdi.mzip.zip.ZipArchive;

public class RestoreBackupAdapter extends ArrayAdapter<String> {

    private Activity activity;
    ArrayList<RestoreBackup> restores;
    private Session session;
    private Database db;
    private Offline offline = new Offline();



    public RestoreBackupAdapter(Activity activity, ArrayList<RestoreBackup> restore) {
        super(activity, R.layout.adapter_restore_backup);
        this.activity = activity;
        this.restores = restore;
        session=new Session(this.activity);
        db = new Database(this.activity);
    }

    static class ViewHolder {
    }

    public int getCount() {
        return restores.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView,
                        final ViewGroup parent) {

        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = activity.getLayoutInflater();
            view = inflator.inflate(R.layout.adapter_restore_backup, null);
            final Switch description = (Switch) view.findViewById(R.id.swicth_restore);
            RestoreBackup object = restores.get(position);

            description.setText(object.getNombre());
            description.setId(object.getId());
            description.setChecked(object.isActivo());

            description.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                    //aqui restaurar
                    //actualizarMapas(restores.get(position),isChecked);

                    if(isChecked){
                        ArrayList<RestoreBackup> tempo = new ArrayList<>();
                        for(int i = 0; i < restores.size();i++){
                            RestoreBackup back_up =  restores.get(i);
                            back_up.setActivo(false);
                            tempo.add(back_up);
                        }
                        restores.clear();
                        restores = tempo;
                    }

                    RestoreBackup back_up = restores.get(position);
                    actualizarBaseDatos(back_up.getNombre());
                    Log.d("isChecked:", String.valueOf(isChecked));
                    Log.d("position:", String.valueOf(position));

                }
            });
        } else {
            view = (View) convertView;
        }
        return view;
    }


    /***
     * etodo que actualiza la DB
     *
     * @param nombreDb
     */
    private void actualizarBaseDatos(String nombreDb) {
        String path_backup = Environment.getExternalStorageDirectory() + File.separator + "Censo_Economico"
                + File.separator + "backup" + File.separator;

        Mensajes mitoast =new Mensajes(this.activity);

            String rutaArhivoOrigen = path_backup + nombreDb+".zip";
            String nombreExtact = "";
            Long time = new Date().getTime();
            nombreExtact = time.toString();

            ZipArchive zipArchive = new ZipArchive();
            try {
                zipArchive.unzip(rutaArhivoOrigen, path_backup + nombreExtact, "bf81f34965eddf8f3c291848ae64015f");
            } catch (Exception e) {
                Mensajes mensaje = new Mensajes(this.activity);
                mensaje.generarToast("Fallo al hacer restauración del Backup, archivo corrupto", "error");
                e.printStackTrace();

                Intent mainIntent = new Intent(this.activity,MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                this.activity.startActivity(mainIntent);
                this.activity.finish();
                return;
            }

            File carpeta = new File(path_backup + nombreExtact);
            String[] listado = carpeta.list();
            if (listado == null || listado.length == 0) {
                System.out.println("No hay elementos dentro de la carpeta actual");
                return;
            }
            else {
                for (int i=0; i< listado.length; i++) {
                    nombreExtact = nombreExtact+File.separator+listado[i];
                    nombreDb = listado[i];
                    break;
                }
            }

            String actualizarDB = this.activity.getDatabasePath(Database.DATABASE_NAME).getPath();
            String rutaArhivoDestino = path_backup + nombreExtact;

            final String inFileName = rutaArhivoDestino;

            try{
                File dbFile = new File(inFileName);
                FileInputStream fis = new FileInputStream(dbFile);
                String outFileName = actualizarDB;
                // Open the empty db as the output stream
                OutputStream output = new FileOutputStream(outFileName);
                // Transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                // Close the streams
                output.flush();
                output.close();
                fis.close();

                File file = new File(outFileName);
                //boolean deleted = file.delete();
            } catch (Exception e) {
                Mensajes mensaje = new Mensajes(this.activity);
                mensaje.generarToast("Fallo al hacer restauración del Backup, archivo no corresponde al formato", "error");
                e.printStackTrace();

                Intent mainIntent = new Intent(this.activity,MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                this.activity.startActivity(mainIntent);
                this.activity.finish();
                return;
            }

            offline = db.getOffline("OFFLINE", session.getusename());
            if(offline!= null && !offline.isActivo()){
                new AlertDialog.Builder(this.activity)
                        .setTitle("Informativo")
                        .setCancelable(false)
                        .setMessage("Backup restaurado con nombre:  "+nombreDb+
                                "\n\nSeñor usuario, el sistema automáticamente cerrará la sesion actual. Lo cual debe ingresar nuevamente las " +
                                "credenciales de usuario y clave."+
                                ".\n\nRecuerde que el sistema lo dejará en modo ONLINE y requiere nuevamente autenticación." )
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                redirect();
                            }
                        })
                        .show();
            }else {
                new AlertDialog.Builder(this.activity)
                        .setTitle("Informativo")
                        .setCancelable(false)
                        .setMessage("Backup restaurado con nombre:  "+nombreDb+
                                "\n\nSeñor usuario, el sistema automáticamente cerrará la sesion actual. Lo cual debe ingresar nuevamente las " +
                                "credenciales de usuario y clave."+
                                ".\n\nRecuerde que el sistema lo dejará en modo ONLINE y requiere nuevamente autenticación." )
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                redirect();
                            }
                        })
                        .show();
            }
    }


    /**
     * Metodo que redireccion
     */
    public void redirect(){
        session.borrarSession();
        Intent mainIntent = new Intent(this.activity,Splash.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.activity.startActivity(mainIntent);
        this.activity.finish();
    }

}