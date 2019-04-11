package service;

import java.util.List;

import entity.Category;
import okhttp3.Response;
import util.GsonUtil;
import util.HttpUtil;

public class CategoryService {
    private HttpUtil httpUtil = new HttpUtil();

    public List<Category> getAllCategories() throws Exception{
        try{
            Response response = httpUtil.sendGetRequest("/getCategories");
            return GsonUtil.parseJsonArrayWithGson(response.body().string(),Category.class);
        }catch (Exception e){
            throw e;
        }
    }
}
