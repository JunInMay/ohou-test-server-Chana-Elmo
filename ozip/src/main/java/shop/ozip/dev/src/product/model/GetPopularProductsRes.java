package shop.ozip.dev.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.ozip.dev.src.product.entity.Product;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetPopularProductsRes {
    private List<SimpleProductInfoRes> popularProductList;
}
