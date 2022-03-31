package shop.ozip.dev.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetProductOrderRes {
    private List<ProductOrderInfo> productOrderInfoList;
}
