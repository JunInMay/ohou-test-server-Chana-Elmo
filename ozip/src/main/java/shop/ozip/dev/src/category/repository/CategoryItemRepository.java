package shop.ozip.dev.src.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.ozip.dev.src.category.entity.CategoryItem;

public interface CategoryItemRepository extends JpaRepository<CategoryItem, Long> {
}
