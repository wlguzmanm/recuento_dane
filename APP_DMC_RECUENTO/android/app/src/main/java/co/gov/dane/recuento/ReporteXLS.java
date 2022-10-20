package co.gov.dane.recuento;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;
import org.apache.poi.hssf.usermodel.HSSFTextbox;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import co.gov.dane.recuento.backend.Database;
import co.gov.dane.recuento.model.EnvioFormularioViewModel;
import co.gov.dane.recuento.model.EsquemaEdificacionEnvioViewModel;
import co.gov.dane.recuento.model.EsquemaManzanaEnvioViewModel;
import co.gov.dane.recuento.model.EsquemaUnidadesEnvioViewModel;

public class ReporteXLS {

    InputStream input;
    Context context;
    String outPath;
    Workbook wb;
    Sheet sh;
    EnvioFormularioViewModel envio;
    Database db;
    final public HashMap<Integer, Integer> departamentoNombre = createMap(9, 9);
    final public HashMap<Integer, Integer> departamentoCodigo1 = createMap(9, 18);
    final public HashMap<Integer, Integer> departamentoCodigo2 = createMap(9, 19);
    final public HashMap<Integer, Integer> municipioNombre = createMap(9, 26);
    final public HashMap<Integer, Integer> municipioCodigo1 = createMap(9, 37);
    final public HashMap<Integer, Integer> municipioCodigo2 = createMap(9, 38);
    final public HashMap<Integer, Integer> municipioCodigo3 = createMap(9, 39);
    final public HashMap<Integer, Integer> claseCodigo = createMap(9, 45);
    final public HashMap<Integer, Integer> cpobCodigo1 = createMap(9, 53);
    final public HashMap<Integer, Integer> cpobCodigo2 = createMap(9, 54);
    final public HashMap<Integer, Integer> cpobCodigo3 = createMap(9, 55);
    final public HashMap<Integer, Integer> acCodigo1 = createMap(15, 11);
    final public HashMap<Integer, Integer> acCodigo2 = createMap(15, 12);
    final public HashMap<Integer, Integer> acCodigo3 = createMap(15, 13);
    final public HashMap<Integer, Integer> aoCodigo1 = createMap(15, 24);
    final public HashMap<Integer, Integer> aoCodigo2 = createMap(15, 25);
    final public HashMap<Integer, Integer> ucCodigo1 = createMap(15, 36);
    final public HashMap<Integer, Integer> ucCodigo2 = createMap(15, 37);
    final public HashMap<Integer, Integer> ucCodigo3 = createMap(15, 38);
    final public HashMap<Integer, Integer> ucCodigo4 = createMap(15, 39);
    final public HashMap<Integer, Integer> ucCodigo5 = createMap(15, 40);
    final public HashMap<Integer, Integer> ucCodigo6 = createMap(15, 41);
    final public HashMap<Integer, Integer> acerCodigo1 = createMap(15, 50);
    final public HashMap<Integer, Integer> acerCodigo2 = createMap(15, 51);
    final public HashMap<Integer, Integer> novedadUnion = createMap(10, 65);
    final public HashMap<Integer, Integer> novedadDivision = createMap(13, 65);
    final public HashMap<Integer, Integer> novedadNueva = createMap(16, 65);
    final public HashMap<Integer, Integer> orden_edificacion = createMap(28, 1);
    final public HashMap<Integer, Integer> orden_unidad = createMap(28, 5);
    final public HashMap<Integer, Integer> direccion = createMap(28, 9);
    final public HashMap<Integer, Integer> estadoOcupado = createMap(28, 20);
    final public HashMap<Integer, Integer> estadoDesocupado = createMap(28, 22);
    final public HashMap<Integer, Integer> estadoObra = createMap(28, 24);
    final public HashMap<Integer, Integer> tipoFijo = createMap(28, 26);
    final public HashMap<Integer, Integer> tipoSemifijo = createMap(28, 28);
    final public HashMap<Integer, Integer> tipoMovil = createMap(28, 30);
    final public HashMap<Integer, Integer> tipoMovilSin = createMap(28, 32);
    final public HashMap<Integer, Integer> tipoVivienda = createMap(28, 34);
    final public HashMap<Integer, Integer> tipoObra = createMap(28, 37);
    final public HashMap<Integer, Integer> sectorComercio = createMap(28, 40);
    final public HashMap<Integer, Integer> sectorIndustria = createMap(28, 42);
    final public HashMap<Integer, Integer> sectorServicio = createMap(28, 44);
    final public HashMap<Integer, Integer> sectorTransporte = createMap(28, 46);
    final public HashMap<Integer, Integer> sectorConstruccion = createMap(28, 48);
    final public HashMap<Integer, Integer> sectorNoaplica = createMap(28, 50);
    final public HashMap<Integer, Integer> nombreUnidad = createMap(28, 52);
    final public HashMap<Integer, Integer> observacionUnidad = createMap(28, 60);
    final public HashMap<Integer, Integer> totalOcupado = createMap(29, 20);
    final public HashMap<Integer, Integer> totalDesocupado = createMap(29, 22);
    final public HashMap<Integer, Integer> totalObra = createMap(29, 24);
    final public HashMap<Integer, Integer> totalFijo = createMap(29, 26);
    final public HashMap<Integer, Integer> totalSemifijo = createMap(29, 28);
    final public HashMap<Integer, Integer> totalPuestoMovil = createMap(29, 30);
    final public HashMap<Integer, Integer> totalSinPuestoMovil = createMap(29, 32);
    final public HashMap<Integer, Integer> totalVivienda = createMap(29, 34);
    final public HashMap<Integer, Integer> totalObraEdificacion = createMap(29, 37);
    final public HashMap<Integer, Integer> totalComercio = createMap(29, 40);
    final public HashMap<Integer, Integer> totalIndustria = createMap(29, 42);
    final public HashMap<Integer, Integer> totalServicio = createMap(29, 44);
    final public HashMap<Integer, Integer> totalTransporte = createMap(29, 46);
    final public HashMap<Integer, Integer> totalConstruccion = createMap(29, 48);
    final public HashMap<Integer, Integer> totalNoAplica = createMap(29, 50);
    final public HashMap<Integer, Integer> total = createMap(30, 52);
    final public HashMap<Integer, Integer> codRecuentista = createMap(30, 60);
    final public HashMap<Integer, Integer> codSupervisor = createMap(32, 60);
    final public HashMap<Integer, Integer> fechaRecuento = createMap(30, 1);
    final public HashMap<Integer, Integer> barrioRecuento = createMap(32, 1);
    final public HashMap<Integer, Integer> observacionRecuento = createMap(32, 20);
    final public HashMap<Integer, Integer> id_dmc = createMap(32, 32);
    final public HashMap<Integer, Integer> longitude = createMap(32, 44);
    final public HashMap<Integer, Integer> latitude = createMap(32, 52);
    final public HashMap<Integer, Integer> barrioRecuentoTitle = createMap(13, 0);
    final public HashMap<Integer, Integer> observacionRecuentoTitle = createMap(13, 4);
    final public HashMap<Integer, Integer> id_dmcTitle = createMap(13, 12);
    final public HashMap<Integer, Integer> longitudeTitle = createMap(13, 17);
    final public HashMap<Integer, Integer> latitudeTitle = createMap(13, 20);

