package shop.ozip.dev.src.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.ozip.dev.src.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
