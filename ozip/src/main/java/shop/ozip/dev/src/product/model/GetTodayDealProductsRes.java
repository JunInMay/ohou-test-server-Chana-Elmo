package shop.ozip.dev.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetTodayDealProductsRes {
    private List<SimpleProductInfoRes> todayDealList;
}
