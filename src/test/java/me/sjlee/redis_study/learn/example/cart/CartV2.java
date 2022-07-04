package me.sjlee.redis_study.learn.example.cart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CartV2 implements Cart {

    private static final String KEY_CART_PRODUCT = ":cart:productid:";
    private static final String KEY_CART_LIST = ":cart:product";

    private final RedisTemplate<String, String> redisTemplate;

    public CartV2(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 장바구니 정보 조회
    public List<CartProduct> getCartInfos(String userId) {
        String productInfo = redisTemplate.opsForValue().get(userId + KEY_CART_LIST);
        if (!StringUtils.hasText(productInfo)) {
            return new ArrayList<>();
        }
        try {
            return new ObjectMapper().readValue(productInfo, new TypeReference<List<CartProduct>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // will not reach here
        return new ArrayList<>();
    }

    // 장바구니를 깨끗하게 비운다.
    public void flushCart(String userId) {
        List<CartProduct> cartInfos = getCartInfos(userId);
        redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            cartInfos.stream()
                    .forEach(cartInfo -> {
                        ((StringRedisConnection) connection).del(userId + KEY_CART_PRODUCT + cartInfo.getProductNo());
                    });

            return null;
        });

        redisTemplate.opsForValue().set(userId + KEY_CART_PRODUCT, "");
    }

    // 장바구니에 상품을 추가한다.
    public void addProduct(String userId, CartProduct cartProduct) {
        List<CartProduct> cartInfos = getCartInfos(userId);
        CartProduct newProduct = new CartProduct(cartProduct.getProductNo(), cartProduct.getProductName(), cartProduct.getCount());
        cartInfos.add(newProduct);

        try {
            redisTemplate.opsForValue().set(userId + KEY_CART_PRODUCT + cartProduct.getProductNo(), new ObjectMapper().writeValueAsString(newProduct));
            redisTemplate.opsForValue().set(userId + KEY_CART_LIST, new ObjectMapper().writeValueAsString(cartInfos));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 장바구니 정보를 조회한다.
    public void deleteProduct(String userId, List<Long> productNos) {
        List<CartProduct> cartInfos = getCartInfos(userId);

        redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            for (long target : productNos) {
                ((StringRedisConnection) connection).del(userId + KEY_CART_PRODUCT + target);
                cartInfos.removeIf(e -> e.getProductNo().equals(target));
            }
            return null;
        });

        try {
            redisTemplate.opsForValue().set(userId + KEY_CART_LIST, new ObjectMapper().writeValueAsString(cartInfos));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
