package shop.ozip.dev.src.cart.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.ozip.dev.src.global.repository.BaseTimeEntity;
import shop.ozip.dev.src.product.entity.Product;
import shop.ozip.dev.src.product.option.entity.MachineOption;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class CartItem extends BaseTimeEntity {
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
    private int count;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    private int deliveryAmount;

    @ManyToOne
    @JoinColumn(name = "cartId", referencedColumnName = "id", nullable = false)
    private Cart cartId;
}
