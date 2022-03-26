package shop.ozip.dev.src.seller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.ozip.dev.src.seller.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}

