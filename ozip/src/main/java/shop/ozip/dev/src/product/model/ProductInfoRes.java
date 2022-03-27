package shop.ozip.dev.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.ozip.dev.src.product.entity.Product;

@Getter
@Setter
@AllArgsConstructor
public class ProductInfoRes {
    private Long id;
    private String imgUrl;
    private String brand;
    private String name;
    private int reviewCount;
    private byte isDeal;
    private int leftTime = 0;
    private int price;
    private int discountRate;
    private int point;
    private int deliveryAmount;
    private String description;
    private double ratedAverage;
    // 별점별 개수 리스트
//    private List<Riview>
    // 문의 개수
    // private int questionCount;

    public ProductInfoRes(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.deliveryAmount = product.getDeliveryAmount();
        this.discountRate = product.getDiscountRate();
        this.point = product.getPoint();
        this.description = product.getDescription();
        this.imgUrl = product.getImgUrl();
        this.ratedAverage = product.getRatedAverage();
        this.reviewCount = product.getReviewCount();
        this.brand = product.getBrand();
        this.isDeal = product.getIsDeal();
    }
}
