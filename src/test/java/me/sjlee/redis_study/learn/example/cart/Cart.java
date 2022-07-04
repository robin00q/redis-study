package me.sjlee.redis_study.learn.example.cart;

import java.util.List;

public interface Cart {

    List<CartProduct> getCartInfos(String userId);
    void flushCart(String userId);
    void addProduct(String userId, CartProduct cartProduct);
    void deleteProduct(String userId, List<Long> productNos);
}
