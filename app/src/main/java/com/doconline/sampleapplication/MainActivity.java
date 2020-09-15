package com.doconline.sampleapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.doconline.doconline.SplashScreenActivity;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static com.doconline.doconline.app.Constants.STAGING_BASE_URL;
import static com.doconline.doconline.app.Constants.STAGING_BUILD_TYPE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void goToDoconline(View view) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Please Wait");
        pd.show();
        final JsonObject requestObject = new JsonObject();
        requestObject.addProperty("first_name","Abhishek");
        requestObject.addProperty("middle_name","VidyaSagar");
        requestObject.addProperty("last_name","Velpula");
        requestObject.addProperty("email","abhishekvidyasagar@gmail.com");
        requestObject.addProperty("mobile_no","9948632536");
        requestObject.addProperty("date_of_birth","1991-06-04");
        requestObject.addProperty("gender","Male");
        requestObject.addProperty("country_id",362);
        requestObject.addProperty("member_id","DOC0071");
        requestObject.addProperty("service_starts_at","2020-01-01 12:11:26");
        requestObject.addProperty("service_ends_at","2020-12-31 12:11:26");

        Call<JsonObject> call = apiInterface.goLoginAPI(requestObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                pd.dismiss();
                JsonObject responseObject = response.body();

                SplashScreenActivity.start(MainActivity.this, String.valueOf(requestObject), String.valueOf(responseObject), STAGING_BUILD_TYPE);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                call.cancel();
                pd.dismiss();
            }
        });

    }
}

class APIClient {

    private static Retrofit retrofit = null;

    static Retrofit getClient(){
        HttpLoggingInterceptor interceptor = new
                HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(STAGING_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

}

interface APIInterface {

    @Headers({"Accept:application/json", "Content-Type:application/json", "Authorization:Bearer QFLqog5py2guJPluS4hvw86n26PHNp1r"})
    @POST("/api/customer/user-login")
    Call<JsonObject> goLoginAPI(@Body JsonObject jsonObject);
}
