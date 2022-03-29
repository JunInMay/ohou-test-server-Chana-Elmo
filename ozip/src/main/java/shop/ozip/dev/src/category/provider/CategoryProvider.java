package shop.ozip.dev.src.category.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.ozip.dev.src.category.entity.Category;
import shop.ozip.dev.src.category.entity.CategoryItem;
import shop.ozip.dev.src.category.entity.SubCategory;
import shop.ozip.dev.src.category.entity.SubCategoryItem;
import shop.ozip.dev.src.category.repository.CategoryItemRepository;
import shop.ozip.dev.src.category.repository.CategoryRepository;
import shop.ozip.dev.src.category.repository.SubCategoryItemRepository;
import shop.ozip.dev.src.category.repository.SubCategoryRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@ResponseBody
@Service
public class CategoryProvider {
    public String getFullCategoryPath(SubCategory subCategory) {
        SubCategoryItem subCategoryItem = subCategory.getSubCategoryItem();
        Category category = subCategory.getCategory();
        CategoryItem categoryItem = category.getCategoryItem();
        String categoryPath = "";

        categoryPath = category.getName() + " > " + categoryItem.getName() + " > " + subCategory.getName();
        categoryPath += (subCategoryItem != null) ? String.format(" > " + subCategoryItem.getName()) : "";

        return categoryPath;
    }
}
