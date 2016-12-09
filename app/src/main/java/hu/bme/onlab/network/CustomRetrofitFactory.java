package hu.bme.onlab.network;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomRetrofitFactory {

    private CustomRetrofitFactory() {}

    @NonNull
    public static Retrofit createRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request newRequest;

                        Request.Builder requestBuilder = request.newBuilder();
                        if(NetworkSessionStore.getSessionId() != null) {
                            requestBuilder.addHeader("Cookie", "JSESSIONID=" + NetworkSessionStore.getSessionId());
                        }
                        if(NetworkSessionStore.getXsrfToken() != null) {
                            requestBuilder.addHeader("X-XSRF-TOKEN", NetworkSessionStore.getXsrfToken());
                        }
                        newRequest = requestBuilder.build();

                        Response response = chain.proceed(newRequest);

                        List<String> setCookieHeaders = response.headers().values("Set-Cookie");
                        for (String cookieHeader : setCookieHeaders) {
                            String[] cookieSplit = cookieHeader.split(";")[0].split("=");
                            if("JSESSIONID".equals(cookieSplit[0])) {
                                NetworkSessionStore.setSessionId(cookieSplit[1]);
                            } else if("XSRF-TOKEN".equals(cookieSplit[0])) {
                                NetworkSessionStore.setXsrfToken(cookieSplit[1]);
                            }
                        }

                        return response;
                    }
                })
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl(NetworkConfig.host)
                .build();
    }
}
