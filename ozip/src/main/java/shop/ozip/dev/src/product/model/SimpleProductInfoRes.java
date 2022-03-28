package shop.ozip.dev.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.ozip.dev.src.product.entity.Product;

@Getter
@Setter
@AllArgsConstructor
public class SimpleProductInfoRes {
    private Long productId;
    private String brand;
    private String name;
    private byte isDeal;
    private int leftTime = 0;
    private int price;
    private int discountRate;
    private double ratedAverage;
    private int reviewCount;
    private int deliveryAmount;

    public SimpleProductInfoRes(Product product) {
        this.productId = product.getId();
        this.brand = product.getBrand();
        this.name = product.getName();
        this.price = product.getPrice();
        this.discountRate = product.getDiscountRate();
        this.ratedAverage = product.getRatedAverage();
        this.reviewCount = product.getReviewCount();
        this.deliveryAmount = product.getDeliveryAmount();
        this.isDeal = product.getIsDeal();
    }
}
