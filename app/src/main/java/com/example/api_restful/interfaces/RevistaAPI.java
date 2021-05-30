package com.example.api_restful.interfaces;

import com.example.api_restful.Revista;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RevistaAPI {

    @GET("ws/issues.php")
    public Call<List<Revista>> consultaRevista(@Query("j_id") int id_revista);

}



