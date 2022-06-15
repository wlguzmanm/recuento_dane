package co.gov.dane.recuento.interfaces;

import co.gov.dane.recuento.model.EnvioFormularioViewModel;
import co.gov.dane.recuento.model.NotificacionesViewModel;
import co.gov.dane.recuento.model.RequestAuthentication;
import co.gov.dane.recuento.model.ResponseEnvioManzanaViewModel;
import co.gov.dane.recuento.model.ResponseFile;
import co.gov.dane.recuento.model.ResponseToken;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IAuthentication {

    /**Componente Authentication**/
    @POST("login")
    Call<ResponseToken> login(
            @Header("Content-Type") String content_type,
            @Body RequestAuthentication body
    );

    @POST("javainuse-rabbitmq/consulta-status")
    Call<NotificacionesViewModel> consultaStatus(
            @Header("Content-Type") String content_type,
            @Header("Authorization") String token,
            @Body NotificacionesViewModel body
    );

    @POST("javainuse-rabbitmq/formulario")
    Call<ResponseEnvioManzanaViewModel> sincronizarFormulario(
            @Header("Content-Type") String content_type,
            @Header("Authorization") String token,
            @Body EnvioFormularioViewModel body
    );

    @POST("javainuse-rabbitmq/reporte")
    Call<ResponseFile> descargarReporte(
            @Header("Content-Type") String content_type,
            @Header("Authorization") String token,
            @Body EnvioFormularioViewModel body
    );
}
