package shop.ozip.dev.src.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.ozip.dev.src.order.entity.ProductOrder;
import shop.ozip.dev.src.product.entity.Product;

import java.util.List;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
    List<ProductOrder> findAllByUserId (Long userId);
}
