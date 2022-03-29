package shop.ozip.dev.src.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponse;
import shop.ozip.dev.src.order.service.ProductOrderService;
import shop.ozip.dev.src.product.model.ProductOrderReq;
import shop.ozip.dev.src.product.model.ProductOrderRes;

@RequiredArgsConstructor
@RestController
@ResponseBody
@RequestMapping("/app/stores")
public class ProductOrderController {

    private final ProductOrderService productOrderService;

    @PostMapping("/products/{productId}/order")
    public BaseResponse<ProductOrderRes> orderProduct(@PathVariable Long productId, @RequestBody ProductOrderReq productOrderReq) {
        productOrderReq.setProductId(productId);

        try {
            ProductOrderRes productOrderRes = productOrderService.order(productOrderReq);
            return new BaseResponse<>(productOrderRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
