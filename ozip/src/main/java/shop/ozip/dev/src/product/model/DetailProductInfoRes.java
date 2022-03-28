package shop.ozip.dev.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.ozip.dev.src.product.entity.Product;

@Getter
@Setter
@AllArgsConstructor
public class DetailProductInfoRes {
    // 상품 아이디
    private Long id;
    // 상품 카테고리 정보
    private String category;
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
    // 별점 별 개수
    private RatedCountList ratedCountList;
    // 문의 개수
    private int questionCount;

    public DetailProductInfoRes(Product product) {
        this.id = product.getId();
        this.imgUrl = product.getImgUrl();
        this.brand = product.getBrand();
        this.name = product.getName();
        this.reviewCount = product.getReviewCount();
        this.isDeal = product.getIsDeal();
        this.price = product.getPrice();
        this.discountRate = product.getDiscountRate();
        this.point = product.getPoint();
        this.deliveryAmount = product.getDeliveryAmount();
        this.description = product.getDescription();
        this.ratedAverage = product.getRatedAverage();
        this.questionCount = product.getQuestionCount();
    }
}