    public ReporteXLS(Context context, EnvioFormularioViewModel envio) {
        this.context = context;
        outPath = Environment.getExternalStorageDirectory() + File.separator + "Censo_Economico" + File.separator + "reportes" + File.separator + "Manzana_prueba.xls";
        this.envio = envio;
        this.db = new Database(this.context);
    }

    public void generateReport() {
        List<EsquemaManzanaEnvioViewModel> manzanas = envio.getEsquemaManzana();

        try {
            if (manzanas.size() > 0) {
                for (EsquemaManzanaEnvioViewModel mz : manzanas) {
                    Boolean existeReporte = existsManzana(mz.getId_manzana());
                    if (!existeReporte) {
                        InputStream result = context.getAssets().open("formato_recuentos_ce_v2.xls");
                        wb = new HSSFWorkbook(result);
                        sh = wb.getSheetAt(0);
                        String nombreDeptoValor = db.getDepartamento(mz.getDpto());
                        String nombreMpioValor = db.getMunicipio(mz.getDpto() + mz.getMpio());
                        llenarCampo(departamentoNombre, nombreDeptoValor, sh);
                        llenarCampo(departamentoCodigo1, mz.getDpto().substring(0, 1), sh);
                        llenarCampo(departamentoCodigo2, mz.getDpto().substring(1, 2), sh);
                        llenarCampo(municipioNombre, nombreMpioValor, sh);
                        llenarCampo(municipioCodigo1, mz.getMpio().substring(0, 1), sh);
                        llenarCampo(municipioCodigo2, mz.getMpio().substring(1, 2), sh);
                        llenarCampo(municipioCodigo3, mz.getMpio().substring(2, 3), sh);
                        llenarCampo(claseCodigo, mz.getClase(), sh);
                        llenarCampo(cpobCodigo1, mz.getC_pob().substring(0, 1), sh);
                        llenarCampo(cpobCodigo2, mz.getC_pob().substring(1, 2), sh);
                        llenarCampo(cpobCodigo3, mz.getC_pob().substring(2, 3), sh);
                        llenarCampo(acCodigo1, mz.getCo().substring(0, 1), sh);
                        llenarCampo(acCodigo2, mz.getCo().substring(1, 2), sh);
                        llenarCampo(acCodigo3, mz.getCo().substring(2, 3), sh);
                        llenarCampo(aoCodigo1, mz.getAo().substring(0, 1), sh);
                        llenarCampo(aoCodigo2, mz.getAo().substring(1, 2), sh);
                        llenarCampo(ucCodigo1, mz.getAg().substring(0, 1), sh);
                        llenarCampo(ucCodigo2, mz.getAg().substring(1, 2), sh);
                        llenarCampo(ucCodigo3, mz.getAg().substring(2, 3), sh);
                        llenarCampo(ucCodigo4, mz.getAg().substring(3, 4), sh);
                        llenarCampo(ucCodigo5, mz.getAg().substring(4, 5), sh);
                        llenarCampo(ucCodigo6, mz.getAg().substring(5, 6), sh);
                        llenarCampo(acerCodigo1, mz.getAcer().substring(0, 1), sh);
                        llenarCampo(acerCodigo2, mz.getAcer().substring(1, 2), sh);
                        if (mz.getNov_carto() != null) {
                            if (mz.getNov_carto().equals("2")) {
                                llenarCampo(novedadDivision, "X", sh);
                            } else if (mz.getNov_carto().equals("3")) {
                                llenarCampo(novedadNueva, "X", sh);
                            } else if (mz.getNov_carto().equals("4")) {
                                llenarCampo(novedadUnion, "X", sh);
                            }
                        }

                        List<EsquemaEdificacionEnvioViewModel> edificaciones = mz.getEsquemaEdificacion();
                        int i = 0;
                        int countUnd = 0;
                        int countRow = 0;
                        int totUnd = 0;
                        int totViviendas = 0;
                        int totHogares = 0;
                        int totPersonas = 0;
                        int totOcupado = 0;
                        int totDesocupado = 0;
                        int totObra = 0;
                        int totFijo = 0;
                        int totSemifijo = 0;
                        int totPuestoMovil = 0;
                        int totSinPuestoMovil = 0;
                        int totVivienda = 0;
                        int totObraEdificacion = 0;
                        int totComercio = 0;
                        int totIndustria = 0;
                        int totServicio = 0;
                        int totTransporte = 0;
                        int totConstruccion = 0;
                        int totNoAplica = 0;
                        if (edificaciones.size() > 0) {
                            for (EsquemaEdificacionEnvioViewModel ed : mz.getEsquemaEdificacion()) {
                                List<EsquemaUnidadesEnvioViewModel> unidades = ed.getUnidades();
                                if (unidades.size() > 0) {
                                    for (EsquemaUnidadesEnvioViewModel ud : unidades) {
                                        i = i + 1;
                                        if (countRow >= 0) {
                                            copyRow(wb, sh, addRow(orden_edificacion, countRow).entrySet().iterator().next().getKey(),
                                                    addRow(orden_edificacion, countRow).entrySet().iterator().next().getKey() + 1);
                                        }
//
                                        if (countRow > 0) {
                                            mergeCells(sh, addRow(orden_edificacion, countRow).entrySet().iterator().next().getKey(), orden_edificacion.entrySet().iterator().next().getValue(), orden_edificacion.entrySet().iterator().next().getValue() + 3);
                                            mergeCells(sh, addRow(orden_unidad, countRow).entrySet().iterator().next().getKey(), orden_unidad.entrySet().iterator().next().getValue(), orden_unidad.entrySet().iterator().next().getValue() + 3);
                                            mergeCells(sh, addRow(direccion, countRow).entrySet().iterator().next().getKey(), direccion.entrySet().iterator().next().getValue(), direccion.entrySet().iterator().next().getValue() + 10);
                                            mergeCells(sh, addRow(estadoOcupado, countRow).entrySet().iterator().next().getKey(), estadoOcupado.entrySet().iterator().next().getValue(), estadoOcupado.entrySet().iterator().next().getValue() + 1);
                                            mergeCells(sh, addRow(estadoDesocupado, countRow).entrySet().iterator().next().getKey(), estadoDesocupado.entrySet().iterator().next().getValue(), estadoDesocupado.entrySet().iterator().next().getValue() + 1);
                                            mergeCells(sh, addRow(estadoObra, countRow).entrySet().iterator().next().getKey(), estadoObra.entrySet().iterator().next().getValue(), estadoObra.entrySet().iterator().next().getValue() + 1);
                                            mergeCells(sh, addRow(tipoFijo, countRow).entrySet().iterator().next().getKey(), tipoFijo.entrySet().iterator().next().getValue(), tipoFijo.entrySet().iterator().next().getValue() + 1);
                                            mergeCells(sh, addRow(tipoSemifijo, countRow).entrySet().iterator().next().getKey(), tipoSemifijo.entrySet().iterator().next().getValue(), tipoSemifijo.entrySet().iterator().next().getValue() + 1);
                                            mergeCells(sh, addRow(tipoMovil, countRow).entrySet().iterator().next().getKey(), tipoMovil.entrySet().iterator().next().getValue(), tipoMovil.entrySet().iterator().next().getValue() + 1);
                                            mergeCells(sh, addRow(tipoMovilSin, countRow).entrySet().iterator().next().getKey(), tipoMovilSin.entrySet().iterator().next().getValue(), tipoMovilSin.entrySet().iterator().next().getValue() + 1);
                                            mergeCells(sh, addRow(tipoVivienda, countRow).entrySet().iterator().next().getKey(), tipoVivienda.entrySet().iterator().next().getValue(), tipoVivienda.entrySet().iterator().next().getValue() + 2);
                                            mergeCells(sh, addRow(tipoObra, countRow).entrySet().iterator().next().getKey(), tipoObra.entrySet().iterator().next().getValue(), tipoObra.entrySet().iterator().next().getValue() + 2);
                                            mergeCells(sh, addRow(sectorComercio, countRow).entrySet().iterator().next().getKey(), sectorComercio.entrySet().iterator().next().getValue(), sectorComercio.entrySet().iterator().next().getValue() + 1);
                                            mergeCells(sh, addRow(sectorIndustria, countRow).entrySet().iterator().next().getKey(), sectorIndustria.entrySet().iterator().next().getValue(), sectorIndustria.entrySet().iterator().next().getValue() + 1);
                                            mergeCells(sh, addRow(sectorServicio, countRow).entrySet().iterator().next().getKey(), sectorServicio.entrySet().iterator().next().getValue(), sectorServicio.entrySet().iterator().next().getValue() + 1);
                                            mergeCells(sh, addRow(sectorTransporte, countRow).entrySet().iterator().next().getKey(), sectorTransporte.entrySet().iterator().next().getValue(), sectorTransporte.entrySet().iterator().next().getValue() + 1);
                                            mergeCells(sh, addRow(sectorConstruccion, countRow).entrySet().iterator().next().getKey(), sectorConstruccion.entrySet().iterator().next().getValue(), sectorConstruccion.entrySet().iterator().next().getValue() + 1);
                                            mergeCells(sh, addRow(sectorNoaplica, countRow).entrySet().iterator().next().getKey(), sectorNoaplica.entrySet().iterator().next().getValue(), sectorNoaplica.entrySet().iterator().next().getValue() + 1);
                                            mergeCells(sh, addRow(nombreUnidad, countRow).entrySet().iterator().next().getKey(), nombreUnidad.entrySet().iterator().next().getValue(), nombreUnidad.entrySet().iterator().next().getValue() + 7);
                                            mergeCells(sh, addRow(observacionUnidad, countRow).entrySet().iterator().next().getKey(), observacionUnidad.entrySet().iterator().next().getValue(), observacionUnidad.entrySet().iterator().next().getValue() + 7);

                                        }

                                        countUnd = countUnd + 1;llenarCampo(addRow(orden_edificacion, countRow), ed.getId_edificacion(), sh);
                                        llenarCampo(addRow(orden_unidad, countRow), ud.getId_unidad_economica(), sh);
                                        llenarCampo(addRow(direccion, countRow), ud.getDirecc(), sh);
                                        String estadoUnidad = ud.getEstado_unidad_observacion();
                                        if(estadoUnidad.equals("1")){
                                            llenarCampo(addRow(estadoOcupado, countRow), "1", sh);
                                            totOcupado = totOcupado + 1;
                                        } else if (estadoUnidad.equals("2")) {
                                            llenarCampo(addRow(estadoDesocupado, countRow), "1", sh);
                                            totDesocupado = totDesocupado + 1;
                                        } else if (estadoUnidad.equals("3")) {
                                            llenarCampo(addRow(estadoObra, countRow), "1", sh);
                                            totObra = totObra + 1;
                                        }

                                        String tipoUnidad = ud.getTipo_unidad_observacion();
                                        if(tipoUnidad.equals("1")){
                                            llenarCampo(addRow(tipoFijo, countRow), "1", sh);
                                            totFijo = totFijo + 1;
                                        } else if(tipoUnidad.equals("2")){
                                            llenarCampo(addRow(tipoSemifijo, countRow), "1", sh);
                                            totSemifijo = totSemifijo + 1;
                                        } else if(tipoUnidad.equals("3")){
                                            String vendedorCalle = ud.getTipo_vendedor();
                                            if(vendedorCalle.equals("1")){
                                                llenarCampo(addRow(tipoMovil, countRow), "1", sh);
                                                totPuestoMovil = totPuestoMovil + 1;
                                            } else if(vendedorCalle.equals("2")){
                                                llenarCampo(addRow(tipoMovilSin, countRow), "1", sh);
                                                totSinPuestoMovil = totSinPuestoMovil + 1;
                                            }
                                        } else if(tipoUnidad.equals("4")){
                                            llenarCampo(addRow(tipoVivienda, countRow), "1", sh);
                                            totVivienda = totVivienda + 1;
                                        } else if(tipoUnidad.equals("5")){
                                            llenarCampo(addRow(tipoObra, countRow), "1", sh);
                                            totObraEdificacion = totObraEdificacion + 1;
                                        }
//
                                        String sectorUnidad = ud.getSector_economico();
                                        if(sectorUnidad.equals("1")){
                                            llenarCampo(addRow(sectorComercio, countRow), "1", sh);
                                            llenarCampo(addRow(sectorIndustria, countRow), "", sh);
                                            llenarCampo(addRow(sectorServicio, countRow), "", sh);
                                            llenarCampo(addRow(sectorTransporte, countRow), "", sh);
                                            llenarCampo(addRow(sectorConstruccion, countRow), "", sh);
                                            llenarCampo(addRow(sectorNoaplica, countRow), "", sh);
                                            totComercio = totComercio + 1;
                                        } else if(sectorUnidad.equals("2")){
                                            llenarCampo(addRow(sectorComercio, countRow), "", sh);
                                            llenarCampo(addRow(sectorIndustria, countRow), "1", sh);
                                            llenarCampo(addRow(sectorServicio, countRow), "", sh);
                                            llenarCampo(addRow(sectorTransporte, countRow), "", sh);
                                            llenarCampo(addRow(sectorConstruccion, countRow), "", sh);
                                            llenarCampo(addRow(sectorNoaplica, countRow), "", sh);
                                            totIndustria = totIndustria + 1;
                                        } else if(sectorUnidad.equals("3")){
                                            llenarCampo(addRow(sectorComercio, countRow), "", sh);
                                            llenarCampo(addRow(sectorIndustria, countRow), "", sh);
                                            llenarCampo(addRow(sectorServicio, countRow), "1", sh);
                                            llenarCampo(addRow(sectorTransporte, countRow), "", sh);
                                            llenarCampo(addRow(sectorConstruccion, countRow), "", sh);
                                            llenarCampo(addRow(sectorNoaplica, countRow), "", sh);
                                            totServicio = totServicio + 1;
                                        } else if(sectorUnidad.equals("4")){
                                            llenarCampo(addRow(sectorComercio, countRow), "", sh);
                                            llenarCampo(addRow(sectorIndustria, countRow), "", sh);
                                            llenarCampo(addRow(sectorServicio, countRow), "", sh);
                                            llenarCampo(addRow(sectorTransporte, countRow), "1", sh);
                                            llenarCampo(addRow(sectorConstruccion, countRow), "", sh);
                                            llenarCampo(addRow(sectorNoaplica, countRow), "", sh);
                                            totTransporte = totTransporte + 1;
                                        } else if(sectorUnidad.equals("5")){
                                            llenarCampo(addRow(sectorComercio, countRow), "", sh);
                                            llenarCampo(addRow(sectorIndustria, countRow), "", sh);
                                            llenarCampo(addRow(sectorServicio, countRow), "", sh);
                                            llenarCampo(addRow(sectorTransporte, countRow), "", sh);
                                            llenarCampo(addRow(sectorConstruccion, countRow), "1", sh);
                                            llenarCampo(addRow(sectorNoaplica, countRow), "", sh);
                                            totConstruccion = totConstruccion + 1;
                                        } else if(sectorUnidad.equals("6")){
                                            llenarCampo(addRow(sectorComercio, countRow), "", sh);
                                            llenarCampo(addRow(sectorIndustria, countRow), "", sh);
                                            llenarCampo(addRow(sectorServicio, countRow), "", sh);
                                            llenarCampo(addRow(sectorTransporte, countRow), "", sh);
                                            llenarCampo(addRow(sectorConstruccion, countRow), "", sh);
                                            llenarCampo(addRow(sectorNoaplica, countRow), "1", sh);
                                            totNoAplica = totNoAplica + 1;
                                        }

                                        llenarCampo(addRow(nombreUnidad, countRow), ud.getUnidad_osbservacion(), sh);
                                        llenarCampo(addRow(observacionUnidad, countRow), ud.getObservacion(), sh);

                                        countRow = countRow + 1;

                                    }
                                }
                            }
                        }

                        llenarCampo(addRow(totalOcupado, countRow), String.valueOf(totOcupado), sh);
                        llenarCampo(addRow(totalDesocupado, countRow), String.valueOf(totDesocupado), sh);
                        llenarCampo(addRow(totalObra, countRow), String.valueOf(totObra), sh);
                        llenarCampo(addRow(totalFijo, countRow), String.valueOf(totFijo), sh);
                        llenarCampo(addRow(totalSemifijo, countRow), String.valueOf(totSemifijo), sh);
                        llenarCampo(addRow(totalPuestoMovil, countRow), String.valueOf(totPuestoMovil), sh);
                        llenarCampo(addRow(totalSinPuestoMovil, countRow), String.valueOf(totSinPuestoMovil), sh);
                        llenarCampo(addRow(totalVivienda, countRow), String.valueOf(totVivienda), sh);
                        llenarCampo(addRow(totalObraEdificacion, countRow), String.valueOf(totObraEdificacion), sh);
                        llenarCampo(addRow(totalComercio, countRow), String.valueOf(totComercio), sh);
                        llenarCampo(addRow(totalIndustria, countRow), String.valueOf(totIndustria), sh);
                        llenarCampo(addRow(totalServicio, countRow), String.valueOf(totServicio), sh);
                        llenarCampo(addRow(totalTransporte, countRow), String.valueOf(totTransporte), sh);
                        llenarCampo(addRow(totalConstruccion, countRow), String.valueOf(totConstruccion), sh);
                        llenarCampo(addRow(totalNoAplica, countRow), String.valueOf(totNoAplica), sh);
                        llenarCampo(addRow(total, countRow), String.valueOf(i), sh);
                        llenarCampo(addRow(codRecuentista, countRow), mz.getCod_enumerador(), sh);
                        llenarCampo(addRow(codSupervisor, countRow), mz.getSupervisor(), sh);
                        llenarCampo(addRow(barrioRecuento, countRow), mz.getObsmz(), sh);
                        llenarCampo(addRow(observacionRecuento, countRow), mz.getDirec_barrio(), sh);
                        llenarCampo(addRow(id_dmc, countRow), mz.getImei(), sh);
                        llenarCampo(addRow(longitude, countRow), mz.getPto_lon_gps(), sh);
                        llenarCampo(addRow(latitude, countRow), mz.getPto_lat_gps(), sh);
                        llenarCampo(addRow(fechaRecuento, countRow), mz.getFechaConteo(), sh);
                        outPath = Environment.getExternalStorageDirectory() + File.separator + "Censo_Economico" + File.separator + "reportes" + File.separator + "Manzana_" + mz.getId_manzana() + ".xls";
                        sh.protectSheet("123456");
                        FileOutputStream outputStream = new FileOutputStream(outPath);
                        wb.write(outputStream);
                    }

                }
                Toast.makeText(this.context, "Reportes generados para las manzanas finalizadas", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this.context, "No existen manzanas finalizadas", Toast.LENGTH_LONG).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private InputStream openXLS() {
        InputStream result = null;
        try {
            result = context.getAssets().open("formato_recuentos_ce_v2.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private void initializeWorkbook() {
        try {
            wb = new HSSFWorkbook(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<Integer, Integer> createMap(Integer a, Integer b) {
        HashMap<Integer, Integer> myMap = new HashMap<Integer, Integer>();
        myMap.put(a, b);
        return myMap;
    }

    public HashMap<Integer, Integer> addRow(HashMap<Integer, Integer> hm, int sum) {
        HashMap<Integer, Integer> myMap = new HashMap<Integer, Integer>();
        Integer row = hm.entrySet().iterator().next().getKey();
        Integer column = hm.entrySet().iterator().next().getValue();
        myMap.put(row + sum, column);
        return myMap;
    }

    public void llenarCampo(HashMap<Integer, Integer> posicion, String valor, Sheet sh) {
        Row row = sh.getRow(posicion.entrySet().iterator().next().getKey());
        Cell fechaConteo = row.getCell(posicion.entrySet().iterator().next().getValue());
        fechaConteo.setCellValue(valor);
    }

    public void llenarCajaTexto(String valor, Sheet sh, String ubicacion) {
        HSSFPatriarch pat = (HSSFPatriarch) sh.createDrawingPatriarch();
        List<HSSFShape> children = pat.getChildren();
        Iterator<HSSFShape> it = children.iterator();
        HSSFRichTextString richStr = new HSSFRichTextString(valor);
        while (it.hasNext()) {
            HSSFShape shape = it.next();
            if (shape instanceof HSSFTextbox) {
                if (((HSSFTextbox) shape).getShapeName().trim().equals(ubicacion)) {
                    ((HSSFTextbox) shape).setString(richStr);
                }
            }
        }
    }

    private String llenarCeros(String cadena) {
        int maxSize = 3;
        int cadenaSize = cadena.length();
        int faltante = maxSize - cadenaSize;
        String resultado = cadena;
        if (cadena.equals("")) {
            resultado = "";
        } else {
            for (int i = 0; i < faltante; i++) {
                resultado = "0" + resultado;
            }
        }


        return resultado;
    }

    private static Row copyRow(Workbook workbook, Sheet worksheet, int sourceRowNum, int destinationRowNum) {
        // Get the source / new row
        Row newRow = worksheet.getRow(destinationRowNum);
        Row sourceRow = worksheet.getRow(sourceRowNum);


        // If the row exist in destination, push down all rows by 1 else create a new row
        if (newRow != null) {
//            worksheet.shiftRows(destinationRowNum, destinationRowNum + 1, 1);
            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
        } else {
            newRow = worksheet.createRow(destinationRowNum);
        }

        newRow.setHeightInPoints(sourceRow.getHeightInPoints());


        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
            Cell oldCell = sourceRow.getCell(i);
            Cell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null) {
                newCell = null;
                continue;
            }

            // Copy style from old cell and apply to new cell
            CellStyle newCellStyle = oldCell.getCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            newCell.setCellStyle(newCellStyle);

            newCell.setCellType(oldCell.getCellTypeEnum());
        }

        return newRow;
    }

    private void mergeCells(Sheet sh, int rownum, int start, int end) {
        sh.addMergedRegion(new CellRangeAddress(rownum, rownum, start, end));
    }

    private boolean existsManzana(String cod_manzana) {
        Boolean result = false;
        String myDirectoryPath = Environment.getExternalStorageDirectory() + File.separator + "Censo_Economico" + File.separator + "reportes";
        File dir = new File(myDirectoryPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                // Do something with child
                if (child.getName().equals("Manzana_" + cod_manzana + ".xls")) {
                    result = true;
                }
            }
        }

        return result;
    }


}
