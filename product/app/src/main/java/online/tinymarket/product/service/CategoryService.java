package online.tinymarket.product.service;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.List;

import online.tinymarket.product.entity.Category;
import online.tinymarket.product.entity.Result;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import online.tinymarket.product.session.SessionUserInfo;
import online.tinymarket.product.util.GsonUtil;
import online.tinymarket.product.util.HttpUtil;

public class CategoryService {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private HttpUtil httpUtil = new HttpUtil();

    private static final int REQUEST_OK = 1;
    private static final int REQUEST_NG = 0;

    private Handler handler;

    public CategoryService(Handler handler){this.handler = handler;}


    public void getAllCategories() {
        try{
            httpUtil.sendRequest(
                    "/p/getCategories",
                    SessionUserInfo.getInstance().token,
                    new okhttp3.Callback(){

                        @Override
                        public void onResponse(Call call, Response response) {
                            try{
                                String responseStr = response.body().string();
                                List<Category> categories = GsonUtil.GsonToList(responseStr, Category.class);
                                Message message = new Message();
                                message.what = REQUEST_OK;
                                message.obj = (Object) categories;
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
            throw e;
        }
    }

    public void insertProduct(Category category) {
        try{
            RequestBody requestBody = RequestBody.create(JSON, GsonUtil.BeanToJson(category));
            httpUtil.sendRequest(
                    "/p/insert-category",
                    requestBody,
                    SessionUserInfo.getInstance().token,
                    new okhttp3.Callback(){

                        @Override
                        public void onResponse(Call call, Response response) {
                            try{
                                String responseStr = response.body().string();
                                Result result = GsonUtil.GsonToBean(responseStr, Result.class);
                                Message message = new Message();
                                message.what = REQUEST_OK;
                                message.obj = (Object) result;
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

                    }
            );
        }catch (Exception e){
            throw e;
        }
    }

    public void updateProduct(Category category) {
        try{
            RequestBody requestBody = RequestBody.create(JSON, GsonUtil.BeanToJson(category));
            httpUtil.sendRequest(
                    "/p/update-category",
                    requestBody,
                    SessionUserInfo.getInstance().token,
                    new okhttp3.Callback(){

                        @Override
                        public void onResponse(Call call, Response response) {
                            try{
                                String responseStr = response.body().string();
                                Result result = GsonUtil.GsonToBean(responseStr, Result.class);
                                Message message = new Message();
                                message.what = REQUEST_OK;
                                message.obj = (Object) result;
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

                    }
            );
        }catch (Exception e){
            throw e;
        }
    }

    public void deleteProduct(String id) throws Exception{
        try{
            httpUtil.sendRequest(
                    "/p/delete-category?id=" + id,
                    SessionUserInfo.getInstance().token,
                    new okhttp3.Callback(){

                        @Override
                        public void onResponse(Call call, Response response) {
                            try{
                                String responseStr = response.body().string();
                                Result result = GsonUtil.GsonToBean(responseStr, Result.class);
                                Message message = new Message();
                                message.what = REQUEST_OK;
                                message.obj = (Object) result;
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

                    }
            );
        }catch (Exception e){
            throw e;
        }
    }
}
