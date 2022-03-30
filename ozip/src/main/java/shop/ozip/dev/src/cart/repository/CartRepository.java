package shop.ozip.dev.src.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.ozip.dev.src.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long userId);
}
