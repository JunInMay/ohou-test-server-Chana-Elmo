package shop.ozip.dev.src.seller.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.seller.entity.Seller;
import shop.ozip.dev.src.seller.repository.SellerRepository;

@RequiredArgsConstructor
@Service
@ResponseBody
public class SellerProvider {

    private final SellerRepository sellerRepository;

    public Seller getSeller(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(()
        -> new IllegalArgumentException("해당 아이디의 판매자가 존재하지 않습니다."));

        return seller;
    }
}
