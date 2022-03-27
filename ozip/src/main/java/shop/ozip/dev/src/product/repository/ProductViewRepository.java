package shop.ozip.dev.src.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.ozip.dev.src.product.entity.ProductView;

import java.util.List;

public interface ProductViewRepository extends JpaRepository<ProductView, Long> {
    public List<ProductView> findAllByUserId(Long userId);
}
