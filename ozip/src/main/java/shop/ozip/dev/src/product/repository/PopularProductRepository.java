package shop.ozip.dev.src.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.ozip.dev.src.product.entity.PopularProduct;

public interface PopularProductRepository extends JpaRepository<PopularProduct, Long> {
}
