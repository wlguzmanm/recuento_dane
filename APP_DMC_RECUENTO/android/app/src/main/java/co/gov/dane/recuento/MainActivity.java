package co.gov.dane.recuento;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;



import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.google.android.material.navigation.NavigationView;


import co.gov.dane.recuento.Helper.RetrofitClientInstance;
import co.gov.dane.recuento.Preguntas.Edificacion;
import co.gov.dane.recuento.Preguntas.Manzana;
import co.gov.dane.recuento.Preguntas.UnidadEconomica;
import co.gov.dane.recuento.adapter.ConteoAdapterManzana;
import co.gov.dane.recuento.backend.Database;
import co.gov.dane.recuento.backend.EsquemaMapa;
import co.gov.dane.recuento.dtos.ComplementoNormalizadorDTO;
import co.gov.dane.recuento.dtos.NormalizadorDireccionDTO;
import co.gov.dane.recuento.interfaces.IAuthentication;
import co.gov.dane.recuento.model.ConteoManzana;
import co.gov.dane.recuento.model.EnvioFormularioViewModel;
import co.gov.dane.recuento.model.EsquemaEdificacionEnvioViewModel;
import co.gov.dane.recuento.model.EsquemaManzanaEnvioViewModel;
import co.gov.dane.recuento.model.EsquemaUnidadesEnvioViewModel;
import co.gov.dane.recuento.model.Offline;
import co.gov.dane.recuento.model.ResponseFile;
import ir.mahdi.mzip.zip.ZipArchive;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private EditText fecha;
    private LinearLayout linear_subida, adicionar, sincronizar_formulario, cancelar_seleccionar;

    private List<ConteoManzana> formularios = new ArrayList<>();
    private RecyclerView recyclerView;
    private ConteoAdapterManzana mAdapter;
    private CheckBox check_finalizado;
    private Database db;
    private SpatiaLite sp;
    private  AlertDialog dialog ;

    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 101;

    String[] permissions = new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    private Session session;
    private Controlador controlador;

    private int termino_sincronizar = 0;
    private int refrescar = 0;

    public Boolean select_all = false, isCompleto;
    public int cancel_sel = 0;

    private Mensajes msg;
    private EnvioFormularioViewModel envio = null;

    private Offline offline = new Offline();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST_ACCOUNTS);
        } else {

        }
        msg = new Mensajes(MainActivity.this);
        try {
            final String inFileName = Environment.getExternalStorageDirectory() + File.separator + "Censo_Economico"
                    + File.separator + "db" + File.separator + "geom.db";

            File dbFile = new File(inFileName);
            if (dbFile.exists()) {
                FileInputStream fis = new FileInputStream(dbFile);

                String outFileName = "/data/data/co.gov.dane.recuento/databases/geom.db";

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
            }else{
                msg.generarToast("Error en la importación de la base de datos geom.db, no existe la ruta :"+inFileName,"error");
            }
        } catch (Exception e) {
            msg.generarToast("Error en la importación de la base de datos geom.db","error");
        }
        session = new Session(MainActivity.this);
        db = new Database(MainActivity.this);
        sp = new SpatiaLite(MainActivity.this);

        controlador = new Controlador(MainActivity.this);
        cancelar_seleccionar = (LinearLayout) findViewById(R.id.cancelar_seleccionar);
        check_finalizado = (CheckBox) findViewById((R.id.check_finalizado));
        check_finalizado.setChecked(false);
        isCompleto = false;

        linear_subida = (LinearLayout) findViewById(R.id.linear_subida);
        linear_subida.setVisibility(View.GONE);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new ConteoAdapterManzana(formularios, MainActivity.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        prepararDatos();

        check_finalizado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isCompleto = true;
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));
                    formularios.clear();
                    prepararDatos();
                    mAdapter = new ConteoAdapterManzana(formularios, MainActivity.this);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else {
                    isCompleto = false;
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));
                    formularios.clear();
                    prepararDatos();
                    mAdapter = new ConteoAdapterManzana(formularios, MainActivity.this);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        adicionar = (LinearLayout) findViewById(R.id.adicionar);

        adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        offline = db.getOffline("OFFLINE", session.getusename());
                        if(offline!= null && !offline.isActivo()){
                            try{
                                //TODO: Se coloca directo.
                                Intent formulario = new Intent(MainActivity.this, MapsActivityInicial.class);
                                startActivity(formulario);
                                MainActivity.this.finish();

                                //TODO: Asignaciones se quita
                                /*controlador.getAsignaciones(session.getusename(), new VolleyCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        if(!db.getAsignacionManzanaUsuario(session.getusename())){
                                            Mensajes mitoast =new Mensajes(MainActivity.this);
                                            mitoast.generarToast("El usuario no tiene asignaciones de manzanas.","error");
                                        }else{
                                            Intent formulario = new Intent(MainActivity.this, MapsActivityInicial.class);
                                            startActivity(formulario);
                                            MainActivity.this.finish();
                                        }
                                    }
                                });*/
                            }catch (Exception e){
                                Mensajes mitoast =new Mensajes(MainActivity.this);
                                mitoast.generarToast("Ocurrio un error en el servicio de asignaciones. Con error "+e.getMessage(),"error");
                            }
                        }else{
                            Intent formulario = new Intent(MainActivity.this, MapsActivityInicial.class);
                            startActivity(formulario);
                            MainActivity.this.finish();
                        }
                    }
                },1000);
            }
        });

        cancelar_seleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_all = false;
                linear_subida.setVisibility(View.GONE);
                adicionar.setVisibility(View.VISIBLE);
                sincronizar_formulario.setVisibility(View.GONE);
                cancel_sel = 1;
                mAdapter.notifyDataSetChanged();
            }
        });

        sincronizar_formulario = (LinearLayout) findViewById(R.id.sincronizar_formulario);
        sincronizar_formulario.setVisibility(View.GONE);

        SearchView searchView = (SearchView) findViewById(R.id.svSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getList(db.getFormulario(isCompleto, session.getusename()));
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        sincronizar_formulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Manzana> listado_manzanas = new ArrayList<>();
                ConteoAdapterManzana ca = mAdapter;
                for (int j = 0; j < ca.getFormulario().size(); j++) {
                    if (ca.getFormulario().get(j).getEnviar() == 1) {
                        Manzana man = new Manzana(ca.getFormulario().get(j).getId());  //Con la idManzana
                        listado_manzanas.add(man);
                    }
                }

                if (listado_manzanas.size() == 0) {
                    Mensajes mensaje = new Mensajes(MainActivity.this);
                    mensaje.generarToast("Seleccione al menos una manzana", "error");
                } else {
                    if(listado_manzanas.size() > 1){
                        Mensajes mensaje = new Mensajes(MainActivity.this);
                        mensaje.generarToast("Debe seleccionar una manzana para enviar", "error");
                    }else{
                        refrescar = listado_manzanas.size();
                        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                        progressBar.setMax(refrescar);
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(0);

                        envio = getEnvio(listado_manzanas);
                        offline = db.getOffline("OFFLINE", session.getusename());
                        if(offline!= null && !offline.isActivo()){
                            controlador.uploadData(envio, new VolleyCallBack() {
                                @Override
                                public void onSuccess() {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            termino_sincronizar = termino_sincronizar + envio.getEsquemaManzana().size();
                                            progressBar.setProgress(termino_sincronizar);
                                            refresh();
                                        }
                                    }, 1000);

                                }
                            });
                        }else{
                            for (EsquemaManzanaEnvioViewModel lista : envio.getEsquemaManzana()){
                                db.putManzanaPendienteSincronizar(lista.getId_manzana(), "Si");
                            }

                            termino_sincronizar = termino_sincronizar + envio.getEsquemaManzana().size();
                            progressBar.setProgress(termino_sincronizar);
                            refresh();
                        }
                    }
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        Menu menu = navigationView.getMenu();
        MenuItem nav_camara = menu.findItem(R.id.nav_usuario);
        nav_camara.setTitle(session.getnombre());

        MenuItem nav_user = menu.findItem(R.id.nav_user);
        nav_user.setTitle(session.getusename());
    }


    /**
     * Metodo que carga la info de envio
     *
     * @param listado_manzanas
     * @return
     */
    private EnvioFormularioViewModel getEnvio(List<Manzana> listado_manzanas) {
        envio = new EnvioFormularioViewModel();
        envio.setFechaInicial(getFechaActual());

        List<EsquemaManzanaEnvioViewModel> lstManzana = new ArrayList<>();

        for (Manzana manzana : listado_manzanas) {
            Manzana mz = db.getManzana(manzana.getId_manzana());
            EsquemaManzanaEnvioViewModel retornoManzana = new EsquemaManzanaEnvioViewModel();
            retornoManzana  = getManzana(mz);
            retornoManzana.setCod_enumerador(session.getusename());

            List<Edificacion> listado_edificaciones = db.getAllEdificaciones(manzana.getId_manzana());

            List<EsquemaEdificacionEnvioViewModel> retornoEdificacion = new ArrayList<>();
            for (Edificacion edificacion : listado_edificaciones) {
                EsquemaEdificacionEnvioViewModel objetoEdifi = new EsquemaEdificacionEnvioViewModel();
                objetoEdifi = getEdificacion(edificacion);

                List<UnidadEconomica> listado_unidades = db.getAllUnidades(manzana.getId_manzana(),edificacion.getId_edificacion());
                List<EsquemaUnidadesEnvioViewModel> lstUnidadEconomica = new ArrayList<>();
                for (UnidadEconomica unidadEconomica : listado_unidades) {
                    EsquemaUnidadesEnvioViewModel objetoUnidad = new EsquemaUnidadesEnvioViewModel();
                    UnidadEconomica unidad = db.getUnidadEconomica(manzana.getId_manzana(), edificacion.getId_edificacion(),unidadEconomica.getId_unidad());
                    objetoUnidad = getUnidadEconomica(unidad);
                    NormalizadorDireccionDTO normalizador = db.getNormalizador(manzana.getId_manzana(), edificacion.getId_edificacion(), unidadEconomica.getId_unidad());

                    if(normalizador!= null && normalizador.getId()!= null){
                        List<ComplementoNormalizadorDTO> complementos = db.getComplementoNormalizador(normalizador.getId());
                        normalizador.setComplememnto(complementos);
                    }

                    objetoUnidad.setDireccionNormalizada(normalizador);
                    lstUnidadEconomica.add(objetoUnidad);
                }
                objetoEdifi.setUnidades(lstUnidadEconomica);
                retornoEdificacion.add(objetoEdifi);
            }
            retornoManzana.setEsquemaEdificacion(retornoEdificacion);
            lstManzana.add(retornoManzana);

        }
        envio.setEsquemaManzana(lstManzana);
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(envio);

        return envio;
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
     * Metodo que devuelve la fecha
     * @return
     */
    public String getFechaActual(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_resumen) {
            offline = db.getOffline("OFFLINE", session.getusename());
            if(offline!= null && !offline.isActivo()){
                resumenPdf();
            }else{
                msg.generarToast("Aplicación esta en modo Fuera de Linea y esta funcionalidad necesita estan en linea para la conexión con Internet");
            }
        } else if (id == R.id.nav_mapa) {
            mapas();
        } else if (id == R.id.nav_backup) {
            backup_db();
        } else if (id == R.id.nav_restore) {
            restore_backup_db();
        } else if (id == R.id.nav_offline) {
            offline();
        } else if (id == R.id.nav_acerca) {
            msg.generarToast("Aplicación Desarrollada por la coordinación de Sistema del DANE");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void refresh() {
        if (refrescar == termino_sincronizar) {
            finish();
            startActivity(getIntent());


            select_all = false;
            linear_subida.setVisibility(View.GONE);
            adicionar.setVisibility(View.VISIBLE);
            sincronizar_formulario.setVisibility(View.GONE);
            cancel_sel = 1;
            mAdapter.notifyDataSetChanged();
        }

    }

    private void prepararDatos() {
        formularios.addAll(db.getFormulario(isCompleto,session.getusename()));
        mAdapter.notifyDataSetChanged();
    }

    public boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (hasAllPermissionsGranted(grantResults)) {
        } else {
            Mensajes mensaje = new Mensajes(this);
            mensaje.generarToast("Debe aceptar todos los permisos!");
        }
    }

    /**
     * Metodo que genera el reporte
     */
    public void resumenPdf(){
        Mensajes mensaje = new Mensajes(this);
        envio = new EnvioFormularioViewModel();
        envio = getEnvio(db.getListadofinalizados());

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Reporte PDF")
                .setCancelable(false)
                .setMessage("¿Desea generar el reporte? (Se descargará el archivo respectivo. Es necesario tener instalado un lector de archivos PDF)")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(envio);
                        IAuthentication service = RetrofitClientInstance.getRetrofitInstance().create(IAuthentication.class);
                        Call<ResponseFile> call = service.descargarReporte("application/json;charset=UTF-8","Bearer  "+session.getToken(), envio);
                        call.enqueue(new Callback<ResponseFile>() {
                            @Override
                            public void onResponse(Call<ResponseFile> call, retrofit2.Response<ResponseFile> response) {
                                if (response.isSuccessful()){
                                    String writtenToDisk = writeResponseBodyToDisk(response.body());
                                    if(writtenToDisk!= null){
                                        new AlertDialog.Builder(MainActivity.this)
                                                .setTitle("Generación PDF")
                                                .setCancelable(false)
                                                .setMessage("Se generó el reporte en la siguiente ruta : "+writtenToDisk)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                    }
                                                })
                                                .show();
                                    }else{
                                        mensaje.generarToast("Error en la generación del reporte en la DMC","error");
                                    }
                                }else{
                                    mensaje.generarToast("Error en el servidor al descargar el reporte","error");
                                }
                            }
                            @Override
                            public void onFailure(Call<ResponseFile> call, Throwable t) {
                                mensaje.generarToast("Error en el servidor al descargar el archivo","error");
                            }
                        });
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }


    public void offline() {
        Intent activity = new Intent(MainActivity.this, ConfiguracionLocal.class);
        startActivity(activity);
        MainActivity.this.finish();
    }


    public void restore_backup_db(){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        AlertDialog.Builder mBuilder =new AlertDialog.Builder(this);
        final View mView =inflater.inflate(R.layout.dialog_restore_back_up,null);
        mBuilder.setView(mView);
        dialog = mBuilder.create();

        ArrayList<RestoreBackup> restores=new ArrayList<>();
        ArrayList<RestoreBackup> listado_restore = new ArrayList<>();
        String ruta = Environment.getExternalStorageDirectory() + File.separator + "Censo_Economico"+ File.separator + "backup";
        File dir = new File(ruta);
        if(dir.exists()){
            String[] files = dir.list(
                    new FilenameFilter()
                    {
                        public boolean accept(File dir, String name)
                        {
                            return name.endsWith(".zip");
                        }
                    });
            for (String s: files) {
                String nombre=s.split("\\.")[0];
                listado_restore.add(new RestoreBackup(0,nombre,ruta+"/"+s,false));
            }
        }

        if(listado_restore.size() > 0){
            RestoreBackupAdapter listAdapter = new RestoreBackupAdapter(this, listado_restore);
            ListView listView=(ListView)mView.findViewById(R.id.listview_restore);
            listView.setAdapter(listAdapter);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            listAdapter.notifyDataSetChanged();
            Button btn_cerrar= (Button) mView.findViewById(R.id.cerrar_dialog_restore);
            btn_cerrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }else{
            Mensajes mensaje = new Mensajes(this);
            mensaje.generarToast("Por favor generar un backup en la aplicación.", "error");
        }
    }

    public void mapas() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        AlertDialog.Builder mBuilder =new AlertDialog.Builder(this);
        final View mView =inflater.inflate(R.layout.dialog_mapa_offline,null);
        mBuilder.setView(mView);
        final AlertDialog dialog =mBuilder.create();

        ArrayList<MapaOffline> mapas=new ArrayList<>();
        //Mapa base del aplciativo
        ArrayList<MapaOffline> listado_mapas_offline = new ArrayList<>();
        String ruta_mbtiles = Environment.getExternalStorageDirectory() + File.separator + "Censo_Economico"+ File.separator+"mbtiles";
        File dir = new File(ruta_mbtiles);
        if(dir.exists()){
            String[] files = dir.list(
                    new FilenameFilter()
                    {
                        public boolean accept(File dir, String name)
                        {
                            return name.endsWith(".mbtiles");
                        }
                    });
            for (String s: files) {
                String nombre_capa=s.split("\\.")[0];
                guardarMapas(nombre_capa, ruta_mbtiles+"/"+s);
                listado_mapas_offline.add(new MapaOffline(0,nombre_capa,ruta_mbtiles+"/"+s,true));
            }
        }
        limpiarNoexistentes(listado_mapas_offline);
        mapas = getMapaOffline();

        MapaOfflineAdapter listAdapter = new MapaOfflineAdapter(this, mapas);
        ListView listView=(ListView)mView.findViewById(R.id.listview_mapa_offline);
        listView.setAdapter(listAdapter);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Button btn_cerrar= (Button) mView.findViewById(R.id.cerrar_dialog_mapa_offline);
        btn_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void backup_db() {
        String path_backup = Environment.getExternalStorageDirectory() + File.separator + "Censo_Economico"
                + File.separator + "backup" + File.separator;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            final String inFileName = MainActivity.this.getDatabasePath(Database.DATABASE_NAME).getPath();

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);
            String salida = currentDateandTime + ".db";
            String outFileName = path_backup + salida;
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

            ZipArchive zipArchive = new ZipArchive();
            zipArchive.zip(outFileName, path_backup + currentDateandTime + ".zip", "bf81f34965eddf8f3c291848ae64015f");

            File file = new File(outFileName);
            boolean deleted = file.delete();

            Mensajes mensaje = new Mensajes(this);
            mensaje.generarToast("Backup Realizado");

        } catch (IOException e) {
            Mensajes mensaje = new Mensajes(this);
            mensaje.generarToast("Fallo al hacer Backup", "error");
            e.printStackTrace();
        }
    }

    public void verMensaje() {
        linear_subida.setVisibility(View.VISIBLE);
        adicionar.setVisibility(View.GONE);
        sincronizar_formulario.setVisibility(View.VISIBLE);
        final CheckBox check_all = (CheckBox) findViewById(R.id.check_all);

        check_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    select_all = true;
                    mAdapter.notifyDataSetChanged();
                    check_all.setText("Quitar selección");
                } else {
                    select_all = false;
                    mAdapter.notifyDataSetChanged();
                    check_all.setText("Seleccionar Todo");
                }
            }
        });
    }

    /**
     * Metodo que escribe un reporte en el archivo
     *
     * @param body
     * @return
     */
    private String writeResponseBodyToDisk(ResponseFile body) {
        String rutaFile = "Censo_Economico"
                + File.separator+"reportes" + File.separator  + body.getName() + body.getExtension();
        try {
            File file = new File( Environment.getExternalStorageDirectory() + File.separator +rutaFile);
            FileOutputStream fos = new FileOutputStream(file);
            try {
                if (body.getArchivo() != null) {
                     byte[] decodedString = null;
                    if(body.getValidar()){
                        decodedString =  android.util.Base64.decode(getReporteConstrucccion(), android.util.Base64.DEFAULT);
                    }else{
                        decodedString =  android.util.Base64.decode(body.getArchivo(), android.util.Base64.DEFAULT);
                    }
                    fos.write(decodedString);
                    fos.flush();
                    fos.close();
                    return rutaFile;
                }else{
                    return null;
                }
            } catch (Exception e) {
                Log.d("Error en la generación del reporte ",e.getMessage());
                return null;
            } finally {
                if (fos != null) {
                    fos = null;
                }
            }
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Metodo que tiene un reporte en construccion
     *
     * @return
     */
    private String getReporteConstrucccion(){
        StringBuffer archivoBase64 = new StringBuffer();
        archivoBase64.append("JVBERi0xLjcNCiW1tbW1DQoxIDAgb2JqDQo8PC9UeXBlL0NhdGFsb2cvUGFnZXMgMiAwIFIvTGFuZyhlcy1DTykgL1N0cnVjdFRyZWVSb290IDE1IDAgUi9NYXJrSW5mbzw8L01hcmtlZCB0cnVlPj4vTWV0YWRhdGEgMzEgMCBSL1ZpZXdlclByZWZlcmVuY2VzIDMyIDAgUj4+DQplbmRvYmoNCjIgMCBvYmoNCjw8L1R5cGUvUGFnZXMvQ291bnQgMS9LaWRzWyAzIDAgUl0gPj4NCmVuZG9iag0KMyAwIG9iag0KPDwvVHlwZS9QYWdlL1BhcmVudCAyIDAgUi9SZXNvdXJjZXM8PC9Gb250PDwvRjEgNSAwIFIvRjIgOSAwIFI+Pi9FeHRHU3RhdGU8PC9HUzcgNyAwIFIvR1M4IDggMCBSPj4vWE9iamVjdDw8L0ltYWdlMTEgMTEgMCBSL0ltYWdlMTMgMTMgMCBSPj4vUHJvY1NldFsvUERGL1RleHQvSW1hZ2VCL0ltYWdlQy9JbWFnZUldID4+L01lZGlhQm94WyAwIDAgNjEyIDc5Ml0gL0NvbnRlbnRzIDQgMCBSL0dyb3VwPDwvVHlwZS9Hcm91cC9TL1RyYW5zcGFyZW5jeS9DUy9EZXZpY2VSR0I+Pi9UYWJzL1MvU3RydWN0UGFyZW50cyAwPj4NCmVuZG9iag0KNCAwIG9iag0KPDwvRmlsdGVyL0ZsYXRlRGVjb2RlL0xlbmd0aCAzMTk+Pg0Kc3RyZWFtDQp4nKWS3WoCMRCF7wN5h3OZFIyZZLO7AfHC1YoFoT8LvSi9KGKlF2qr9sn7Ak3SFXehpdgu7M+cmcl3NhP0rzEY9OfVbAw9HGI0rvDGmVY6Xp4MNPLwLLzBbsnZ/QU2nI1qzvqXBCKlM9TPnFGo0yBktlDehhavyKFeh7rpXYHVPqyJVYrKJppy9iAgH1FfcTYJK95whsm8Alqe6ExPBlR2DBmyKi/gvFHeJ0PJR4O/lVYsZc+KV5mJrXRiJ3uZOATJCKTEJiRQRTWmY7Q/hCIn3sPnIkjxfpG5+JC9MuQ7v/Nn19aXSuc/uf5t08x/Ny2LgzWweZnmeybenvBUZOksGAeTW+U8SJPyLQeNnLgp1WpZxOMzWz+tlkQYb/EdLDvBjNaqbFybMHitrDu+WsAvIQKPJZ2+FtS2oZ8hsKDdDQplbmRzdHJlYW0NCmVuZG9iag0KNSAwIG9iag0KPDwvVHlwZS9Gb250L1N1YnR5cGUvVHJ1ZVR5cGUvTmFtZS9GMS9CYXNlRm9udC9CQ0RFRUUrQ2FsaWJyaS9FbmNvZGluZy9XaW5BbnNpRW5jb2RpbmcvRm9udERlc2NyaXB0b3IgNiAwIFIvRmlyc3RDaGFyIDMyL0xhc3RDaGFyIDMyL1dpZHRocyAyNyAwIFI+Pg0KZW5kb2JqDQo2IDAgb2JqDQo8PC9UeXBlL0ZvbnREZXNjcmlwdG9yL0ZvbnROYW1lL0JDREVFRStDYWxpYnJpL0ZsYWdzIDMyL0l0YWxpY0FuZ2xlIDAvQXNjZW50IDc1MC9EZXNjZW50IC0yNTAvQ2FwSGVpZ2h0IDc1MC9BdmdXaWR0aCA1MjEvTWF4V2lkdGggMTc0My9Gb250V2VpZ2h0IDQwMC9YSGVpZ2h0IDI1MC9TdGVtViA1Mi9Gb250QkJveFsgLTUwMyAtMjUwIDEyNDAgNzUwXSAvRm9udEZpbGUyIDI4IDAgUj4+DQplbmRvYmoNCjcgMCBvYmoNCjw8L1R5cGUvRXh0R1N0YXRlL0JNL05vcm1hbC9jYSAxPj4NCmVuZG9iag0KOCAwIG9iag0KPDwvVHlwZS9FeHRHU3RhdGUvQk0vTm9ybWFsL0NBIDE+Pg0KZW5kb2JqDQo5IDAgb2JqDQo8PC9UeXBlL0ZvbnQvU3VidHlwZS9UcnVlVHlwZS9OYW1lL0YyL0Jhc2VGb250L0JDREZFRStDYWxpYnJpLUJvbGQvRW5jb2RpbmcvV2luQW5zaUVuY29kaW5nL0ZvbnREZXNjcmlwdG9yIDEwIDAgUi9GaXJzdENoYXIgMzIvTGFzdENoYXIgMjQzL1dpZHRocyAyOSAwIFI+Pg0KZW5kb2JqDQoxMCAwIG9iag0KPDwvVHlwZS9Gb250RGVzY3JpcHRvci9Gb250TmFtZS9CQ0RGRUUrQ2FsaWJyaS1Cb2xkL0ZsYWdzIDMyL0l0YWxpY0FuZ2xlIDAvQXNjZW50IDc1MC9EZXNjZW50IC0yNTAvQ2FwSGVpZ2h0IDc1MC9BdmdXaWR0aCA1MzYvTWF4V2lkdGggMTc4MS9Gb250V2VpZ2h0IDcwMC9YSGVpZ2h0IDI1MC9TdGVtViA1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");
        archivoBase64.append("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dWJ0eXBlL1hNTC9MZW5ndGggMzA3OD4+DQpzdHJlYW0NCjw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+PHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iMy4xLTcwMSI+CjxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+CjxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiICB4bWxuczpwZGY9Imh0dHA6Ly9ucy5hZG9iZS5jb20vcGRmLzEuMy8iPgo8cGRmOlByb2R1Y2VyPk1pY3Jvc29mdMKuIFdvcmQgMjAxOTwvcGRmOlByb2R1Y2VyPjwvcmRmOkRlc2NyaXB0aW9uPgo8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiAgeG1sbnM6ZGM9Imh0dHA6Ly9wdXJsLm9yZy9kYy9lbGVtZW50cy8xLjEvIj4KPGRjOmNyZWF0b3I+PHJkZjpTZXE+PHJkZjpsaT5NYXJjbyBUdWxpbyBHdXptw6FuIE1hcnRpbmV6PC9yZGY6bGk+PC9yZGY6U2VxPjwvZGM6Y3JlYXRvcj48L3JkZjpEZXNjcmlwdGlvbj4KPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyI+Cjx4bXA6Q3JlYXRvclRvb2w+TWljcm9zb2Z0wq4gV29yZCAyMDE5PC94bXA6Q3JlYXRvclRvb2w+PHhtcDpDcmVhdGVEYXRlPjIwMjAtMTAtMjJUMTA6NDA6MDMtMDU6MDA8L3htcDpDcmVhdGVEYXRlPjx4bXA6TW9kaWZ5RGF0ZT4yMDIwLTEwLTIyVDEwOjQwOjAzLTA1OjAwPC94bXA6TW9kaWZ5RGF0ZT48L3JkZjpEZXNjcmlwdGlvbj4KPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIj4KPHhtcE1NOkRvY3VtZW50SUQ+dXVpZDo2NzYyOTlFNy02NUI1LTRBNTUtOTczNy1CODRCMzZBREY1NTE8L3htcE1NOkRvY3VtZW50SUQ+PHhtcE1NOkluc3RhbmNlSUQ+dXVpZDo2NzYyOTlFNy02NUI1LTRBNTUtOTczNy1CODRCMzZBREY1NTE8L3htcE1NOkluc3RhbmNlSUQ+PC9yZGY6RGVzY3JpcHRpb24+CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAo8L3JkZjpSREY+PC94OnhtcG1ldGE+PD94cGFja2V0IGVuZD0idyI/Pg0KZW5kc3RyZWFtDQplbmRvYmoNCjMyIDAgb2JqDQo8PC9EaXNwbGF5RG9jVGl0bGUgdHJ1ZT4+DQplbmRvYmoNCjMzIDAgb2JqDQo8PC9UeXBlL1hSZWYvU2l6ZSAzMy9XWyAxIDQgMl0gL1Jvb3QgMSAwIFIvSW5mbyAxNCAwIFIvSURbPEU3OTk2MjY3QjU2NTU1NEE5NzM3Qjg0QjM2QURGNTUxPjxFNzk5NjI2N0I1NjU1NTRBOTczN0I4NEIzNkFERjU1MT5dIC9GaWx0ZXIvRmxhdGVEZWNvZGUvTGVuZ3RoIDEyND4+DQpzdHJlYW0NCnicY2AAgv//GYGkIAMDiFoGoe6BKSZJMMW8CEyxeIIpVgsIlQuhIHJsAWCK3RVMqT8GU47HwVThaqBJQDPFGNggFDuE4oBQnBCKFUJBVXIB9RXthPEYIRQThGKGUCxAJSXfwDaUQhx/UBNMHWYBUYxKe8GUPtBMAArVEQ0NCmVuZHN0cmVhbQ0KZW5kb2JqDQp4cmVmDQowIDM0DQowMDAwMDAwMDE1IDY1NTM1IGYNCjAwMDAwMDAwMTcgMDAwMDAgbg0KMDAwMDAwMDE2NiAwMDAwMCBuDQowMDAwMDAwMjIyIDAwMDAwIG4NCjAwMDAwMDA1MzcgMDAwMDAgbg0KMDAwMDAwMDkzMCAwMDAwMCBuDQowMDAwMDAxMDk3IDAwMDAwIG4NCjAwMDAwMDEzMzYgMDAwMDAgbg0KMDAwMDAwMTM4OSAwMDAwMCBuDQowMDAwMDAxNDQyIDAwMDAwIG4NCjAwMDAwMDE2MTYgMDAwMDAgbg0KMDAwMDAwMTg2MSAwMDAwMCBuDQowMDAwMDEwMjExIDAwMDAwIG4NCjAwMDAwMTY4MzkgMDAwMDAgbg0KMDAwMDAyOTA5OSAwMDAwMCBuDQowMDAwMDAwMDE2IDY1NTM1IGYNCjAwMDAwMDAwMTcgNjU1MzUgZg0KMDAwMDAwMDAxOCA2NTUzNSBmDQowMDAwMDAwMDE5IDY1NTM1IGYNCjAwMDAwMDAwMjAgNjU1MzUgZg0KMDAwMDAwMDAyMSA2NTUzNSBmDQowMDAwMDAwMDIyIDY1NTM1IGYNCjAwMDAwMDAwMjMgNjU1MzUgZg0KMDAwMDAwMDAyNCA2NTUzNSBmDQowMDAwMDAwMDI1IDY1NTM1IGYNCjAwMDAwMDAwMjYgNjU1MzUgZg0KMDAwMDAwMDAwMCA2NTUzNSBmDQowMDAwMDI5OTQyIDAwMDAwIG4NCjAwMDAwMjk5NjkgMDAwMDAgbg0KMDAwMDA0OTQ0OSAwMDAwMCBuDQowMDAwMDQ5OTI0IDAwMDAwIG4NCjAwMDAwNzQ0MjkgMDAwMDAgbg0KMDAwMDA3NzU5MCAwMDAwMCBuDQowMDAwMDc3NjM1IDAwMDAwIG4NCnRyYWlsZXINCjw8L1NpemUgMzQvUm9vdCAxIDAgUi9JbmZvIDE0IDAgUi9JRFs8RTc5OTYyNjdCNTY1NTU0QTk3MzdCODRCMzZBREY1NTE+PEU3OTk2MjY3QjU2NTU1NEE5NzM3Qjg0QjM2QURGNTUxPl0gPj4NCnN0YXJ0eHJlZg0KNzc5NjANCiUlRU9GDQp4cmVmDQowIDANCnRyYWlsZXINCjw8L1NpemUgMzQvUm9vdCAxIDAgUi9JbmZvIDE0IDAgUi9JRFs8RTc5OTYyNjdCNTY1NTU0QTk3MzdCODRCMzZBREY1NTE+PEU3OTk2MjY3QjU2NTU1NEE5NzM3Qjg0QjM2QURGNTUxPl0gL1ByZXYgNzc5NjAvWFJlZlN0bSA3NzYzNT4+DQpzdGFydHhyZWYNCjc4Nzk3DQolJUVPRg==");
        return archivoBase64.toString();
    }


    /**
     * Metodo que inserta en la tabla mapas y actualiza si existe
     *
     * @param nombre
     * @param ruta
     */
    private void guardarMapas(String nombre, String ruta){
        try {
            if(!validarExistenciaMapa(ruta)){
                Database db=new Database(MainActivity.this);
                SQLiteDatabase sp=db.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(EsquemaMapa.NOMBRE, nombre);
                values.put(EsquemaMapa.RUTA, ruta);
                values.put(EsquemaMapa.ACTIVO, 0);

                sp.insert(EsquemaMapa.TABLE_NAME, null, values);
                sp.close();
            }
        }catch (Exception e){
        }
    }

    /***
     * metodo que valida la exitencia del mapa
     *
     * @param ruta
     * @return
     */
    private Boolean validarExistenciaMapa(String ruta){
        try{
            Boolean existe=false;
            String whereClause = EsquemaMapa.RUTA+" = ? ";
            String[] whereArgs = new String[] {
                    ruta
            };
            Database db=new Database(MainActivity.this);
            SQLiteDatabase sp=db.getWritableDatabase();

            Cursor c = sp.query(
                    EsquemaMapa.TABLE_NAME,  // Nombre de la tabla
                    null,  // Lista de Columnas a consultar
                    whereClause,  // Columnas para la cláusula WHERE
                    whereArgs,  // Valores a comparar con las columnas del WHERE
                    null,  // Agrupar con GROUP BY
                    null,  // Condición HAVING para GROUP BY
                    null  // Cláusula ORDER BY
            );
            int count = c.getCount();

            if(count>=1){
                existe=true;
            }
            return existe;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * Metodo que carga los mapas
     *
     * @return
     */
    private ArrayList<MapaOffline> getMapaOffline(){
        ArrayList<MapaOffline> mapas  = new ArrayList<>();
        try{
            Database db=new Database(MainActivity.this);
            SQLiteDatabase sp=db.getWritableDatabase();
            Cursor c = sp.query(
                    EsquemaMapa.TABLE_NAME,  // Nombre de la tabla
                    null,  // Lista de Columnas a consultar
                    null,  // Columnas para la cláusula WHERE
                    null,  // Valores a comparar con las columnas del WHERE
                    null,  // Agrupar con GROUP BY
                    null,  // Condición HAVING para GROUP BY
                    null  // Cláusula ORDER BY
            );
            while (c.moveToNext()) {
                int index;

                index = c.getColumnIndexOrThrow(EsquemaMapa.ID);
                int id = c.getInt(index);

                index = c.getColumnIndexOrThrow(EsquemaMapa.NOMBRE);
                String nombre = c.getString(index);

                index = c.getColumnIndexOrThrow(EsquemaMapa.RUTA);
                String ruta = c.getString(index);

                index = c.getColumnIndexOrThrow(EsquemaMapa.ACTIVO);
                Boolean activo = ((c.getString(index) != null && c.getString(index).equals("1") ) ?  true  :  false );

                mapas.add(new MapaOffline(id, nombre, ruta, activo));
            }
            db.close();
            return mapas;
        }catch (Exception e){
            ArrayList<MapaOffline> retorno=new ArrayList<>();
            return retorno;
        }
    }

    /**
     * Metodo que limpia los no existentes.
     *
     * @param listado_mapas_offline
     */
    private void limpiarNoexistentes(ArrayList<MapaOffline> listado_mapas_offline) {
        try{
            ArrayList<MapaOffline> all =  getMapaOffline();
            for(MapaOffline todos : all){
                Boolean encontro = false;
                for (MapaOffline lista : listado_mapas_offline){
                    if(todos.getRuta().equals(lista.getRuta())){
                        encontro= true;
                    }
                }
                if(!encontro){
                    eliminarmapa(todos.getRuta());
                }
            }
        }catch (Exception e){
        }
    }

    /**
     * Metodo que elimina un registro
     *
     * @param ruta
     */
    private void eliminarmapa(String ruta) {
        try{
            Database db=new Database(MainActivity.this);
            SQLiteDatabase sp=db.getWritableDatabase();

            String table = EsquemaMapa.TABLE_NAME;
            String whereClause = EsquemaMapa.RUTA+"=?";
            String[] whereArgs = new String[] { String.valueOf(ruta) };
            sp.delete(table, whereClause, whereArgs);

            sp.close();
        }catch (Exception e){
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

}
