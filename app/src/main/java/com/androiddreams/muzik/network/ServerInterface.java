package com.androiddreams.muzik.network;

import com.androiddreams.muzik.models.AuthRequest;
import com.androiddreams.muzik.models.AuthResponse;
import com.androiddreams.muzik.models.CardItem;
import com.androiddreams.muzik.models.FavRequest;
import com.androiddreams.muzik.models.FavResponse;
import com.androiddreams.muzik.models.Track;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServerInterface {
    @GET("/search/{keyword}/{username}")
    Call<List<Track>> getSearchResult(@Path("keyword") String keyword, @Path("username") String username);

    @GET("/{endpoint}/{keyword}/{username}")
    Call<List<Track>> getFilterResult(@Path("endpoint") String endpoint, @Path("keyword") String keyword, @Path("username") String username);

    @GET("/get_favour/{username}")
    Call<List<Track>> getFavouriteTracks(@Path("username") String username);

    @GET("/{endpoint}")
    Call<List<CardItem>> getCardItems(@Path("endpoint") String endpoint);

    @POST("/{auth_type}")
    Call<AuthResponse> authenticate(@Path("auth_type") String auth_type, @Body AuthRequest authRequest);

    @POST("/favour")
    Call<FavResponse> addFavourite(@Body FavRequest favRequest);

    @POST("/delete_fav")
    Call<FavResponse> removeFavourite(@Body FavRequest favRequest);
}

