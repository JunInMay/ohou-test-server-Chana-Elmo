package shop.ozip.dev.src.product.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductOrderReq {
    private Long productId;
    private Long optionId;
    private int productCount;
    private int price;
}
