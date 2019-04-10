package util;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {

    private String baseURL = "http://101.132.123.27:8080";
    private int timeout = 8000;

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

    public Response sendPostRequest(String requestURL, RequestBody requestBody) throws IOException{
        Request request = new Request.Builder()
                .url(baseURL + requestURL)
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
