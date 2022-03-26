package shop.ozip.dev.src.product.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.ozip.dev.src.global.repository.BaseTimeEntity;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int deliveryAmount;

    @Column(nullable = false)
    private int discountRate;

    @Column(nullable = false)
    private int point;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private double ratedAverage;

    @Column(nullable = false)
    private int reviewCount;

    @Column(nullable = false)
    private int sellCount;

    @Column(nullable = false)
    private Long categoryId;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private byte isDeal;
}
