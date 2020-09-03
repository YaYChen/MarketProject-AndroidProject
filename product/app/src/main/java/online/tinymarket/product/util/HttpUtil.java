package online.tinymarket.product.util;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {

    private String baseURL = "http://106.54.251.156:8000";

    private OkHttpClient client = new OkHttpClient();

    public void sendRequest(String requestURL, okhttp3.Callback callback) {
        Request request = new Request.Builder()
                .url(baseURL + requestURL)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void sendRequest(String requestURL, RequestBody requestBody, okhttp3.Callback callback) {
        Request request = new Request.Builder()
                .url(baseURL + requestURL)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void sendRequest(String requestURL, RequestBody requestBody, String token, okhttp3.Callback callback) {
        Request request = new Request.Builder()
                .url(baseURL + requestURL)
                .header("Authorization", token)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void sendRequest(String requestURL, String token, okhttp3.Callback callback) {
        Request request = new Request.Builder()
                .url(baseURL + requestURL)
                .header("Authorization", token)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void sendRequest(String requestURL, MultipartBody requestBody, okhttp3.Callback callback) throws IOException{
        Request request = new Request.Builder()
                .url(baseURL + requestURL)
                .addHeader("Content-Type","multipart/form-data")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
