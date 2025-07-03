package organization.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import organization.dao.ProductsDao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ProductService {

    private ProductsDao productsDao;

    public ProductService(ProductsDao productsDao) {
        this.productsDao = productsDao;
    }

    public JSONArray showMargin(String sortOrder, int limit, String startDate, String endDate, Integer categoryId) {

//        ProductsDao productsDao = new ProductsDao();
        List<Object[]> result = productsDao.getProductsSortedByMarginFromProduct(sortOrder, limit, startDate, endDate, categoryId);
        JSONArray jsonArray = new JSONArray();
//        System.out.println("MARGIN:\n");
        if (!result.isEmpty()) {
            System.out.println("\n=== Lista produktów posortowana wg marży (" + sortOrder.toUpperCase() + ") ===");
            int i = 1;
            for (Object[] row : result) {
                String name = (String) row[0];
                double buyPrice = ((Number) row[1]).doubleValue();
                double sellPrice = ((Number) row[2]).doubleValue();
                double diffNetto = ((Number) row[3]).doubleValue();
                double margin = ((Number) row[4]).doubleValue();

                System.out.printf("%2d. Produkt: %-20s | Kupno: %.2f | Sprzedaż: %.2f | Różnica: %.2f | Marża: %.2f%%\n",
                        i++, name, buyPrice, sellPrice, diffNetto, margin);
            }
        } else {
            System.out.println("Brak wyników do wyświetlenia.");
        }

        for (Object[] row : result) {
            JSONObject obj = new JSONObject();
            obj.put("name", row[0]);

            double buyPrice = ((Number) row[1]).doubleValue();
            double sellPrice = ((Number) row[2]).doubleValue();
            double diffNetto = ((Number) row[3]).doubleValue();
            double margin = ((Number) row[4]).doubleValue();

            obj.put("buyPrice", round(buyPrice));
            obj.put("sellPrice", round(sellPrice));
            obj.put("diffNetto", round(diffNetto));
            obj.put("margin", round(margin));

            jsonArray.put(obj);
        }
        return jsonArray;
    }
    private double round(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
