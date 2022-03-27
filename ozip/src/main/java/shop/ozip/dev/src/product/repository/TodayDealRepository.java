package shop.ozip.dev.src.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.ozip.dev.src.product.entity.TodayDeal;

public interface TodayDealRepository extends JpaRepository<TodayDeal, Long> {
}
