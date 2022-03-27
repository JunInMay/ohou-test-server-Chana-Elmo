package shop.ozip.dev.src.product.provider;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.product.entity.PopularProduct;
import shop.ozip.dev.src.product.entity.Product;
import shop.ozip.dev.src.product.entity.TodayDeal;
import shop.ozip.dev.src.product.model.GetPopularProductsRes;
import shop.ozip.dev.src.product.model.GetTodayDealProductsRes;
import shop.ozip.dev.src.product.model.SimpleProductInfoRes;
import shop.ozip.dev.src.product.repository.PopularProductRepository;
import shop.ozip.dev.src.product.repository.ProductRepository;
import shop.ozip.dev.src.product.repository.TodayDealRepository;
import shop.ozip.dev.src.seller.provider.SellerProvider;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@ResponseBody
public class ProductProvider {

    private final ProductRepository productRepository;
    private final PopularProductRepository popularProductRepository;
    private final TodayDealRepository todayDealRepository;

    public Product getProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()
                -> new IllegalArgumentException("해당 아이디의 상품이 존재하지 않습니다."));
        return product;
    }

    public GetPopularProductsRes retrievePopularProducts() throws BaseException {
        List<PopularProduct> popularProductIdList = popularProductRepository.findAll();
        List<SimpleProductInfoRes> productList = new ArrayList<>();

        for (PopularProduct pp : popularProductIdList) {
            Product product = getProduct(pp.getId());
            SimpleProductInfoRes simpleProductInfoRes = new SimpleProductInfoRes(product);

            productList.add(simpleProductInfoRes);
        }

        GetPopularProductsRes getPopularProductsRes = new GetPopularProductsRes(productList);
        return getPopularProductsRes;
    }

    public GetTodayDealProductsRes retrieveTodayDealProducts() throws BaseException {
        List<TodayDeal> todayDealList = todayDealRepository.findAll();
        List<SimpleProductInfoRes> productList = new ArrayList<>();

        for (TodayDeal td : todayDealList) {
            Product product = getProduct(td.getId());
            SimpleProductInfoRes simpleProductInfoRes = new SimpleProductInfoRes(product);
            simpleProductInfoRes.setLeftTime(td.getLeftTime());

            productList.add(simpleProductInfoRes);
        }

        GetTodayDealProductsRes getTodayDealProductsRes = new GetTodayDealProductsRes(productList);
        return getTodayDealProductsRes;
    }

}
