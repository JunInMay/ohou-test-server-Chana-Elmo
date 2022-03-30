package shop.ozip.dev.src.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.global.repository.BaseTimeEntity;
import shop.ozip.dev.src.order.entity.ProductOrder;
import shop.ozip.dev.src.order.provider.ProductOrderProvider;
import shop.ozip.dev.src.order.repository.ProductOrderRepository;
import shop.ozip.dev.src.order.model.PostProductOrderReq;
import shop.ozip.dev.src.order.model.PostProductOrderRes;
import shop.ozip.dev.src.product.option.provider.OptionProvider;
import shop.ozip.dev.src.product.provider.ProductProvider;
import shop.ozip.dev.utils.JwtService;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@ResponseBody
public class ProductOrderService extends BaseTimeEntity {

    private final ProductOrderRepository productOrderRepository;
    private final ProductProvider productProvider;
    private final OptionProvider optionProvider;
    private final JwtService jwtService;

    @Transactional
    public PostProductOrderRes order(PostProductOrderReq productOrderReq) throws BaseException {

        ProductOrder productOrder = ProductOrder.builder()
                .productId(productProvider.getProduct(productOrderReq.getProductId()))
                .optionId(optionProvider.getOption(productOrderReq.getOptionId()))
                .productCount(productOrderReq.getProductCount())
                .price(productOrderReq.getPrice())
                .userId(jwtService.getUserId())
                .status("ORDER-COMPLETE")
                .build();
        ProductOrder po = productOrderRepository.save(productOrder);

        PostProductOrderRes productOrderRes = new PostProductOrderRes(po.getId());
        return productOrderRes;
    }
}
