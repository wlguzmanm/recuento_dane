package co.gov.dane.recuento;

import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import co.gov.dane.recuento.Preguntas.Edificacion;
import co.gov.dane.recuento.Preguntas.Manzana;
import co.gov.dane.recuento.Preguntas.UnidadEconomica;
import co.gov.dane.recuento.adapter.ConteoAdapterComplementos;
import co.gov.dane.recuento.adapter.ConteoAdapterEdificacion;
import co.gov.dane.recuento.backend.Database;
import co.gov.dane.recuento.backend.Normalizador;
import co.gov.dane.recuento.dtos.ComplementoNormalizadorDTO;
import co.gov.dane.recuento.dtos.NormalizadorDireccionDTO;
import co.gov.dane.recuento.model.ConteoEdificacion;
import co.gov.dane.recuento.model.EsquemaEdificacionEnvioViewModel;
import co.gov.dane.recuento.model.EsquemaManzanaEnvioViewModel;
import co.gov.dane.recuento.model.EsquemaUnidadesEnvioViewModel;

public class FormularioUnidad extends AppCompatActivity {

    private String id_manzana,id_edificacion,id_unidad;

    private Spinner spinnerDIREC_PREVIA,spinnerDIREC_P_TIPO,
            spinnerUnidadObservacion, spinnerTipoObservacion, spinnerTipoVendedor, spinnerSectorEconomico,
            spinnerDIREC_VP, spinnerDIREC_LET_VP, spinnerDIREC_SF_VP, spinnerDIREC_LET_SVP, spinnerDIREC_CUAD_VP,
            spinnerDIREC_LET_VG, spinnerDIREC_SF_VG, spinnerDIREC_LET_SVG, spinnerDIREC_CUAD_VG ;

    private AutoCompleteTextView spinnerDIREC_COMP;

    private EditText editUnidadObservacion, editObservacionUnidad,
            editDIREC_NNOM_VP, editDIREC_NUM_VP, editDIREC_NUM_VG, editDIREC_NUM_PLACA, editDIREC_TEX_COM, editDireccionCompleta,
            editDIREC_LET_VP_OTRO, editDIREC_LET_SVP_OTRO, editDIREC_LET_VG_OTRO, editDIREC_LET_SVG_OTRO, editDireccion;

    private RadioGroup radioDIREC_P_COMP;

    private RadioButton id_complemento_si, id_complemento_no;
    private RecyclerView recycler_complementos;

    private Database db;
    private Util util;
    private Mensajes msj;

    private LinearLayout linearCabecera, linearPregunta6, linearPregunta7, linearNormalizador, guardar_formulario, linearComplemento,
            linearComplementoAcomp, linearAdicionarComplemento, linearAdicionarAgregarDireccion, linearTipoVendedor, linearBtnNormalizador,
            linearDIREC_LET_VP_OTRO, linearDIREC_LET_SVP_OTRO, linearDIREC_LET_VG_OTRO,  linearDIREC_LET_SVG_OTRO,
            linearDIREC_VP,
            linearDIREC_NNOM_VP, linearDIREC_NUM_VP, linearDIREC_LET_VP, linearDIREC_SF_VP, linearDIREC_LET_SVP,
            linearDIREC_CUAD_VP, linearDIREC_NUM_VG, linearDIREC_LET_VG, linearDIREC_SF_VG, linearDIREC_LET_SVG,
            linearDIREC_NUM_PLACA, linearDIREC_CUAD_VG, linearDIREC_P_COMP,
            linearDireccion;

    private TextView textUnidadEconomica, tx_label_normalizador, tv_adicionarComplemento;
    private String idDevice, DIREC_PREVIA,  DIREC_P_TIPO, DIRECC, UNIDAD_OBSERVACION, TIPO_OBSERVACION, TIPO_VENDEDOR, SECTOR_ECONOMICO,
            DIREC_VP,
            DIREC_NNOM_VP,
            DIREC_NUM_VP,
            DIREC_LET_VP,
            DIREC_LET_VP_OTRO,
            DIREC_SF_VP,
            DIREC_LET_SVP,
            DIREC_LET_SVP_OTRO,
            DIREC_CUAD_VP,
            DIREC_NUM_VG,
            DIREC_LET_VG,
            DIREC_LET_VG_OTRO,
            DIREC_SF_VG,
            DIREC_LET_SVG,
            DIREC_LET_SVG_OTRO,
            DIREC_NUM_PLACA,
            DIREC_CUAD_VG,
            DIREC_P_COMP,
            DIREC_COMP,
            DIREC_TEX_COM;
    private List<ComplementoNormalizadorDTO> lstComplemento = new ArrayList<>();
    private ConteoAdapterComplementos mAdapterComplemento;

    private Session session;
    private NormalizadorDireccionDTO normalizador = new NormalizadorDireccionDTO();
    private int isEdit;

    ArrayAdapter<CharSequence> arrDefault;
    ArrayAdapter<CharSequence> arr_estado_unidad;
    ArrayAdapter<CharSequence> arr_unidad_observacion;
    ArrayAdapter<CharSequence> arr_unidad_observacion_1;
    ArrayAdapter<CharSequence> arr_unidad_observacion_2;
    ArrayAdapter<CharSequence> arr_tipo_vendedor;
    ArrayAdapter<CharSequence> arr_sector_economico;
    ArrayAdapter<CharSequence> arr_sector_economico1;
    ArrayAdapter<CharSequence> arr_sector_economico2;

    private Boolean isEditarComplemento = false, cambiosPendientes = false;
    private ComplementoNormalizadorDTO complemento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isEdit = 1;
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
        linearBtnNormalizador = (LinearLayout) findViewById(R.id.normalizador);
        linearComplementoAcomp = (LinearLayout) findViewById(R.id.linearComplementoAcomp);
        linearAdicionarComplemento = (LinearLayout) findViewById(R.id.linearAdicionarComplemento);
        linearAdicionarAgregarDireccion = (LinearLayout) findViewById(R.id.linearAdicionarAgregarDireccion);
        linearTipoVendedor = (LinearLayout) findViewById(R.id.linearTipoVendedor);
        linearDIREC_LET_VP_OTRO = (LinearLayout) findViewById(R.id.linearDIREC_LET_VP_OTRO);
        linearDIREC_LET_SVP_OTRO = (LinearLayout) findViewById(R.id.linearDIREC_LET_SVP_OTRO);
        linearDIREC_LET_VG_OTRO = (LinearLayout) findViewById(R.id.linearDIREC_LET_VG_OTRO);
        linearDIREC_LET_SVG_OTRO = (LinearLayout) findViewById(R.id.linearDIREC_LET_SVG_OTRO);

        linearDIREC_VP = (LinearLayout) findViewById(R.id.linearDIREC_VP);
        linearDIREC_NNOM_VP = (LinearLayout) findViewById(R.id.linearDIREC_NNOM_VP);
        linearDIREC_NUM_VP = (LinearLayout) findViewById(R.id.linearDIREC_NUM_VP);
        linearDIREC_LET_VP = (LinearLayout) findViewById(R.id.linearDIREC_LET_VP);
        linearDIREC_SF_VP = (LinearLayout) findViewById(R.id.linearDIREC_SF_VP);
        linearDIREC_LET_SVP = (LinearLayout) findViewById(R.id.linearDIREC_LET_SVP);
        linearDIREC_CUAD_VP = (LinearLayout) findViewById(R.id.linearDIREC_CUAD_VP);
        linearDIREC_NUM_VG = (LinearLayout) findViewById(R.id.linearDIREC_NUM_VG);
        linearDIREC_LET_VG = (LinearLayout) findViewById(R.id.linearDIREC_LET_VG);
        linearDIREC_SF_VG = (LinearLayout) findViewById(R.id.linearDIREC_SF_VG);
        linearDIREC_LET_SVG = (LinearLayout) findViewById(R.id.linearDIREC_LET_SVG);
        linearDIREC_NUM_PLACA = (LinearLayout) findViewById(R.id.linearDIREC_NUM_PLACA);
        linearDIREC_CUAD_VG = (LinearLayout) findViewById(R.id.linearDIREC_CUAD_VG);
        linearDIREC_P_COMP = (LinearLayout) findViewById(R.id.linearDIREC_P_COMP);
        linearDireccion = (LinearLayout) findViewById(R.id.linearDireccion);


        linearNormalizador.setVisibility(View.GONE);
        linearDireccion.setVisibility(View.GONE);

        spinnerDIREC_PREVIA = (Spinner) findViewById(R.id.spinnerDIREC_PREVIA);
        spinnerDIREC_P_TIPO = (Spinner) findViewById(R.id.spinnerDIREC_P_TIPO);
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
        spinnerDIREC_COMP = (AutoCompleteTextView) findViewById(R.id.spinnerDIREC_COMP);

        editUnidadObservacion = (EditText) findViewById(R.id.editUnidadObservacion);
        editObservacionUnidad = (EditText) findViewById(R.id.editObservacionUnidad);
        editDIREC_NNOM_VP = (EditText) findViewById(R.id.editDIREC_NNOM_VP);
        editDIREC_NUM_VP = (EditText) findViewById(R.id.editDIREC_NUM_VP);
        editDIREC_NUM_VG = (EditText) findViewById(R.id.editDIREC_NUM_VG);
        editDIREC_NUM_PLACA = (EditText) findViewById(R.id.editDIREC_NUM_PLACA);
        editDIREC_TEX_COM = (EditText) findViewById(R.id.editDIREC_TEX_COM);
        editDireccionCompleta = (EditText) findViewById(R.id.editDireccionCompleta);
        editDIREC_LET_VP_OTRO = (EditText) findViewById(R.id.editDIREC_LET_VP_OTRO);
        editDIREC_LET_SVP_OTRO = (EditText) findViewById(R.id.editDIREC_LET_SVP_OTRO);
        editDIREC_LET_VG_OTRO = (EditText) findViewById(R.id.editDIREC_LET_VG_OTRO);
        editDIREC_LET_SVG_OTRO = (EditText) findViewById(R.id.editDIREC_LET_SVG_OTRO);
        editDireccion = (EditText) findViewById(R.id.editDireccion);
        tx_label_normalizador = (TextView) findViewById(R.id.tx_label_normalizador);
        tv_adicionarComplemento = (TextView) findViewById(R.id.tv_adicionarComplemento);

        textUnidadEconomica = (TextView) findViewById(R.id.textUnidadEconomica);

