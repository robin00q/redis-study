package me.sjlee.redis_study.learn.example.cart;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CartTest {

    @Autowired Cart cart;

    @Test
    void test() {
        // given
        String userId = "lee";
        CartProduct cartProduct = new CartProduct(1L, "상품1", 3);

        cart.flushCart(userId);

        List<CartProduct> start = cart.getCartInfos(userId);
        System.out.println(start);

        // when
        cart.addProduct(userId, cartProduct);
        List<CartProduct> cartInfos = cart.getCartInfos(userId);
        System.out.println(cartInfos);

        // then
        assertThat(cartInfos.size()).isEqualTo(1);
        assertThat(cartInfos.get(0)).isEqualTo(cartProduct);

        // when2
        cart.deleteProduct(userId, Arrays.asList(cartProduct.getProductNo()));

        // then
        List<CartProduct> cartInfos2 = cart.getCartInfos(userId);
        System.out.println(cartInfos2);
        assertThat(cartInfos2.size()).isEqualTo(0);
    }

    @TestConfiguration
    static class Config {

        @Autowired
        RedisTemplate<String, String> redisTemplate;

        @Bean
        public Cart cart() {
            return new CartV2(redisTemplate);
        }
    }
}
