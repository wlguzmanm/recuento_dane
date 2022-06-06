package co.gov.dane.reconteo;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import co.gov.dane.reconteo.Preguntas.Edificacion;
import co.gov.dane.reconteo.Preguntas.Manzana;
import co.gov.dane.reconteo.Preguntas.UnidadEconomica;
import co.gov.dane.reconteo.adapter.ConteoAdapterEdificacion;
import co.gov.dane.reconteo.adapter.SimpleItemTouchHelperCallback;
import co.gov.dane.reconteo.backend.Database;
import co.gov.dane.reconteo.model.ConteoEdificacion;
import co.gov.dane.reconteo.model.EnvioFormularioViewModel;
import co.gov.dane.reconteo.model.EsquemaEdificacionEnvioViewModel;
import co.gov.dane.reconteo.model.EsquemaManzanaEnvioViewModel;
import co.gov.dane.reconteo.model.EsquemaUnidadesEnvioViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Formulario extends AppCompatActivity {


    private EditText fecha_conteo;
    private String id_manzana;

    private EditText cedula_supervisor, cedula_enumerador,departamento,municipio,clase,
    centro_poblado,sector_urbano,seccion_urbana,manzana_trabajo, barrio, codigo_ag, observaciones;

    private Spinner spinner_codigo_supervisor, spinner_codigo_enumerador, spinner_departamento,spinner_presencia_unidades_economicas, novedades;
    private LinearLayout existe_unidades,finalizar_formulario,add_grupo,guardar_formulario,btn_latlon,eliminar_formulario, habilitar_formulario;

    private List<ConteoEdificacion> formularios = new ArrayList<>();
    private RecyclerView recyclerView;
    private ConteoAdapterEdificacion mAdapter;

    private ArrayAdapter<CharSequence> adapter_codigo_supervisor, adapter_codigo_enumerador, adapter_departamento;
    Boolean validacionExxistencia = true;
    private Database db;
    private Util util;
    private Mensajes msj;
    private SpatiaLite sp;
    private Manzana manzana;

    private TextView latlon;
    private Session session;
    private int click=0;
    private Boolean entroPrimeraVez;
    private String coord="";
    private String idDevice;

    private Boolean finalizacion = false;
    LocationManager mLocationManager;

    public Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_manzana);

        idDevice = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        entroPrimeraVez = true;
        manzana=new Manzana();

        db = new Database(Formulario.this);
        util=new Util();
        msj=new Mensajes(Formulario.this);
        sp=new SpatiaLite(Formulario.this);

        session=new Session(Formulario.this);

        fecha_conteo =(EditText) findViewById(R.id.fecha_conteo);
        departamento=(EditText) findViewById(R.id.departamento);
        municipio=(EditText) findViewById(R.id.municipio);
        clase=(EditText) findViewById(R.id.clase);
        centro_poblado=(EditText) findViewById(R.id.centro_poblado);
        sector_urbano=(EditText) findViewById(R.id.sector_urbano);
        seccion_urbana=(EditText) findViewById(R.id.seccion_urbana);
        manzana_trabajo=(EditText) findViewById(R.id.manzana_trabajo);
        barrio=(EditText) findViewById(R.id.barrio);
        observaciones=(EditText) findViewById(R.id.observaciones);
        codigo_ag=(EditText) findViewById(R.id.codigo_ag);
        //novedades=(EditText) findViewById(R.id.novedades);

        existe_unidades=(LinearLayout) findViewById(R.id.existe_unidades);
        //departamento.setEnabled(false);

        spinner_presencia_unidades_economicas = (Spinner) findViewById(R.id.presencia_unidades_economicas);
        novedades = (Spinner) findViewById(R.id.novedades);

        latlon=(TextView)  findViewById(R.id.latlon);

        finalizar_formulario=(LinearLayout) findViewById(R.id.finalizar_formulario);

        id_manzana = getIntent().getStringExtra("id_manzana");
        add_grupo = (LinearLayout) findViewById(R.id.add_grupo);
        guardar_formulario = (LinearLayout) findViewById(R.id.guardar_formulario);
        btn_latlon = (LinearLayout) findViewById(R.id.btn_latlon);
        eliminar_formulario=(LinearLayout) findViewById(R.id.eliminar_formulario);
        habilitar_formulario=(LinearLayout) findViewById(R.id.habilitar_formulario);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
        mAdapter = new ConteoAdapterEdificacion(formularios,Formulario.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(mAdapter);

        final ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        ArrayAdapter<CharSequence> adapter_presencia_unidades_economicas = ArrayAdapter.createFromResource(this,
                R.array.presencia_unidades_economicas, android.R.layout.simple_spinner_item);
        spinner_presencia_unidades_economicas.setAdapter(adapter_presencia_unidades_economicas);

        ArrayAdapter<CharSequence> adapter_novedades= ArrayAdapter.createFromResource(this,
                R.array.Novedades_cartograficas, android.R.layout.simple_spinner_item);
        novedades.setAdapter(adapter_novedades);

        prepararDatos(id_manzana);
        llenarFormulario();

        fecha_conteo = (EditText) findViewById(R.id.fecha_conteo);
        fecha_conteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        fecha_conteo.setInputType(InputType.TYPE_NULL);


        ImageView atras = (ImageView) findViewById(R.id.atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retornar();
            }
        });

        guardar_formulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Manzana manzana=new Manzana();
                if(validarFormularioSoloManzana()){
                    manzana=saveManzana();
                    manzana.setImei(idDevice);
                    if(db.guardarManzana(manzana, id_manzana)){
                        msj.generarToast("Formulario Guardado");
                    }
                    click=1;
                }else{
                    msj.generarToast("Debe diligenciar todos los campos obligatorios","error");
                }

            }
        });

        eliminar_formulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) Formulario.this.getSystemService( Formulario.this.LAYOUT_INFLATER_SERVICE );

                AlertDialog.Builder mBuilder =new AlertDialog.Builder(Formulario.this);
                final View mView =inflater.inflate(R.layout.dialog_eliminar_formulario,null);
                mBuilder.setView(mView);
                final AlertDialog dialog =mBuilder.create();
                LinearLayout dialog_elimnar_formulario_si= (LinearLayout) mView.findViewById(R.id.dialog_elimnar_formulario_si);
                LinearLayout dialog_elimnar_formulario_no= (LinearLayout) mView.findViewById(R.id.dialog_elimnar_formulario_no);
                dialog.show();

                dialog_elimnar_formulario_si.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(Formulario.this);
                        builder.setTitle("Confirmación");
                        builder.setMessage("Desea ELIMINAR la Manzana ?");
                        builder.setIcon(R.drawable.ic_delete);
                        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog2, int id) {

                                if(db.borrarManzana(id_manzana)){
                                    dialog.dismiss();
                                    Intent intent = new Intent(Formulario.this, MainActivity.class);
                                    startActivity(intent);
                                    Formulario.this.finish();
                                }else{
                                    dialog.dismiss();
                                    Toast toast2 =
                                            Toast.makeText(getApplicationContext(),
                                                    "No se pudo eliminar el registro", Toast.LENGTH_SHORT);
                                    toast2.show();
                                }
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog3, int id) {
                                dialog3.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
                dialog_elimnar_formulario_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.setCanceledOnTouchOutside(false);
            }
        });

        finalizar_formulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) Formulario.this.getSystemService( Formulario.this.LAYOUT_INFLATER_SERVICE );

                AlertDialog.Builder mBuilder =new AlertDialog.Builder(Formulario.this);
                final View mView =inflater.inflate(R.layout.dialog_finalizar_formulario,null);
                mBuilder.setView(mView);
                final AlertDialog dialog =mBuilder.create();
                LinearLayout dialog_finalizar_si= (LinearLayout) mView.findViewById(R.id.dialog_finalizar_si);
                LinearLayout dialog_finalizar_no= (LinearLayout) mView.findViewById(R.id.dialog_finalizar_no);

                if(spinner_presencia_unidades_economicas.getSelectedItem().toString().contains("No")){
                  if(db.tieneEdificaciones(id_manzana)){
                       validacionExxistencia = false;
                        AlertDialog.Builder builder = new AlertDialog.Builder(Formulario.this);
                        builder.setTitle("Confirmación");
                        builder.setMessage("Confirme para eliminar la información de Edificacion(es) y Unidad Económica(s) asociadas.");
                        builder.setIcon(R.drawable.ic_menu_salir);
                        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                if(!validaExistenciaAlguna(id_manzana)){
                                    db.eliminarEdificacionesUnidades(id_manzana);
                                }
                                validacionExxistencia = true;
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                validacionExxistencia = false;
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else{
                      validacionExxistencia = true;
                    }
                }

                if(validarFormulario() && validacionExxistencia){
                    dialog_finalizar_si.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(db.finalizarFormulario(id_manzana)){
                                Manzana manzana =new Manzana();
                                manzana=db.getManzana(id_manzana);
                                if(manzana.getLatitud().equals("") ){
                                    msj.generarToast("Coordenadas incompletas","error");
                                }else{
                                    msj.generarToast("Finalizado");
                                    Intent intent = new Intent(Formulario.this, MainActivity.class);
                                    startActivity(intent);
                                    Formulario.this.finish();
                                }
                            }else{
                                msj.generarToast("No se pudo finalizar","error");
                            }
                        }
                    });
                    dialog_finalizar_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                }else{
                    if(!validacionExxistencia){
                        msj.generarToast("Debe eliminar la información inconsistente ya que seleccionó (No/No se logró realizar el conteo)","error");
                    }else{
                        msj.generarToast("Debe agregar todos los campos requeridos en la Manzana, la(s) Edificacion(es) y en la(s) Unidad(es) Económica(s).","error");
                    }


                }
            }
        });

        habilitar_formulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) Formulario.this.getSystemService( Formulario.this.LAYOUT_INFLATER_SERVICE );

                AlertDialog.Builder mBuilder =new AlertDialog.Builder(Formulario.this);
                final View mView =inflater.inflate(R.layout.dialog_habilitar_formulario,null);
                mBuilder.setView(mView);
                final AlertDialog dialog =mBuilder.create();
                LinearLayout dialog_habilitar_si= (LinearLayout) mView.findViewById(R.id.dialog_habilitar_si);
                LinearLayout dialog_habilitar_no= (LinearLayout) mView.findViewById(R.id.dialog_habilitar_no);

                dialog_habilitar_si.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(db.habilitarFormulario(id_manzana)){
                                msj.generarToast("Se ha hablitado la manzana :  "+id_manzana);
                                Intent intent = new Intent(Formulario.this, MainActivity.class);
                                startActivity(intent);
                                Formulario.this.finish();
                        }else{
                            msj.generarToast("No se pudo habilitar","error");
                        }
                    }
                });

                dialog_habilitar_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
            }
        });

        add_grupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(coord.equals("")){
                    msj.generarToast("Obtenga las coordenadas","error");
                }else{
                    if(click==1){
                        int siguiente=db.getMaxEdificacion(id_manzana)+1;
                        Log.d("siguiente", String.valueOf(siguiente));
                        db.crearEdificacion(id_manzana,String.valueOf(siguiente));
                        formularios.clear();
                        formularios.addAll(db.getAcordeonEdificacion(id_manzana));
                        mAdapter.notifyDataSetChanged();
                    }else{
                        msj.generarToast("Guarde el formulario","error");
                    }
                }
            }
        });

        btn_latlon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(Formulario.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Formulario.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = getLastKnownLocation();
                if(location!= null){
                    LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());

                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    double altura = location.getAltitude();
                    double precision = location.getAccuracy();

                    manzana.setLatitud(String.valueOf(latitude));
                    manzana.setLongitud(String.valueOf(longitude));
                    manzana.setAltura(String.valueOf(altura));
                    manzana.setPrecision(String.valueOf(precision));
                    coord="1";

                    latlon.setText("Latitud: "+manzana.getLatitud()+"\nLongitud: "+manzana.getLongitud()
                            +"\nAltura: "+manzana.getAltura()+" m\nPrecisión: "+manzana.getPrecision()+" m");
                }else{
                    msj.generarToast("GPS Desactivado","error");
                }
            }
        });

        spinner_presencia_unidades_economicas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(entroPrimeraVez){
                    entroPrimeraVez = false;
                    if(spinner_presencia_unidades_economicas.getSelectedItem().toString().equals("Si")){
                        existe_unidades.setVisibility(View.VISIBLE);
                    }else{
                        existe_unidades.setVisibility(View.GONE);
                    }
                }else{
                    if(spinner_presencia_unidades_economicas.getSelectedItem().toString().equals("Si")){
                        existe_unidades.setVisibility(View.VISIBLE);
                    }else{
                        if(db.tieneEdificaciones(id_manzana)){
                            existe_unidades.setVisibility(View.GONE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(Formulario.this);
                            builder.setTitle("Confirmación");
                            builder.setMessage("Confirme para eliminar la información de Edificacion(es) y Unidad Económica(s) asociadas.");
                            builder.setIcon(R.drawable.ic_menu_salir);
                            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    if(!validaExistenciaAlguna(id_manzana)){
                                        db.eliminarEdificacionesUnidades(id_manzana);
                                    }
                                    if(spinner_presencia_unidades_economicas.getSelectedItem().toString().equals("Si")){
                                        existe_unidades.setVisibility(View.VISIBLE);
                                    }else{
                                        existe_unidades.setVisibility(View.GONE);
                                    }
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                   /* if(db.tieneEdificaciones(id_manzana)){
                                        db.eliminarEdificacionesUnidades(id_manzana);
                                    }*/
                                    if(spinner_presencia_unidades_economicas.getSelectedItem().toString().equals("Si")){
                                        existe_unidades.setVisibility(View.VISIBLE);
                                    }else{
                                        existe_unidades.setVisibility(View.GONE);
                                    }
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }else{
                            existe_unidades.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        novedades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

    }

    private Boolean validaExistenciaAlguna(String id_manzana){
        if(spinner_presencia_unidades_economicas.getSelectedItem().toString().contains("No")){
            if(db.tieneEdificaciones(id_manzana)){
                return false;
            }else{
                return true;
            }
        }else{
            return true;
        }
    }


    /**
     * Metodo ue valida solo la manzana
     * @return
     */
    private boolean validarFormularioSoloManzana() {
        boolean retorno = true;
        if( barrio.getText().toString().equals("") || novedades.getSelectedItem().toString().equals("") ||  manzana.getLatitud().equals("")
                ||  manzana.getLongitud().equals("") ||  manzana.getAltura().equals("") ||  manzana.getPrecision().equals("") ){
            retorno = false;
        }else{
            retorno = true;
        }
        return retorno;
    }

    /**
     * Metodo que valida el formulario completo de la manzana y sus edificaciones y unidades economicas
     *
     * @return
     */
    private boolean validarFormulario() {
        boolean retorno = true;
        EsquemaManzanaEnvioViewModel validar = getFormulario();

        if((validar.getBarrio()!=null && !validar.getBarrio().equals(""))
                || (validar.getCodigoAG()!=null && !validar.getCodigoAG().equals(""))
                || (validar.getLatitud()!=null && !validar.getLatitud().equals(""))
                || (validar.getLongitud()!=null && !validar.getLongitud().equals(""))
                || (validar.getAltura()!=null && !validar.getAltura().equals(""))
                || (validar.getSector_urbano()!=null && !validar.getSector_urbano().equals(""))
                || (validar.getCentro_poblado()!=null && !validar.getCentro_poblado().equals(""))
                || (validar.getSeccion_urbana()!=null && !validar.getSeccion_urbana().equals(""))
                || (validar.getPrecision()!=null && !validar.getPrecision().equals(""))
                || (validar.getClase()!=null && !validar.getClase().equals(""))
                || (validar.getFechaConteo()!=null && !validar.getFechaConteo().equals(""))
                || (validar.getManzana()!=null && !validar.getManzana().equals(""))
                || (validar.getImei()!=null && !validar.getImei().equals(""))){
            retorno = true;
        }else{
            return false;
        }

        if(validar.getPresencia_und().equals("Si")){
            if(validar.getEsquemaEdificacion().size()>0){
                for (EsquemaEdificacionEnvioViewModel lstEdifi : validar.getEsquemaEdificacion()){
                    if((lstEdifi.getLatitud()!=null && lstEdifi.getLatitud().equals(""))
                            ||(lstEdifi.getLongitud()!=null && !lstEdifi.getLongitud().equals(""))){
                        retorno = true;
                    }else{
                        return false;
                    }
                    if(lstEdifi.getUnidades().size()>0){
                        for (EsquemaUnidadesEnvioViewModel lstUnid : lstEdifi.getUnidades()){
                            if((lstUnid.getTipo_via_principal()!=null && !lstUnid.getTipo_via_principal().equals(""))
                                    || (lstUnid.getVia_secundaria()!=null && !lstUnid.getVia_secundaria().equals(""))
                                    || (lstUnid.getPlaca_cuadrante()!=null && !lstUnid.getPlaca_cuadrante().equals(""))
                                    || (lstUnid.getNombre_unidad_observacion()!=null && !lstUnid.getNombre_unidad_observacion().equals(""))){
                                retorno = true;
                            }else{
                                return false;
                            }
                        }
                    }else{
                        return false;
                    }
                }
            }else{
                return false;
            }
        }

        return retorno;
    }


    /**
     * Metodo que carga la informacion de todo el formulario de una manzana.
     *
     * @return
     */
    private EsquemaManzanaEnvioViewModel getFormulario() {
        EnvioFormularioViewModel validarFormulario = new EnvioFormularioViewModel();

            Manzana mz = db.getManzana(id_manzana);
            EsquemaManzanaEnvioViewModel retornoManzana = new EsquemaManzanaEnvioViewModel();
            retornoManzana  = getManzana(mz);
            retornoManzana.setCod_enumerador(session.getusename());

            List<Edificacion> listado_edificaciones = db.getAllEdificaciones(mz.getId_manzana());

            List<EsquemaEdificacionEnvioViewModel> retornoEdificacion = new ArrayList<>();
            for (Edificacion edificacion : listado_edificaciones) {
                EsquemaEdificacionEnvioViewModel objetoEdifi = new EsquemaEdificacionEnvioViewModel();
                objetoEdifi = getEdificacion(edificacion);

                List<UnidadEconomica> listado_unidades = db.getAllUnidades(mz.getId_manzana(),edificacion.getId_edificacion());
                List<EsquemaUnidadesEnvioViewModel> lstUnidadEconomica = new ArrayList<>();
                for (UnidadEconomica unidadEconomica : listado_unidades) {
                    EsquemaUnidadesEnvioViewModel objetoUnidad = new EsquemaUnidadesEnvioViewModel();
                    UnidadEconomica unidad = db.getUnidadEconomica(mz.getId_manzana(), edificacion.getId_edificacion(),unidadEconomica.getId_unidad());
                    objetoUnidad = getUnidadEconomica(unidad);
                    lstUnidadEconomica.add(objetoUnidad);
                }
                objetoEdifi.setUnidades(lstUnidadEconomica);
                retornoEdificacion.add(objetoEdifi);
            }
            retornoManzana.setEsquemaEdificacion(retornoEdificacion);

        return retornoManzana;
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

    private Manzana saveManzana(){

        manzana.setFecha(fecha_conteo.getText().toString());
        manzana.setDepartamento(departamento.getText().toString());
        manzana.setMunicipio(municipio.getText().toString());
        manzana.setClase(clase.getText().toString());
        manzana.setCentroPoblado(centro_poblado.getText().toString());
        manzana.setSectorUrbano(sector_urbano.getText().toString());
        manzana.setSeccionUrbana(seccion_urbana.getText().toString());
        manzana.setManzana(manzana_trabajo.getText().toString());
        manzana.setBarrio( barrio.getText().toString());
        manzana.setObservaciones( observaciones.getText().toString());
        manzana.setCodigoAG( codigo_ag.getText().toString());
        manzana.setNovedadesCartografias(novedades.getSelectedItem().toString());



        manzana.setMPresenciaUnidades(spinner_presencia_unidades_economicas.getSelectedItem().toString());
        manzana.setCod_enumerador(session.getusename());
        return manzana;

    }

    private void llenarFormulario(){
        Manzana manzana_db =new Manzana();
        Manzana manzana_calculado=new Manzana();
        manzana_db=db.getManzana(id_manzana);
        manzana_calculado=sp.getDatosCalculados(id_manzana);

        fecha_conteo.setText(manzana_db.getFecha());

        if(TextUtils.isEmpty(manzana_db.getFecha())){
            SimpleDateFormat sdf = new SimpleDateFormat("dd / MM / yyyy", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            fecha_conteo.setText(currentDateandTime);
        }

        departamento.setText(manzana_calculado.getDepartamento());
        municipio.setText(manzana_calculado.getMunicipio());
        clase.setText(manzana_calculado.getClase());
        centro_poblado.setText(manzana_calculado.getCentroPoblado());
        sector_urbano.setText(manzana_calculado.getSectorUrbano());
        seccion_urbana.setText(manzana_calculado.getSeccionUrbana());
        manzana_trabajo.setText(manzana_calculado.getManzana());
        barrio.setText(manzana_db.getBarrio());
        observaciones.setText(manzana_db.getObservaciones());
        codigo_ag.setText(manzana_calculado.getCodigoAG());

        novedades.setSelection(getIndexSpinner(novedades,manzana_db.getNovedadesCartografias()));

        if(manzana_db.getFinalizado().toUpperCase().equals("SI")){
            finalizacion = true;
        }else{
            finalizacion = false;
        }

        spinner_presencia_unidades_economicas.setSelection(getIndexSpinner(spinner_presencia_unidades_economicas,manzana_db.getPresenciaUnidades()));

        latlon.setText("Latitud: "+manzana_db.getLatitud()+"\nLongitud: "+manzana_db.getLongitud()
                +"\nAltura: "+manzana_db.getAltura()+" m\nPrecisión: "+manzana_db.getPrecision()+" m");

        coord=manzana_db.getLatitud();

        manzana.setLatitud(manzana_db.getLatitud());
        manzana.setLongitud(manzana_db.getLongitud());
        manzana.setAltura(manzana_db.getAltura());
        manzana.setPrecision(manzana_db.getPrecision());
        Log.d("hola","hola");
        Log.d("lat:",manzana_db.getLatitud());

        if(!manzana_db.getLatitud().isEmpty()){
            btn_latlon.setVisibility(View.GONE);
        }

        if(manzana_db.getFinalizado().equals("Si")){
            finalizar_formulario.setVisibility(View.GONE);
            fecha_conteo.setEnabled(false);
            spinner_presencia_unidades_economicas.setEnabled(false);
            add_grupo.setVisibility(View.GONE);
            guardar_formulario.setVisibility(View.GONE);
            btn_latlon.setVisibility(View.GONE);
            eliminar_formulario.setVisibility(View.GONE);
            boolean entro = false;
            if(manzana_db.getDoble_sincronizado() != null && manzana_db.getDoble_sincronizado().equals("No")
                && manzana_db.getSincronizado_nube()!= null && manzana_db.getSincronizado_nube().equals("No")){
                entro = true;
            }
            if(manzana_db.getDoble_sincronizado() == null  && manzana_db.getSincronizado_nube()== null ){
                entro = true;
            }
            if(entro)
                habilitar_formulario.setVisibility(View.GONE);  // Se desahlita la opcion por el ing.
            else
                habilitar_formulario.setVisibility(View.GONE);
        }else{
            habilitar_formulario.setVisibility(View.GONE);
        }


    }

    private int getIndexSpinner(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                fecha_conteo.setText(selectedDate);
            }
        });

        newFragment.show(this.getSupportFragmentManager(), "datePicker");
    }



    private void prepararDatos(String id_manzana) {

        formularios.addAll(db.getAcordeonEdificacion(id_manzana));
        mAdapter.notifyDataSetChanged();

    }




    public void showDialogBorrarConteo(final int position ){

        LayoutInflater inflater = (LayoutInflater) Formulario.this.getSystemService( Formulario.this.LAYOUT_INFLATER_SERVICE );

        if(db.getFinalizadaManzana(id_manzana)){
            msj.generarToast("No se puede eliminar la edificación en un formulario Finalizado", "error");
        }else{
            AlertDialog.Builder mBuilder =new AlertDialog.Builder(Formulario.this);
            final View mView =inflater.inflate(R.layout.dialog_eliminar_item,null);
            mBuilder.setView(mView);
            final AlertDialog dialog1 =mBuilder.create();
            LinearLayout dialog_elimnar_formulario_si= (LinearLayout) mView.findViewById(R.id.dialog_elimnar_formulario_si);
            LinearLayout dialog_elimnar_formulario_no= (LinearLayout) mView.findViewById(R.id.dialog_elimnar_formulario_no);

            dialog_elimnar_formulario_si.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog1.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Formulario.this);
                    builder.setTitle("Confirmación");
                    builder.setMessage("Desea ELIMINAR la edificación ?");
                    builder.setIcon(R.drawable.ic_delete);
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();

                            ConteoEdificacion edificacion = formularios.get(position);

                            if(db.borrarEdificacion(id_manzana,edificacion.getId_edificacion())){
                                formularios.remove(edificacion);
                                mAdapter.notifyDataSetChanged();
                                msj.generarToast("Edificación borrada");
                            }
                            dialog.dismiss();


                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();


                }
            });
            dialog_elimnar_formulario_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog1.dismiss();
                }
            });


            dialog1.show();
            dialog1.setCanceledOnTouchOutside(false);
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


    public void retornar(){
        if(!finalizacion){
            AlertDialog.Builder builder = new AlertDialog.Builder(Formulario.this);
            builder.setTitle("Confirmación");
            builder.setMessage("Ya guardo la información?");
            builder.setIcon(R.drawable.ic_menu_salir);
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();

                    Intent intent = new Intent(Formulario.this, MainActivity.class);
                    startActivity(intent);
                    Formulario.this.finish();

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }else{
            Intent intent = new Intent(Formulario.this, MainActivity.class);
            startActivity(intent);
            Formulario.this.finish();
        }


    }



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        retornar();
    }


}
