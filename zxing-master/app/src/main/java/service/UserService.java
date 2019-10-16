package service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import org.json.JSONObject;

import java.io.IOException;

import entity.LoginResult;
import entity.User;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import session.SessionUserInfo;
import util.GsonUtil;
import util.HttpUtil;

/**
 * @author: yaychen
 * @date: 2019/10/09 23:50
 * @declare :
 */

public class UserService implements Runnable{

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private HttpUtil httpUtil = new HttpUtil();

    private User user;
    private Handler handler;

    private static final int LOGIN_OK = 1;
    private static final int LOGIN_NG = 0;

    public UserService(User user, Handler handler) {
        this.user = user;
        this.handler = handler;
    }

    public void run() {
        try{
            RequestBody requestBody = RequestBody.create(JSON, GsonUtil.parseObjectWithGson(this.user));
            httpUtil.sendPostRequest("/sign-in", requestBody, new okhttp3.Callback(){

                @Override
                public void onResponse(Call call, Response response) {
                    try{
                        String responseStr = response.body().string();
                        LoginResult result = GsonUtil.parseJsonWithGson(responseStr, LoginResult.class);
                        if(result.getMessage().equals("Success!")){
                            SessionUserInfo.getInstance().userId = result.getUserId();
                            SessionUserInfo.getInstance().userName = result.getUserName();
                            SessionUserInfo.getInstance().token = result.getToken().getToken();
                            Message message = new Message();
                            message.what = LOGIN_OK;
                            handler.sendMessage(message);
                        }else{
                            Message message = new Message();
                            message.what = LOGIN_NG;
                            message.obj = (Object) result.getMessage();
                            handler.sendMessage(message);
                        }
                    }catch (Exception e){
                        Message message = new Message();
                        message.what = LOGIN_NG;
                        message.obj = (Object)e.getMessage();
                        handler.sendMessage(message);
                    }
                }

                @Override
                public void onFailure(Call call, IOException e){
                    Message message = new Message();
                    message.what = LOGIN_NG;
                    message.obj = (Object)e.getMessage();
                    handler.sendMessage(message);
                }

            });
        }catch (Exception e){
            Message message = new Message();
            message.what = LOGIN_NG;
            message.obj = (Object)e.getMessage();
            handler.sendMessage(message);
        }
    }
}
