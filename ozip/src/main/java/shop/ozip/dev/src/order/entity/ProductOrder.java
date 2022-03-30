package shop.ozip.dev.src.order.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.ozip.dev.src.category.entity.SubCategory;
import shop.ozip.dev.src.global.repository.BaseTimeEntity;
import shop.ozip.dev.src.product.entity.Product;
import shop.ozip.dev.src.product.option.entity.MachineOption;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class ProductOrder extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "id", nullable = false)
    private Product productId;

    @ManyToOne
    @JoinColumn(name = "optionId", referencedColumnName = "id", nullable = false)
    private MachineOption optionId;

    @Column(nullable = false)
    private int productCount;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String status;

}
