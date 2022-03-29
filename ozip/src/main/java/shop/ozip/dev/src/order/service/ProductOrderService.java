package shop.ozip.dev.src.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponse;
import shop.ozip.dev.src.global.repository.BaseTimeEntity;
import shop.ozip.dev.src.order.entity.ProductOrder;
import shop.ozip.dev.src.order.repository.ProductOrderRepository;
import shop.ozip.dev.src.product.model.ProductOrderReq;
import shop.ozip.dev.src.product.model.ProductOrderRes;
import shop.ozip.dev.utils.JwtService;

import javax.persistence.criteria.Order;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@ResponseBody
public class ProductOrderService extends BaseTimeEntity {

    private final ProductOrderRepository productOrderRepository;
    private final JwtService jwtService;

    @Transactional
    public ProductOrderRes order(ProductOrderReq productOrderReq) throws BaseException {

        ProductOrder productOrder = ProductOrder.builder()
                .productId(productOrderReq.getProductId())
                .optionId(productOrderReq.getOptionId())
                .productCount(productOrderReq.getProductCount())
                .price(productOrderReq.getPrice())
                .userId(jwtService.getUserId())
                .status("ORDER-COMPLETE")
                .build();
        ProductOrder po = productOrderRepository.save(productOrder);

        ProductOrderRes productOrderRes = new ProductOrderRes(po.getId());
        return productOrderRes;
    }
}
