package shop.ozip.dev.src.cart.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostCartReq {
    private Long productId;
    private Long optionId;
    private int productCount;
    private int price;
    private int deliveryAmount;
}
