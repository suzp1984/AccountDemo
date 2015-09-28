package io.github.suzp1984.account.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;

public interface GithubAuthTokenService {
    @Headers("Accept: application/json")
    @GET("/authorizations")
    Call<JsonArray> getAuthTokens(@Header("Authorization") String authorization);
}
