package service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

import entity.Result;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import util.GsonUtil;
import util.HttpUtil;

public class ImgService {

    public static final MediaType IMAGE = MediaType.parse("image/jpg");

    private HttpUtil httpUtil = new HttpUtil();

    public Bitmap getProductImage(String fileName) throws Exception{
        try{
            Response response = httpUtil.sendGetRequest("/show-img?fileName=" + fileName);
            byte[] bitMapData = response.body().bytes();
            return BitmapFactory.decodeByteArray(bitMapData, 0, bitMapData.length);
        }catch (Exception e){
            throw e;
        }
    }

    public Result uploadImage(File file) throws Exception{
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            RequestBody requestBody = RequestBody.create(IMAGE, file);
            builder.addFormDataPart("file", file.getName(), requestBody);
            builder.setType(MultipartBody.FORM);
            MultipartBody multipartBody = builder.build();
            Response response = httpUtil.sendPostRequestWithFile("/upload-img", multipartBody);
            return GsonUtil.parseJsonWithGson(response.body().string(),Result.class);

        } catch (Exception e) {
            throw e;
        }
    }
}
