package online.tinymarket.product.handler;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import online.tinymarket.product.enums.HttpOperating;
import online.tinymarket.product.interfaces.IHandlerImg;

public class ImgHandler {

    private static final int REQUEST_OK = 1;
    private static final int REQUEST_NG = 0;

    private IHandlerImg handlerImgImp;

    public HttpOperating operating;

    public ImgHandler(IHandlerImg handlerImgImp, HttpOperating operating){
        this.handlerImgImp = handlerImgImp;
        this.operating = operating;
    }

    public Handler doHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REQUEST_OK:
                    if(operating == HttpOperating.DownloadData){
                        handlerImgImp.handlerImgWhenOK(((Bitmap)msg.obj));
                    }else{
                        handlerImgImp.handlerImgWhenOK(msg.obj.toString());
                    }
                    break;
                case REQUEST_NG:
                    handlerImgImp.handlerImgWhenNG(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
}
