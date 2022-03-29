package shop.ozip.dev.src.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.ozip.dev.src.category.entity.SubCategory;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
}
