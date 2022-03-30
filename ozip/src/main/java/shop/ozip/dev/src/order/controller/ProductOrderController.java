package shop.ozip.dev.src.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponse;
import shop.ozip.dev.src.order.entity.ProductOrder;
import shop.ozip.dev.src.order.model.GetProductOrderRes;
import shop.ozip.dev.src.order.model.ProductOrderInfo;
import shop.ozip.dev.src.order.provider.ProductOrderProvider;
import shop.ozip.dev.src.order.service.ProductOrderService;
import shop.ozip.dev.src.order.model.PostProductOrderReq;
import shop.ozip.dev.src.order.model.PostProductOrderRes;
import shop.ozip.dev.src.product.option.model.ColorOptions;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@ResponseBody
@RequestMapping("/app/stores")
public class ProductOrderController {

    private final ProductOrderService productOrderService;
    private final ProductOrderProvider productOrderProvider;

    @PostMapping("/products/{productId}/order")
    public BaseResponse<PostProductOrderRes> orderProduct(@PathVariable Long productId, @RequestBody PostProductOrderReq productOrderReq) {
        productOrderReq.setProductId(productId);

        try {
            PostProductOrderRes productOrderRes = productOrderService.order(productOrderReq);
            return new BaseResponse<>(productOrderRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/orders")
    public BaseResponse<GetProductOrderRes> getAllOrders() throws BaseException {
        try {
            List<ProductOrder> orders = productOrderProvider.getProductOrder();
            List<ProductOrderInfo> orderInfos = new ArrayList<>();

            for (ProductOrder o : orders) {
                ProductOrderInfo productOrderInfo = new ProductOrderInfo(o);
                productOrderInfo.setColorOptions(new ColorOptions(o.getOptionId()));
                orderInfos.add(productOrderInfo);
            }

            GetProductOrderRes getProductOrderRes = new GetProductOrderRes(orderInfos);
            return new BaseResponse<>(getProductOrderRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
