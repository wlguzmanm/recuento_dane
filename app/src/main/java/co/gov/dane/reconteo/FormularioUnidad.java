package co.gov.dane.reconteo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.gov.dane.reconteo.Preguntas.Edificacion;
import co.gov.dane.reconteo.Preguntas.Manzana;
import co.gov.dane.reconteo.Preguntas.UnidadEconomica;
import co.gov.dane.reconteo.backend.Database;
import co.gov.dane.reconteo.model.EsquemaEdificacionEnvioViewModel;
import co.gov.dane.reconteo.model.EsquemaManzanaEnvioViewModel;
import co.gov.dane.reconteo.model.EsquemaUnidadesEnvioViewModel;

public class FormularioUnidad extends AppCompatActivity {

    private String id_manzana,id_edificacion,id_unidad;

    private Spinner spinner_dir_a,estado_unidad,unidad_observacion,sector_economico;
    private EditText via_principal,via_secundaria,placa_cuadrante,
            complemento_direccion,estado_ocupado,estado_desocupado,estado_obra,
            emplazamiento_fijo,emplazamiento_semifijo,puesto_movil,emplazamiento_vivienda_actividad_economica,
            emplazamiento_obra_edificacion,unidades_comercio,unidades_industria,unidades_servicios,
            unidades_transporte,unidades_construccion,unidades_no_aplica,nombre_unidad,observaciones_unidad;

    private RadioGroup unidades_ocupadas, unidades_movil;
    private ViewGroup div_agrupado;

    private Database db;
    private Util util;
    private Mensajes msj;

    private LinearLayout div_estado_unidad,div_unidad_observacion,div_sector_economico,div_viv_economica,btn_dir_automatica,div_puesto_movil,div_unidades_movil;
    private TextView div_unidades_movil_text;
    private Integer unidades_se_encuentran_ocupadas = 0, unidades_puesto_movil = 0;
    //private final ArrayAdapter<String> arr_unidad_observacion,arr_unidad_observacion_1;
    private String idDevice;
    private Session session;

    ArrayAdapter<CharSequence> arr_unidad_observacion;
    ArrayAdapter<CharSequence> arr_unidad_observacion_1;
    ArrayAdapter<CharSequence> arr_unidad_observacion_2;

    ArrayAdapter<CharSequence> arr_sector_economico;
    ArrayAdapter<CharSequence> arr_sector_economico1;
    ArrayAdapter<CharSequence> arr_sector_economico2;

    int check = 0;

    private Boolean mostrar_div_obras_ocupado=false;
    private Boolean mostrar_div_obras_desocupado=false;