        radioDIREC_P_COMP = (RadioGroup) findViewById(R.id.radioDIREC_P_COMP);

        recycler_complementos = (RecyclerView) findViewById(R.id.recycler_complementos);
        mAdapterComplemento = new ConteoAdapterComplementos(lstComplemento,FormularioUnidad.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_complementos.setLayoutManager(mLayoutManager);
        recycler_complementos.setItemAnimator(new DefaultItemAnimator());
        recycler_complementos.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recycler_complementos.setAdapter(mAdapterComplemento);

        id_complemento_si  = (RadioButton) findViewById(R.id.id_complemento_si);
        id_complemento_no = (RadioButton) findViewById(R.id.id_complemento_no);

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

        ArrayAdapter<CharSequence> adapter_DIREC_PREVIA = ArrayAdapter.createFromResource(this,
                R.array.direccion_previa, android.R.layout.simple_spinner_item);
        adapter_DIREC_PREVIA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDIREC_PREVIA.setAdapter(adapter_DIREC_PREVIA);

        ArrayAdapter<CharSequence> adapter_DIREC_P_TIPO = ArrayAdapter.createFromResource(this,
                R.array.direccion_tipo, android.R.layout.simple_spinner_item);
        adapter_DIREC_P_TIPO.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDIREC_P_TIPO.setAdapter(adapter_DIREC_P_TIPO);

        ArrayAdapter<CharSequence> adapter_estado_unidad = ArrayAdapter.createFromResource(this,
                R.array.estado_unidad, android.R.layout.simple_spinner_item);
        adapter_estado_unidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnidadObservacion.setAdapter(adapter_estado_unidad);

        arrDefault = ArrayAdapter.createFromResource(this,
                R.array.listado_default, android.R.layout.simple_spinner_item);
        arrDefault.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
                R.array.sector_economico2, android.R.layout.simple_spinner_item);
        arr_sector_economico2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        linearPregunta6.setVisibility(View.VISIBLE);
        linearPregunta7.setVisibility(View.VISIBLE);
        linearComplemento.setVisibility(View.GONE);
        linearComplementoAcomp.setVisibility(View.GONE);

