package service;

import java.util.List;

import entity.Category;
import entity.Result;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import util.GsonUtil;
import util.HttpUtil;

public class CategoryService {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private HttpUtil httpUtil = new HttpUtil();

    public List<Category> getAllCategories() throws Exception{
        try{
            Response response = httpUtil.sendGetRequest("/getCategories");
            return GsonUtil.parseJsonArrayWithGson(response.body().string(),Category.class);
        }catch (Exception e){
            throw e;
        }
    }

    public Result insertProduct(Category category) throws Exception{
        try{
            RequestBody requestBody = RequestBody.create(JSON, GsonUtil.parseObjectWithGson(category));
            Response response = httpUtil.sendPostRequest("/insert-category", requestBody);
            return GsonUtil.parseJsonWithGson(response.body().toString(),Result.class);
        }catch (Exception e){
            throw e;
        }
    }

    public Result updateProduct(Category category) throws Exception{
        try{
            RequestBody requestBody = RequestBody.create(JSON, GsonUtil.parseObjectWithGson(category));
            Response response = httpUtil.sendPostRequest("/update-category", requestBody);
            return GsonUtil.parseJsonWithGson(response.body().toString(),Result.class);
        }catch (Exception e){
            throw e;
        }
    }

    public Result deleteProduct(String id) throws Exception{
        try{
            Response response = httpUtil.sendGetRequest("/delete-category?code=" + id);
            return GsonUtil.parseJsonWithGson(response.body().toString(),Result.class);
        }catch (Exception e){
            throw e;
        }
    }
}
