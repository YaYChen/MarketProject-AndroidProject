package online.tinymarket.product.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.IOException;

import online.tinymarket.product.entity.Result;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import online.tinymarket.product.session.SessionUserInfo;
import online.tinymarket.product.util.GsonUtil;
import online.tinymarket.product.util.HttpUtil;

public class ImgService {

    public static final MediaType IMAGE = MediaType.parse("image/jpg");

    private HttpUtil httpUtil = new HttpUtil();

    private Handler handler;

    private static final int REQUEST_OK = 1;
    private static final int REQUEST_NG = 0;

    public ImgService(Handler handler){
        this.handler = handler;
    }

    public void getProductImage(String fileName) {
        try{
            httpUtil.sendRequest(
                    "/show-img?fileName=" + fileName + "&userId=" + SessionUserInfo.getInstance().userId,
                    new okhttp3.Callback(){
                @Override
                public void onResponse(Call call, Response response) {
                    try{
                        byte[] bitMapData = response.body().bytes();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bitMapData, 0, bitMapData.length);
                        Message message = new Message();
                        message.what = REQUEST_OK;
                        message.obj = (Object) bitmap;
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

    public void uploadImage(File file) throws Exception{
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            RequestBody requestBody = RequestBody.create(IMAGE, file);
            builder.addFormDataPart("file", file.getName(), requestBody);
            builder.setType(MultipartBody.FORM);
            MultipartBody multipartBody = builder.build();
            httpUtil.sendRequest(
                    "/upload-img?userId=" + SessionUserInfo.getInstance().userId,
                    multipartBody,
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

        } catch (Exception e) {
            throw e;
        }
    }
}
