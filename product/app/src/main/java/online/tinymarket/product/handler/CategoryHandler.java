package online.tinymarket.product.handler;

import android.os.Handler;
import android.os.Message;

import online.tinymarket.product.entity.Category;
import online.tinymarket.product.enums.HttpOperating;
import online.tinymarket.product.interfaces.IHandlerCategory;

import java.util.List;

public class CategoryHandler {
    private static final int REQUEST_OK = 1;
    private static final int REQUEST_NG = 0;

    private IHandlerCategory handlerCategoryImp;

    public HttpOperating operating;

    public CategoryHandler(IHandlerCategory handlerCategoryImp, HttpOperating operating){
        this.handlerCategoryImp = handlerCategoryImp;
        this.operating = operating;
    }

    public Handler doHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REQUEST_OK:
                    if(operating == HttpOperating.UploadData){
                        handlerCategoryImp.handlerCategoryWhenOK(msg.obj.toString());
                    }else{
                        handlerCategoryImp.handlerCategoryWhenOK((List<Category>)msg.obj);
                    }
                    break;
                case REQUEST_NG:
                    handlerCategoryImp.handlerCategoryWhenNG(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
}
