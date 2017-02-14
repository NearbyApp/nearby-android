package io.nearby.android.data.remote;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.nearby.android.R;
import io.nearby.android.data.local.SharedPreferencesHelper;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by Marc on 2017-02-14.
 */

public class ServiceCreator<T> {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String SERVICE_PROVIDER_HEADER = "Service-Provider";

    private OkHttpClient.Builder mOkHttpClientBuilder;
    private String mEndPoint;
    private Context mContext;
    private SharedPreferencesHelper mSharedPreferenceHelper;
    private Class<T> mServiceClass;

    public ServiceCreator(Class serviceClass, String endPoint, Context context, SharedPreferencesHelper sharedPreferencesHelper){
        mOkHttpClientBuilder = new OkHttpClient.Builder();
        mServiceClass = serviceClass;
        mEndPoint = endPoint;
        mContext = context;
        mSharedPreferenceHelper = sharedPreferencesHelper;
    }

    public T create() {
        Gson gson = new GsonBuilder().create();

        addSslInterceptor();
        addAuthentificationInterceptor();

        OkHttpClient client = mOkHttpClientBuilder.build();

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(mEndPoint)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return mRetrofit.create(mServiceClass);
    }

    private void addSslInterceptor() {
        try{
            // Loading CAs from an InputStream
            CertificateFactory cf;
            cf = CertificateFactory.getInstance("X.509");

            Certificate ca;

            InputStream cert = mContext.getResources().openRawResource(R.raw.my_cert);
            ca = cf.generateCertificate(cert);


            // Creating a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore   = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Creating a TrustManager that trusts the CAs in our KeyStore.
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }

            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

            // Creating an SSLSocketFactory that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { trustManager }, null);
            SSLSocketFactory factory = sslContext.getSocketFactory();

            mOkHttpClientBuilder.sslSocketFactory(factory,trustManager);
        }
        catch(Exception e){
            Timber.e(e);
        }
    }

    private void addAuthentificationInterceptor() {
        mOkHttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder requestBuilder = originalRequest.newBuilder();

                int lastSignInMethod = mSharedPreferenceHelper.getLastSignInMethod();

                String userId = "";
                String token = "";
                String provider = "";

                switch(lastSignInMethod){
                    case SharedPreferencesHelper.LAST_SIGN_IN_METHOD_FACEBOOK:
                        provider = "Facebook";
                        userId = mSharedPreferenceHelper.getFacebookUserId();
                        token = mSharedPreferenceHelper.getFacebookToken();
                        break;
                    case SharedPreferencesHelper.LAST_SIGN_IN_METHOD_GOOGLE:
                        provider = "Google";
                        userId = mSharedPreferenceHelper.getGoogleUserId();
                        token = mSharedPreferenceHelper.getGoogleToken();
                        break;
                }

                String authToken = Credentials.basic(userId, token);

                requestBuilder.header(AUTHORIZATION_HEADER,authToken);
                requestBuilder.addHeader(SERVICE_PROVIDER_HEADER, provider);

                Request request = requestBuilder.build();

                return chain.proceed(request);
            }
        });
    }
}
