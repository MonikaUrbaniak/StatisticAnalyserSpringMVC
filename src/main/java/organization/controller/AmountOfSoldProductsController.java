package organization.controller;

import org.json.JSONArray;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import organization.service.SellService;

@RestController
public class AmountOfSoldProductsController {

    private final SellService sellService;

    public AmountOfSoldProductsController(SellService sellService) {
        this.sellService = sellService;
    }

    @GetMapping("/product-sale")
    public Object getAmountOfSoldProducts(
            @RequestParam(name = "limit", required = false, defaultValue = "20") int limit,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "productName", required = false) String productName
    ) {
        if (startDate == null || endDate == null) {
            return new ErrorResponse("Brakuje parametrów 'startDate' lub 'endDate'.");
        }

        try {
            return new JSONArray(
                    sellService.showAmountOfSoldProdustsByDate(limit, startDate, endDate, productName).toString()
            );
        } catch (Exception e) {
            return new ErrorResponse("Wystąpił błąd podczas przetwarzania danych.");
        }
    }

    // Można dodać prostą klasę do komunikatów błędów
    static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}



//@WebServlet("/product-sale")
//public class AmountOfSoldProducts extends HttpServlet {
//
//    private SellService sellService;
//    @Override
//    public void init() throws ServletException {
//        sellService = SpringContext.getBean(SellService.class);
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        String limitParam = request.getParameter("limit");
//        String startDate = request.getParameter("startDate");
//        String endDate = request.getParameter("endDate");
//        String productName = request.getParameter("productName");
//
//        // Walidacja limitu
//        int limit = 20;
//        if (limitParam != null) {
//            try {
//                limit = Integer.parseInt(limitParam);
//            } catch (NumberFormatException e) {
//                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                response.getWriter().write("{\"error\": \"Nieprawidłowy parametr 'limit'.\"}");
//                return;
//            }
//        }
//
//        // Walidacja dat
//        if (startDate == null || endDate == null) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            response.getWriter().write("{\"error\": \"Brakuje parametrów 'startDate' lub 'endDate'.\"}");
//            return;
//        }
//
//        JSONArray jsonArray = sellService.showAmountOfSoldProdustsByDate(limit, startDate, endDate, productName);
//
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(jsonArray.toString());
//    }
//}
