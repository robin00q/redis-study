package me.sjlee.redis_study.learn.example.cart;

import java.util.Objects;

public class CartProduct {

    private Long productNo;
    private String productName;
    private Integer count;

    public CartProduct() {
    }

    public CartProduct(Long productNo, String productName, Integer count) {
        this.productNo = productNo;
        this.productName = productName;
        this.count = count;
    }

    @Override
    public String toString() {
        return "CartProduct{" +
                "productNo=" + productNo +
                ", productName='" + productName + '\'' +
                ", count=" + count +
                '}';
    }

    public Long getProductNo() {
        return productNo;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartProduct that = (CartProduct) o;
        return Objects.equals(productNo, that.productNo) && Objects.equals(productName, that.productName) && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productNo, productName, count);
    }
}
