package organization.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;
import organization.util.JpaUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductsDao {
    public List<Object[]> getProductsSortedByMarginFromProduct(String sortOrder, int limit, String startDate, String endDate,
                                                               Integer categoryId) { // <- null = brak filtra
        EntityManager entityManager = JpaUtil.getEntityManager();
        try {
            // 1) sanitacja sortOrder
            String sort = "ASC".equalsIgnoreCase(sortOrder) ? "ASC" : "DESC";

            // 2) budujemy JPQL z opcjonalnym warunkiem po category_id
            StringBuilder jpql = new StringBuilder(
                    "SELECT p.name, p.buyPrice, p.sellPrice, p.tax " +
                            "FROM Product p " +
                            "WHERE p.deliveryDate >= :startDate AND p.deliveryDate <= :endDate"
            );
            if (categoryId != null) {
                jpql.append(" AND p.categoryId = :categoryId");
            }
            jpql.append(" ORDER BY p.name");

            TypedQuery<Object[]> q = entityManager.createQuery(jpql.toString(), Object[].class);
            q.setParameter("startDate", startDate);
            q.setParameter("endDate", endDate);
            if (categoryId != null) {
                q.setParameter("categoryId", categoryId);
            }

            // 3) przetwarzamy, sortujemy po marży i limitujemy
            return q.getResultStream()
                    .map(row -> {
                        String name = (String) row[0];
                        Double buyBrutto = (Double) row[1];
                        Double sellBrutto = (Double) row[2];
                        Integer tax = (Integer) row[3];

                        double mult = (100.0 + tax) / 100.0;
                        double buyNetto = buyBrutto / mult;
                        double sellNetto = sellBrutto / mult;
                        double diff = sellNetto - buyNetto;
                        double margin = diff / sellNetto * 100;

                        return new Object[]{name, buyNetto, sellNetto, diff, margin};
                    })
                    .sorted((a, b) -> {
                        double m1 = (Double) a[4], m2 = (Double) b[4];
                        return "ASC".equalsIgnoreCase(sort)
                                ? Double.compare(m1, m2)
                                : Double.compare(m2, m1);
                    })
                    .limit(limit)
                    .collect(Collectors.toList());

        } finally {
            entityManager.close();
        }
    }
}