package organization.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jdk.jfr.Category;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import organization.util.JpaUtil;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryDao {

    public List<String> getAllCategories() {
        EntityManager entityManager = JpaUtil.getEntityManager();
        List<String> results = new ArrayList<>();
        try {
            String jpql = "SELECT DISTINCT p.name " +
                    "FROM Category p " +
                    "WHERE p.active = 'T' " +
                    "ORDER BY p.name";
            TypedQuery<String> query = entityManager.createQuery(jpql, String.class);
            results = query.getResultList();
//            results = rawResults.stream()
//                    .map( category -> category.getName())
//                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return results;
    }

    public Integer getCategoryIdByName(String name) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        Integer result;
        try {
            String jpql = "SELECT c.id " +
                    "FROM Category c " +
                    "WHERE c.name = :name";
            TypedQuery<Integer> query = entityManager.createQuery(jpql, Integer.class);
            query.setParameter("name", name);
            result = query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            entityManager.close();
        }
        return result;
    }
}
