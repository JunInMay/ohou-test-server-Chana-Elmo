package shop.ozip.dev.src.product.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.category.provider.CategoryProvider;
import shop.ozip.dev.src.product.entity.PopularProduct;
import shop.ozip.dev.src.product.entity.Product;
import shop.ozip.dev.src.product.entity.ProductView;
import shop.ozip.dev.src.product.entity.TodayDeal;
import shop.ozip.dev.src.product.model.*;
import shop.ozip.dev.src.product.repository.PopularProductRepository;
import shop.ozip.dev.src.product.repository.ProductRepository;
import shop.ozip.dev.src.product.repository.ProductViewRepository;
import shop.ozip.dev.src.product.repository.TodayDealRepository;
import shop.ozip.dev.src.product.service.ProductService;
import shop.ozip.dev.utils.JwtService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@ResponseBody
public class ProductProvider {

    private final ProductRepository productRepository;
    private final PopularProductRepository popularProductRepository;
    private final TodayDealRepository todayDealRepository;
    private final ProductViewRepository productViewRepository;
    private final JwtService jwtService;
    private final CategoryProvider categoryProvider;
    private final ProductService productService;

    public Product getProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()
                -> new IllegalArgumentException("해당 아이디의 상품이 존재하지 않습니다."));
        return product;
    }

    public GetPopularProductsRes retrievePopularProducts() throws BaseException {
        List<PopularProduct> popularProductList = popularProductRepository.findAll();
        List<SimpleProductInfoRes> productList = new ArrayList<>();

        for (PopularProduct pp : popularProductList) {
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
            Product product = getProduct(td.getProductId());
            SimpleProductInfoRes simpleProductInfoRes = new SimpleProductInfoRes(product);
            simpleProductInfoRes.setLeftTime(td.getLeftTime());

            productList.add(simpleProductInfoRes);
        }

        GetTodayDealProductsRes getTodayDealProductsRes = new GetTodayDealProductsRes(productList);
        return getTodayDealProductsRes;
    }

    public GetViewProductRes retrieveViewProducts() throws BaseException {
        Long userId = jwtService.getUserId();
        // user01 Test
//        Long userId = 1L;
        List<ProductView> viewProductList = productViewRepository.findAllByUserId(userId);
        List<SimpleProductInfoRes> productList = new ArrayList<>();

        for (ProductView pv : viewProductList) {
            Product product = getProduct(pv.getProductId());
            SimpleProductInfoRes simpleProductInfoRes = new SimpleProductInfoRes(product);

            productList.add(simpleProductInfoRes);
        }

        GetViewProductRes getViewProductRes = new GetViewProductRes(productList);
        return getViewProductRes;
    }

    public GetProductDetailsInfoRes getProductDetailInfo(Long productId) throws BaseException{
        Product product = getProduct(productId);
        DetailProductInfoRes detailProductInfoRes = new DetailProductInfoRes(product);
        RatedCountList ratedCountList = new RatedCountList();

        detailProductInfoRes.setCategory(categoryProvider.getFullCategoryPath(product.getSubCategory()));
        detailProductInfoRes.setRatedCountList(ratedCountList);

        GetProductDetailsInfoRes getProductDetailsInfoRes = new GetProductDetailsInfoRes(detailProductInfoRes);
        productService.addViewedProduct(productId);
        return getProductDetailsInfoRes;
    }

}
