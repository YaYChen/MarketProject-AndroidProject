package service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import entity.Product;
import okhttp3.Response;
import util.GsonUtil;
import util.HttpUtil;

public class ProductService {

    private HttpUtil httpUtil = new HttpUtil();

    public Product getProductByCode(String code) throws Exception{
        try{
            Response response = httpUtil.sendGetRequest("/product-ByCode?code=" + code);
            return GsonUtil.parseJsonWithGson(response.body().string(),Product.class);
        }catch (Exception e){
            throw e;
        }
    }

    public Bitmap getProductImage(String fileName) throws Exception{
        try{
            Response response = httpUtil.sendGetRequest("/show-img?fileName=" + fileName);
            byte[] bitMapData = response.body().bytes();
            return BitmapFactory.decodeByteArray(bitMapData, 0, bitMapData.length);
        }catch (Exception e){
            throw e;
        }
    }
}
