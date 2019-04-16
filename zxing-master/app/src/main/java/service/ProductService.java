package service;

import entity.Product;
import entity.Result;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import util.GsonUtil;
import util.HttpUtil;

public class ProductService {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private HttpUtil httpUtil = new HttpUtil();

    public Product getProductByCode(String code) throws Exception{
        try{
            Response response = httpUtil.sendGetRequest("/product-ByCode?code=" + code);
            return GsonUtil.parseJsonWithGson(response.body().string(),Product.class);
        }catch (Exception e){
            throw e;
        }
    }

    public Result updateProduct(Product product) throws Exception{
        try{
            RequestBody requestBody = RequestBody.create(JSON, GsonUtil.parseObjectWithGson(product));
            Response response = httpUtil.sendPostRequest("/update-product", requestBody);
            return GsonUtil.parseJsonWithGson(response.body().toString(),Result.class);
        }catch (Exception e){
            throw e;
        }
    }

    public Result insertProduct(Product product) throws Exception{
        try{
            RequestBody requestBody = RequestBody.create(JSON, GsonUtil.parseObjectWithGson(product));
            Response response = httpUtil.sendPostRequest("/insert-product", requestBody);
            return GsonUtil.parseJsonWithGson(response.body().toString(),Result.class);
        }catch (Exception e){
            throw e;
        }
    }

    public Result deleteProduct(String id) throws Exception{
        try{
            Response response = httpUtil.sendGetRequest("/delete-product?code=" + id);
            return GsonUtil.parseJsonWithGson(response.body().toString(),Result.class);
        }catch (Exception e){
            throw e;
        }
    }
}
