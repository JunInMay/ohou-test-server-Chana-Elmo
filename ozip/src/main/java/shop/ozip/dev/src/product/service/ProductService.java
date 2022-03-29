package shop.ozip.dev.src.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.product.entity.ProductView;
import shop.ozip.dev.src.product.repository.ProductViewRepository;
import shop.ozip.dev.utils.JwtService;

@RequiredArgsConstructor
@Service
@ResponseBody
public class ProductService {
    // 상품 상세 조회 시 최근 본 상품 리스트에 해당 상품 추가
    private final ProductViewRepository productViewRepository;
    private final JwtService jwtService;

    public void addViewedProduct(Long productId) throws BaseException {
        Long userId = jwtService.getUserId();

        if (productViewRepository.findProductViewByProductIdAndUserId(productId, userId) != null) {
            return;
        }

        ProductView productView = ProductView.builder()
                .productId(productId)
                .userId(userId)
                .build();

        productViewRepository.save(productView);
    }
}
