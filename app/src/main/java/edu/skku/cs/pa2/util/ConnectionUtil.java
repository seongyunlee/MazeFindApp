package edu.skku.cs.pa2.util;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import edu.skku.cs.pa2.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ConnectionUtil {
    final String IP = "http://115.145.175.57:10099/";
    OkHttpClient client = new OkHttpClient();


    private void sendQuery(Request req, Callback callback){
        client.newCall(req).enqueue(callback);
    }
    public void signIn(String username,Callback callback){
        SignInModel model = new SignInModel(username);
        Gson gson = new Gson();
        String json = gson.toJson(model,SignInModel.class);
        Request req = new Request.Builder().url(IP+"users").post(RequestBody.create(MediaType.parse("application/json"),json)).build();
        sendQuery(req,callback);
    }
    public void getMazeList(Callback callback){
        Request req = new Request.Builder().url(IP+"maps").build();
        sendQuery(req,callback);
    }
    public void getMap(String mazeName,Callback callback){
        HttpUrl.Builder builder = HttpUrl.parse(IP+"/maze/map").newBuilder();
        builder.addQueryParameter("name",mazeName);
        Request req= new Request.Builder().url(builder.toString()).build();
        Log.d("get_map_try",mazeName);
        sendQuery(req,callback);
    }
    class SignInModel{
        private String username;

        SignInModel(String username) {
            this.username = username;
        }
    }
}
