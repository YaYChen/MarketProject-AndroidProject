package service;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import entity.Product;
import entity.Result;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import session.SessionUserInfo;
import util.GsonUtil;
import util.HttpUtil;

public class ProductService {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private HttpUtil httpUtil = new HttpUtil();
    private Handler handler;

    private static final int REQUEST_OK = 1;
    private static final int REQUEST_NG = 0;

    public ProductService(Handler handler){
        this.handler = handler;
    }

    public void getProductByCode(String code) {
        try{
            httpUtil.sendGetRequest(
                    "/p/product-ByCode?code=" + code,
                    SessionUserInfo.getInstance().token,
                    new okhttp3.Callback(){

                @Override
                public void onResponse(Call call, Response response) {
                    try{
                        String responseStr = response.body().string();
                        Product product = GsonUtil.parseJsonWithGson(responseStr, Product.class);
                        Message message = new Message();
                        message.what = REQUEST_OK;
                        message.obj = (Object) product;
                        handler.sendMessage(message);
                    }catch (Exception e){
                        Message message = new Message();
                        message.what = REQUEST_NG;
                        message.obj = (Object)e.getMessage();
                        handler.sendMessage(message);
                    }
                }

                @Override
                public void onFailure(Call call, IOException e){
                    Message message = new Message();
                    message.what = REQUEST_NG;
                    message.obj = (Object)e.getMessage();
                    handler.sendMessage(message);
                }

            });
        }catch (Exception e){
            Message message = new Message();
            message.what = REQUEST_NG;
            message.obj = (Object)e.getMessage();
            handler.sendMessage(message);
        }
    }

    public Result updateProduct(Product product) throws Exception{
        try{
            RequestBody requestBody = RequestBody.create(JSON, GsonUtil.parseObjectWithGson(product));
            Response response = httpUtil.sendPostRequest("/update-product", requestBody, SessionUserInfo.getInstance().token);
            return GsonUtil.parseJsonWithGson(response.body().toString(),Result.class);
        }catch (Exception e){
            throw e;
        }
    }

    public Result insertProduct(Product product) throws Exception{
        try{
            RequestBody requestBody = RequestBody.create(JSON, GsonUtil.parseObjectWithGson(product));
            Response response = httpUtil.sendPostRequest("/insert-product", requestBody, SessionUserInfo.getInstance().token);
            return GsonUtil.parseJsonWithGson(response.body().toString(),Result.class);
        }catch (Exception e){
            throw e;
        }
    }

    public Result deleteProduct(String id) throws Exception{
        try{
            Response response = httpUtil.sendGetRequest("/delete-product?code=" + id, SessionUserInfo.getInstance().token);
            return GsonUtil.parseJsonWithGson(response.body().toString(),Result.class);
        }catch (Exception e){
            throw e;
        }
    }
}
