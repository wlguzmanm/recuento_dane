package co.gov.dane.recuento;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.gov.dane.recuento.Preguntas.Edificacion;
import co.gov.dane.recuento.Preguntas.Manzana;
import co.gov.dane.recuento.adapter.ConteoAdapterUnidadEconomica;
import co.gov.dane.recuento.adapter.SimpleItemTouchHelperCallback;
import co.gov.dane.recuento.backend.Database;
import co.gov.dane.recuento.model.ConteoUnidad;

import java.util.ArrayList;
import java.util.List;


public class FormularioEdificacion extends AppCompatActivity {

    private List<ConteoUnidad> formularios = new ArrayList<>();
    private RecyclerView recyclerView;
    private ConteoAdapterUnidadEconomica mAdapter;

    private String id_manzana,id_edificacion;
    private Database db;
    private Util util;
    private Mensajes msg;
    private TextView edificacion_numero;
    private String idDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_edificacion);
        idDevice = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);

         id_manzana = getIntent().getStringExtra("id_manzana");
         id_edificacion = getIntent().getStringExtra("id_edificacion");

         db=new Database(FormularioEdificacion.this);
         util=new Util();
         msg=new Mensajes(FormularioEdificacion.this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
        edificacion_numero=(TextView) findViewById(R.id.edificacion_numero);

        edificacion_numero.setText("Edificación Nro: "+id_edificacion);

        mAdapter = new ConteoAdapterUnidadEconomica(formularios,FormularioEdificacion.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);



        prepararDatos();


        ImageView atras = (ImageView) findViewById(R.id.atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormularioEdificacion.this.finish();
            }
        });

        LinearLayout add_grupo = (LinearLayout) findViewById(R.id.add_grupo);

        add_grupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Edificacion edificacion=db.getMapa(id_manzana,id_edificacion);

                if(edificacion.getLatitud()!=null || edificacion.getLongitud()!=null ){
                    int siguiente=db.getMaxUnidad(id_manzana,id_edificacion)+1;
                    db.crearUnidad(id_manzana,id_edificacion,String.valueOf(siguiente));
                    formularios.clear();
                    formularios.addAll(db.getAcordeonUnidades(id_manzana,id_edificacion));
                    mAdapter.notifyDataSetChanged();

                }else{
                    msg.generarToast("Ubique la edificación","error");
                }
            }
        });

        LinearLayout btn_ubicacion_manual=(LinearLayout) findViewById(R.id.btn_ubicacion_manual);
        Manzana manzana_db =new Manzana();
        manzana_db=db.getManzana(id_manzana);

        boolean entro = false;
        if(manzana_db.getFinalizado().equals("Si")){
                btn_ubicacion_manual.setEnabled(false);
        }else{
            btn_ubicacion_manual.setEnabled(true);
        }

        btn_ubicacion_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent formulario = new Intent(FormularioEdificacion.this, MapsActivity.class);

                formulario.putExtra("id_manzana",id_manzana);
                formulario.putExtra("id_edificacion",id_edificacion);
                startActivity(formulario);
            }
        });


        Manzana manzana =new Manzana();
        manzana=db.getManzana(id_manzana);
        if(manzana.getFinalizado().equals("Si")){
            add_grupo.setVisibility(View.GONE);
        }
    }

    private void prepararDatos() {
        formularios.addAll(db.getAcordeonUnidades(id_manzana,id_edificacion));
        mAdapter.notifyDataSetChanged();
    }



    public void showDialogBorrarConteo(final int position ){

        if(db.getFinalizadaManzana(id_manzana)){
            msg.generarToast("No se puede eliminar la unidad en un formulario Finalizado", "error");
        }else{
            LayoutInflater inflater = (LayoutInflater) FormularioEdificacion.this.getSystemService( FormularioEdificacion.this.LAYOUT_INFLATER_SERVICE );

            AlertDialog.Builder mBuilder =new AlertDialog.Builder(FormularioEdificacion.this);
            final View mView =inflater.inflate(R.layout.dialog_eliminar_item,null);
            mBuilder.setView(mView);
            final AlertDialog dialog =mBuilder.create();
            LinearLayout dialog_elimnar_formulario_si= (LinearLayout) mView.findViewById(R.id.dialog_elimnar_formulario_si);
            LinearLayout dialog_elimnar_formulario_no= (LinearLayout) mView.findViewById(R.id.dialog_elimnar_formulario_no);

            dialog_elimnar_formulario_si.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConteoUnidad unidad = formularios.get(position);
                    if(db.borrarUnidad(id_manzana,id_edificacion,unidad.getId_unidad())){
                        formularios.remove(unidad);
                        mAdapter.notifyDataSetChanged();
                        msg.generarToast("Unidad Borrada");
                    }
                    dialog.dismiss();
                }
            });
            dialog_elimnar_formulario_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


}
