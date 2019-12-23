package online.tinymarket.product.interfaces;

import online.tinymarket.product.entity.Category;

import java.util.List;

public interface IHandlerCategory {
    void handlerCategoryWhenOK(String msg);
    void handlerCategoryWhenOK(List<Category> product);
    void handlerCategoryWhenNG(String errorMsg);
}
