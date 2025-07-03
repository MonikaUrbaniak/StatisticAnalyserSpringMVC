package organization.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import organization.service.CategoryService;
import organization.service.ProductService;

@RestController
@RequestMapping("/margin")
public class ProductsMarginController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public ProductsMarginController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping
    public void getMarginData(
            @RequestParam String sortOrder,
            @RequestParam(required = false, defaultValue = "20") int limit,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false) String category,
            HttpServletResponse response
    ) throws Exception {

        // Walidacja sortOrder
        if (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Parametr 'sortOrder' musi być 'ASC' lub 'DESC'.\"}");
            return;
        }

        // Pobranie categoryId jeśli podano nazwę
        Integer categoryId = null;
        if (category != null && !category.isEmpty()) {
            categoryId = categoryService.getCategoryIdByName(category);
            if (categoryId == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(
                        "{\"error\":\"Kategoria '" + category + "' nie istnieje.\"}"
                );
                return;
            }
        }

        // Pobranie danych
        JSONArray jsonArray = productService.showMargin(sortOrder, limit, startDate, endDate, categoryId);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonArray.toString());
    }
}


//@WebServlet("/margin")
//public class ProductsMargin extends HttpServlet {
//    private CategoryService categoryService;
//    private ProductService productService;
//
//    @Override
//    public void init() throws ServletException {
//        categoryService = SpringContext.getBean(CategoryService.class);
//        productService = SpringContext.getBean(ProductService.class);
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        String sortOrder = request.getParameter("sortOrder");
//        String limitParam = request.getParameter("limit");
//        String startDate = request.getParameter("startDate");
//        String endDate = request.getParameter("endDate");
//        String categoryName = request.getParameter("category");
//
//        Integer categoryId = null;
//        if (categoryName != null && !categoryName.isEmpty()) {
//            categoryId = categoryService.getCategoryIdByName(categoryName);
//            if (categoryId == null) {
//                // nie ma takiej nazwy w bazie → możesz zwrócić błąd 400
//                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                response.getWriter().write(
//                        "{\"error\":\"Kategoria '" + categoryName + "' nie istnieje.\"}"
//                );
//                return;
//            }
//        }        // jeżeli categoryName było null/empty, to categoryId zostaje null = brak filtra
//
//        // Walidacja sortOrder
//        if (sortOrder == null || !(sortOrder.equalsIgnoreCase("ASC") || sortOrder.equalsIgnoreCase("DESC"))) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            response.getWriter().write("{\"error\": \"Parametr 'sortOrder' musi być 'ASC' lub 'DESC'.\"}");
//            return;
//        }
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
//        JSONArray jsonArray = productService.showMargin(sortOrder, limit, startDate, endDate, categoryId);
//
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(jsonArray.toString());
//    }
//}