package co.gov.dane.recuento;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import co.gov.dane.recuento.Preguntas.Edificacion;
import co.gov.dane.recuento.Preguntas.Manzana;
import co.gov.dane.recuento.Preguntas.UnidadEconomica;
import co.gov.dane.recuento.adapter.ConteoAdapterEdificacion;
import co.gov.dane.recuento.adapter.SimpleItemTouchHelperCallback;
import co.gov.dane.recuento.backend.Database;
import co.gov.dane.recuento.model.ConteoEdificacion;
import co.gov.dane.recuento.model.EnvioFormularioViewModel;
import co.gov.dane.recuento.model.EsquemaEdificacionEnvioViewModel;
import co.gov.dane.recuento.model.EsquemaManzanaEnvioViewModel;
import co.gov.dane.recuento.model.EsquemaUnidadesEnvioViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Formulario extends AppCompatActivity {


    private String id_manzana;
    private Spinner spinnerCOD_RESG_ETNICO,  spinnerCOD_COMUN_ETNICO;
    private LinearLayout linearTerritorioEtnico, linearResguardo, linearComunidadNegra, linearTipoNovedad, linearExisteEdificaciones, linearFinalizarFormulario,
            linearGuardarFormularioManzana, linearEliminarFormulario, linearHabilitarFormulario, linearAgregarEdificacion;
    private EditText  editDPTO, editMPIO, editCLASE, editCOM_LOC, editC_POB, editCO, editAO, editAG, editACER, editDIREC_BARRIO, latlon;
    private RadioGroup radioTERRITORIO_ETNICO, radioSEL_TERR_ETNICO, radioEXISTE_UNIDAD, radioTIPO_NOVEDAD;
    private RadioButton id_pregunta2_4_si, id_pregunta2_4_no, id_pregunta2_5_1, id_pregunta2_5_2, id_pregunta5_2_si, id_pregunta5_2_no,
            id_pregunta5_3_1,id_pregunta5_3_2,id_pregunta5_3_3,id_pregunta5_3_4,id_pregunta5_3_5,id_pregunta5_3_6,id_pregunta5_3_7;

    private String TERRITORIO_ETNICO, SEL_TERR_ETNICO, EXISTE_UNIDAD, TIPO_NOVEDAD, selRESGUARDO, selCOMUNIDAD_NEGRA;

    private TextView fecha_conteo;
    private List<ConteoEdificacion> lstEdificaciones = new ArrayList<>();
    private RecyclerView recyclerView;
    private ConteoAdapterEdificacion mAdapterEdificacion;

    private ArrayAdapter<CharSequence> adapter_codigo_supervisor, adapter_codigo_enumerador, adapter_departamento;
    Boolean validacionExxistencia = true;
    private Database db;
    private Util util;
    private Mensajes msj;
    private SpatiaLite sp;
    private Manzana manzana;
    private Session session;
    private int click=0;
    private Boolean entroPrimeraVez;
    private String coord="";
    private String idDevice;

    private Boolean finalizacion = false;
    LocationManager mLocationManager;
    public Activity activity;

    String noDEPA, noMPIO, noLOCALIDAD, noCENTRO;

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

        editDPTO=(EditText) findViewById(R.id.editDPTO);
        editMPIO=(EditText) findViewById(R.id.editMPIO);
        editCLASE=(EditText) findViewById(R.id.editCLASE);
        editCOM_LOC=(EditText) findViewById(R.id.editCOM_LOC);
        editC_POB=(EditText) findViewById(R.id.editC_POB);
        editCO=(EditText) findViewById(R.id.editCO);
        editAO=(EditText) findViewById(R.id.editAO);
        editAG=(EditText) findViewById(R.id.editAG);
        editACER=(EditText) findViewById(R.id.editACER);
        editDIREC_BARRIO=(EditText) findViewById(R.id.editDIREC_BARRIO);
        latlon=(EditText) findViewById(R.id.latlon);

        linearExisteEdificaciones=(LinearLayout) findViewById(R.id.linearExisteEdificaciones);
        linearFinalizarFormulario=(LinearLayout) findViewById(R.id.linearFinalizarFormulario);
        linearGuardarFormularioManzana = (LinearLayout) findViewById(R.id.linearGuardarFormularioManzana);
        linearEliminarFormulario=(LinearLayout) findViewById(R.id.linearEliminarFormulario);
        linearHabilitarFormulario=(LinearLayout) findViewById(R.id.linearHabilitarFormulario);
        linearTerritorioEtnico=(LinearLayout) findViewById(R.id.linearTerritorioEtnico);
        linearResguardo=(LinearLayout) findViewById(R.id.linearResguardo);
        linearComunidadNegra=(LinearLayout) findViewById(R.id.linearComunidadNegra);
        linearTipoNovedad=(LinearLayout) findViewById(R.id.linearTipoNovedad);
        linearAgregarEdificacion=(LinearLayout) findViewById(R.id.linearAgregarEdificacion);

        radioTERRITORIO_ETNICO = (RadioGroup) findViewById(R.id.radioTERRITORIO_ETNICO);
        radioSEL_TERR_ETNICO = (RadioGroup) findViewById(R.id.radioSEL_TERR_ETNICO);
        radioEXISTE_UNIDAD = (RadioGroup) findViewById(R.id.radioEXISTE_UNIDAD);
        radioTIPO_NOVEDAD = (RadioGroup) findViewById(R.id.radioTIPO_NOVEDAD);

        id_pregunta2_4_si = (RadioButton) findViewById(R.id.id_pregunta2_4_si);
        id_pregunta2_4_no = (RadioButton) findViewById(R.id.id_pregunta2_4_no);
        id_pregunta2_5_1 = (RadioButton) findViewById(R.id.id_pregunta2_5_1);
        id_pregunta2_5_2 = (RadioButton) findViewById(R.id.id_pregunta2_5_2);
        id_pregunta5_2_si = (RadioButton) findViewById(R.id.id_pregunta5_2_si);
        id_pregunta5_2_no = (RadioButton) findViewById(R.id.id_pregunta5_2_no);
        id_pregunta5_3_1 = (RadioButton) findViewById(R.id.id_pregunta5_3_1);
        id_pregunta5_3_2 = (RadioButton) findViewById(R.id.id_pregunta5_3_2);
        id_pregunta5_3_3 = (RadioButton) findViewById(R.id.id_pregunta5_3_3);
        id_pregunta5_3_4 = (RadioButton) findViewById(R.id.id_pregunta5_3_4);
        id_pregunta5_3_5 = (RadioButton) findViewById(R.id.id_pregunta5_3_5);
        id_pregunta5_3_6 = (RadioButton) findViewById(R.id.id_pregunta5_3_6);
        id_pregunta5_3_7 = (RadioButton) findViewById(R.id.id_pregunta5_3_7);

        fecha_conteo = (TextView) findViewById(R.id.fecha_conteo);

        linearTerritorioEtnico.setVisibility(View.GONE);
        linearResguardo.setVisibility(View.GONE);
        linearComunidadNegra.setVisibility(View.GONE);
        linearTipoNovedad.setVisibility(View.GONE);
        linearEliminarFormulario.setVisibility(View.GONE);
        linearHabilitarFormulario.setVisibility(View.GONE);
        linearExisteEdificaciones.setVisibility(View.GONE);
        linearFinalizarFormulario.setVisibility(View.GONE);
        linearAgregarEdificacion.setVisibility(View.VISIBLE);

        spinnerCOD_RESG_ETNICO = (Spinner) findViewById(R.id.spinnerCOD_RESG_ETNICO);
        spinnerCOD_COMUN_ETNICO = (Spinner) findViewById(R.id.spinnerCOD_COMUN_ETNICO);

        id_manzana = getIntent().getStringExtra("id_manzana");

        manzana = db.getManzana(id_manzana);

        //Cargar la informacion del GPS-----------------------------------------------------------------
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(Formulario.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(Formulario.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //----------------------------------------------------------------------------------------------

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
        mAdapterEdificacion = new ConteoAdapterEdificacion(lstEdificaciones,Formulario.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(mAdapterEdificacion);

        final ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapterEdificacion);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        Manzana manzana_calculado=new Manzana();
        manzana_calculado=sp.getDatosCalculados(id_manzana);
        //Cargue de los resguardoIndigena
        //List<String> resguardoIndigena = db.getResguardoIndigena(manzana_calculado.getDepartamento(), manzana_calculado.getMunicipio());
        List<String> resguardoIndigena = db.getResguardoIndigena("76", "76275");
        ArrayAdapter<String> adapterResguardoIndigena = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, resguardoIndigena);
        adapterResguardoIndigena.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCOD_RESG_ETNICO.setAdapter(adapterResguardoIndigena);

        if(resguardoIndigena.size() == 1){
            linearComunidadNegra.setVisibility(View.GONE);
        }

        //Cargue de los comunidadNegra
        //List<String> comunidadNegra = db.getComunidadNegra(manzana_calculado.getDepartamento(), manzana_calculado.getMunicipio());
        List<String> comunidadNegra = db.getComunidadNegra("05", "05475");
        ArrayAdapter<String> adapterComunidad = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, comunidadNegra);
        adapterComunidad.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCOD_COMUN_ETNICO.setAdapter(adapterComunidad);

        prepararDatos(id_manzana);
        llenarFormulario();

        //Metodo que va atras
        ImageView atras = (ImageView) findViewById(R.id.atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retornar();
            }
        });

        linearGuardarFormularioManzana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarFormularioSoloManzana()){
                    manzana=saveManzana();
                    if(db.guardarManzana(manzana, id_manzana)){
                        msj.dialogoMensaje("Success", "Formulario Guardado");
                    }
                    click=1;
                }else{
                    msj.dialogoMensajeError("Error","Debe diligenciar todos los campos obligatorios");
                }

            }
        });

        linearEliminarFormulario.setOnClickListener(new View.OnClickListener() {
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

        linearFinalizarFormulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) Formulario.this.getSystemService( Formulario.this.LAYOUT_INFLATER_SERVICE );

                AlertDialog.Builder mBuilder =new AlertDialog.Builder(Formulario.this);
                final View mView =inflater.inflate(R.layout.dialog_finalizar_formulario,null);
                mBuilder.setView(mView);
                final AlertDialog dialog =mBuilder.create();
                LinearLayout dialog_finalizar_si= (LinearLayout) mView.findViewById(R.id.dialog_finalizar_si);
                LinearLayout dialog_finalizar_no= (LinearLayout) mView.findViewById(R.id.dialog_finalizar_no);

                if(radioEXISTE_UNIDAD.toString().contains("2")){
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

        linearHabilitarFormulario.setOnClickListener(new View.OnClickListener() {
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

        linearAgregarEdificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click==1){
                    int siguiente=db.getMaxEdificacion(id_manzana)+1;
                    Log.d("siguiente", String.valueOf(siguiente));
                    db.crearEdificacion(id_manzana,String.valueOf(siguiente));
                    msj.dialogoMensaje("Success","Se ha agregado una edificación.");
                    lstEdificaciones.clear();
                    lstEdificaciones.addAll(db.getAcordeonEdificacion(id_manzana));
                    mAdapterEdificacion.notifyDataSetChanged();
                }else{
                    msj.dialogoMensajeError("Error","Guarde el formulario");
                }

            }
        });


        radioTERRITORIO_ETNICO.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        TERRITORIO_ETNICO = "";
                        linearTerritorioEtnico.setVisibility(View.GONE);
                        id_pregunta2_5_1.setChecked(false);
                        id_pregunta2_5_1.setChecked(false);
                        radioSEL_TERR_ETNICO.clearCheck();
                        break;
                    case R.id.id_pregunta2_4_si:
                        TERRITORIO_ETNICO = "1";
                        linearTerritorioEtnico.setVisibility(View.VISIBLE);
                        linearResguardo.setVisibility(View.GONE);
                        linearComunidadNegra.setVisibility(View.GONE);
                        break;
                    case R.id.id_pregunta2_4_no:
                        TERRITORIO_ETNICO = "2";
                        linearTerritorioEtnico.setVisibility(View.GONE);
                        linearResguardo.setVisibility(View.GONE);
                        linearComunidadNegra.setVisibility(View.GONE);
                        id_pregunta2_5_1.setChecked(false);
                        id_pregunta2_5_1.setChecked(false);
                        radioSEL_TERR_ETNICO.clearCheck();
                        break;
                }
            }
        });

        radioSEL_TERR_ETNICO.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        SEL_TERR_ETNICO = "";
                        linearResguardo.setVisibility(View.GONE);
                        linearComunidadNegra.setVisibility(View.GONE);
                        break;
                    case R.id.id_pregunta2_5_1:
                        SEL_TERR_ETNICO = "1";
                        selRESGUARDO = null;
                        selCOMUNIDAD_NEGRA = null;
                        linearResguardo.setVisibility(View.VISIBLE);
                        linearComunidadNegra.setVisibility(View.GONE);
                        spinnerCOD_RESG_ETNICO.setSelection(
                                getIndexSpinner(spinnerCOD_RESG_ETNICO,"Seleccione..."));
                        spinnerCOD_COMUN_ETNICO.setSelection(
                                getIndexSpinner(spinnerCOD_RESG_ETNICO,"Seleccione..."));
                        break;
                    case R.id.id_pregunta2_5_2:
                        SEL_TERR_ETNICO = "2";
                        selRESGUARDO = null;
                        selCOMUNIDAD_NEGRA = null;
                        linearResguardo.setVisibility(View.GONE);
                        linearComunidadNegra.setVisibility(View.VISIBLE);
                        spinnerCOD_RESG_ETNICO.setSelection(
                                getIndexSpinner(spinnerCOD_RESG_ETNICO,"Seleccione..."));
                        spinnerCOD_COMUN_ETNICO.setSelection(
                                getIndexSpinner(spinnerCOD_RESG_ETNICO,"Seleccione..."));
                        break;
                }
            }
        });

        radioEXISTE_UNIDAD.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        EXISTE_UNIDAD = "";
                        id_pregunta5_3_1.setChecked(false);
                        id_pregunta5_3_2.setChecked(false);
                        id_pregunta5_3_3.setChecked(false);
                        id_pregunta5_3_4.setChecked(false);
                        id_pregunta5_3_5.setChecked(false);
                        id_pregunta5_3_6.setChecked(false);
                        id_pregunta5_3_7.setChecked(false);
                        radioTIPO_NOVEDAD.clearCheck();
                        break;
                    case R.id.id_pregunta5_2_si:
                        EXISTE_UNIDAD = "1";
                        linearAgregarEdificacion.setVisibility(View.VISIBLE);
                        linearExisteEdificaciones.setVisibility(View.VISIBLE);
                        linearTipoNovedad.setVisibility(View.GONE);
                        id_pregunta5_3_1.setChecked(false);
                        id_pregunta5_3_2.setChecked(false);
                        id_pregunta5_3_3.setChecked(false);
                        id_pregunta5_3_4.setChecked(false);
                        id_pregunta5_3_5.setChecked(false);
                        id_pregunta5_3_6.setChecked(false);
                        id_pregunta5_3_7.setChecked(false);
                        radioTIPO_NOVEDAD.clearCheck();
                        break;
                    case R.id.id_pregunta5_2_no:
                        EXISTE_UNIDAD = "2";
                        linearAgregarEdificacion.setVisibility(View.GONE);
                        linearExisteEdificaciones.setVisibility(View.GONE);
                        linearTipoNovedad.setVisibility(View.VISIBLE);
                        id_pregunta5_3_1.setChecked(false);
                        id_pregunta5_3_2.setChecked(false);
                        id_pregunta5_3_3.setChecked(false);
                        id_pregunta5_3_4.setChecked(false);
                        id_pregunta5_3_5.setChecked(false);
                        id_pregunta5_3_6.setChecked(false);
                        id_pregunta5_3_7.setChecked(false);
                        radioTIPO_NOVEDAD.clearCheck();
                        break;
                }
            }
        });

        radioTIPO_NOVEDAD.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        TIPO_NOVEDAD = "";
                        break;
                    case R.id.id_pregunta5_3_1:
                        TIPO_NOVEDAD = "1";
                        break;
                    case R.id.id_pregunta5_3_2:
                        TIPO_NOVEDAD = "2";
                        break;
                    case R.id.id_pregunta5_3_3:
                        TIPO_NOVEDAD = "3";
                        break;
                    case R.id.id_pregunta5_3_4:
                        TIPO_NOVEDAD = "4";
                        break;
                    case R.id.id_pregunta5_3_5:
                        TIPO_NOVEDAD = "5";
                        break;
                    case R.id.id_pregunta5_3_6:
                        TIPO_NOVEDAD = "6";
                        break;
                    case R.id.id_pregunta5_3_7:
                        TIPO_NOVEDAD = "7";
                        break;
                }
            }
        });

        spinnerCOD_RESG_ETNICO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinnerCOD_RESG_ETNICO.getSelectedItem().toString().equals("Seleccione...")){
                    selRESGUARDO = spinnerCOD_RESG_ETNICO.getSelectedItem().toString();
                    selCOMUNIDAD_NEGRA = null;
                }else{
                    selRESGUARDO = null;
                    selCOMUNIDAD_NEGRA = null;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selRESGUARDO = null;
                selCOMUNIDAD_NEGRA = null;
            }
        });

        spinnerCOD_COMUN_ETNICO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinnerCOD_COMUN_ETNICO.getSelectedItem().toString().equals("Seleccione...")){
                    selCOMUNIDAD_NEGRA = spinnerCOD_COMUN_ETNICO.getSelectedItem().toString();
                    selRESGUARDO = null;
                }else{
                    selRESGUARDO = null;
                    selCOMUNIDAD_NEGRA = null;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selRESGUARDO = null;
                selCOMUNIDAD_NEGRA = null;
            }
        });

    }

    /**
     *
     * @param id_manzana
     * @return
     */
    private Boolean validaExistenciaAlguna(String id_manzana){
        if(radioEXISTE_UNIDAD.toString().contains("2")){
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
        if((editDIREC_BARRIO.getText().toString()!=null && !editDIREC_BARRIO.getText().toString().equals(""))
                && (TERRITORIO_ETNICO!=null && !TERRITORIO_ETNICO.equals(""))
                && (fecha_conteo.getText().toString()!=null && !fecha_conteo.getText().toString().equals(""))  ){

            if(TERRITORIO_ETNICO!= null && TERRITORIO_ETNICO.equals("1") && SEL_TERR_ETNICO!= null && !SEL_TERR_ETNICO.equals("")){
                if(SEL_TERR_ETNICO!= null && SEL_TERR_ETNICO.equals("1") && selRESGUARDO!= null && !selRESGUARDO.equals("") ){
                    retorno = true;
                }else{
                    if(SEL_TERR_ETNICO!= null && SEL_TERR_ETNICO.equals("2") && selCOMUNIDAD_NEGRA!= null && !selCOMUNIDAD_NEGRA.equals("") ){
                        retorno = true;
                    }else{
                        return false;
                    }
                }
            }

            if(EXISTE_UNIDAD!= null && !EXISTE_UNIDAD.equals("")){
                if(EXISTE_UNIDAD.equals("2") && TIPO_NOVEDAD!= null && !TIPO_NOVEDAD.equals("")){
                    retorno = true;
                }else{
                    if(EXISTE_UNIDAD.equals("1") ){
                        retorno = true;
                    }else{
                        return false;
                    }
                }
            }else{
                return false;
            }
        }else{
            return false;
        }

        if(manzana.getLatitud().equals("")
                ||  manzana.getLongitud().equals("") ||  manzana.getAltura().equals("") ||  manzana.getPrecision().equals("")){
            return false;
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

        if((validar.getDirec_barrio()!=null && !validar.getDirec_barrio().equals(""))
                || (validar.getTerritorio_etnico()!=null && !validar.getTerritorio_etnico().equals(""))
                || (validar.getClase()!=null && !validar.getClase().equals(""))
                || (validar.getFechaConteo()!=null && !validar.getFechaConteo().equals(""))
                || (validar.getManzana()!=null && !validar.getManzana().equals(""))
                || (validar.getImei()!=null && !validar.getImei().equals(""))){

            if(validar.getTerritorio_etnico().equals("1") && validar.getSel_terr_etnico()!= null && !validar.getSel_terr_etnico().equals("")){
                if(validar.getSel_terr_etnico().equals("1") && validar.getCod_resg_etnico()!= null && !validar.getCod_resg_etnico().equals("") ){
                    retorno = true;
                }else{
                    if(validar.getSel_terr_etnico().equals("2") && validar.getCod_comun_etnico()!= null && !validar.getCod_comun_etnico().equals("") ){
                        retorno = true;
                    }
                }
            }

            if(validar.getExiste_unidad()!= null && !validar.getExiste_unidad().equals("")){
                if(validar.getExiste_unidad().equals("2") && validar.getTipo_novedad()!= null && !validar.getTipo_novedad().equals("")){
                    retorno = true;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }

        if(validar.getExiste_unidad().equals("1")){
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
                            if((lstUnid.getDirec_previa()!=null && !lstUnid.getDirec_previa().equals(""))
                            || (lstUnid.getDirec_p_tipo()!= null && !lstUnid.getDirec_p_tipo().equals("")
                            || (lstUnid.getNov_carto()!= null && !lstUnid.getNov_carto().equals(""))
                            || (lstUnid.getDirecc()!= null && !lstUnid.getDirecc().equals("")) )   ){
                                retorno = true;
                            }else{
                                return false;
                            }
                            if(lstUnid.getEstado_unidad_observacion()!= null && lstUnid.getEstado_unidad_observacion().equals("")){
                                if(lstUnid.getEstado_unidad_observacion().equals("1") && lstUnid.getTipo_unidad_observacion()!= null && !lstUnid.getTipo_unidad_observacion().equals("")   ){
                                    if(lstUnid.getTipo_unidad_observacion().equals("3") && lstUnid.getTipo_vendedor()!= null && !lstUnid.getTipo_vendedor().equals("")){
                                        if(lstUnid.getSector_economico()!= null && !lstUnid.getSector_economico().equals("")
                                                && lstUnid.getUnidad_osbservacion()!= null && !lstUnid.getUnidad_osbservacion().equals("")){
                                            retorno = true;
                                        }else{
                                            return false;
                                        }
                                    }else{
                                        return false;
                                    }
                                }
                                if(lstUnid.getEstado_unidad_observacion().equals("2")){
                                    if((lstUnid.getTipo_unidad_observacion()!= null && !lstUnid.getTipo_unidad_observacion().equals(""))
                                    || (lstUnid.getSector_economico()!= null && !lstUnid.getSector_economico().equals(""))
                                    || (lstUnid.getObservacion()!= null && !lstUnid.getObservacion().equals("")) ){
                                        retorno = true;
                                    }else{
                                        return false;
                                    }
                                }

                                if(lstUnid.getEstado_unidad_observacion().equals("3")){
                                    if((lstUnid.getTipo_unidad_observacion()!= null && !lstUnid.getTipo_unidad_observacion().equals(""))
                                            || (lstUnid.getSector_economico()!= null && !lstUnid.getSector_economico().equals("")) ){
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
        retorno.setPto_alt_gps(manzana.getAltura());
        retorno.setPto_lat_gps(manzana.getLatitud());
        retorno.setPto_lon_gps(manzana.getLongitud());
        retorno.setPto_pre_gp(manzana.getPrecision());
        retorno.setDpto(manzana.getDepartamento());
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
        retorno.setNov_carto(unidad.getNov_carto());
        retorno.setEstado_unidad_observacion(unidad.getEstado_unidad_observacion());
        retorno.setTipo_unidad_observacion(unidad.getTipo_unidad_observacion());
        retorno.setTipo_vendedor(unidad.getTipo_vendedor());
        retorno.setSector_economico(unidad.getSector_economico());
        retorno.setUnidad_osbservacion(unidad.getUnidad_observacion());
        retorno.setObservacion(unidad.getObservacion());

        retorno.setFecha_modificacion(unidad.getFechaModificacion());
        retorno.setId_edificacion(unidad.getId_edificacion());
        retorno.setId_manzana(unidad.getId_manzana());
        retorno.setId_manzana_edificio_unidad(unidad.getId_manzana()+unidad.getId_edificacion()+unidad.getId_unidad());
        retorno.setId_unidad_economica(unidad.getId_unidad());

        return retorno;
    }

    /**
     * Metodo de guardado de la manzana
     * @return
     */
    private Manzana saveManzana(){
        manzana.setId_manzana(id_manzana);
        manzana.setFecha(util.getFechaActual());
        manzana.setDepartamento(noDEPA);
        manzana.setMunicipio(noMPIO);
        manzana.setClase(editCLASE.getText().toString());
        manzana.setLocalidad(noLOCALIDAD);
        manzana.setCentro_poblado(noCENTRO);
        manzana.setTerritorioEtnico(TERRITORIO_ETNICO);
        manzana.setSelTerritorioEtnico(SEL_TERR_ETNICO);

        if(Util.stringNullEmptys(TERRITORIO_ETNICO) && TERRITORIO_ETNICO.equals("2")){
            manzana.setResguardoEtnico(null);
            manzana.setComunidadEtnica(null);
            manzana.setSelTerritorioEtnico(null);
        }

        if(Util.stringNullEmptys(selRESGUARDO)){
            manzana.setResguardoEtnico(db.getCodResguardoIndigena(selRESGUARDO,false));
        }else{
            manzana.setResguardoEtnico(null);
        }

        if(Util.stringNullEmptys(selCOMUNIDAD_NEGRA)){
            manzana.setComunidadEtnica(db.getCodComunidadNegra(selCOMUNIDAD_NEGRA,false));
        }else{
            manzana.setComunidadEtnica(null);
        }

        manzana.setCoordinacionOperativa(editCO.getText().toString());
        manzana.setAreaOperativa(editAO.getText().toString());
        manzana.setUnidad_cobertura(editAG.getText().toString());
        manzana.setACER(editACER.getText().toString());
        manzana.setBarrio(editDIREC_BARRIO.getText().toString());
        manzana.setExisteUnidad(EXISTE_UNIDAD);
        manzana.setTipoNovedad(TIPO_NOVEDAD);

        if(Util.stringNullEmptys(EXISTE_UNIDAD) && EXISTE_UNIDAD.equals("1")){
            manzana.setTipoNovedad(null);
        }

        manzana.setManzana(manzana.getManzana());
        manzana.setCod_enumerador(session.getusename());
        manzana.setImei(idDevice);
        return manzana;

    }

    /**
     * Metodo para llamar el formulario
     */
    private void llenarFormulario(){
        Manzana manzana_db =new Manzana();
        Manzana manzana_calculado=new Manzana();
        manzana_db=db.getManzana(id_manzana);
        manzana_calculado=sp.getDatosCalculados(id_manzana);

        fecha_conteo.setText("Fecha: "+manzana_db.getFecha());

        if(TextUtils.isEmpty(manzana_db.getFecha())){
            SimpleDateFormat sdf = new SimpleDateFormat("dd / MM / yyyy", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            fecha_conteo.setText("Fecha: "+currentDateandTime);
        }

        if(manzana_db!= null
                && manzana_db.getDepartamento()!= null && !manzana_db.getDepartamento().isEmpty()
                && manzana_db.getMunicipio()!= null && !manzana_db.getMunicipio().isEmpty()
                && manzana_db.getClase()!= null && !manzana_db.getClase().isEmpty()
                && manzana_db.getLocalidad()!= null && !manzana_db.getLocalidad().isEmpty()
                && manzana_db.getCentro_poblado()!= null && !manzana_db.getCentro_poblado().isEmpty()
        ){
            noDEPA = manzana_db.getDepartamento();
            noMPIO = manzana_db.getMunicipio();
            //noLOCALIDAD = manzana_db.getLocalidad();
            noLOCALIDAD = "05001000";
            noCENTRO = manzana_db.getCentro_poblado();

            editDPTO.setText(db.getDepartamento(manzana_db.getDepartamento()));
            editMPIO.setText(db.getMunicipio(manzana_db.getMunicipio()));
            editCLASE.setText(manzana_db.getClase());

            editCOM_LOC.setText(db.getLocalidadDIVIPOLA("05001000"));
            //editCOM_LOC.setText(db.getLocalidad(manzana_db.getLocalidad()));
            editC_POB.setText(db.getCentroPoblado(manzana_db.getCentro_poblado()));
        }else{
            noDEPA = manzana_calculado.getDepartamento();
            noMPIO = manzana_calculado.getMunicipio();
            //noLOCALIDAD = manzana_calculado.getLocalidad();
            noLOCALIDAD = "05001000";
            noCENTRO = manzana_calculado.getCentro_poblado();

            editDPTO.setText(db.getDepartamento(manzana_calculado.getDepartamento()));
            editMPIO.setText(db.getMunicipio(manzana_calculado.getMunicipio()));
            editCLASE.setText(manzana_calculado.getClase());

            editCOM_LOC.setText(db.getLocalidadDIVIPOLA("05001000"));
            //editCOM_LOC.setText(db.getLocalidad(manzana_calculado.getLocalidad()));
            editC_POB.setText(db.getCentroPoblado(manzana_calculado.getCentro_poblado()));
        }

        if(manzana_db.getTerritorioEtnico()!= null && manzana_db.getTerritorioEtnico().toString().equals("1")){
            id_pregunta2_4_si.setChecked(true);
            TERRITORIO_ETNICO = manzana_db.getTerritorioEtnico();
            linearTerritorioEtnico.setVisibility(View.VISIBLE);
        }
        if(manzana_db.getTerritorioEtnico()!= null && manzana_db.getTerritorioEtnico().toString().equals("2")){
            id_pregunta2_4_no.setChecked(true);
            TERRITORIO_ETNICO = manzana_db.getTerritorioEtnico();
            linearTerritorioEtnico.setVisibility(View.GONE);
        }

        if(manzana_db.getSelTerritorioEtnico()!= null && manzana_db.getSelTerritorioEtnico().toString().equals("1")){
            id_pregunta2_5_1.setChecked(true);
            id_pregunta2_5_2.setChecked(false);
            SEL_TERR_ETNICO = manzana_db.getTerritorioEtnico();
            linearResguardo.setVisibility(View.VISIBLE);
            linearComunidadNegra.setVisibility(View.GONE);
        }
        if(manzana_db.getSelTerritorioEtnico()!= null && manzana_db.getSelTerritorioEtnico().toString().equals("2")){
            id_pregunta2_5_1.setChecked(false);
            id_pregunta2_5_2.setChecked(true);
            SEL_TERR_ETNICO = manzana_db.getTerritorioEtnico();
            linearResguardo.setVisibility(View.GONE);
            linearComunidadNegra.setVisibility(View.VISIBLE);
        }

        if(manzana_db.getExisteUnidad()!= null && manzana_db.getExisteUnidad().toString().equals("1")){
            id_pregunta5_2_si.setChecked(true);
            EXISTE_UNIDAD = "1";
            linearAgregarEdificacion.setVisibility(View.VISIBLE);
            linearExisteEdificaciones.setVisibility(View.VISIBLE);
            linearTipoNovedad.setVisibility(View.GONE);
        }
        if(manzana_db.getExisteUnidad()!= null && manzana_db.getExisteUnidad().toString().equals("2")){
            id_pregunta5_2_no.setChecked(true);
            EXISTE_UNIDAD = "2";
            linearAgregarEdificacion.setVisibility(View.GONE);
            linearExisteEdificaciones.setVisibility(View.GONE);
            linearTipoNovedad.setVisibility(View.VISIBLE);
        }


        if(manzana_db.getTipoNovedad()!= null && manzana_db.getTipoNovedad().toString().equals("1")){
            id_pregunta5_3_1.setChecked(true);
            TIPO_NOVEDAD  = manzana_db.getTipoNovedad();
        }
        if(manzana_db.getTipoNovedad()!= null && manzana_db.getTipoNovedad().toString().equals("2")){
            id_pregunta5_3_2.setChecked(true);
            TIPO_NOVEDAD  = manzana_db.getTipoNovedad();
        }
        if(manzana_db.getTipoNovedad()!= null && manzana_db.getTipoNovedad().toString().equals("3")){
            id_pregunta5_3_3.setChecked(true);
            TIPO_NOVEDAD  = manzana_db.getTipoNovedad();
        }
        if(manzana_db.getTipoNovedad()!= null && manzana_db.getTipoNovedad().toString().equals("4")){
            id_pregunta5_3_4.setChecked(true);
            TIPO_NOVEDAD  = manzana_db.getTipoNovedad();
        }
        if(manzana_db.getTipoNovedad()!= null && manzana_db.getTipoNovedad().toString().equals("5")){
            id_pregunta5_3_5.setChecked(true);
            TIPO_NOVEDAD  = manzana_db.getTipoNovedad();
        }
        if(manzana_db.getTipoNovedad()!= null && manzana_db.getTipoNovedad().toString().equals("6")){
            id_pregunta5_3_6.setChecked(true);
            TIPO_NOVEDAD  = manzana_db.getTipoNovedad();
        }
        if(manzana_db.getTipoNovedad()!= null && manzana_db.getTipoNovedad().toString().equals("7")){
            id_pregunta5_3_7.setChecked(true);
            TIPO_NOVEDAD  = manzana_db.getTipoNovedad();
        }

        editDIREC_BARRIO.setText(manzana_db.getBarrio());
        spinnerCOD_RESG_ETNICO.setSelection(getIndexSpinner(spinnerCOD_RESG_ETNICO, db.getCodResguardoIndigena(manzana_db.getResguardoEtnico(),true) ));
        selRESGUARDO = manzana_db.getResguardoEtnico();
        spinnerCOD_COMUN_ETNICO.setSelection(getIndexSpinner(spinnerCOD_COMUN_ETNICO,db.getCodComunidadNegra(manzana_db.getComunidadEtnica(), true) ));
        selCOMUNIDAD_NEGRA = manzana_db.getComunidadEtnica();

        if(manzana_db.getFinalizado().toUpperCase().equals("SI")){
            finalizacion = true;
        }else{
            finalizacion = false;
        }

        if(manzana!= null && Util.stringNullEmptys(manzana.getAltura()) && Util.stringNullEmptys(manzana.getPrecision())
                && Util.stringNullEmptys(manzana.getLatitud()) && Util.stringNullEmptys(manzana.getLongitud())){
            latlon.setText("Latitud: "+manzana.getLatitud()+"\nLongitud: "+manzana.getLongitud()
                    +"\nAltura: "+manzana.getAltura()+" m\nPrecisión: "+manzana.getPrecision()+" m");
        }else{
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

        if(manzana_db.getLatitud()!= null && !manzana_db.getLatitud().isEmpty()){
            coord=manzana_db.getLatitud();
        }

        if(manzana_db.getLatitud()!= null && !manzana_db.getLatitud().isEmpty()
        && manzana_db.getLongitud()!= null && !manzana_db.getLongitud().isEmpty()
        && manzana_db.getAltura()!= null && !manzana_db.getAltura().isEmpty()
        && manzana_db.getPrecision()!= null && !manzana_db.getPrecision().isEmpty() ){
            manzana.setLatitud(manzana_db.getLatitud());
            manzana.setLongitud(manzana_db.getLongitud());
            manzana.setAltura(manzana_db.getAltura());
            manzana.setPrecision(manzana_db.getPrecision());
            Log.d("lat:",manzana_db.getLatitud());
        }

        if(manzana_db.getFinalizado().equals("Si")){
            linearFinalizarFormulario.setVisibility(View.GONE);
            spinnerCOD_RESG_ETNICO.setEnabled(false);
            spinnerCOD_COMUN_ETNICO.setEnabled(false);
            linearAgregarEdificacion.setVisibility(View.GONE);
            linearExisteEdificaciones.setVisibility(View.GONE);
            linearGuardarFormularioManzana.setVisibility(View.GONE);
            latlon.setVisibility(View.GONE);
            linearEliminarFormulario.setVisibility(View.GONE);
            boolean entro = false;
            if(manzana_db.getDoble_sincronizado() != null && manzana_db.getDoble_sincronizado().equals("No")
                && manzana_db.getSincronizado_nube()!= null && manzana_db.getSincronizado_nube().equals("No")){
                entro = true;
            }
            if(manzana_db.getDoble_sincronizado() == null  && manzana_db.getSincronizado_nube()== null ){
                entro = true;
            }
            if(entro)
                linearHabilitarFormulario.setVisibility(View.GONE);  // Se desahlita la opcion por el ing.
            else
                linearHabilitarFormulario.setVisibility(View.GONE);
        }else{
            linearHabilitarFormulario.setVisibility(View.GONE);
        }


    }

    /**
     * Metodo para cargar los spnnier
     * @param spinner
     * @param myString
     * @return
     */
    private int getIndexSpinner(Spinner spinner, String myString){
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

    /**
     * Metodo que prepara los datos
     * @param id_manzana
     */
    private void prepararDatos(String id_manzana) {
        lstEdificaciones.addAll(db.getAcordeonEdificacion(id_manzana));
        mAdapterEdificacion.notifyDataSetChanged();
    }

    /**
     * Metodo para borrar el conteo
     *
     * @param position
     */
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

                            ConteoEdificacion edificacion = lstEdificaciones.get(position);

                            if(db.borrarEdificacion(id_manzana,edificacion.getId_edificacion())){
                                lstEdificaciones.remove(edificacion);
                                mAdapterEdificacion.notifyDataSetChanged();
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

    /**
     * Metodo que localiza el GPS la info
     * @return
     */
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


    /**
     * Metodo que retorna a la pantalla anterior
     */
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


    /**
     * Metodo que retorna atras
     */
    @Override
    public void onBackPressed() {
        retornar();
    }

}
