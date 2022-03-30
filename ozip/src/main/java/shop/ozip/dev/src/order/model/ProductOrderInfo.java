package shop.ozip.dev.src.order.model;

import lombok.*;
import shop.ozip.dev.src.order.entity.ProductOrder;
import shop.ozip.dev.src.product.option.model.ColorOptions;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ProductOrderInfo {

    private Long orderId;
    private LocalDateTime orderDate;
    private String productImg;
    private String productName;
    private ColorOptions colorOptions;
    private int price;
    private int productCount;
    private String status;

    public ProductOrderInfo(ProductOrder productOrder) {
        this.orderId = productOrder.getId();
        this.orderDate = productOrder.getCreatedAt();
        this.productImg = productOrder.getProductId().getImgUrl();
        this.productName = productOrder.getProductId().getName();
        this.price = productOrder.getPrice();
        this.productCount = productOrder.getProductCount();
        this.status = productOrder.getStatus();
    }
}
