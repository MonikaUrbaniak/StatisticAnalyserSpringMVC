package organization.service;

import org.springframework.stereotype.Service;
import organization.dao.CategoryDao;

import java.util.List;

@Service
public class CategoryService {

    private CategoryDao categoryDao;

    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public List<String> getAllCategories() {
        return categoryDao.getAllCategories();
    }
    public Integer getCategoryIdByName(String name){
        return categoryDao.getCategoryIdByName(name);
    }
}