package online.tinymarket.product.handler;

import android.os.Handler;
import android.os.Message;

import online.tinymarket.product.entity.Product;
import online.tinymarket.product.enums.HttpOperating;
import online.tinymarket.product.interfaces.IHandlerProduct;

public class ProductHandler {

    private static final int REQUEST_OK = 1;
    private static final int REQUEST_NG = 0;

    private IHandlerProduct handlerProductImp;

    public HttpOperating operating;

    public ProductHandler(IHandlerProduct handlerProductImp, HttpOperating operating){
        this.handlerProductImp = handlerProductImp;
        this.operating = operating;
    }

    public Handler doHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REQUEST_OK:
                    if(operating == HttpOperating.UploadData){
                        handlerProductImp.handlerProductWhenOK(msg.obj.toString());
                    }else{
                        handlerProductImp.handlerProductWhenOK((Product)msg.obj);
                    }
                    break;
                case REQUEST_NG:
                    handlerProductImp.handlerProductWhenNG(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
}
