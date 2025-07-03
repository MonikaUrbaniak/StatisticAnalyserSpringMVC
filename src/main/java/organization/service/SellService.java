package organization.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import organization.dao.ProductsDao;
import organization.dao.SellDao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
@Component
public class SellService {
    private SellDao sellDao;

    public SellService(SellDao sellDao) {
        this.sellDao = sellDao;
    }

    public JSONArray showTopSellingProductsLastMonth(int limit) {
//        SellDao sellDao = new SellDao();
        List<Object[]> result = sellDao.getTopSellingProductsFromLastMonth(limit);
        JSONArray jsonArray = new JSONArray();

        if (!result.isEmpty()) {
            System.out.println("=== Top 20 produktów z największą sprzedażą w poprzednim miesiącu ===");
            int i = 1;
            for (Object[] row : result) {
                String name = (String) row[0];
                double totalQuantity = ((Number) row[1]).doubleValue();
                double totalRevenue = ((Number) row[2]).doubleValue();

                System.out.printf("%2d. Produkt: %-20s | Ilość: %.0f | Przychód: %.2f\n",
                        i++, name, totalQuantity, totalRevenue);
            }
        } else {
            System.out.println("Brak wyników.");
        }

        for (Object[] row : result) {
            JSONObject obj = new JSONObject();
            obj.put("name", row[0]);
            obj.put("totalQuantity", ((Number) row[1]).doubleValue());
            obj.put("totalRevenue", ((Number) row[2]).doubleValue());
            jsonArray.put(obj);
        }
        return jsonArray;
    }



    public JSONArray showAmountOfSoldProdustsByDate(int limit, String startDate, String endDate, String productName) {
        SellDao sellDao = new SellDao();
        List<Object[]> result = sellDao.getAmountOfSoldProdustsByDate(limit,  startDate, endDate, productName);
        JSONArray jsonArray = new JSONArray();

        if (!result.isEmpty()) {
            System.out.println("\n=== Ilość produktów sprzedanych ===");
            int i = 1;
            for (Object[] row : result) {
                String name = (String) row[0];
                double quantity = ((Number) row[1]).doubleValue();

                System.out.printf("%2d. Produkt: %-20s | Ilość: %.2f\n",
                        i++, name, quantity);
            }
        } else {
            System.out.println("Brak wyników do wyświetlenia.");
        }

        for (Object[] row : result) {
            JSONObject obj = new JSONObject();
            obj.put("name", row[0]);
            obj.put("quantity", ((Number) row[1]).doubleValue());
            jsonArray.put(obj);
        }
        return jsonArray;
    }

    public List<Object[]> getRawAmountOfGasBottlesByDate(String startDate, String endDate) {
        return new SellDao().getAmountOfGasBottlesByDate(startDate, endDate);
    }

    public List<Object[]> getMonthlyAmountOfGasBottlesByDate(String startDate, String endDate) {
        return new SellDao().getMonthlyAmountOfGasBottlesByDate(startDate, endDate);
    }

    public JSONArray showAmountOfGasBottlesByDate(String startDate, String endDate) {
        SellDao sellDao = new SellDao();
        List<Object[]> result = sellDao.getAmountOfGasBottlesByDate(startDate, endDate);
        JSONArray jsonArray = new JSONArray();

        if (!result.isEmpty()) {
            System.out.println("\n=== Ilość sprzedanych butli===");
            int i = 1;
            for (Object[] row : result) {
                String name = (String) row[0];
                double quantity = ((Number) row[1]).doubleValue();

                System.out.printf("%2d. Produkt: %-20s | Ilość: %.2f\n",
                        i++, name, quantity);
            }
        } else {
            System.out.println("Brak wyników do wyświetlenia.");
        }

        for (Object[] row : result) {
            JSONObject obj = new JSONObject();
            obj.put("name", row[0]);
            obj.put("quantity", ((Number) row[1]).doubleValue());
            jsonArray.put(obj);
        }
        return jsonArray;
    }
}
