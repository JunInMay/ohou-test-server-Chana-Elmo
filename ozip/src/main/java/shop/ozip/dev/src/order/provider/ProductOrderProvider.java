package shop.ozip.dev.src.order.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.order.entity.ProductOrder;
import shop.ozip.dev.src.order.repository.ProductOrderRepository;
import shop.ozip.dev.utils.JwtService;

import java.util.List;

@RequiredArgsConstructor
@Service
@ResponseBody
public class ProductOrderProvider {

    private final ProductOrderRepository productOrderRepository;
    private final JwtService jwtService;

    public List<ProductOrder> getProductOrder() throws BaseException {
        List<ProductOrder> orders = productOrderRepository.findAllByUserId(jwtService.getUserId());

        return orders;
    }
}
