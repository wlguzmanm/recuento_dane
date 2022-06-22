package co.gov.dane.recuento;

import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

import co.gov.dane.recuento.Preguntas.Edificacion;
import co.gov.dane.recuento.Preguntas.Manzana;
import co.gov.dane.recuento.Preguntas.UnidadEconomica;
import co.gov.dane.recuento.backend.Database;
import co.gov.dane.recuento.model.EsquemaEdificacionEnvioViewModel;
import co.gov.dane.recuento.model.EsquemaManzanaEnvioViewModel;
import co.gov.dane.recuento.model.EsquemaUnidadesEnvioViewModel;

public class FormularioUnidad extends AppCompatActivity {

    private String id_manzana,id_edificacion,id_unidad;

    private Spinner spinnerDIREC_PREVIA,spinnerDIREC_P_TIPO, spinnerNOV_CARTO,
            spinnerUnidadObservacion, spinnerTipoObservacion, spinnerTipoVendedor, spinnerSectorEconomico,
            spinnerDIREC_VP, spinnerDIREC_LET_VP, spinnerDIREC_SF_VP, spinnerDIREC_LET_SVP, spinnerDIREC_CUAD_VP,
            spinnerDIREC_LET_VG, spinnerDIREC_SF_VG, spinnerDIREC_LET_SVG, spinnerDIREC_CUAD_VG, spinnerDIREC_COMP ;

    private EditText editUnidadObservacion, editObservacionUnidad,
            editDIREC_NNOM_VP, editDIREC_NUM_VP, editDIREC_NUM_VG, editDIREC_NUM_PLACA, editDIREC_TEX_COM, editDireccionCompleta ;

    private RadioGroup radioDIREC_P_COMP;

    private RadioButton id_pregunta5_2_si, id_pregunta5_2_no;

    private Database db;
    private Util util;
    private Mensajes msj;

    private LinearLayout linearCabecera, linearPregunta6, linearPregunta7, linearNormalizador, guardar_formulario, linearComplemento,
            linearComplementoAcomp, linearAdicionarComplemento, linearAdicionarAgregarDireccion;
    private TextView textUnidadEconomica;
    private String idDevice;
    private Session session;

    ArrayAdapter<CharSequence> arr_estado_unidad;
    ArrayAdapter<CharSequence> arr_unidad_observacion;
    ArrayAdapter<CharSequence> arr_unidad_observacion_1;
    ArrayAdapter<CharSequence> arr_unidad_observacion_2;
    ArrayAdapter<CharSequence> arr_tipo_vendedor;
    ArrayAdapter<CharSequence> arr_sector_economico;
    ArrayAdapter<CharSequence> arr_sector_economico1;
    ArrayAdapter<CharSequence> arr_sector_economico2;

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

