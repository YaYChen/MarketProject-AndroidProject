package util;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {

    private String baseURL = "http://101.132.123.27:8080";

    private OkHttpClient client = new OkHttpClient();

    public Response sendGetRequest(String requestURL) throws IOException{
        Request request = new Request.Builder()
                .url(baseURL + requestURL)
                .build();
        try{
            Response response = client.newCall(request).execute();
            return response;
        }catch (IOException e){
            throw e;
        }
    }

    public void sendGetRequest(String requestURL, okhttp3.Callback callback) {
        Request request = new Request.Builder()
                .url(baseURL + requestURL)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public Response sendPostRequest(String requestURL, RequestBody requestBody) throws Exception{
        Request request = new Request.Builder()
                .url(baseURL + requestURL)
                .post(requestBody)
                .build();
        try{
            Response response = client.newCall(request).execute();
            return response;
        }catch (Exception e){
            throw e;
        }
    }

    public void sendPostRequest(String requestURL, RequestBody requestBody, okhttp3.Callback callback) {
        Request request = new Request.Builder()
                .url(baseURL + requestURL)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public Response sendGetRequest(String requestURL, String token) throws IOException{
        Request request = new Request.Builder()
                .url(baseURL + requestURL)
                .header("Authorization", token)
                .build();
        try{
            Response response = client.newCall(request).execute();
            return response;
        }catch (IOException e){
            throw e;
        }
    }

    public void sendGetRequest(String requestURL, String token, okhttp3.Callback callback) {
        Request request = new Request.Builder()
                .url(baseURL + requestURL)
                .header("Authorization", token)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public Response sendPostRequest(String requestURL, RequestBody requestBody, String token) throws IOException{
        Request request = new Request.Builder()
                .url(baseURL + requestURL)
                .header("Authorization", token)
                .post(requestBody)
                .build();
        try{
            Response response = client.newCall(request).execute();
            return response;
        }catch (IOException e){
            throw e;
        }
    }

    public Response sendPostRequestWithFile(String requestURL, MultipartBody requestBody, String token) throws IOException{
        Request request = new Request.Builder()
                .url(baseURL + requestURL)
                .header("Authorization", token)
                .post(requestBody)
                .build();
        try{
            Response response = client.newCall(request).execute();
            return response;
        }catch (IOException e){
            throw e;
        }
    }
}
