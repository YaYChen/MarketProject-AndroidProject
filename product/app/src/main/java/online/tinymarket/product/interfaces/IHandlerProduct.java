package online.tinymarket.product.interfaces;


import online.tinymarket.product.entity.Product;

public interface IHandlerProduct {
    void handlerProductWhenOK(String msg);
    void handlerProductWhenOK(Product product);
    void handlerProductWhenNG(String errorMsg);
}
