package organization.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import organization.service.SellService;

@RestController
@RequestMapping("/top-products")
public class TopSellingProductsController {

    private final SellService sellService;

    public TopSellingProductsController(SellService sellService) {
        this.sellService = sellService;
    }

    @GetMapping
    public void getTopProducts(
            @RequestParam(name = "limit", required = false, defaultValue = "20") String limitParam,
            HttpServletResponse response
    ) throws Exception {
        int limit;

        try {
            limit = Integer.parseInt(limitParam);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Nieprawidłowy parametr 'limit'.\"}");
            return;
        }

        JSONArray jsonArray = sellService.showTopSellingProductsLastMonth(limit);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonArray.toString());
    }
}


//@WebServlet("/top-products")
//public class TopSellingProductsServlet extends HttpServlet {
//
//    private SellService sellService;
//    @Override
//    public void init() throws ServletException {
//        sellService = SpringContext.getBean(SellService.class);
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        int limit = 20; // Możesz też pobierać z parametru request.getParameter("limit")
//        String limitParam = request.getParameter("limit");
//
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
//        JSONArray jsonArray = sellService.showTopSellingProductsLastMonth(limit);
//
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(jsonArray.toString());
//    }
//}