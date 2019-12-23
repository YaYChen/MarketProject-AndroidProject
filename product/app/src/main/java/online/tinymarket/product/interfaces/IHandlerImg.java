package online.tinymarket.product.interfaces;

import android.graphics.Bitmap;

public interface IHandlerImg {
    void handlerImgWhenOK(String msg);
    void handlerImgWhenOK(Bitmap bitmap);
    void handlerImgWhenNG(String errorMsg);
}
