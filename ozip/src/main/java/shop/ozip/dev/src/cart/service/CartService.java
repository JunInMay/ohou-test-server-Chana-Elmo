package shop.ozip.dev.src.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.cart.entity.Cart;
import shop.ozip.dev.src.cart.model.PostCartReq;
import shop.ozip.dev.src.cart.provider.CartProvider;
import shop.ozip.dev.src.cart.repository.CartRepository;
import shop.ozip.dev.utils.JwtService;

@RequiredArgsConstructor
@Service
@ResponseBody
public class CartService {

    private final CartProvider cartProvider;
    private final CartItemService cartItemService;
    private final JwtService jwtService;
    private final CartRepository cartRepository;

    public Cart createCart(Long userId) {
        Cart cart = cartRepository.save(Cart.builder()
                .userId(userId)
                .totalPrice(0)
                .deliveryAmount(0)
                .productCount(0)
                .build());

        return cart;
    }

    public Long insert(PostCartReq postCartReq) throws BaseException {
        Long userId = jwtService.getUserId();
        Cart cart = cartProvider.getCart(userId);

        // 카트가 없으면 새로 생성
        if (cart == null) {
            cart = createCart(userId);
        }

        // 장바구니에 담을 아이템 생성
        if (cartItemService.createItem(cart, postCartReq) == null) {
            return null;
        }

        return cart.getId();
    }
}
