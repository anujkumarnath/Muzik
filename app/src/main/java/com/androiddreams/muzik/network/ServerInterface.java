package com.androiddreams.muzik.network;

import com.androiddreams.muzik.models.Track;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServerInterface {
    @GET("/search/{keyword}")
    Call<List<Track>> getSearchResult(@Path("keyword") String keyword);
}
