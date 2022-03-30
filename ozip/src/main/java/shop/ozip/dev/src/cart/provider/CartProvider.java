package shop.ozip.dev.src.cart.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.ozip.dev.src.cart.entity.Cart;
import shop.ozip.dev.src.cart.repository.CartRepository;

@RequiredArgsConstructor
@Service
@ResponseBody
public class CartProvider {
    private final CartRepository cartRepository;

    public Cart getCart(Long userId) {
        try {
            Cart cart = cartRepository.findByUserId(userId);
            return cart;
        } catch (NullPointerException exception) {
            return null;
        }
    }
}
