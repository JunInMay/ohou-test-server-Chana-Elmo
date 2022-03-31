package shop.ozip.dev.src.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.ozip.dev.src.cart.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
