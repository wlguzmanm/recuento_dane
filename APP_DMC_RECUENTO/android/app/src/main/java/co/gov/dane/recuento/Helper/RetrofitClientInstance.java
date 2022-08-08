package co.gov.dane.recuento.Helper;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    //private static final String BASE_URL = "http://10.0.2.2:8084/";
    //private static final String BASE_URL = "https://texconteoce.dane.gov.co";
    private static final String BASE_URL = "http://192.168.1.23:9080/";  //DANE VPN PRUEBAS
    //public static String HostApi = "https://texconteoce.dane.gov.co";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