        ImageView atras = (ImageView) findViewById(R.id.atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retornar();
            }
        });

        linearCabecera = (LinearLayout) findViewById(R.id.linearCabecera);
        linearPregunta6 = (LinearLayout) findViewById(R.id.linearPregunta6);
        linearPregunta7 = (LinearLayout) findViewById(R.id.linearPregunta7);
        linearNormalizador = (LinearLayout) findViewById(R.id.linearNormalizador);
        guardar_formulario = (LinearLayout) findViewById(R.id.guardar_formulario);
        linearComplemento = (LinearLayout) findViewById(R.id.linearComplemento);
        linearComplementoAcomp = (LinearLayout) findViewById(R.id.linearComplementoAcomp);
        linearAdicionarComplemento = (LinearLayout) findViewById(R.id.linearAdicionarComplemento);
        linearAdicionarAgregarDireccion = (LinearLayout) findViewById(R.id.linearAdicionarAgregarDireccion);

        linearNormalizador.setVisibility(View.GONE);

        spinnerDIREC_PREVIA = (Spinner) findViewById(R.id.spinnerDIREC_PREVIA);
        spinnerDIREC_P_TIPO = (Spinner) findViewById(R.id.spinnerDIREC_P_TIPO);
        spinnerNOV_CARTO = (Spinner) findViewById(R.id.spinnerNOV_CARTO);
        spinnerUnidadObservacion = (Spinner) findViewById(R.id.spinnerUnidadObservacion);
        spinnerTipoObservacion = (Spinner) findViewById(R.id.spinnerTipoObservacion);
        spinnerTipoVendedor = (Spinner) findViewById(R.id.spinnerTipoVendedor);
        spinnerSectorEconomico = (Spinner) findViewById(R.id.spinnerSectorEconomico);
        spinnerDIREC_VP = (Spinner) findViewById(R.id.spinnerDIREC_VP);
        spinnerDIREC_LET_VP = (Spinner) findViewById(R.id.spinnerDIREC_LET_VP);
        spinnerDIREC_SF_VP = (Spinner) findViewById(R.id.spinnerDIREC_SF_VP);
        spinnerDIREC_LET_SVP = (Spinner) findViewById(R.id.spinnerDIREC_LET_SVP);
        spinnerDIREC_CUAD_VP = (Spinner) findViewById(R.id.spinnerDIREC_CUAD_VP);
        spinnerDIREC_LET_VG = (Spinner) findViewById(R.id.spinnerDIREC_LET_VG);
        spinnerDIREC_SF_VG = (Spinner) findViewById(R.id.spinnerDIREC_SF_VG);
        spinnerDIREC_LET_SVG = (Spinner) findViewById(R.id.spinnerDIREC_LET_SVG);
        spinnerDIREC_CUAD_VG = (Spinner) findViewById(R.id.spinnerDIREC_CUAD_VG);
        spinnerDIREC_COMP = (Spinner) findViewById(R.id.spinnerDIREC_COMP);

        editUnidadObservacion = (EditText) findViewById(R.id.editUnidadObservacion);
        editObservacionUnidad = (EditText) findViewById(R.id.editObservacionUnidad);
        editDIREC_NNOM_VP = (EditText) findViewById(R.id.editDIREC_NNOM_VP);
        editDIREC_NUM_VP = (EditText) findViewById(R.id.editDIREC_NUM_VP);
        editDIREC_NUM_VG = (EditText) findViewById(R.id.editDIREC_NUM_VG);
        editDIREC_NUM_PLACA = (EditText) findViewById(R.id.editDIREC_NUM_PLACA);
        editDIREC_TEX_COM = (EditText) findViewById(R.id.editDIREC_TEX_COM);
        editDireccionCompleta = (EditText) findViewById(R.id.editDireccionCompleta);

        radioDIREC_P_COMP = (RadioGroup) findViewById(R.id.radioDIREC_P_COMP);

        id_pregunta5_2_si  = (RadioButton) findViewById(R.id.id_pregunta5_2_si);
        id_pregunta5_2_no = (RadioButton) findViewById(R.id.id_pregunta5_2_no);

        ArrayAdapter<CharSequence> adapter_DIREC_VP = ArrayAdapter.createFromResource(this,
                R.array.DIREC_VP, android.R.layout.simple_spinner_item);
        adapter_DIREC_VP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDIREC_VP.setAdapter(adapter_DIREC_VP);

        ArrayAdapter<CharSequence> adapter_DIREC_LET_VP = ArrayAdapter.createFromResource(this,
                R.array.letra, android.R.layout.simple_spinner_item);
        adapter_DIREC_LET_VP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDIREC_LET_VP.setAdapter(adapter_DIREC_LET_VP);

        ArrayAdapter<CharSequence> adapter_DIREC_SF_VP = ArrayAdapter.createFromResource(this,
                R.array.sufijo, android.R.layout.simple_spinner_item);
        adapter_DIREC_SF_VP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDIREC_SF_VP.setAdapter(adapter_DIREC_SF_VP);

        ArrayAdapter<CharSequence> adapter_DIREC_LET_SVP = ArrayAdapter.createFromResource(this,
                R.array.letra, android.R.layout.simple_spinner_item);
        adapter_DIREC_LET_SVP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDIREC_LET_SVP.setAdapter(adapter_DIREC_LET_SVP);

        ArrayAdapter<CharSequence> adapter_DIREC_CUAD_VP = ArrayAdapter.createFromResource(this,
                R.array.cuadrante, android.R.layout.simple_spinner_item);
        adapter_DIREC_CUAD_VP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDIREC_CUAD_VP.setAdapter(adapter_DIREC_CUAD_VP);

        ArrayAdapter<CharSequence> adapter_DIREC_LET_VG = ArrayAdapter.createFromResource(this,
                R.array.letra, android.R.layout.simple_spinner_item);
        adapter_DIREC_LET_VG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDIREC_LET_VG.setAdapter(adapter_DIREC_LET_VG);

        ArrayAdapter<CharSequence> adapter_DIREC_SF_VG = ArrayAdapter.createFromResource(this,
                R.array.sufijo, android.R.layout.simple_spinner_item);
        adapter_DIREC_SF_VG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDIREC_SF_VG.setAdapter(adapter_DIREC_SF_VG);

        ArrayAdapter<CharSequence> adapter_DIREC_LET_SVG = ArrayAdapter.createFromResource(this,
                R.array.letra, android.R.layout.simple_spinner_item);
        adapter_DIREC_LET_SVG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDIREC_LET_SVG.setAdapter(adapter_DIREC_LET_SVG);

        ArrayAdapter<CharSequence> adapter_DIREC_CUAD_VG = ArrayAdapter.createFromResource(this,
                R.array.cuadrante, android.R.layout.simple_spinner_item);
        adapter_DIREC_CUAD_VG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDIREC_CUAD_VG.setAdapter(adapter_DIREC_CUAD_VG);

        ArrayAdapter<CharSequence> adapter_DIREC_COMP = ArrayAdapter.createFromResource(this,
                R.array.complemento, android.R.layout.simple_spinner_item);
        adapter_DIREC_COMP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDIREC_COMP.setAdapter(adapter_DIREC_COMP);


        spinnerDIREC_PREVIA = (Spinner) findViewById(R.id.spinnerDIREC_PREVIA);
        spinnerDIREC_P_TIPO = (Spinner) findViewById(R.id.spinnerDIREC_P_TIPO);
        spinnerNOV_CARTO = (Spinner) findViewById(R.id.spinnerNOV_CARTO);
        spinnerUnidadObservacion = (Spinner) findViewById(R.id.spinnerUnidadObservacion);
        spinnerTipoObservacion = (Spinner) findViewById(R.id.spinnerTipoObservacion);
        spinnerTipoVendedor = (Spinner) findViewById(R.id.spinnerTipoVendedor);
        spinnerSectorEconomico = (Spinner) findViewById(R.id.spinnerSectorEconomico);

        ArrayAdapter<CharSequence> adapter_DIREC_PREVIA = ArrayAdapter.createFromResource(this,
                R.array.direccion_previa, android.R.layout.simple_spinner_item);
        adapter_DIREC_PREVIA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDIREC_PREVIA.setAdapter(adapter_DIREC_PREVIA);

        ArrayAdapter<CharSequence> adapter_DIREC_P_TIPO = ArrayAdapter.createFromResource(this,
                R.array.direccion_tipo, android.R.layout.simple_spinner_item);
        adapter_DIREC_P_TIPO.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDIREC_P_TIPO.setAdapter(adapter_DIREC_P_TIPO);

        ArrayAdapter<CharSequence> adapter_NOV_CARTO = ArrayAdapter.createFromResource(this,
                R.array.Novedades_cartograficas, android.R.layout.simple_spinner_item);
        adapter_NOV_CARTO.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNOV_CARTO.setAdapter(adapter_NOV_CARTO);

        ArrayAdapter<CharSequence> adapter_estado_unidad = ArrayAdapter.createFromResource(this,
                R.array.estado_unidad, android.R.layout.simple_spinner_item);
        adapter_estado_unidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnidadObservacion.setAdapter(adapter_estado_unidad);



        arr_estado_unidad = ArrayAdapter.createFromResource(this,
                R.array.estado_unidad, android.R.layout.simple_spinner_item);
        arr_estado_unidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        arr_tipo_vendedor = ArrayAdapter.createFromResource(this,
                R.array.tipo_vendedor, android.R.layout.simple_spinner_item);
        arr_tipo_vendedor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


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

        }

    }

    private String validarFormulario() {
        String retorno = "";

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
        retorno.setUnidad_osbservacion(unidad.getUnidad_osbservacion());
        retorno.setObservacion(unidad.getObservacion());

        retorno.setFecha_modificacion(unidad.getFechaModificacion());
        retorno.setId_edificacion(unidad.getId_edificacion());
        retorno.setId_manzana(unidad.getId_manzana());
        retorno.setId_manzana_edificio_unidad(unidad.getId_manzana()+unidad.getId_edificacion()+unidad.getId_unidad());
        retorno.setId_unidad_economica(unidad.getId_unidad());

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

        return unidad;
    }




    //TODO: AJUSTAR
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



    /**
     * Metodo que busca la lista de index de los spinner
     *
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
     * Metodo para retornar
     */
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
