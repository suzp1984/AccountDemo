package io.github.suzp1984.account.network;

import android.util.Base64;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.io.IOException;

import io.github.suzp1984.account.service.IAuthenticatorServer;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class GithubAuthToken implements IAuthenticatorServer {

    private final String TAG = GithubAuthToken.class.getSimpleName();

    private GithubAuthTokenService mGithubAuthService;

    public GithubAuthToken() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mGithubAuthService = retrofit.create(GithubAuthTokenService.class);
    }

    @Override
    public String getAuthToken(String userName, String pwd, String tokenType) {
        final String credentials = userName + ":" + pwd;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        Call<JsonArray> tokens = mGithubAuthService.getAuthTokens(auth);

        try {
            Response<JsonArray> response = tokens.execute();
            JsonArray jsonRet = response.body();

            Log.e(TAG, jsonRet.toString());

            for (JsonElement element : jsonRet) {
                Log.e(TAG, element.toString());
                String appName = element.getAsJsonObject().getAsJsonObject("app").get("name").toString();
                appName = appName.replace("\"", "");

                Log.e(TAG, "appName = " + appName + "; tokenType = " + tokenType);

                if (tokenType.equalsIgnoreCase(appName)) {
                    Log.e(TAG, "auth_token: " + element.getAsJsonObject().getAsJsonPrimitive("hashed_token").toString());
                    return element.getAsJsonObject().getAsJsonPrimitive("hashed_token").toString().replace("\"", "");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
