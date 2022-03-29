package shop.ozip.dev.src.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponse;
import shop.ozip.dev.src.product.model.GetPopularProductsRes;
import shop.ozip.dev.src.product.model.GetProductDetailsInfoRes;
import shop.ozip.dev.src.product.model.GetTodayDealProductsRes;
import shop.ozip.dev.src.product.model.GetViewProductRes;
import shop.ozip.dev.src.product.option.entity.ColorOption;
import shop.ozip.dev.src.product.option.entity.MachineOption;
import shop.ozip.dev.src.product.option.model.ColorOptions;
import shop.ozip.dev.src.product.option.model.MachineOptionsRes;
import shop.ozip.dev.src.product.option.provider.OptionProvider;
import shop.ozip.dev.src.product.provider.ProductProvider;
import shop.ozip.dev.src.product.service.ProductService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@ResponseBody
@RequestMapping("/app/stores")
public class ProductController {

    private final ProductService productService;
    private final ProductProvider productProvider;
    private final OptionProvider optionProvider;

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

    @GetMapping("/products/{productId}")
    public BaseResponse<GetProductDetailsInfoRes> getProductDetailInfo(@PathVariable Long productId) {
        try {
            GetProductDetailsInfoRes getProductDetailsInfoRes = productProvider.getProductDetailInfo(productId);
            return new BaseResponse<>(getProductDetailsInfoRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/products/{productId}/options")
    public BaseResponse<MachineOptionsRes> getProductOption(@PathVariable Long productId) {
        try {
            List<MachineOption> machineOptions = optionProvider.getAllMachineOptions(productId);
            List<ColorOptions> options = new ArrayList<>();

            for (MachineOption mo : machineOptions) {
                options.add(new ColorOptions(mo.getColorId().getColor()));
            }

            MachineOptionsRes machineOptionsRes = new MachineOptionsRes(options);
            return new BaseResponse<>(machineOptionsRes);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
