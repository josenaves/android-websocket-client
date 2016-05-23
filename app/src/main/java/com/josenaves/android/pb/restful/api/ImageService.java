package com.josenaves.android.pb.restful.api;

import com.josenaves.android.pb.restful.Image;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ImageService {
    @GET("package")
    Call<Image> getImageData();
}
