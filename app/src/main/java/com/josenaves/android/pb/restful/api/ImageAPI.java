package com.josenaves.android.pb.restful.api;

import android.content.Context;
import android.util.Log;
import com.josenaves.android.pb.restful.Image;
import com.josenaves.android.pb.restful.PreferencesUtils;

import java.io.IOException;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.wire.WireConverterFactory;

public class ImageAPI {

    private static final String TAG = ImageAPI.class.getSimpleName();

    // Trailing slash is needed
    //private static final String BASE_URL = "http://192.168.0.16:9090/";
    //private static final String BASE_URL = PreferencesUtils.getServerHost();


    private Retrofit retrofit;

    public ImageAPI(Context context) {
        retrofit = new Retrofit.Builder()
                .baseUrl(PreferencesUtils.getServerHost(context))
                .addConverterFactory(WireConverterFactory.create())
                .build();
    }


    public Image getRandomImage() {
        ImageService service = retrofit.create(ImageService.class);
        Call<Image> image = service.getImageData();
        try {
            Response<Image> response = image.execute();
            return response.body();
        }
        catch (IOException io) {
            Log.e(TAG, io.getMessage());
        }
        return null;
    }

}