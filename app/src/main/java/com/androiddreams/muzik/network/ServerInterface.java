package com.androiddreams.muzik.network;

import com.androiddreams.muzik.models.AuthRequest;
import com.androiddreams.muzik.models.AuthResponse;
import com.androiddreams.muzik.models.CardItem;
import com.androiddreams.muzik.models.Track;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServerInterface {
    @GET("/search/{keyword}")
    Call<List<Track>> getSearchResult(@Path("keyword") String keyword);

    @GET("/{endpoint}/{keyword}")
    Call<List<Track>> getFilterResult(@Path("endpoint") String endpoint, @Path("keyword") String keyword);

    @GET("/{endpoint}")
    Call<List<CardItem>> getCardItems(@Path("endpoint") String endpoint);

    @POST("/{auth_type}")
    Call<AuthResponse> authenticate(@Path("auth_type") String auth_type, @Body AuthRequest authRequest);
}