        spinnerDIREC_PREVIA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinnerDIREC_PREVIA.getSelectedItem().toString().equals("Seleccione...")){
                    linearBtnNormalizador.setEnabled(true);
                    linearBtnNormalizador.setBackgroundColor( getResources().getColor(R.color.verde));
                    tx_label_normalizador.setBackgroundColor( getResources().getColor(R.color.verde));
                    if(spinnerDIREC_PREVIA.getSelectedItem().toString().contains("1"))
                         DIREC_PREVIA = "1";
                    if(spinnerDIREC_PREVIA.getSelectedItem().toString().contains("2"))
                        DIREC_PREVIA = "2";
                    if(spinnerDIREC_PREVIA.getSelectedItem().toString().contains("3"))
                        DIREC_PREVIA = "3";
                }else{
                    UnidadEconomica unidad= new UnidadEconomica();
                    unidad=db.getUnidadEconomica(id_manzana,id_edificacion,id_unidad);

                    if(unidad!= null && unidad.getId() != null && !unidad.getId().isEmpty()){
                        if(!unidad.getId_unidad().equals("1")){
                            linearBtnNormalizador.setEnabled(false);
                            linearBtnNormalizador.setBackgroundColor( getResources().getColor(R.color.ms_bottomNavigationButtonTextColor));
                            tx_label_normalizador.setBackgroundColor( getResources().getColor(R.color.ms_bottomNavigationButtonTextColor));
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                DIREC_PREVIA = null;
            }
        });

        spinnerDIREC_P_TIPO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinnerDIREC_P_TIPO.getSelectedItem().toString().equals("Seleccione...")){
                    if(DIREC_PREVIA!= null){
                        linearBtnNormalizador.setEnabled(true);
                        linearBtnNormalizador.setBackgroundColor( getResources().getColor(R.color.verde));
                        tx_label_normalizador.setBackgroundColor( getResources().getColor(R.color.verde));
                    }

                    if(spinnerDIREC_P_TIPO.getSelectedItem().toString().contains("1"))
                        DIREC_P_TIPO = "1";
                    if(spinnerDIREC_P_TIPO.getSelectedItem().toString().contains("2"))
                        DIREC_P_TIPO = "2";
                    if(spinnerDIREC_P_TIPO.getSelectedItem().toString().contains("3"))
                        DIREC_P_TIPO = "3";
                }else{
                    UnidadEconomica unidad= new UnidadEconomica();
                    unidad=db.getUnidadEconomica(id_manzana,id_edificacion,id_unidad);

                    if(unidad!= null && unidad.getId() != null && !unidad.getId().isEmpty()){
                        if(unidad.getId_unidad().equals("1")){
                            linearBtnNormalizador.setEnabled(false);
                            linearBtnNormalizador.setBackgroundColor( getResources().getColor(R.color.ms_bottomNavigationButtonTextColor));
                            tx_label_normalizador.setBackgroundColor( getResources().getColor(R.color.ms_bottomNavigationButtonTextColor));
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                DIREC_P_TIPO = null;
            }
        });

        editDIREC_NNOM_VP.addTextChangedListener(new TextWatcher() { @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {} @Override public void onTextChanged(CharSequence s, int start, int before, int count) {} @Override public void afterTextChanged(Editable s) { getValidarTextoPregunta2(editDIREC_NNOM_VP); } });

        //Pregunta 7
        spinnerUnidadObservacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isEdit == 1){
                    if(!spinnerUnidadObservacion.getSelectedItem().toString().equals("Seleccione...")){
                        if(spinnerUnidadObservacion.getSelectedItem().toString().contains("1")){
                            spinnerTipoObservacion.setAdapter(arr_unidad_observacion);
                            spinnerTipoVendedor.setAdapter(arrDefault);
                            spinnerSectorEconomico.setAdapter(arrDefault);
                            UNIDAD_OBSERVACION = "1";
                            editUnidadObservacion.setEnabled(true);
                        }
                        if(spinnerUnidadObservacion.getSelectedItem().toString().contains("2")){
                            spinnerTipoObservacion.setAdapter(arr_unidad_observacion_1);
                            spinnerTipoVendedor.setAdapter(arrDefault);
                            spinnerSectorEconomico.setAdapter(arrDefault);
                            UNIDAD_OBSERVACION = "2";
                            editUnidadObservacion.setEnabled(false);
                            editUnidadObservacion.setText("");
                        }
                        if(spinnerUnidadObservacion.getSelectedItem().toString().contains("3")){
                            spinnerTipoObservacion.setAdapter(arr_unidad_observacion_2);
                            spinnerTipoVendedor.setAdapter(arrDefault);
                            spinnerSectorEconomico.setAdapter(arrDefault);
                            UNIDAD_OBSERVACION = "3";
                            editUnidadObservacion.setEnabled(true);
                        }
                    }else{
                        spinnerTipoObservacion.setAdapter(arrDefault);
                        spinnerTipoVendedor.setAdapter(arrDefault);
                        spinnerSectorEconomico.setAdapter(arrDefault);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerTipoObservacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isEdit == 1){
                    if(!spinnerTipoObservacion.getSelectedItem().toString().equals("Seleccione...")){
                        if(spinnerUnidadObservacion.getSelectedItem().toString().contains("1")){
                            if(spinnerTipoObservacion.getSelectedItem().toString().contains("1")
                                    || spinnerTipoObservacion.getSelectedItem().toString().contains("2")
                                    || spinnerTipoObservacion.getSelectedItem().toString().contains("4")){
                                spinnerSectorEconomico.setAdapter(arr_sector_economico);
                                linearTipoVendedor.setVisibility(View.GONE);
                                spinnerTipoVendedor.setAdapter(arrDefault);

                                if(spinnerTipoObservacion.getSelectedItem().toString().contains("1")){
                                    TIPO_OBSERVACION = "1";
                                }

                                if(spinnerTipoObservacion.getSelectedItem().toString().contains("2")){
                                    TIPO_OBSERVACION = "2";
                                }

                                if(spinnerTipoObservacion.getSelectedItem().toString().contains("4")){
                                    TIPO_OBSERVACION = "4";
                                }
                            }else{
                                spinnerTipoVendedor.setAdapter(arr_tipo_vendedor);
                                spinnerSectorEconomico.setAdapter(arrDefault);
                                linearTipoVendedor.setVisibility(View.VISIBLE);
                                TIPO_OBSERVACION = "3";
                            }
                        }

                        if(spinnerUnidadObservacion.getSelectedItem().toString().contains("2")){
                            spinnerSectorEconomico.setAdapter(arr_sector_economico2);
                            linearTipoVendedor.setVisibility(View.GONE);
                            if(spinnerTipoObservacion.getSelectedItem().toString().contains("1")){
                                TIPO_OBSERVACION = "1";
                            }
                            if(spinnerTipoObservacion.getSelectedItem().toString().contains("2")){
                                TIPO_OBSERVACION = "2";
                            }
                        }

                        if(spinnerUnidadObservacion.getSelectedItem().toString().contains("3")){
                            spinnerSectorEconomico.setAdapter(arr_sector_economico1);
                            linearTipoVendedor.setVisibility(View.GONE);
                            if(spinnerTipoObservacion.getSelectedItem().toString().contains("5")){
                                TIPO_OBSERVACION = "5";
                            }
                        }
                    }else{
                        spinnerTipoVendedor.setAdapter(arrDefault);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerTipoVendedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isEdit == 1){
                    if(!spinnerTipoVendedor.getSelectedItem().toString().equals("Seleccione...")){
                        if(spinnerTipoVendedor.getSelectedItem().toString().contains("1")){
                            TIPO_VENDEDOR = "1";
                            spinnerSectorEconomico.setAdapter(arr_sector_economico);
                        }
                        if(spinnerTipoVendedor.getSelectedItem().toString().contains("2")){
                            TIPO_VENDEDOR = "2";
                            spinnerSectorEconomico.setAdapter(arr_sector_economico);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerSectorEconomico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isEdit == 1){
                    if(!spinnerSectorEconomico.getSelectedItem().toString().equals("Seleccione...")){
                        if(spinnerUnidadObservacion.getSelectedItem().toString().contains("1")){
                            if(spinnerSectorEconomico.getSelectedItem().toString().contains("1")){
                                SECTOR_ECONOMICO = "1";
                            }
                            if(spinnerSectorEconomico.getSelectedItem().toString().contains("2")){
                                SECTOR_ECONOMICO = "2";
                            }
                            if(spinnerSectorEconomico.getSelectedItem().toString().contains("3")){
                                SECTOR_ECONOMICO = "3";
                            }
                            if(spinnerSectorEconomico.getSelectedItem().toString().contains("4")){
                                SECTOR_ECONOMICO = "4";
                            }
                            if(spinnerSectorEconomico.getSelectedItem().toString().contains("5")){
                                SECTOR_ECONOMICO = "5";
                            }
                        }
                        if(spinnerUnidadObservacion.getSelectedItem().toString().contains("2")){
                            if(spinnerSectorEconomico.getSelectedItem().toString().contains("6")){
                                SECTOR_ECONOMICO = "6";
                            }
                        }
                        if(spinnerUnidadObservacion.getSelectedItem().toString().contains("3")){
                            if(spinnerSectorEconomico.getSelectedItem().toString().contains("6")){
                                SECTOR_ECONOMICO = "6";
                            }
                        }
                    }
                }else{
                    if(isEdit==100){
                        isEdit = 1;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        linearBtnNormalizador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalizadorDireccionDTO diligenciadoNormalizador = db.getNormalizador(id_manzana,id_edificacion,id_unidad);

                if(diligenciadoNormalizador!= null && diligenciadoNormalizador.getId() != null){
                    msj.dialogoMensaje("Informativo", "El sistema detectó una dirección ingresada anteriormente.");
                    if(Util.stringNullEmptys(diligenciadoNormalizador.getDirecVp()) && diligenciadoNormalizador.getDirecVp().contains("NA")){
                        linearDIREC_NNOM_VP.setVisibility(View.GONE);
                        linearDIREC_NUM_VP.setVisibility(View.GONE);
                        linearDIREC_LET_VP.setVisibility(View.GONE);
                        linearDIREC_SF_VP.setVisibility(View.GONE);
                        linearDIREC_LET_SVP.setVisibility(View.GONE);
                        linearDIREC_CUAD_VP.setVisibility(View.GONE);
                        linearDIREC_NUM_VG.setVisibility(View.GONE);
                        linearDIREC_LET_VG.setVisibility(View.GONE);
                        linearDIREC_SF_VG.setVisibility(View.GONE);
                        linearDIREC_LET_SVG.setVisibility(View.GONE);
                        linearDIREC_NUM_PLACA.setVisibility(View.GONE);
                        linearDIREC_CUAD_VG.setVisibility(View.GONE);
                        linearDIREC_P_COMP.setVisibility(View.GONE);
                    }else{
                        linearDIREC_NNOM_VP.setVisibility(View.VISIBLE);
                        linearDIREC_NUM_VP.setVisibility(View.VISIBLE);
                        linearDIREC_LET_VP.setVisibility(View.VISIBLE);
                        linearDIREC_SF_VP.setVisibility(View.VISIBLE);
                        linearDIREC_LET_SVP.setVisibility(View.VISIBLE);
                        linearDIREC_CUAD_VP.setVisibility(View.VISIBLE);
                        linearDIREC_NUM_VG.setVisibility(View.VISIBLE);
                        linearDIREC_LET_VG.setVisibility(View.VISIBLE);
                        linearDIREC_SF_VG.setVisibility(View.VISIBLE);
                        linearDIREC_LET_SVG.setVisibility(View.VISIBLE);
                        linearDIREC_NUM_PLACA.setVisibility(View.VISIBLE);
                        linearDIREC_CUAD_VG.setVisibility(View.VISIBLE);
                        linearDIREC_P_COMP.setVisibility(View.VISIBLE);
                    }
                    linearDIREC_NNOM_VP.setVisibility(View.VISIBLE);
                    DIREC_VP=diligenciadoNormalizador.getDirecVp();
                    DIREC_NNOM_VP=diligenciadoNormalizador.getDirecNnomVp();
                    DIREC_NUM_VP=diligenciadoNormalizador.getDirecNumVp();
                    DIREC_LET_VP=diligenciadoNormalizador.getDirecLetVp();
                    if(DIREC_LET_VP!= null && DIREC_LET_VP.equals("Otros"))
                        linearDIREC_LET_VP_OTRO.setVisibility(View.VISIBLE);

                    DIREC_LET_VP_OTRO=diligenciadoNormalizador.getDirecLetVpOtro();
                    DIREC_SF_VP=diligenciadoNormalizador.getDirecSfVp();
                    DIREC_LET_SVP=diligenciadoNormalizador.getDirecLetSvp();
                    if(DIREC_LET_SVP!= null && DIREC_LET_SVP.equals("Otros"))
                        linearDIREC_LET_SVP_OTRO.setVisibility(View.VISIBLE);

                    DIREC_LET_SVP_OTRO=diligenciadoNormalizador.getDirecLetSvpOtro();
                    DIREC_CUAD_VP=diligenciadoNormalizador.getDirecCuadVp();
                    DIREC_NUM_VG=diligenciadoNormalizador.getDirecNumVg();
                    DIREC_LET_VG=diligenciadoNormalizador.getDirecLetVg();
                    if(DIREC_LET_VG!= null && DIREC_LET_VG.equals("Otros"))
                        linearDIREC_LET_VG_OTRO.setVisibility(View.VISIBLE);

                    DIREC_LET_VG_OTRO=diligenciadoNormalizador.getDirecLetVgOtro();
                    DIREC_SF_VG=diligenciadoNormalizador.getDirecSfVg();
                    DIREC_LET_SVG=diligenciadoNormalizador.getDirecLetSvg();
                    if(DIREC_LET_SVG!= null && DIREC_LET_SVG.equals("Otros"))
                        linearDIREC_LET_SVG_OTRO.setVisibility(View.VISIBLE);

                    DIREC_LET_SVG_OTRO=diligenciadoNormalizador.getDirecLetSvgOtro();
                    DIREC_NUM_PLACA=diligenciadoNormalizador.getDirecNumPlaca();
                    DIREC_CUAD_VG=diligenciadoNormalizador.getDirecCuadVg();
                    DIREC_P_COMP=diligenciadoNormalizador.getDirecPComp();

                    spinnerDIREC_VP.setSelection(getIndexSpinnerContains(spinnerDIREC_VP,DIREC_VP));
                    editDIREC_NNOM_VP.setText(DIREC_NNOM_VP);
                    editDIREC_NUM_VP.setText(DIREC_NUM_VP);
                    spinnerDIREC_LET_VP.setSelection(getIndexSpinnerContains(spinnerDIREC_LET_VP,DIREC_LET_VP));
                    editDIREC_LET_VP_OTRO.setText(DIREC_LET_VP_OTRO);
                    if(!Util.stringNullEmptys(DIREC_SF_VP)){
                        spinnerDIREC_SF_VP.setSelection(getIndexSpinnerContains(spinnerDIREC_SF_VP,"2"));
                    }else{
                        spinnerDIREC_SF_VP.setSelection(getIndexSpinnerContains(spinnerDIREC_SF_VP,DIREC_SF_VP));
                    }

                    spinnerDIREC_LET_SVP.setSelection(getIndexSpinnerContains(spinnerDIREC_LET_SVP,DIREC_LET_SVP));
                    editDIREC_LET_SVP_OTRO.setText(DIREC_LET_SVP_OTRO);
                    if(!Util.stringNullEmptys(DIREC_CUAD_VP)){
                        spinnerDIREC_CUAD_VP.setSelection(getIndexSpinnerContains(spinnerDIREC_CUAD_VP,"1"));
                    }else{
                        spinnerDIREC_CUAD_VP.setSelection(getIndexSpinnerContains(spinnerDIREC_CUAD_VP,DIREC_CUAD_VP));
                    }

                    editDIREC_NUM_VG.setText(DIREC_NUM_VG);
                    spinnerDIREC_LET_VG.setSelection(getIndexSpinnerContains(spinnerDIREC_LET_VG,DIREC_LET_VG));
                    editDIREC_LET_VG_OTRO.setText(DIREC_LET_VG_OTRO);
                    spinnerDIREC_SF_VG.setSelection(getIndexSpinnerContains(spinnerDIREC_SF_VG,DIREC_SF_VG));
                    if(!Util.stringNullEmptys(DIREC_SF_VG)){
                        spinnerDIREC_SF_VG.setSelection(getIndexSpinnerContains(spinnerDIREC_SF_VG,"2"));
                    }else{
                        spinnerDIREC_SF_VG.setSelection(getIndexSpinnerContains(spinnerDIREC_SF_VG,DIREC_SF_VG));
                    }

                    spinnerDIREC_LET_SVG.setSelection(getIndexSpinnerContains(spinnerDIREC_LET_SVG,DIREC_LET_SVG));
                    editDIREC_LET_SVG_OTRO.setText(DIREC_LET_SVG_OTRO);
                    editDIREC_NUM_PLACA.setText(DIREC_NUM_PLACA);

                    if(DIREC_CUAD_VG!= null){
                        if(DIREC_CUAD_VG.equals("")){
                            spinnerDIREC_CUAD_VG.setSelection(getIndexSpinnerContains(spinnerDIREC_CUAD_VG,"1"));
                        }else{
                            spinnerDIREC_CUAD_VG.setSelection(getIndexSpinnerContains(spinnerDIREC_CUAD_VG,DIREC_CUAD_VG));
                        }
                    }


                    if(DIREC_P_COMP!= null ){
                        if(DIREC_P_COMP.equals("1")){
                            id_complemento_si.setChecked(true);
                            id_complemento_no.setChecked(false);
                            linearComplemento.setVisibility(View.VISIBLE);
                            linearComplementoAcomp.setVisibility(View.VISIBLE);
                        }
                        if(DIREC_P_COMP.equals("2")){
                            id_complemento_si.setChecked(false);
                            id_complemento_no.setChecked(true);
                            linearComplemento.setVisibility(View.GONE);
                            linearComplementoAcomp.setVisibility(View.GONE);
                        }
                    }

                    lstComplemento = db.getComplementoNormalizador(diligenciadoNormalizador.getId());
                    recycler_complementos = (RecyclerView) findViewById(R.id.recycler_complementos);
                    mAdapterComplemento = new ConteoAdapterComplementos(lstComplemento,FormularioUnidad.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recycler_complementos.setLayoutManager(mLayoutManager);
                    recycler_complementos.setItemAnimator(new DefaultItemAnimator());
                    recycler_complementos.addItemDecoration(new DividerItemDecoration(v.getContext(), LinearLayoutManager.VERTICAL));

                    recycler_complementos.setAdapter(mAdapterComplemento);
                    mAdapterComplemento.notifyDataSetChanged();
                    concatDireccion();

                }else{
                    if(id_unidad!= null && !id_unidad.equals("1")){
                        Integer valor = Integer.valueOf(id_unidad) - 1;
                        NormalizadorDireccionDTO normalizadorAnterior = db.getNormalizador(id_manzana,id_edificacion,valor.toString());
                        if(normalizadorAnterior!= null && normalizadorAnterior.getId()!= null){
                            if(Util.stringNullEmptys(DIREC_PREVIA) &&  DIREC_PREVIA.equals("1")
                                    || DIREC_PREVIA.equals("2") || DIREC_PREVIA.equals("3")){  // Diligencia de la 1 a la 10
                                DIREC_VP=normalizadorAnterior.getDirecVp();
                                DIREC_NNOM_VP=normalizadorAnterior.getDirecNnomVp();
                                DIREC_NUM_VP=normalizadorAnterior.getDirecNumVp();
                                DIREC_LET_VP=normalizadorAnterior.getDirecLetVp();
                                if(DIREC_LET_VP!= null && DIREC_LET_VP.equals("Otros"))
                                    linearDIREC_LET_VP_OTRO.setVisibility(View.VISIBLE);

                                DIREC_LET_VP_OTRO=normalizadorAnterior.getDirecLetVpOtro();
                                DIREC_SF_VP=normalizadorAnterior.getDirecSfVp();
                                DIREC_LET_SVP=normalizadorAnterior.getDirecLetSvp();
                                if(DIREC_LET_SVP!= null && DIREC_LET_SVP.equals("Otros"))
                                    linearDIREC_LET_SVP_OTRO.setVisibility(View.VISIBLE);

                                DIREC_LET_SVP_OTRO=normalizadorAnterior.getDirecLetSvpOtro();
                                DIREC_CUAD_VP=normalizadorAnterior.getDirecCuadVp();
                                DIREC_NUM_VG=normalizadorAnterior.getDirecNumVg();
                                DIREC_LET_VG=normalizadorAnterior.getDirecLetVg();
                                if(DIREC_LET_VG!= null && DIREC_LET_VG.equals("Otros"))
                                    linearDIREC_LET_VG_OTRO.setVisibility(View.VISIBLE);

                                DIREC_LET_VG_OTRO=normalizadorAnterior.getDirecLetVgOtro();
                                DIREC_SF_VG=normalizadorAnterior.getDirecSfVg();
                                DIREC_LET_SVG=normalizadorAnterior.getDirecLetSvg();
                                if(DIREC_LET_SVG!= null && DIREC_LET_SVG.equals("Otros"))
                                    linearDIREC_LET_SVG_OTRO.setVisibility(View.VISIBLE);

                                DIREC_LET_SVG_OTRO=normalizadorAnterior.getDirecLetSvgOtro();  //pregunta 10

                                spinnerDIREC_VP.setSelection(getIndexSpinnerContains(spinnerDIREC_VP,DIREC_VP));
                                editDIREC_NNOM_VP.setText(DIREC_NNOM_VP);
                                editDIREC_NUM_VP.setText(DIREC_NUM_VP);
                                spinnerDIREC_LET_VP.setSelection(getIndexSpinnerContains(spinnerDIREC_LET_VP,DIREC_LET_VP));
                                editDIREC_LET_VP_OTRO.setText(DIREC_LET_VP_OTRO);
                                if(Util.stringNullEmptys(DIREC_SF_VP)){
                                    spinnerDIREC_SF_VP.setSelection(getIndexSpinnerContains(spinnerDIREC_SF_VP,"2"));
                                }else{
                                    spinnerDIREC_SF_VP.setSelection(getIndexSpinnerContains(spinnerDIREC_SF_VP,DIREC_SF_VP));
                                }

                                spinnerDIREC_LET_SVP.setSelection(getIndexSpinnerContains(spinnerDIREC_LET_SVP,DIREC_LET_SVP));
                                editDIREC_LET_SVP_OTRO.setText(DIREC_LET_SVP_OTRO);
                                if(Util.stringNullEmptys(DIREC_CUAD_VP)){
                                    spinnerDIREC_CUAD_VP.setSelection(getIndexSpinnerContains(spinnerDIREC_CUAD_VP,"1"));
                                }else{
                                    spinnerDIREC_CUAD_VP.setSelection(getIndexSpinnerContains(spinnerDIREC_CUAD_VP,DIREC_CUAD_VP));
                                }

                                editDIREC_NUM_VG.setText(DIREC_NUM_VG);
                                spinnerDIREC_LET_VG.setSelection(getIndexSpinnerContains(spinnerDIREC_LET_VG,DIREC_LET_VG));
                                editDIREC_LET_VG_OTRO.setText(DIREC_LET_VG_OTRO);
                                spinnerDIREC_SF_VG.setSelection(getIndexSpinnerContains(spinnerDIREC_SF_VG,DIREC_SF_VG));
                                if(Util.stringNullEmptys(DIREC_SF_VG)){
                                    spinnerDIREC_SF_VG.setSelection(getIndexSpinnerContains(spinnerDIREC_SF_VG,"2"));
                                }else{
                                    spinnerDIREC_SF_VG.setSelection(getIndexSpinnerContains(spinnerDIREC_SF_VG,DIREC_SF_VG));
                                }

                                spinnerDIREC_LET_SVG.setSelection(getIndexSpinnerContains(spinnerDIREC_LET_SVG,DIREC_LET_SVG));
                                editDIREC_LET_SVG_OTRO.setText(DIREC_LET_SVG_OTRO);
                                lstComplemento = new ArrayList<>();
                                linearComplemento.setVisibility(View.GONE);
                                linearComplementoAcomp.setVisibility(View.GONE);
                                recargarListadoComplementos();
                                concatDireccion();
                            }

                            if(Util.stringNullEmptys(DIREC_PREVIA) && ( DIREC_PREVIA.equals("2")|| DIREC_PREVIA.equals("3"))){  // Diligencia de la 10 a la 12
                                DIREC_NUM_PLACA=normalizadorAnterior.getDirecNumPlaca();
                                DIREC_CUAD_VG=normalizadorAnterior.getDirecCuadVg();

                                editDIREC_NUM_PLACA.setText(DIREC_NUM_PLACA);

                                if(DIREC_CUAD_VG!= null){
                                    if(DIREC_CUAD_VG.equals("")){
                                        spinnerDIREC_CUAD_VG.setSelection(getIndexSpinnerContains(spinnerDIREC_CUAD_VG,"1"));
                                    }else{
                                        spinnerDIREC_CUAD_VG.setSelection(getIndexSpinnerContains(spinnerDIREC_CUAD_VG,DIREC_CUAD_VG));
                                    }
                                }
                                lstComplemento = new ArrayList<>();
                                linearComplemento.setVisibility(View.GONE);
                                linearComplementoAcomp.setVisibility(View.GONE);
                                recargarListadoComplementos();
                                concatDireccion();
                            }

                            if(Util.stringNullEmptys(DIREC_PREVIA) && DIREC_PREVIA.equals("3")){  // Diligencia de la 12 a la 15
                                DIREC_P_COMP=normalizadorAnterior.getDirecPComp();
                                if(DIREC_P_COMP!= null ){
                                    if(DIREC_P_COMP.equals("1")){
                                        id_complemento_si.setChecked(true);
                                        id_complemento_no.setChecked(false);
                                        linearComplemento.setVisibility(View.VISIBLE);
                                        linearComplementoAcomp.setVisibility(View.VISIBLE);
                                    }
                                    if(DIREC_P_COMP.equals("2")){
                                        id_complemento_si.setChecked(false);
                                        id_complemento_no.setChecked(true);
                                        linearComplemento.setVisibility(View.GONE);
                                        linearComplementoAcomp.setVisibility(View.GONE);
                                    }
                                }

                                lstComplemento = db.getComplementoNormalizador(normalizadorAnterior.getId());
                                recargarListadoComplementos();
                                concatDireccion();
                            }
                        }
                    }
                }

                linearNormalizador.setVisibility(View.VISIBLE);
                linearAdicionarAgregarDireccion.setVisibility(View.VISIBLE);
                linearPregunta6.setVisibility(View.GONE);
                linearPregunta7.setVisibility(View.GONE);
                guardar_formulario.setVisibility(View.GONE);

                getCargarTipoDireccion();
            }
        });

        linearAdicionarAgregarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String retorno = "";

                if(Util.stringNullEmptys(DIREC_P_TIPO) && DIREC_P_TIPO.equals("1") ){
                    if(Util.stringNullEmptys(DIREC_VP) && DIREC_VP.contains("NA")){
                        if(DIREC_P_COMP==null){
                            retorno = retorno + "Seleccionar un tipo de complemento.\n";
                        }else{
                            if(DIREC_P_COMP.equals("1")) {
                                if(lstComplemento.size() == 0){
                                    retorno = retorno + "Agregar por lo menos un complemento de la dirección.\n";
                                }
                            }
                        }
                    }else{
                        DIREC_NNOM_VP = editDIREC_NNOM_VP.getText().toString();//No Obligatorio
                        DIREC_NUM_VP = editDIREC_NUM_VP.getText().toString(); //Obligatorio, si pregunta DIREC_NNOM_VP seleccionó NA
                        DIREC_LET_VP_OTRO = editDIREC_LET_VP_OTRO.getText().toString();//Obligatorio si selecciona otro
                        DIREC_LET_SVP_OTRO = editDIREC_LET_SVP_OTRO.getText().toString();//Obligatorio si selecciona otro
                        DIREC_NUM_VG = editDIREC_NUM_VG.getText().toString();//Obligatorio
                        DIREC_LET_VG_OTRO = editDIREC_LET_VG_OTRO.getText().toString();//Obligatorio si selecciona otro
                        DIREC_LET_SVG_OTRO = editDIREC_LET_SVG_OTRO.getText().toString(); //Obligatorio si selecciona otro
                        DIREC_NUM_PLACA = editDIREC_NUM_PLACA.getText().toString();  //Obligatorio
                        DIREC_TEX_COM = editDIREC_TEX_COM.getText().toString(); // no obligatorio


                        if(DIREC_VP == null) //Obligatorio
                            retorno = retorno + "Debe seleccionar el tipo de vía.\n";


                        if(DIREC_NNOM_VP!= null ){// No Obligatorio pero - Longitud > 3 y <= 150 caracteres
                            if( !DIREC_NNOM_VP.equals("") && !DIREC_NNOM_VP.toUpperCase().equals("NA") &&  DIREC_NNOM_VP.length() < 3 ){
                                retorno = retorno + "El nombre común de via principal debe ser entre 3 y <= 150 caracteres.\n";
                            }
                            if( !DIREC_NNOM_VP.equals("") &&  DIREC_NNOM_VP.toUpperCase().contains("NA")){
                                if(DIREC_NUM_VP.equals("") && DIREC_NUM_VP.length() < 1){
                                    retorno = retorno + "El número de la via principal debete tener el rango válido >0 y <=999.\n";
                                }
                            }
                        }

                        if(DIREC_LET_VP != null && DIREC_LET_VP.equals("Otro")){
                            if(DIREC_LET_VP_OTRO.equals("")){
                                retorno = retorno + "Diligenciar Otro en letra via principal.\n";
                            }
                        }

                        if(DIREC_SF_VP == null || DIREC_SF_VP.equals("-1")){
                            retorno = retorno + "Seleccionar el sufijo en via principal.\n";
                        }

                        if(DIREC_LET_SVP != null && DIREC_LET_SVP.equals("Otro")){
                            if(DIREC_LET_SVP_OTRO.equals("")){
                                retorno = retorno + "Diligenciar Otro en Sufijo en letra via principal.\n";
                            }
                        }

                        if(DIREC_NUM_VG.equals("")){
                            retorno = retorno + "Diligenciar número de la vía generadora, Rango válido >0 y <=999.\n";
                        }

                        if(DIREC_LET_VG != null && DIREC_LET_VG.equals("Otro")){
                            if(DIREC_LET_VG_OTRO.equals("")){
                                retorno = retorno + "Diligenciar Otro en letra  via generadora.\n";
                            }
                        }

                        if(DIREC_SF_VG == null || DIREC_SF_VG.equals("-1")){
                            retorno = retorno + "Seleccionar el sufijo de la vía generadora.\n";
                        }

                        if(DIREC_LET_SVG != null && DIREC_LET_SVG.equals("Otro")){
                            if(DIREC_LET_SVG_OTRO.equals("")){
                                retorno = retorno + "Diligenciar Otro en letra de Sufijo via generadora.\n";
                            }
                        }

                        if(DIREC_NUM_PLACA.equals("")){
                            retorno = retorno + "Diligenciar el numero de placa, Rango válido >0 y <=999.\n";
                        }

                        if(DIREC_P_COMP==null){
                            retorno = retorno + "Seleccionar un tipo de complemento.\n";
                        }else{
                            if(DIREC_P_COMP.equals("1")) {
                                if(DIREC_P_COMP.equals("1")) {
                                    if(lstComplemento.size() == 0){
                                        retorno = retorno + "Agregar por lo menos un complemento de la dirección.\n";
                                    }
                                }
                            }
                        }
                    }
                }else{
                    if(lstComplemento.size() == 0){
                        retorno = retorno + "Agregar por lo menos un complemento de la dirección.\n";
                    }
                }

                if(retorno.equals("")){
                    linearDireccion.setVisibility(View.VISIBLE);
                    normalizador.setIdManzana(id_manzana);
                    normalizador.setIdUnidadEconomica(id_unidad);
                    normalizador.setIdEdificacion(id_edificacion);
                    normalizador.setDirecVp(DIREC_VP);
                    normalizador.setDirecNnomVp(DIREC_NNOM_VP);
                    normalizador.setDirecNumVp(DIREC_NUM_VP);
                    normalizador.setDirecLetVp(DIREC_LET_VP);
                    normalizador.setDirecLetVpOtro(DIREC_LET_VP_OTRO);
                    normalizador.setDirecSfVp(DIREC_SF_VP);
                    normalizador.setDirecLetSvp(DIREC_LET_SVP);
                    normalizador.setDirecLetSvpOtro(DIREC_LET_SVP_OTRO);
                    normalizador.setDirecCuadVp(DIREC_CUAD_VP);
                    normalizador.setDirecNumVg(DIREC_NUM_VG);
                    normalizador.setDirecLetVg(DIREC_LET_VG);
                    normalizador.setDirecLetVgOtro(DIREC_LET_VG_OTRO);
                    normalizador.setDirecSfVg(DIREC_SF_VG);
                    normalizador.setDirecLetSvg(DIREC_LET_SVG);
                    normalizador.setDirecLetSvgOtro(DIREC_LET_SVG_OTRO);
                    normalizador.setDirecNumPlaca(DIREC_NUM_PLACA);
                    normalizador.setDirecCuadVg(DIREC_CUAD_VG);
                    normalizador.setDirecPComp(DIREC_P_COMP);
                    normalizador.setImei(idDevice);
                    normalizador.setFechaCreacion(util.getFechaActual());
                    normalizador.setUsuario(session.getnombre());
                    db.postNormalizador(normalizador);
                    cambiosPendientes = false;
                    complemento = null;

                    NormalizadorDireccionDTO normalizador = db.getNormalizador(id_manzana,id_edificacion,id_unidad);

                    ComplementoNormalizadorDTO complemnetoCargado = new ComplementoNormalizadorDTO();
                    complemnetoCargado.setIdNormalizador(normalizador.getId());

                    db.postComplementoNormalizador(complemnetoCargado, true);
                    for (ComplementoNormalizadorDTO lista : lstComplemento) {
                        lista.setIdNormalizador(normalizador.getId());
                        lista.setId(null);
                        db.postComplementoNormalizador(lista, false);
                    }

                    linearNormalizador.setVisibility(View.GONE);
                    linearAdicionarAgregarDireccion.setVisibility(View.GONE);
                    linearPregunta6.setVisibility(View.VISIBLE);
                    linearPregunta7.setVisibility(View.VISIBLE);
                    guardar_formulario.setVisibility(View.VISIBLE);
                }else{
                    msj.dialogoMensajeError("Error Validación", retorno);
                }
            }
        });

        linearAdicionarComplemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = "";

                if(DIREC_COMP == null)
                    mensaje = mensaje+ "Seleccionar el complemento de la direccion. \n";

                if(editDIREC_TEX_COM.getText().toString().equals(""))
                    mensaje = mensaje+ "Digitar el complemento de la direccion. \n";

                if(Util.stringNullEmptys(mensaje)){
                    msj.dialogoMensajeError("Error", mensaje);
                }else{
                    if(isEditarComplemento){
                        tv_adicionarComplemento.setText(getString(R.string.text_pregunta6_3_5));
                        isEditarComplemento = false;
                        for(ComplementoNormalizadorDTO listado : lstComplemento){
                            if(listado.getId().equals(complemento.getId())){
                                listado.setDirecComp(DIREC_COMP);
                                listado.setDirecTextComp(editDIREC_TEX_COM.getText().toString());
                                listado.setDirecComplemento((DIREC_COMP + " " + editDIREC_TEX_COM.getText().toString()).trim());
                                mAdapterComplemento.notifyDataSetChanged();
                                msj.dialogoMensaje("Success", "Se ha editado un complemento. Cambios pendientes en guardar en el Normalizador.");
                                editDIREC_TEX_COM.setText("");
                                spinnerDIREC_COMP.setText(getIndexAutoComplet(Arrays.asList(getResources().getStringArray(R.array.complemento)), "Seleccione..."),false);
                                DIREC_COMP = null;
                                concatDireccion();
                                cambiosPendientes  = true;
                                complemento = null;
                                break;
                            }
                        }
                    }else{
                        ComplementoNormalizadorDTO nuevo = new ComplementoNormalizadorDTO();
                        nuevo.setDirecTextComp(editDIREC_TEX_COM.getText().toString());
                        nuevo.setDirecComp(DIREC_COMP);
                        nuevo.setDirecComplemento((DIREC_COMP + " " + editDIREC_TEX_COM.getText().toString()).trim());
                        lstComplemento.add(nuevo);
                        mAdapterComplemento.notifyDataSetChanged();
                        msj.dialogoMensaje("Success", "Se ha agregado un complemento.");
                        editDIREC_TEX_COM.setText("");
                        spinnerDIREC_COMP.setText(getIndexAutoComplet(Arrays.asList(getResources().getStringArray(R.array.complemento)), "Seleccione..."),false);
                        DIREC_COMP = null;
                        concatDireccion();
                    }

                }
            }
        });

        guardar_formulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnidadEconomica unidad=new UnidadEconomica();

                String retorno = validarFormulario();
                if(retorno == null){
                    unidad=saveFormulario();

                    if(db.guardarUnidadEconomica(unidad,id_manzana,id_edificacion,id_unidad)){
                        normalizador.setIdManzana(id_manzana);
                        normalizador.setIdUnidadEconomica(id_unidad);
                        //db.postNormalizador(normalizador);
                        msj.generarToast("Formulario Guardado");
                    }
                    FormularioUnidad.this.finish();
                }else{
                    msj.dialogoMensajeError("Error", retorno);
                }
            }
        });

        Manzana manzana =new Manzana();
        manzana=db.getManzana(id_manzana);

        if(manzana.getFinalizado().equals("Si")){
            guardar_formulario.setVisibility(View.GONE);
        }

        linearDIREC_NNOM_VP.setVisibility(View.GONE);
        linearDIREC_NUM_VP.setVisibility(View.GONE);
        linearDIREC_LET_VP.setVisibility(View.GONE);
        linearDIREC_SF_VP.setVisibility(View.GONE);
        linearDIREC_LET_SVP.setVisibility(View.GONE);
        linearDIREC_CUAD_VP.setVisibility(View.GONE);
        linearDIREC_NUM_VG.setVisibility(View.GONE);
        linearDIREC_LET_VG.setVisibility(View.GONE);
        linearDIREC_SF_VG.setVisibility(View.GONE);
        linearDIREC_LET_SVG.setVisibility(View.GONE);
        linearDIREC_NUM_PLACA.setVisibility(View.GONE);
        linearDIREC_CUAD_VG.setVisibility(View.GONE);
        linearDIREC_P_COMP.setVisibility(View.GONE);

        llenarFormulario();

        spinnerDIREC_VP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinnerDIREC_VP.getSelectedItem().toString().equals("Seleccione...")){
                   DIREC_VP = Util.getLimpiarAcronimoDireccion(spinnerDIREC_VP.getSelectedItem().toString());
                   if(DIREC_VP.contains("NA")){
                       linearDIREC_NNOM_VP.setVisibility(View.GONE);
                       linearDIREC_NUM_VP.setVisibility(View.GONE);
                       linearDIREC_LET_VP.setVisibility(View.GONE);
                       linearDIREC_SF_VP.setVisibility(View.GONE);
                       linearDIREC_LET_SVP.setVisibility(View.GONE);
                       linearDIREC_CUAD_VP.setVisibility(View.GONE);
                       linearDIREC_NUM_VG.setVisibility(View.GONE);
                       linearDIREC_LET_VG.setVisibility(View.GONE);
                       linearDIREC_SF_VG.setVisibility(View.GONE);
                       linearDIREC_LET_SVG.setVisibility(View.GONE);
                       linearDIREC_NUM_PLACA.setVisibility(View.GONE);
                       linearDIREC_CUAD_VG.setVisibility(View.GONE);
                       linearDIREC_P_COMP.setVisibility(View.GONE);

                       editDIREC_NNOM_VP.setText("");
                       editDIREC_NUM_VP.setText("");
                       spinnerDIREC_LET_VP.setSelection(getIndexSpinnerContains(spinnerDIREC_LET_VP,"Seleccione..."));
                       editDIREC_LET_VP_OTRO.setText("");
                       spinnerDIREC_SF_VP.setSelection(getIndexSpinnerContains(spinnerDIREC_SF_VP,"Seleccione..."));
                       spinnerDIREC_LET_SVP.setSelection(getIndexSpinnerContains(spinnerDIREC_LET_SVP,"Seleccione..."));
                       editDIREC_LET_SVP_OTRO.setText("");
                       spinnerDIREC_CUAD_VP.setSelection(getIndexSpinnerContains(spinnerDIREC_CUAD_VP,"Seleccione..."));


                       editDIREC_NUM_VG.setText("");
                       spinnerDIREC_LET_VG.setSelection(getIndexSpinnerContains(spinnerDIREC_LET_VG,"Seleccione..."));
                       editDIREC_LET_VG_OTRO.setText("");
                       spinnerDIREC_SF_VG.setSelection(getIndexSpinnerContains(spinnerDIREC_SF_VG,"Seleccione..."));
                       spinnerDIREC_LET_SVG.setSelection(getIndexSpinnerContains(spinnerDIREC_LET_SVG,"Seleccione..."));
                       editDIREC_LET_SVG_OTRO.setText("");
                       editDIREC_NUM_PLACA.setText("");
                       spinnerDIREC_CUAD_VG.setSelection(getIndexSpinnerContains(spinnerDIREC_CUAD_VG,"Seleccione..."));

                       DIREC_NNOM_VP = null;
                       DIREC_NUM_VP = null;
                       DIREC_LET_VP = null;
                       DIREC_SF_VP = null;
                       DIREC_LET_SVP = null;
                       DIREC_CUAD_VP = null;
                       DIREC_NUM_VG = null;
                       DIREC_LET_VG = null;
                       DIREC_SF_VG = null;
                       DIREC_LET_SVG = null;
                       DIREC_NUM_PLACA = null;
                       DIREC_CUAD_VG = null;
                       DIREC_P_COMP = "1";

                       id_complemento_si.setChecked(true);
                   }else{
                       linearDIREC_NNOM_VP.setVisibility(View.VISIBLE);
                       linearDIREC_NUM_VP.setVisibility(View.VISIBLE);
                       linearDIREC_LET_VP.setVisibility(View.VISIBLE);
                       linearDIREC_SF_VP.setVisibility(View.VISIBLE);
                       linearDIREC_LET_SVP.setVisibility(View.VISIBLE);
                       linearDIREC_CUAD_VP.setVisibility(View.VISIBLE);
                       linearDIREC_NUM_VG.setVisibility(View.VISIBLE);
                       linearDIREC_LET_VG.setVisibility(View.VISIBLE);
                       linearDIREC_SF_VG.setVisibility(View.VISIBLE);
                       linearDIREC_LET_SVG.setVisibility(View.VISIBLE);
                       linearDIREC_NUM_PLACA.setVisibility(View.VISIBLE);
                       linearDIREC_CUAD_VG.setVisibility(View.VISIBLE);
                       linearDIREC_P_COMP.setVisibility(View.VISIBLE);
                   }
                }
                concatDireccion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDIREC_SF_VP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinnerDIREC_SF_VP.getSelectedItem().toString().equals("Seleccione...")){
                    if(spinnerDIREC_SF_VP.getSelectedItem().toString().contains("BIS")){
                        DIREC_SF_VP = "BIS";
                    }
                    if(spinnerDIREC_SF_VP.getSelectedItem().toString().contains("Sin sufijo")){
                        DIREC_SF_VP = "";
                    }
                }else{
                    DIREC_SF_VP = "-1";
                }
                concatDireccion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDIREC_CUAD_VP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinnerDIREC_CUAD_VP.getSelectedItem().toString().equals("Seleccione...")){
                    DIREC_CUAD_VP = Util.primerValor(spinnerDIREC_CUAD_VP.getSelectedItem().toString());
                    if(DIREC_CUAD_VP.equals("1")){
                        DIREC_CUAD_VP = "";
                    }
                }
                concatDireccion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDIREC_SF_VG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinnerDIREC_SF_VG.getSelectedItem().toString().equals("Seleccione...")){
                    if(spinnerDIREC_SF_VG.getSelectedItem().toString().contains("BIS")){
                        DIREC_SF_VG = "BIS";
                    }
                    if(spinnerDIREC_SF_VG.getSelectedItem().toString().contains("Sin sufijo")){
                        DIREC_SF_VG = "";
                    }
                }else{
                    DIREC_SF_VG = "-1";
                }
                concatDireccion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDIREC_CUAD_VG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinnerDIREC_CUAD_VG.getSelectedItem().toString().equals("Seleccione...")){
                    DIREC_CUAD_VG = Util.primerValor(spinnerDIREC_CUAD_VG.getSelectedItem().toString());
                    if(DIREC_CUAD_VG.equals("1")){
                        DIREC_CUAD_VG = "";
                    }
                }
                concatDireccion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDIREC_COMP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if(!item.equals("Seleccione...") && !item.equals("")){
                    DIREC_COMP = Util.getLimpiarAcronimoDireccion(item);
                    if(DIREC_COMP.contains("NA")){
                        DIREC_COMP = "";
                    }
                }
                concatDireccion();
            }
        });

        //Preguntas OTRO
        spinnerDIREC_LET_VP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinnerDIREC_LET_VP.getSelectedItem().toString().equals("Seleccione...")){
                    if(spinnerDIREC_LET_VP.getSelectedItem().toString().contains("Otros")){
                        linearDIREC_LET_VP_OTRO.setVisibility(View.VISIBLE);
                    }else{
                        linearDIREC_LET_VP_OTRO.setVisibility(View.GONE);
                        editDIREC_LET_VP_OTRO.setText("");
                        DIREC_LET_SVG_OTRO = null;
                    }
                    DIREC_LET_VP = spinnerDIREC_LET_VP.getSelectedItem().toString();
                }
                concatDireccion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Preguntas OTRO
        spinnerDIREC_LET_SVP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinnerDIREC_LET_SVP.getSelectedItem().toString().equals("Seleccione...")){
                    if(spinnerDIREC_LET_SVP.getSelectedItem().toString().contains("Otros")){
                        linearDIREC_LET_SVP_OTRO.setVisibility(View.VISIBLE);
                    }else{
                        linearDIREC_LET_SVP_OTRO.setVisibility(View.GONE);
                        editDIREC_LET_SVP_OTRO.setText("");
                        DIREC_LET_SVG_OTRO = null;
                    }
                    DIREC_LET_SVP = spinnerDIREC_LET_SVP.getSelectedItem().toString();
                }
                concatDireccion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Preguntas OTRO
        spinnerDIREC_LET_VG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinnerDIREC_LET_VG.getSelectedItem().toString().equals("Seleccione...")){
                    if(spinnerDIREC_LET_VG.getSelectedItem().toString().contains("Otros")){
                        linearDIREC_LET_VG_OTRO.setVisibility(View.VISIBLE);
                    }else{
                        linearDIREC_LET_VG_OTRO.setVisibility(View.GONE);
                        editDIREC_LET_VG_OTRO.setText("");
                        DIREC_LET_SVG_OTRO = null;
                    }
                    DIREC_LET_VG = spinnerDIREC_LET_VG.getSelectedItem().toString();
                }
                concatDireccion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Preguntas OTRO
        spinnerDIREC_LET_SVG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinnerDIREC_LET_SVG.getSelectedItem().toString().equals("Seleccione...")){
                    if(spinnerDIREC_LET_SVG.getSelectedItem().toString().contains("Otros")){
                        linearDIREC_LET_SVG_OTRO.setVisibility(View.VISIBLE);
                    }else{
                        linearDIREC_LET_SVG_OTRO.setVisibility(View.GONE);
                        editDIREC_LET_SVG_OTRO.setText("");
                        DIREC_LET_SVG_OTRO = null;
                    }
                    DIREC_LET_SVG = spinnerDIREC_LET_SVG.getSelectedItem().toString();
                }
                concatDireccion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Preguntas CONPLEMENTO TIENE
        radioDIREC_P_COMP.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        DIREC_P_COMP = null;
                        linearComplemento.setVisibility(View.GONE);
                        linearComplementoAcomp.setVisibility(View.GONE);
                        spinnerDIREC_COMP.setText(getIndexAutoComplet(Arrays.asList(getResources().getStringArray(R.array.complemento)), "Seleccione..."),false);
                        editDIREC_TEX_COM.setText("");
                        break;
                    case R.id.id_complemento_si:
                        DIREC_P_COMP = "1";
                        linearComplemento.setVisibility(View.VISIBLE);
                        linearComplementoAcomp.setVisibility(View.VISIBLE);
                        spinnerDIREC_COMP.setText(getIndexAutoComplet(Arrays.asList(getResources().getStringArray(R.array.complemento)), "Seleccione..."),false);
                        editDIREC_TEX_COM.setText("");
                        break;
                    case R.id.id_complemento_no:
                        DIREC_P_COMP = "2";
                        linearComplemento.setVisibility(View.GONE);
                        linearComplementoAcomp.setVisibility(View.GONE);
                        spinnerDIREC_COMP.setText(getIndexAutoComplet(Arrays.asList(getResources().getStringArray(R.array.complemento)), "Seleccione..."),false);
                        editDIREC_TEX_COM.setText("");
                        break;
                }
                concatDireccion();
            }
        });

    }

    /**
     * Metodo que calcula el total de hombre
     */
    private void getValidarTextoPregunta2(EditText edit) {
        String text = edit.getText().toString();
        linearDIREC_NNOM_VP.setVisibility(View.VISIBLE);
        if(Util.stringNullEmptys(text) && (text.toUpperCase().equals("NA"))){
            linearDIREC_NUM_VP.setVisibility(View.VISIBLE);
            editDIREC_NUM_VP.setText("");
        }else{
            editDIREC_NUM_VP.setText("");
            linearDIREC_NUM_VP.setVisibility(View.GONE);
        }
        concatDireccion();
    }

    /**
     * Metodo que recarga la lista de los complementos
     */
    private void recargarListadoComplementos() {
        recycler_complementos = (RecyclerView) findViewById(R.id.recycler_complementos);
        mAdapterComplemento = new ConteoAdapterComplementos(lstComplemento,FormularioUnidad.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_complementos.setLayoutManager(mLayoutManager);
        recycler_complementos.setItemAnimator(new DefaultItemAnimator());
        recycler_complementos.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recycler_complementos.setAdapter(mAdapterComplemento);
        mAdapterComplemento.notifyDataSetChanged();
    }

    /**
     * Metodoq ue habiilita o deshabilita
     */
    private void getCargarTipoDireccion(){

        if(Util.stringNullEmptys(DIREC_P_TIPO) && DIREC_P_TIPO.equals("1") ){
            linearDIREC_VP.setVisibility(View.VISIBLE);
            linearDIREC_NNOM_VP.setVisibility(View.GONE);
            linearDIREC_NUM_VP.setVisibility(View.VISIBLE);
            linearDIREC_LET_VP.setVisibility(View.VISIBLE);
            linearDIREC_SF_VP.setVisibility(View.VISIBLE);
            linearDIREC_LET_SVP.setVisibility(View.VISIBLE);
            linearDIREC_CUAD_VP.setVisibility(View.VISIBLE);
            linearDIREC_NUM_VG.setVisibility(View.VISIBLE);
            linearDIREC_LET_VG.setVisibility(View.VISIBLE);
            linearDIREC_SF_VG.setVisibility(View.VISIBLE);
            linearDIREC_LET_SVG.setVisibility(View.VISIBLE);
            linearDIREC_NUM_PLACA.setVisibility(View.VISIBLE);
            linearDIREC_CUAD_VG.setVisibility(View.VISIBLE);
            linearDIREC_P_COMP.setVisibility(View.VISIBLE);
            //linearComplemento.setVisibility(View.VISIBLE);
            //linearComplementoAcomp.setVisibility(View.VISIBLE);
        }



        if(Util.stringNullEmptys(DIREC_P_TIPO) && (DIREC_P_TIPO.equals("2") || DIREC_P_TIPO.equals("3")) ){
            linearDIREC_VP.setVisibility(View.GONE);
            linearDIREC_NNOM_VP.setVisibility(View.GONE);
            linearDIREC_NUM_VP.setVisibility(View.GONE);
            linearDIREC_LET_VP.setVisibility(View.GONE);
            linearDIREC_SF_VP.setVisibility(View.GONE);
            linearDIREC_LET_SVP.setVisibility(View.GONE);
            linearDIREC_CUAD_VP.setVisibility(View.GONE);
            linearDIREC_NUM_VG.setVisibility(View.GONE);
            linearDIREC_LET_VG.setVisibility(View.GONE);
            linearDIREC_SF_VG.setVisibility(View.GONE);
            linearDIREC_LET_SVG.setVisibility(View.GONE);
            linearDIREC_NUM_PLACA.setVisibility(View.GONE);
            linearDIREC_CUAD_VG.setVisibility(View.GONE);
            linearDIREC_P_COMP.setVisibility(View.GONE);
            linearComplemento.setVisibility(View.VISIBLE);
            linearComplementoAcomp.setVisibility(View.VISIBLE);
        }



    }

    /**
     * Metodo que valida el formulario
     *
     * @return
     */
    private String validarFormulario() {
        String retorno = "";

        if(DIREC_PREVIA == null)
            retorno = retorno + "Debe seleccionar la dirección de unidad previa.\n";

        if(DIREC_P_TIPO == null )
            retorno = retorno + "Debe seleccionar el tipo dirección.\n";

        if(UNIDAD_OBSERVACION == null )
            retorno = retorno + "Debe seleccionar el estado de la unidad de observación.\n";

        if(TIPO_OBSERVACION == null )
            retorno = retorno + "Debe seleccionar el tipo de la unidad de observación.\n";

        if(TIPO_OBSERVACION!= null && !TIPO_OBSERVACION.isEmpty() && TIPO_OBSERVACION == "3"){
            if(TIPO_VENDEDOR == null)
                retorno = retorno + "Debe seleccionar el tipo de vendedor.\n";
        }

        if(SECTOR_ECONOMICO == null )
            retorno = retorno + "Debe seleccionar el sector económico de la unidad.\n";

        NormalizadorDireccionDTO normalizador = db.getNormalizador(id_manzana,id_edificacion, id_unidad);
        if(normalizador!= null && !Util.stringNullEmptys(normalizador.getId())){
            retorno = retorno + "Debe agregar la dirección normalizada.\n";
        }

        if(retorno.equals("")) retorno = null;

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
        retorno.setUc(manzana.getUc());
        retorno.setObsmz(manzana.getObsmz());
        retorno.setNov_carto(manzana.getNov_carto());
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
     * Metodo que asigna las variables
     * @return
     */
    private UnidadEconomica saveFormulario(){
        UnidadEconomica unidad=new UnidadEconomica();
        unidad.setId_edificacion(id_edificacion);
        unidad.setId_manzana(id_manzana);
        unidad.setId_unidad(id_unidad);
        unidad.setDirec_previa(DIREC_PREVIA);
        unidad.setDirec_p_tipo(DIREC_P_TIPO);
        unidad.setEstado_unidad_observacion(UNIDAD_OBSERVACION);
        unidad.setTipo_unidad_observacion(TIPO_OBSERVACION);
        unidad.setTipo_vendedor(TIPO_VENDEDOR);
        unidad.setSector_economico(SECTOR_ECONOMICO);
        unidad.setUnidad_observacion(editUnidadObservacion.getText().toString());
        unidad.setObservacion(editObservacionUnidad.getText().toString());
        unidad.setImei(idDevice);
        unidad.setDirecc(concatDireccion());

        return unidad;
    }


    /**
     * Metodo que llena el formulario si tiene informacion.
     */
    private void llenarFormulario(){
        UnidadEconomica unidad= new UnidadEconomica();
        unidad=db.getUnidadEconomica(id_manzana,id_edificacion,id_unidad);

        if(unidad!= null && unidad.getId() != null && !unidad.getId().isEmpty()){

            if(unidad.getId_unidad().equals("1")){
                spinnerDIREC_PREVIA.setSelection(getIndexSpinnerContains(spinnerDIREC_PREVIA, "4"));
                DIREC_PREVIA = "4";
                spinnerDIREC_PREVIA.setEnabled(false);
                DIREC_P_TIPO = unidad.getDirec_p_tipo();
                spinnerDIREC_P_TIPO.setSelection(getIndexSpinnerContains(spinnerDIREC_P_TIPO, DIREC_P_TIPO));
            }else{
                ArrayAdapter<CharSequence> adapter_DIREC_PREVIA = ArrayAdapter.createFromResource(this,
                        R.array.direccion_previa2, android.R.layout.simple_spinner_item);
                adapter_DIREC_PREVIA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDIREC_PREVIA.setAdapter(adapter_DIREC_PREVIA);

                Integer valor = Integer.valueOf(id_unidad) - 1;
                UnidadEconomica unidadAnterior = db.getUnidadEconomica(id_manzana,id_edificacion,  valor.toString() );
                DIREC_P_TIPO = unidadAnterior.getDirec_p_tipo();
                spinnerDIREC_P_TIPO.setSelection(getIndexSpinnerContains(spinnerDIREC_P_TIPO, DIREC_P_TIPO));
                spinnerDIREC_P_TIPO.setEnabled(false);
            }

            if(unidad.getDirec_previa()!= null){
                DIREC_PREVIA = unidad.getDirec_previa();
                spinnerDIREC_PREVIA.setSelection(getIndexSpinnerContains(spinnerDIREC_PREVIA, DIREC_PREVIA));
            }

            isEdit = 1;
            DIRECC = unidad.getDirecc();
            if(Util.stringNullEmptys(DIRECC)){
                linearDireccion.setVisibility(View.VISIBLE);
                editDireccion.setText(DIRECC.replace("(","").replace(")",""));
            }else{
                linearDireccion.setVisibility(View.GONE);
            }
            UNIDAD_OBSERVACION = unidad.getEstado_unidad_observacion();
            TIPO_OBSERVACION = unidad.getTipo_unidad_observacion();
            TIPO_VENDEDOR = unidad.getTipo_vendedor();
            SECTOR_ECONOMICO = unidad.getSector_economico();

            spinnerUnidadObservacion.setSelection(getIndexSpinnerContains(spinnerUnidadObservacion, UNIDAD_OBSERVACION));

            if(UNIDAD_OBSERVACION!= null && UNIDAD_OBSERVACION.equals("1")){
                isEdit = 100;
                spinnerTipoObservacion.setAdapter(arr_unidad_observacion);
                if(TIPO_OBSERVACION!= null && TIPO_OBSERVACION.equals("1")){
                    spinnerSectorEconomico.setAdapter(arr_sector_economico);
                    linearTipoVendedor.setVisibility(View.GONE);
                    spinnerTipoVendedor.setAdapter(arrDefault);
                }
                if(TIPO_OBSERVACION!= null && TIPO_OBSERVACION.equals("2")){
                    spinnerSectorEconomico.setAdapter(arr_sector_economico);
                    linearTipoVendedor.setVisibility(View.GONE);
                    spinnerTipoVendedor.setAdapter(arrDefault);
                }
                if(TIPO_OBSERVACION!= null && TIPO_OBSERVACION.equals("3")){
                    spinnerTipoVendedor.setAdapter(arr_tipo_vendedor);
                    spinnerSectorEconomico.setAdapter(arr_sector_economico);
                    linearTipoVendedor.setVisibility(View.VISIBLE);
                }
                if(TIPO_OBSERVACION!= null && TIPO_OBSERVACION.equals("4")){
                    spinnerSectorEconomico.setAdapter(arr_sector_economico);
                    linearTipoVendedor.setVisibility(View.GONE);
                    spinnerTipoVendedor.setAdapter(arrDefault);
                }
                spinnerTipoObservacion.setSelection(getIndexSpinnerContains(spinnerTipoObservacion, TIPO_OBSERVACION));
                spinnerTipoVendedor.setSelection(getIndexSpinnerContains(spinnerTipoVendedor, TIPO_VENDEDOR));
                spinnerSectorEconomico.setSelection(getIndexSpinnerContains(spinnerSectorEconomico, SECTOR_ECONOMICO));
            }
            if(UNIDAD_OBSERVACION!= null && UNIDAD_OBSERVACION.equals("2")){
                isEdit = 100;
                editUnidadObservacion.setEnabled(false);
                editUnidadObservacion.setText("");
                spinnerTipoObservacion.setAdapter(arr_unidad_observacion_1);
                if(TIPO_OBSERVACION!= null && TIPO_OBSERVACION.equals("1")){
                    spinnerSectorEconomico.setAdapter(arr_sector_economico2);
                    linearTipoVendedor.setVisibility(View.GONE);
                }
                if(TIPO_OBSERVACION!= null && TIPO_OBSERVACION.equals("2")){
                    spinnerSectorEconomico.setAdapter(arr_sector_economico2);
                    linearTipoVendedor.setVisibility(View.GONE);
                }
                spinnerTipoObservacion.setSelection(getIndexSpinnerContains(spinnerTipoObservacion, TIPO_OBSERVACION));
                spinnerSectorEconomico.setSelection(getIndexSpinnerContains(spinnerSectorEconomico, SECTOR_ECONOMICO));
            }
            if(UNIDAD_OBSERVACION!= null && UNIDAD_OBSERVACION.equals("3")){
                isEdit = 100;
                spinnerTipoObservacion.setAdapter(arr_unidad_observacion_2);
                if(TIPO_OBSERVACION!= null && TIPO_OBSERVACION.equals("1")){
                    spinnerSectorEconomico.setAdapter(arr_sector_economico1);
                    linearTipoVendedor.setVisibility(View.GONE);
                }
                spinnerTipoObservacion.setSelection(getIndexSpinnerContains(spinnerTipoObservacion, TIPO_OBSERVACION));
                spinnerSectorEconomico.setSelection(getIndexSpinnerContains(spinnerSectorEconomico, SECTOR_ECONOMICO));
            }
            editObservacionUnidad.setText(unidad.getObservacion());
            editUnidadObservacion.setText(unidad.getUnidad_observacion());
        }else{
            isEdit = 1;
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
     * metodo que ubica la lista que contiene
     * @param spinner
     * @param myString
     * @return
     */
    private int getIndexSpinnerContains(Spinner spinner, String myString){
        int index = 0;

        if(myString!= null && !myString.equals("")){
            for (int i=0;i<spinner.getCount();i++){
                if (spinner.getItemAtPosition(i).toString().contains(myString)){
                    index = i;
                }
            }
        }

        return index;
    }

    /**
     * Metodo que carga el valor
     *
     * @param listado
     * @param myString
     * @return
     */
    private String getIndexAutoComplet(List listado, String myString){
        String retorno = "";
        if(Util.stringNullEmptys(myString)){
            for (int i=0;i<listado.size();i++){
                String valor = Util.stringNullEmptys(listado.get(i).toString()) ? listado.get(i).toString(): "";
                if (valor.contains(myString)){;
                    retorno = valor;
                    break;
                }
            }
        }
        return retorno;
    }

    /**
     * Metodo para retornar
     */
    public void retornar(){

        if(cambiosPendientes){
            msj.dialogoMensaje("Success", "Tiene cambios pendiente; por favor guardar primero en el boton Agregar Dirección.");
        }else{
            if(db.getFinalizadaManzana(id_manzana)){
                FormularioUnidad.this.finish();
            }else{
                if(linearNormalizador.getVisibility() == View.VISIBLE){
                    linearNormalizador.setVisibility(View.GONE);
                    linearAdicionarAgregarDireccion.setVisibility(View.GONE);
                    linearPregunta6.setVisibility(View.VISIBLE);
                    linearPregunta7.setVisibility(View.VISIBLE);
                    guardar_formulario.setVisibility(View.VISIBLE);
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
        }
    }


    @Override
    public void onBackPressed() {
        retornar();
    }


    /**
     * Metodo que pasa de string a un valor numerico entero
     *
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

    /**
     * Metodo que concatena la direccion
     *
     * @return
     */
    private String concatDireccion(){
        String direccion = "";

        if(Util.stringNullEmptys(DIREC_VP) && !DIREC_VP.contains("NA")){
            direccion = (direccion +" "+ DIREC_VP).trim();
        }
        if(Util.stringNullEmptys(editDIREC_NNOM_VP.getText().toString()) && !editDIREC_NNOM_VP.getText().toString().toUpperCase().equals("NA") ){
            direccion = (direccion +" "+ editDIREC_NNOM_VP.getText().toString()).trim();
        }else{
            if(Util.stringNullEmptys(editDIREC_NUM_VP.getText().toString())){
                direccion = (direccion +" "+ editDIREC_NUM_VP.getText().toString()).trim();
            }
        }

        if(Util.stringNullEmptys(editDIREC_NUM_VP.getText().toString())){
            direccion = (direccion +" "+ editDIREC_NUM_VP.getText().toString()).trim();
        }
        if(Util.stringNullEmptys(DIREC_LET_VP)){
            if(DIREC_LET_VP.equals("Otros")){
                direccion = (direccion +" "+ editDIREC_LET_VP_OTRO.getText().toString()).trim();
            }else{
                direccion = (direccion +" "+ DIREC_LET_VP).trim();
            }
        }
        if(Util.stringNullEmptys(DIREC_SF_VP) && !DIREC_SF_VP.contains("2")){
            direccion = (direccion +" "+ DIREC_SF_VP).trim();
        }
        if(Util.stringNullEmptys(DIREC_LET_SVP)){
            if(DIREC_LET_SVP.equals("Otros")){
                direccion = (direccion +" "+ editDIREC_LET_SVP_OTRO.getText().toString()).trim();
            }else{
                direccion = (direccion +" "+ DIREC_LET_SVP).trim();
            }
        }
        if(Util.stringNullEmptys(DIREC_CUAD_VP) && !DIREC_CUAD_VP.contains("1")){
            direccion = (direccion +" "+ DIREC_CUAD_VP).trim();
        }
        if(Util.stringNullEmptys(editDIREC_NUM_VG.getText().toString())){
            direccion = (direccion +" "+ editDIREC_NUM_VG.getText().toString()).trim();
        }
        if(Util.stringNullEmptys(DIREC_LET_VG)){
            if(DIREC_LET_VG.equals("Otros")){
                direccion = (direccion +" "+ editDIREC_LET_VG_OTRO.getText().toString()).trim();
            }else{
                direccion = (direccion +" "+ DIREC_LET_VG).trim();
            }
        }
        if(Util.stringNullEmptys(DIREC_SF_VG) && !DIREC_SF_VG.contains("2")){
            direccion = (direccion +" "+ DIREC_SF_VG).trim();
        }
        if(Util.stringNullEmptys(DIREC_LET_SVG)){
            if(DIREC_LET_SVG.equals("Otros")){
                direccion = (direccion +" "+ editDIREC_LET_SVG_OTRO.getText().toString()).trim();
            }else{
                direccion = (direccion +" "+ DIREC_LET_SVG).trim();
            }
        }
        if(Util.stringNullEmptys(editDIREC_NUM_PLACA.getText().toString())){
            direccion = (direccion +" "+ editDIREC_NUM_PLACA.getText().toString()).trim();
        }
        if(Util.stringNullEmptys(DIREC_CUAD_VG) && !DIREC_CUAD_VG.contains("1")){
            direccion = (direccion +" "+ DIREC_CUAD_VG).trim();
        }

        if(Util.stringNullEmptys(DIREC_P_TIPO) && DIREC_P_TIPO.equals("1") ){
            if(Util.stringNullEmptys(DIREC_P_COMP) && DIREC_P_COMP.contains("1")){
                for (ComplementoNormalizadorDTO lista : lstComplemento) {
                    direccion = (direccion +" "+ lista.getDirecComplemento()).trim();
                }
            }
        }else{
            for (ComplementoNormalizadorDTO lista : lstComplemento) {
                direccion = (direccion +" "+ lista.getDirecComplemento()).trim();
            }
        }

        direccion = direccion.replace("(","").replace(")","");
        if(Util.stringNullEmptys(direccion)){
            linearDireccion.setVisibility(View.VISIBLE);
            editDireccion.setText(direccion);
        }else{
            linearDireccion.setVisibility(View.GONE);
        }
        editDireccionCompleta.setText(direccion);

        return direccion;
    }


    /**
     * Metodo para borrar el complemnto
     *
     * @param position
     */
    public void showDialogBorrarComplemneto(final int position ){
        ComplementoNormalizadorDTO complemento = lstComplemento.get(position);
        lstComplemento.remove(complemento);
        mAdapterComplemento.notifyDataSetChanged();
        msj.dialogoMensaje("Success", "Se ha eliminado el complemento.");
        concatDireccion();
    }

    /**
     * Metodo que carga el editar
     * @param position
     */
    public void showDialogEditarComplemneto(final int position ){
        ComplementoNormalizadorDTO complementoEditar = lstComplemento.get(position);
        spinnerDIREC_COMP.setText(getIndexAutoComplet(Arrays.asList(getResources().getStringArray(R.array.complemento)), complementoEditar.getDirecComp()),false);
        editDIREC_TEX_COM.setText(complementoEditar.getDirecTextComp());
        DIREC_COMP = Util.getLimpiarAcronimoDireccion(complementoEditar.getDirecComp());
        tv_adicionarComplemento.setText(getString(R.string.text_pregunta6_3_5_guardar));
        isEditarComplemento = true;
        complemento = complementoEditar;

    }


}
