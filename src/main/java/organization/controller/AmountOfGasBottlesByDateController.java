package organization.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import organization.service.SellService;
import organization.controller.util.ChartGenerator;

import java.io.OutputStream;
import java.util.List;


@RestController
@RequestMapping("/bottles")
public class AmountOfGasBottlesByDateController {

    private final SellService sellService;

    public AmountOfGasBottlesByDateController(SellService sellService) {
        this.sellService = sellService;
    }

    @GetMapping
    public void getBottles(@RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false, defaultValue = "false") boolean chart,
            HttpServletResponse response
    ) {
        try {
            // Walidacja
            if (startDate == null || endDate == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"error\":\"Brakuje parametrów 'startDate' lub 'endDate'.\"}");
                return;
            }

            List<Object[]> rawData = sellService.getMonthlyAmountOfGasBottlesByDate(startDate, endDate);

            if (chart) {
                if (rawData == null || rawData.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    return;
                }

                CategoryChart chartObj = ChartGenerator.buildMonthlyChart(rawData, startDate, endDate);
                response.setContentType("image/png");

                try (OutputStream os = response.getOutputStream()) {
                    BitmapEncoder.saveBitmap(chartObj, os, BitmapEncoder.BitmapFormat.PNG);
                    os.flush();
                }
            } else {
                JSONArray arr = sellService.showAmountOfGasBottlesByDate(startDate, endDate);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(arr.toString());
            }
        } catch (Exception e) {
            try {
                response.reset();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"error\": \"Wystąpił błąd podczas przetwarzania danych.\"}");
            } catch (Exception ignore) {}
            e.printStackTrace();
        }
    }
}

//package organization.controller;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.json.JSONArray;
//import org.knowm.xchart.BitmapEncoder;
//import org.knowm.xchart.CategoryChart;
//import organization.service.SellService;
//import organization.controller.util.ChartGenerator;
//import organization.util.SpringContext;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.util.List;
//
//@WebServlet("/bottles")
//public class AmountOfGasBottlesByDate extends HttpServlet {
//
//    private SellService sellService;
//    @Override
//    public void init() throws ServletException {
//        sellService = SpringContext.getBean(SellService.class);
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//        String startDate = req.getParameter("startDate");
//        String endDate   = req.getParameter("endDate");
//        boolean chart    = "true".equalsIgnoreCase(req.getParameter("chart"));
//
//        // walidacja
//        if (startDate == null || endDate == null) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            resp.setContentType("application/json;charset=UTF-8");
//            resp.getWriter().write("{\"error\":\"Brakuje parametrów 'startDate' lub 'endDate'.\"}");
//            return;
//        }
//
//        List<Object[]> rawData = sellService.getMonthlyAmountOfGasBottlesByDate(startDate, endDate);
//
//        if (chart) {
//            if (rawData == null || rawData.isEmpty()) {
//                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
//                return;
//            }
//            // **użycie wspólnej metody**
//            CategoryChart chartObj =
//                    ChartGenerator.buildMonthlyChart(rawData, startDate, endDate);
//
//            resp.setContentType("image/png");
//            try (OutputStream os = resp.getOutputStream()) {
//                BitmapEncoder.saveBitmap(chartObj, os, BitmapEncoder.BitmapFormat.PNG);
//                os.flush();
//            }
//        } else {
//            // zwracaj JSON dla tabeli
//            JSONArray arr = sellService.showAmountOfGasBottlesByDate(startDate, endDate);
//            resp.setContentType("application/json;charset=UTF-8");
//            resp.getWriter().write(arr.toString());
//        }
//    }
//}
