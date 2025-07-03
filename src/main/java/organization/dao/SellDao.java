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
public class SellDao{


    public List<Object[]> getTopSellingProductsFromLastMonth(int limit) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        List<Object[]> results = new ArrayList<>();
        LocalDate previousMonth = LocalDate.now().minusMonths(1);
        String yearMonthPattern = String.format("%04d-%02d", previousMonth.getYear(), previousMonth.getMonthValue());
        try {
            TypedQuery<Object[]> query = entityManager.createQuery(
                    "SELECT s.productName, SUM(s.quantity), SUM(s.quantity * s.sellPrice) " +
                            "FROM Sell s " +
                            "WHERE s.sellDate LIKE :yearMonthPattern " +
                            "GROUP BY s.productName " +
                            "ORDER BY SUM(s.quantity) DESC",
                    Object[].class
            );
            query.setParameter("yearMonthPattern", yearMonthPattern + "%");
            query.setMaxResults(limit);

            results = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return results;
    }


    public List<Object[]> getAmountOfSoldProdustsByDate(int limit, String startDate, String endDate, String productName) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        List<Object[]> results = new ArrayList<>();

        try {
            String jpql = "SELECT s.productName, SUM(s.quantity) " +
                    "FROM Sell s " +
                    "WHERE s.productName = :productName " +
                    "AND s.sellDate >= :startDate AND s.sellDate <= :endDate " +
                    "AND s.active = 'T' " +
                    "GROUP BY s.productName";

            /*
            String jpql = "SELECT s.productName" +
                    "FROM Sell s " +
                    "WHERE s.productName = :productName " +
                    "AND s.sellDate >= :startDate AND s.sellDate <= :endDate " +
                    "AND s.active = 'T' ";
                    List<Sell> sells = ...;
                    Integer sum = 0;
                    for (Sell sell : sells) {
                        sum += sell.getQuantity();
                    }
             */
            TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
            query.setParameter("productName", productName); // np. "Propan butan gaz butla"
            query.setParameter("startDate", startDate); // np. "2025-03-01
            query.setParameter("endDate", endDate);     // np. "2025-05-31
            query.setMaxResults(limit);

            results = query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return results;
    }


    public List<Object[]> getAmountOfGasBottlesByDate(String startDate, String endDate) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        List<Object[]> results = new ArrayList<>();
        final String GAS_BOTTLES_CATEGORY = "Propan butan gaz butla";

        try {
            String jpql = "SELECT s.productName, SUM(s.quantity) " +
                    "FROM Sell s " +
                    "WHERE s.categoryId = (SELECT c.id FROM Category c WHERE c.name = :name) " +
                    "AND s.sellDate >= :startDate AND s.sellDate <= :endDate " +
                    "AND s.active = 'T' " +
                    "GROUP BY s.productName";

            TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
            query.setParameter("name", GAS_BOTTLES_CATEGORY); // np. "Propan butan gaz butla"
            query.setParameter("startDate", startDate); // np. "2025-03-01
            query.setParameter("endDate", endDate);     // np. "2025-05-31

            results = query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return results;
    }


    public List<Object[]> getMonthlyAmountOfGasBottlesByDate(String startDate, String endDate) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        List<Object[]> results = new ArrayList<>();
        final String GAS_BOTTLES_CATEGORY = "Propan butan gaz butla";

        try {
            // JPQL z użyciem function('strftime',…), aby wyciągnąć rok i miesiąc w formacie "YYYY-MM"
            String jpql = ""
                    + "SELECT function('strftime','%Y-%m', s.sellDate) AS yearMonth, "
                    + "       s.productName, "
                    + "       SUM(s.quantity) "
                    + "FROM Sell s "
                    + "WHERE s.categoryId = (SELECT c.id FROM Category c WHERE c.name = :name) "
                    + "  AND s.sellDate >= :startDate "
                    + "  AND s.sellDate <= :endDate "
                    + "  AND s.active = 'T' "
                    + "GROUP BY function('strftime','%Y-%m', s.sellDate), s.productName "
                    + "ORDER BY function('strftime','%Y-%m', s.sellDate), s.productName";

            // Tworzymy TypedQuery<Object[]> – wynik będzie List<Object[]> bez ostrzeżeń
            TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
            query.setParameter("name", GAS_BOTTLES_CATEGORY);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            results = query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return results;
    }
}