package shop.ozip.dev.src.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponse;
import shop.ozip.dev.src.product.model.GetPopularProductsRes;
import shop.ozip.dev.src.product.model.GetTodayDealProductsRes;
import shop.ozip.dev.src.product.model.GetViewProductRes;
import shop.ozip.dev.src.product.provider.ProductProvider;
import shop.ozip.dev.src.product.service.ProductService;

@RequiredArgsConstructor
@RestController
@ResponseBody
@RequestMapping("/app/stores")
public class ProductController {

    private final ProductService productService;
    private final ProductProvider productProvider;

    @GetMapping("/popular-products")
    public BaseResponse<GetPopularProductsRes> getPopularProducts() {
        try {
            GetPopularProductsRes getPopularProductsRes = productProvider.retrievePopularProducts();
            return new BaseResponse<>(getPopularProductsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/today-deals-products")
    public BaseResponse<GetTodayDealProductsRes> getTodayDealProducts() {
        try {
            GetTodayDealProductsRes getTodayDealProductsRes = productProvider.retrieveTodayDealProducts();
            return new BaseResponse<>(getTodayDealProductsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/view-products")
    public BaseResponse<GetViewProductRes> getViewProducts() {
        try {
            GetViewProductRes getViewProductRes = productProvider.retrieveViewProducts();
            return new BaseResponse<>(getViewProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
