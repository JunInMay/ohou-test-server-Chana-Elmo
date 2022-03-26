package shop.ozip.dev.src.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.ozip.dev.src.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
