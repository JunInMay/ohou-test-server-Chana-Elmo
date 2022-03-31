package shop.ozip.dev.src.order.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostProductOrderReq {
    private Long productId;
    private Long optionId;
    private int productCount;
    private int price;
}