    Boolean chequeo_agrupado=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_unidad);
        session=new Session(FormularioUnidad.this);
        db = new Database(FormularioUnidad.this);
        util=new Util();
        msj=new Mensajes(FormularioUnidad.this);

        id_manzana = getIntent().getStringExtra("id_manzana");
        id_edificacion = getIntent().getStringExtra("id_edificacion");
        id_unidad = getIntent().getStringExtra("id_unidad");
        idDevice = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);

        via_principal =(EditText) findViewById(R.id.via_principal);
        via_secundaria =(EditText) findViewById(R.id.via_secundaria);
        placa_cuadrante =(EditText) findViewById(R.id.placa_cuadrante);
        complemento_direccion =(EditText) findViewById(R.id.complemento_direccion);
        estado_ocupado =(EditText) findViewById(R.id.estado_ocupado);
        estado_desocupado =(EditText) findViewById(R.id.estado_desocupado);
        estado_obra =(EditText) findViewById(R.id.estado_obra);
        emplazamiento_fijo =(EditText) findViewById(R.id.emplazamiento_fijo);
        emplazamiento_semifijo =(EditText) findViewById(R.id.emplazamiento_semifijo);
        puesto_movil =(EditText) findViewById(R.id.puesto_movil);
        emplazamiento_vivienda_actividad_economica =(EditText) findViewById(R.id.emplazamiento_vivienda_actividad_economica);
        emplazamiento_obra_edificacion =(EditText) findViewById(R.id.emplazamiento_obra_edificacion);
        unidades_comercio =(EditText) findViewById(R.id.unidades_comercio);
        unidades_industria =(EditText) findViewById(R.id.unidades_industria);
        unidades_servicios =(EditText) findViewById(R.id.unidades_servicios);
        unidades_transporte =(EditText) findViewById(R.id.unidades_transporte);
        unidades_construccion =(EditText) findViewById(R.id.unidades_construccion);
        unidades_no_aplica =(EditText) findViewById(R.id.unidades_no_aplica);
        unidades_no_aplica.setEnabled(false);
        nombre_unidad =(EditText) findViewById(R.id.nombre_unidad);
        observaciones_unidad =(EditText) findViewById(R.id.observaciones_unidad);

        estado_unidad=(Spinner) findViewById(R.id.estado_unidad);
        unidad_observacion=(Spinner) findViewById(R.id.unidad_observacion);
        sector_economico=(Spinner) findViewById(R.id.sector_economico);

        unidades_ocupadas=(RadioGroup) findViewById(R.id.unidades_ocupadas);
        unidades_movil=(RadioGroup) findViewById(R.id.unidades_movil);

        div_agrupado = (ViewGroup)findViewById(R.id.div_agrupado);

        div_unidades_movil=(LinearLayout) findViewById(R.id.div_unidades_movil);
        div_unidades_movil_text=(TextView) findViewById(R.id.div_unidades_movil_text);
        div_estado_unidad=(LinearLayout) findViewById(R.id.div_estado_unidad);
        div_unidad_observacion=(LinearLayout) findViewById(R.id.div_unidad_observacion);
        div_sector_economico=(LinearLayout) findViewById(R.id.div_sector_economico);
        div_viv_economica=(LinearLayout) findViewById(R.id.div_viv_economica);
        btn_dir_automatica=(LinearLayout) findViewById(R.id.btn_dir_automatica);
        div_puesto_movil=(LinearLayout) findViewById(R.id.div_puesto_movil);


        estado_desocupado.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ocupado = estado_ocupado.getText().toString();
                if(!s.toString().equals("")){
                    int valor=StringToInt(String.valueOf(s));
                        if(valor==0){
                            if(ocupado.equals("") || ocupado.equals("1")){
                                mostrar_div_obras_desocupado=true;
                            }else{
                                mostrar_div_obras_desocupado=true;
                            }
                        }else{
                        }
                    unidades_no_aplica.setText(s);
                }else{
                }
            }
        });

        div_viv_economica.setVisibility(View.GONE);

        estado_ocupado.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String ocupado = estado_ocupado.getText().toString();
                if(ocupado.equals("") || ocupado.equals("0")){
                    div_viv_economica.setVisibility(View.GONE);
                    emplazamiento_vivienda_actividad_economica.setText("0");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String desocupado = estado_desocupado.getText().toString();
                if(!s.toString().equals("")){
                    int valor=StringToInt(String.valueOf(s));
                    if(valor>0){
                        if(desocupado.equals("") || desocupado.equals("0")){
                            mostrar_div_obras_ocupado=true;
                            div_viv_economica.setVisibility(View.VISIBLE);
                        }else{
                            mostrar_div_obras_ocupado=true;
                            div_viv_economica.setVisibility(View.VISIBLE);
                        }
                    }else{
                        if(desocupado.equals("") || desocupado.equals("0")){
                            mostrar_div_obras_ocupado=false;
                            div_viv_economica.setVisibility(View.VISIBLE);
                        }
                    }
                }else{
                    if(desocupado.equals("") || desocupado.equals("0")){
                        mostrar_div_obras_ocupado=null;
                        div_viv_economica.setVisibility(View.VISIBLE);
                        div_estado_unidad.setVisibility(View.VISIBLE);
                        div_unidad_observacion.setVisibility(View.VISIBLE);
                        div_sector_economico.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        div_agrupado.setVisibility(View.GONE);

        ImageView atras = (ImageView) findViewById(R.id.atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retornar();
            }
        });

        spinner_dir_a = (Spinner) findViewById(R.id.dir_a);
        ArrayAdapter<CharSequence> adapter_dir_a = ArrayAdapter.createFromResource(this,
                R.array.dir_a, android.R.layout.simple_spinner_item);
        adapter_dir_a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_dir_a.setAdapter(adapter_dir_a);

        ArrayAdapter<CharSequence> adapter_estado_unidad = ArrayAdapter.createFromResource(this,
                R.array.estado_unidad, android.R.layout.simple_spinner_item);
        adapter_estado_unidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estado_unidad.setAdapter(adapter_estado_unidad);


        //Validaciones

        unidades_ocupadas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton movilSi = (RadioButton) findViewById(R.id.unidades_movil_si);
                RadioButton movilNo = (RadioButton) findViewById(R.id.unidades_movil_no);
                switch (checkedId) {
                    case -1:

                        Log.d("TAG", "Choices cleared!");
                        break;
                    case R.id.unidades_ocupadas_si:
                        movilSi.setChecked(false);
                        movilNo.setChecked(false);

                        chequeo_agrupado=true;
                        unidades_se_encuentran_ocupadas= 1;
                        unidades_puesto_movil  = null;

                        estado_ocupado.setText(null);
                        estado_desocupado.setText(null);

                        div_agrupado.setVisibility(View.VISIBLE);

                        div_estado_unidad.setVisibility(View.VISIBLE);
                        div_unidad_observacion.setVisibility(View.VISIBLE);
                        div_sector_economico.setVisibility(View.VISIBLE);

                        estado_unidad.setVisibility(View.GONE);
                        unidad_observacion.setVisibility(View.GONE);
                        sector_economico.setVisibility(View.GONE);


                        div_unidades_movil.setVisibility(View.VISIBLE);
                        div_unidades_movil_text.setVisibility(View.VISIBLE);

                        RadioButton movilSI = (RadioButton) findViewById(R.id.unidades_movil_si);
                        movilSI.setChecked(false);

                        RadioButton movilNO = (RadioButton) findViewById(R.id.unidades_movil_no);
                        movilNO.setChecked(false);

                        break;
                    case R.id.unidades_ocupadas_no:
                        movilSi.setChecked(false);
                        movilNo.setChecked(false);

                        chequeo_agrupado=true;
                        unidades_se_encuentran_ocupadas= 0;
                        unidades_puesto_movil  = null;
                        div_unidades_movil.setVisibility(View.GONE);
                        div_unidades_movil_text.setVisibility(View.GONE);
                        estado_ocupado.setText(null);
                        estado_desocupado.setText(null);

                        div_agrupado.setVisibility(View.VISIBLE);

                        div_estado_unidad.setVisibility(View.GONE);
                        div_unidad_observacion.setVisibility(View.GONE);
                        div_sector_economico.setVisibility(View.GONE);

                        estado_unidad.setVisibility(View.VISIBLE);
                        unidad_observacion.setVisibility(View.VISIBLE);
                        sector_economico.setVisibility(View.VISIBLE);

                        break;
                }
            }
        });

        unidades_movil.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:

                        Log.d("TAG", "Choices cleared!");
                        break;
                    case R.id.unidades_movil_si:
                        unidades_puesto_movil = 1;
                        //div_unidad_observacion.setVisibility(View.GONE);
                        div_unidad_observacion.setVisibility(View.VISIBLE);
                        div_sector_economico.setVisibility(View.VISIBLE);

                        unidades_comercio.setText(null);
                        unidades_industria.setText(null);
                        unidades_servicios.setText(null);
                        unidades_transporte.setText(null);
                        unidades_construccion.setText(null);
                        unidades_no_aplica.setText(null);
                        emplazamiento_semifijo.setText(null);
                        emplazamiento_fijo.setText(null);

                        div_puesto_movil.setVisibility(View.VISIBLE);
                        emplazamiento_semifijo.setEnabled(false);
                        emplazamiento_fijo.setEnabled(false);
                        emplazamiento_vivienda_actividad_economica.setEnabled(false);
                        emplazamiento_vivienda_actividad_economica.setText(null);

                        unidades_construccion.setEnabled(false);
                        unidades_transporte.setEnabled(true);

                        unidades_transporte.setText(null);
                        unidades_construccion.setText(null);

                        estado_desocupado.setText(null);
                        estado_desocupado.setEnabled(false);

                        break;
                    case R.id.unidades_movil_no:
                        unidades_puesto_movil = 0;
                        div_unidad_observacion.setVisibility(View.VISIBLE);
                        //div_sector_economico.setVisibility(View.GONE);
                        div_sector_economico.setVisibility(View.VISIBLE);
                        div_puesto_movil.setVisibility(View.GONE);
                        puesto_movil.setText("0");
                        unidades_comercio.setText(null);
                        unidades_industria.setText(null);
                        unidades_servicios.setText(null);
                        unidades_transporte.setText(null);
                        unidades_construccion.setText(null);
                        //unidades_no_aplica.setText(null);
                        emplazamiento_fijo.setText(null);
                        emplazamiento_semifijo.setText(null);

                        emplazamiento_semifijo.setEnabled(true);
                        emplazamiento_fijo.setEnabled(true);
                        emplazamiento_vivienda_actividad_economica.setEnabled(true);
                        emplazamiento_vivienda_actividad_economica.setText(null);
                        unidades_construccion.setEnabled(true);
                        estado_desocupado.setEnabled(true);

                        estado_desocupado.setText(null);
                        unidades_no_aplica.setText(null);

                        break;
                }
            }
        });

        arr_unidad_observacion = ArrayAdapter.createFromResource(this,
                R.array.unidad_observacion, android.R.layout.simple_spinner_item);
        arr_unidad_observacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        arr_unidad_observacion_1 = ArrayAdapter.createFromResource(this,
                R.array.unidad_observacion_1, android.R.layout.simple_spinner_item);
        arr_unidad_observacion_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        arr_unidad_observacion_2 = ArrayAdapter.createFromResource(this,
                R.array.unidad_observacion_2, android.R.layout.simple_spinner_item);
        arr_unidad_observacion_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        arr_sector_economico = ArrayAdapter.createFromResource(this,
                R.array.sector_economico, android.R.layout.simple_spinner_item);
        arr_sector_economico.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        arr_sector_economico1 = ArrayAdapter.createFromResource(this,
                R.array.sector_economico1, android.R.layout.simple_spinner_item);
        arr_sector_economico1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        arr_sector_economico2 = ArrayAdapter.createFromResource(this,
                R.array.sector_economico1, android.R.layout.simple_spinner_item);
        arr_sector_economico2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        unidad_observacion.setAdapter(arr_unidad_observacion);

        estado_unidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(++check > 1) {
                    switch(i){

                        case 0:
                            unidad_observacion.setAdapter(arr_unidad_observacion);
                            sector_economico.setAdapter(arr_sector_economico);
                            break;
                        case 1:

                            unidad_observacion.setAdapter(arr_unidad_observacion_1);
                            sector_economico.setAdapter(arr_sector_economico1);
                            break;
                        case 2:
                            unidad_observacion.setAdapter(arr_unidad_observacion_2);
                            sector_economico.setAdapter(arr_sector_economico2);
                            break;
                    }
                }

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        llenarFormulario();

        LinearLayout guardar_formulario = (LinearLayout) findViewById(R.id.guardar_formulario);

        guardar_formulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            UnidadEconomica unidad=new UnidadEconomica();

            if(chequeo_agrupado){
                String validacion = validarFormulario();
                if(validacion.equals("")){
                    if(unidades_se_encuentran_ocupadas == 0){
                        msj.generarToast("Sumas OK");

                            unidad=saveFormulario();
                            unidad.setImei(idDevice);
                            if(db.guardarUnidadEconomica(unidad,id_manzana,id_edificacion,id_unidad)){
                                msj.generarToast("Formulario Guardado");
                            }
                            FormularioUnidad.this.finish();
                    }else{
                        int verificacion=verificarSumas();
                        if(verificacion==2){
                            msj.generarToast("Sumas OK");
                            unidad=saveFormulario();
                            unidad.setImei(idDevice);
                            if(db.guardarUnidadEconomica(unidad,id_manzana,id_edificacion,id_unidad)){
                                msj.generarToast("Formulario Guardado");
                            }

                        }else if(verificacion==1){
                            msj.generarToast("Ingrese valores","error");
                        }else if(verificacion==0){
                            msj.generarToast("Sumas no coinciden","error");
                        }
                    }
                }else{
                    msj.generarToast(validacion,"error");
                }
            }else{
                msj.generarToast("Seleccione agrupamiento","error");
            }

            }
        });

        btn_dir_automatica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnidadEconomica unidad= new UnidadEconomica();

                String id_actual=id_unidad;
                int id_anterior=StringToInt(id_unidad);

                if(id_anterior==1){

                }else{
                    id_anterior=id_anterior-1;
                }

                unidad=db.getUnidadEconomica(id_manzana,id_edificacion,String.valueOf(id_anterior));

                spinner_dir_a.setSelection(getIndexSpinner(spinner_dir_a,unidad.getTipo_via_principal()));

                via_principal.setText(unidad.getVia_principal());
                via_secundaria.setText(unidad.getVia_secundaria());
                msj.generarToast("Dirección Generada");
            }
        });

        Manzana manzana =new Manzana();
        manzana=db.getManzana(id_manzana);

        if(manzana.getFinalizado().equals("Si")){
            guardar_formulario.setVisibility(View.GONE);
            btn_dir_automatica.setVisibility(View.GONE);
        }

        ImageView estado_info=(ImageView) findViewById(R.id.estado_info);
        estado_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msj.dialogoMensaje("Estado","Describir una situación en la cual se encuentra la unidad de observación. Puede ser Ocupado, Desocupado o una Obra.");
            }
        });
        ImageView info_observacion=(ImageView) findViewById(R.id.info_observacion);
        info_observacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msj.dialogoMensaje("Tipo de unidad de observación","Establecimiento: Espacio independiente donde se realiza una actividad económica. Tiene una ubicación fija (permanente) y está delimitado de forma que se puede diferenciar claramente un establecimiento de otros espacios contiguos. \n\nEstablecimiento Fijo: Tipo de establecimiento que cuenta con domicilio. Generalmente cuentan con avisos y letreros. Corresponde a locales, oficinas, consultorios, talleres, fabricas, tiendas, almacenes, bodegas, plantas de producción, etc. Puede tener estado ocupado o desocupado. \n\nEstablecimiento semifijo: Instalaciones permanentes, simples (casetas o kioscos) adheridas al suelo. Están ubicadas en espacio público o privado. Puede tener estado ocupado o desocupado. \n\nVivienda con actividad económica: Aplica para locales y kioscos que se encuentran vacantes. Solo puede tener estado ocupado \n\nPuesto móvil: Estructura de cualquier material, fácilmente transportable (no permanente), no fija al suelo, ubicada generalmente en el espacio público en el cual se desarrolla una actividad económica. Solo puede tener estado ocupado. \n\nObra de edificación. Lugar donde se realiza una obra de edificaciones (las obras civiles no aplican). Generalmente tiene cerramiento, vallas, maquinaria y obreros.");
            }
        });
        ImageView info_sector=(ImageView) findViewById(R.id.info_sector);
        info_sector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msj.dialogoMensaje("Sector económico","Actividad Económica: Grupo de operaciones en las cuales se combinan recursos tales como equipo, mano de obra, técnicas de fabricación e insumos, cuyo resultado es un conjunto de bienes (algo tangible) o servicios (algo intangible). \n\nSector Económico (SE): Grupos de actividades económicas con características afines. Si la unidad de observación se clasificó con estado ocupado, se debe clasificar en un sector económico diferente a <no aplica>. Si el estado es desocupado el sector económico es <no aplica>.");
            }
        });
        ImageView info_agrupado_si=(ImageView) findViewById(R.id.info_agrupado_si);
        info_agrupado_si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msj.dialogoMensaje("Registro Agrupado","Registros de edificaciones en espacio público o espacio privado, con un gran número de unidades.\n\nAplica para: \n\n1) Centros comerciales de menor tamaño (Ej. san andresito, pasajes comerciales, plazas de mercado).\n\n2) Dos o más unidades económicas del tipo puesto móvil presentes en el mismo frente de manzana.\n\n3) Edificaciones con un gran número de unidades económicas.");
            }
        });
        ImageView info_agrupado_no=(ImageView) findViewById(R.id.info_agrupado_no);
        info_agrupado_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msj.dialogoMensaje("Registro por unidad","Registro de unidades económicas una a la vez (unidad por unidad).\n\nAplica para: \n\n1) Una única unidad económica en una edificación. \n\n2) Varias unidades en una edificación diferente a centro comercial.\n\n3) Centros comerciales de grandes superficies (Generalmente albergan establecimientos de marcas reconocidas y que operan con varias sucursales).\n\n4) Unidad establecimiento semifijo en espacio público.\n\n5) Una (única) unidad económica del tipo puesto móvil.\n\n6) Obra de edificación (Las obras civiles no son objeto del conteo).");
            }
        });
    }

    private String validarFormulario() {
        String retorno = "";
        int desocupado = (!estado_desocupado.getText().toString().equals(""))? Integer.valueOf(estado_desocupado.getText().toString()):0;
        int ocupado = (!estado_ocupado.getText().toString().equals(""))? Integer.valueOf(estado_ocupado.getText().toString()):0;
        int vivienda = (!emplazamiento_vivienda_actividad_economica.getText().toString().equals(""))? Integer.valueOf(emplazamiento_vivienda_actividad_economica.getText().toString()):0;

        int total  = desocupado + ocupado;


        if(unidades_se_encuentran_ocupadas==1 && unidades_puesto_movil == 0 ){
            if(ocupado==1 && desocupado==0)
                retorno = retorno + "El estado de la Unidad en Ocupado no puede ser de valor  1, cuando es agrupada.\n";

            if(total<=1)
                retorno = retorno + "La suma de ocupado más desocupado debe ser mayor a 1.\n";

            if(vivienda > ocupado){
                retorno = retorno + "Vivienda con actividad económica no puede ser mayor al estado de la unidad Ocupado en "+ocupado +". \n";
            }
        }
        if(via_principal.getText().toString().equals("") || spinner_dir_a.getSelectedItem().toString().equals("") || via_secundaria.getText().toString().equals("")
                || placa_cuadrante.getText().toString().equals("") || nombre_unidad.getText().toString().equals("") ){
            retorno = retorno + "Debe diligenciar todos los campos obligatorios. \n";
        }
        return retorno;
    }

    /**
     * Metodo que carga la informacion de todo el formulario de una manzana.
     *
     * @return
     */
    private EsquemaManzanaEnvioViewModel getFormulario() {
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



    private UnidadEconomica saveFormulario(){

        UnidadEconomica unidad=new UnidadEconomica();

        unidad.setTipo_via_principal(spinner_dir_a.getSelectedItem().toString());
        unidad.setVia_principal(via_principal.getText().toString());
        unidad.setVia_secundaria(via_secundaria.getText().toString());
        unidad.setPlaca_cuadrante(placa_cuadrante.getText().toString());
        unidad.setComplemento_direccion(complemento_direccion.getText().toString());
        unidad.setUnidades_ocupadas(String.valueOf(unidades_se_encuentran_ocupadas));

        unidad.setNombre_unidad_observacion(nombre_unidad.getText().toString());
        unidad.setObservaciones_unidad_observacion(observaciones_unidad.getText().toString());
        unidad.setObservaciones_sn(String.valueOf((unidades_puesto_movil)));

        if(unidades_se_encuentran_ocupadas == 1){

            unidad.setEstado_ocupado(estado_ocupado.getText().toString());
            unidad.setEstado_desocupado(estado_desocupado.getText().toString());
            unidad.setEstado_obra(estado_obra.getText().toString());
            unidad.setEstablecimiento_fijo(emplazamiento_fijo.getText().toString());
            unidad.setEstablecimiento_semifijo(emplazamiento_semifijo.getText().toString());
            unidad.setPuesto_movil(puesto_movil.getText().toString());
            unidad.setVivienda_actividad_economica(emplazamiento_vivienda_actividad_economica.getText().toString());
            unidad.setObra_edificacion(emplazamiento_obra_edificacion.getText().toString());
            unidad.setComercio(unidades_comercio.getText().toString());
            unidad.setIndustria(unidades_industria.getText().toString());
            unidad.setServicios(unidades_servicios.getText().toString());
            unidad.setTransporte(unidades_transporte.getText().toString());
            unidad.setConstruccion(unidades_construccion.getText().toString());
            unidad.setNo_aplica(unidades_no_aplica.getText().toString());
        }else{

            String oc=estado_unidad.getSelectedItem().toString();
            if(oc.equals("Ocupado")){
                unidad.setEstado_ocupado("1");
                unidad.setEstado_desocupado("0");
                unidad.setEstado_obra("0");
            }else if(oc.equals("Desocupado")){
                unidad.setEstado_desocupado("1");
                unidad.setEstado_ocupado("0");
                unidad.setEstado_obra("0");

            }else if(oc.equals("Obra")){
                unidad.setEstado_obra("1");
                unidad.setEstado_ocupado("0");
                unidad.setEstado_desocupado("0");

            }

            String uo=unidad_observacion.getSelectedItem().toString();
            if(uo.equals("Establecimiento Fijo")){
                unidad.setEstablecimiento_fijo("1");
                unidad.setEstablecimiento_semifijo("0");
                unidad.setPuesto_movil("0");
                unidad.setVivienda_actividad_economica("0");
                unidad.setObra_edificacion("0");
            }
            else if(uo.equals("Establecimiento Semifijo")){
                unidad.setEstablecimiento_fijo("0");
                unidad.setEstablecimiento_semifijo("1");
                unidad.setPuesto_movil("0");
                unidad.setVivienda_actividad_economica("0");
                unidad.setObra_edificacion("0");
            }
            else if(uo.equals("Puesto Móvil")){
                unidad.setEstablecimiento_fijo("0");
                unidad.setEstablecimiento_semifijo("0");
                unidad.setPuesto_movil("1");
                unidad.setVivienda_actividad_economica("0");
                unidad.setObra_edificacion("0");
            }
            else if(uo.equals("Vivienda con actividad económica")){
                unidad.setEstablecimiento_fijo("0");
                unidad.setEstablecimiento_semifijo("0");
                unidad.setPuesto_movil("0");
                unidad.setVivienda_actividad_economica("1");
                unidad.setObra_edificacion("0");
            }else if(uo.equals("Obra de edificación")){
                unidad.setEstablecimiento_fijo("0");
                unidad.setEstablecimiento_semifijo("0");
                unidad.setPuesto_movil("0");
                unidad.setVivienda_actividad_economica("0");
                unidad.setObra_edificacion("1");
            }


            String se=sector_economico.getSelectedItem().toString();
            if(se.equals("Comercio")){
                unidad.setComercio("1");
                unidad.setIndustria("0");
                unidad.setServicios("0");
                unidad.setTransporte("0");
                unidad.setConstruccion("0");
                unidad.setNo_aplica("0");
            }
            else if(se.equals("Industria")){
                unidad.setComercio("0");
                unidad.setIndustria("1");
                unidad.setServicios("0");
                unidad.setTransporte("0");
                unidad.setConstruccion("0");
                unidad.setNo_aplica("0");
            } else if(se.equals("Servicios")){
                unidad.setComercio("0");
                unidad.setIndustria("0");
                unidad.setServicios("1");
                unidad.setTransporte("0");
                unidad.setConstruccion("0");
                unidad.setNo_aplica("0");
            }else if(se.equals("Transporte")){
                unidad.setComercio("0");
                unidad.setIndustria("0");
                unidad.setServicios("0");
                unidad.setTransporte("1");
                unidad.setConstruccion("0");
                unidad.setNo_aplica("0");
            }else if(se.equals("Construcción")){
                unidad.setComercio("0");
                unidad.setIndustria("0");
                unidad.setServicios("0");
                unidad.setTransporte("0");
                unidad.setConstruccion("1");
                unidad.setNo_aplica("0");
            }else if(se.equals("No aplica")){
                unidad.setComercio("0");
                unidad.setIndustria("0");
                unidad.setServicios("0");
                unidad.setTransporte("0");
                unidad.setConstruccion("0");
                unidad.setNo_aplica("1");
            }
        }
        return unidad;
    }




    private void llenarFormulario(){

        UnidadEconomica unidad= new UnidadEconomica();

        unidad=db.getUnidadEconomica(id_manzana,id_edificacion,id_unidad);

        spinner_dir_a.setSelection(getIndexSpinner(spinner_dir_a,unidad.getTipo_via_principal()));

        via_principal.setText(unidad.getVia_principal());
        via_secundaria.setText(unidad.getVia_secundaria());
        placa_cuadrante.setText(unidad.getPlaca_cuadrante());
        complemento_direccion.setText(unidad.getComplemento_direccion());

        nombre_unidad.setText(unidad.getNombre_unidad_observacion());
        observaciones_unidad.setText(unidad.getObservaciones_unidad_observacion());

        String uni=unidad.getUnidades_ocupadas();

        if((uni!=null  && uni.equals("true")) ||  (  uni!= null && uni.equals("1")) ){
            RadioButton b = (RadioButton) findViewById(R.id.unidades_ocupadas_si);
            b.setChecked(true);

            estado_ocupado.setText(unidad.getEstado_ocupado());
            estado_desocupado.setText(unidad.getEstado_desocupado());
            estado_obra.setText(unidad.getEstado_obra());

            emplazamiento_fijo.setText(unidad.getEstablecimiento_fijo());
            emplazamiento_semifijo.setText(unidad.getEstablecimiento_semifijo());
            puesto_movil.setText(unidad.getPuesto_movil());
            unidades_puesto_movil = StringToInt(unidad.getObservaciones_sn());

            emplazamiento_vivienda_actividad_economica.setText(unidad.getVivienda_actividad_economica());
            emplazamiento_obra_edificacion.setText(unidad.getObra_edificacion());
            unidades_comercio.setText(unidad.getComercio());
            unidades_industria.setText(unidad.getIndustria());
            unidades_servicios.setText(unidad.getServicios());
            unidades_transporte.setText(unidad.getTransporte());
            unidades_construccion.setText(unidad.getConstruccion());
            unidades_no_aplica.setText(unidad.getNo_aplica());

        }
        Boolean entro = false;
        if( (uni!=null  && uni.equals("false")) ||  (uni != null && uni.equals("0"))){

            RadioButton b = (RadioButton) findViewById(R.id.unidades_ocupadas_no);
            b.setChecked(true);

            String oc=unidad.getEstado_ocupado();
            if(oc!=null  && oc.equals("1")){
                estado_unidad.setSelection(0);
                unidad_observacion.setAdapter(arr_unidad_observacion);
                sector_economico.setAdapter(arr_sector_economico);
                entro = true;
            }

            String oc1=unidad.getEstado_desocupado();
            if(oc1!=null  && oc1.equals("1")){
                estado_unidad.setSelection(1);
                unidad_observacion.setAdapter(arr_unidad_observacion_1);
                sector_economico.setAdapter(arr_sector_economico1);
                entro = true;
            }

            String oc2=unidad.getEstado_obra();
            if(oc2!=null  && oc2.equals("1")){
                estado_unidad.setSelection(2);
                unidad_observacion.setAdapter(arr_unidad_observacion_2);
                sector_economico.setAdapter(arr_sector_economico2);
                entro = true;
            }


            String uo1=unidad.getEstablecimiento_fijo();
            if(uo1!=null  && uo1.equals("1")){
                unidad_observacion.setSelection(getIndexSpinner(unidad_observacion,"Establecimiento Fijo"));
                div_unidad_observacion.setVisibility(View.GONE);
                entro = true;
            }
            String uo2=unidad.getEstablecimiento_semifijo();
            if(uo2!=null  && uo2.equals("1")){
                unidad_observacion.setSelection(getIndexSpinner(unidad_observacion,"Establecimiento Semifijo"));
                div_unidad_observacion.setVisibility(View.GONE);
                entro = true;
            }

            String uo3=unidad.getPuesto_movil();
            if(uo3!=null  && uo3.equals("1")){
                unidad_observacion.setSelection(getIndexSpinner(unidad_observacion,"Puesto Móvil"));
                div_unidad_observacion.setVisibility(View.GONE);
                entro = true;
            }
            String uo4=unidad.getVivienda_actividad_economica();
            if(uo4!=null  && uo4.equals("1")){
                unidad_observacion.setSelection(getIndexSpinner(unidad_observacion,"Vivienda con actividad económica"));
                div_unidad_observacion.setVisibility(View.GONE);
                entro = true;
            }


            String se1=unidad.getComercio();
            if(se1!=null  && se1.equals("1")){
                sector_economico.setSelection(getIndexSpinner(sector_economico,"Comercio"));
                div_sector_economico.setVisibility(View.GONE);
                entro = true;
            }
            String se2=unidad.getIndustria();
            if(se2!=null  && se2.equals("1")){
                sector_economico.setSelection(getIndexSpinner(sector_economico,"Industria"));
                div_sector_economico.setVisibility(View.GONE);
                entro = true;
            }

            String se3=unidad.getServicios();
            if(se3!=null  && se3.equals("1")){
                sector_economico.setSelection(getIndexSpinner(sector_economico,"Servicios"));
                div_sector_economico.setVisibility(View.GONE);
                entro = true;
            }
            String se4=unidad.getTransporte();
            if(se4!=null  && se4.equals("1")){
                sector_economico.setSelection(getIndexSpinner(sector_economico,"Transporte"));
                div_sector_economico.setVisibility(View.GONE);
                entro = true;
            }
            String se5=unidad.getConstruccion();
            if(se5!=null  && se5.equals("1")){
                sector_economico.setSelection(getIndexSpinner(sector_economico,"Construcción"));
                div_sector_economico.setVisibility(View.GONE);
                entro = true;
            }

        }


        String movi=unidad.getObservaciones_sn();
        if(!entro){
            if(movi!=null  && movi.equals("true") || ( movi!= null &&  movi.equals("1") )  ) {
                RadioButton b = (RadioButton) findViewById(R.id.unidades_movil_si);
                b.setChecked(true);
                unidades_puesto_movil = 1;
                emplazamiento_vivienda_actividad_economica.setText(unidad.getVivienda_actividad_economica());
                emplazamiento_obra_edificacion.setText(unidad.getObra_edificacion());

                emplazamiento_fijo.setText(unidad.getEstablecimiento_fijo());
                emplazamiento_semifijo.setText(unidad.getEstablecimiento_semifijo());
                puesto_movil.setText(unidad.getPuesto_movil());

                unidades_comercio.setText(unidad.getComercio());
                unidades_industria.setText(unidad.getIndustria());
                unidades_servicios.setText(unidad.getServicios());
                unidades_transporte.setText(unidad.getTransporte());
                unidades_construccion.setText(unidad.getConstruccion());
                unidades_no_aplica.setText(unidad.getNo_aplica());

                estado_ocupado.setText(unidad.getEstado_ocupado());
                estado_desocupado.setText(unidad.getEstado_desocupado());
            }
            if( movi!=null  && movi.equals("false") ||  (movi!= null &&  movi.equals("0"))){
                RadioButton b = (RadioButton) findViewById(R.id.unidades_movil_no);
                b.setChecked(true);
                unidades_puesto_movil = 0;
                emplazamiento_fijo.setText(unidad.getEstablecimiento_fijo());
                emplazamiento_semifijo.setText(unidad.getEstablecimiento_semifijo());
                emplazamiento_vivienda_actividad_economica.setText(unidad.getVivienda_actividad_economica());
                puesto_movil.setText(unidad.getPuesto_movil());

                unidades_comercio.setText(unidad.getComercio());
                unidades_industria.setText(unidad.getIndustria());
                unidades_servicios.setText(unidad.getServicios());
                unidades_transporte.setText(unidad.getTransporte());
                unidades_construccion.setText(unidad.getConstruccion());
                unidades_no_aplica.setText(unidad.getNo_aplica());

                estado_ocupado.setText(unidad.getEstado_ocupado());
                estado_desocupado.setText(unidad.getEstado_desocupado());
            }
        }
    }


    public int verificarSumas(){
        int a = 0, b = 0, c = 0;
        a= StringToInt(estado_ocupado.getText().toString())+
                StringToInt(estado_desocupado.getText().toString())+
                StringToInt(estado_obra.getText().toString());

        b= StringToInt(emplazamiento_fijo.getText().toString())+
                StringToInt(emplazamiento_semifijo.getText().toString())+
                StringToInt(puesto_movil.getText().toString())+
                StringToInt(emplazamiento_vivienda_actividad_economica.getText().toString())+
                StringToInt(emplazamiento_obra_edificacion.getText().toString());

        c= StringToInt(unidades_comercio.getText().toString())+
                StringToInt(unidades_industria.getText().toString())+
                StringToInt(unidades_servicios.getText().toString())+
                StringToInt(unidades_transporte.getText().toString())+
                StringToInt(unidades_construccion.getText().toString())+
                StringToInt(unidades_no_aplica.getText().toString());

        if(a==b && b==c && a==c){
            if(b>0){
                return 2;
            }else{
                return 1;
            }

        }else{
            return 0;
        }
    }

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


    private int getIndexSpinner(Spinner spinner, String myString){
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

    public void retornar(){

        if(db.getFinalizadaManzana(id_manzana)){
            FormularioUnidad.this.finish();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(FormularioUnidad.this);
            builder.setTitle("Confirmación");
            builder.setMessage("Ya guardo la información?");
            builder.setIcon(R.drawable.ic_menu_salir);
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    FormularioUnidad.this.finish();
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
    }


    @Override
    public void onBackPressed() {
        retornar();
    }




}
