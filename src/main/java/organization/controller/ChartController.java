package organization.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import organization.service.SellService;
import organization.controller.util.ChartGenerator;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/charts")
public class ChartController {

    private final SellService sellService;
    private final ServletContext servletContext;

    public ChartController(SellService sellService, ServletContext servletContext) {
        this.sellService = sellService;
        this.servletContext = servletContext;
    }

    // 1) ZAPISYWANIE – poprzedni SaveChartServlet przeniesiony do doPost
    @PostMapping
    public void saveChart(
            @RequestParam String startDate,
            @RequestParam String endDate,
            HttpServletResponse response
    ) throws IOException {
        // Walidacja dat
        if (startDate == null || endDate == null
                || !startDate.matches("\\d{4}-\\d{2}-\\d{2}")
                || !endDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    new JSONObject()
                            .put("success", false)
                            .put("error", "Parametry dat w formacie YYYY-MM-DD są wymagane")
                            .toString()
            );
            return;
        }

        // pobranie danych i zbudowanie wykresu
        List<Object[]> rawData = sellService
                .getMonthlyAmountOfGasBottlesByDate(startDate, endDate);
        if (rawData == null || rawData.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }
        CategoryChart chart = ChartGenerator
                .buildMonthlyChart(rawData, startDate, endDate);

        // przygotowanie ścieżki
        String relFolder = "/statistics/pages/zapisane-wykresy/";
        String absFolder = servletContext.getRealPath(relFolder);
        if (absFolder == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        Path folder = Paths.get(absFolder);
        if (!Files.exists(folder)) Files.createDirectories(folder);

        // unikalna nazwa pliku
        String fname = System.currentTimeMillis()
                + "_butle_wykres_"
                + startDate.replace('-', '_')
                + "_"
                + endDate.replace('-', '_')
                + ".png";

        // zapis do pliku
        try (OutputStream os = Files.newOutputStream(folder.resolve(fname))) {
            BitmapEncoder.saveBitmap(chart, os, BitmapEncoder.BitmapFormat.PNG);
        }

        // zwracamy JSON z nazwą
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                new JSONObject()
                        .put("success", true)
                        .put("fileName", fname)
                        .put("filePath", "zapisane-wykresy/" + fname)
                        .toString()
        );
    }
}


//@WebServlet("/charts")
//public class ChartServlet extends HttpServlet {
//
//    private SellService sellService;
//    @Override
//    public void init() throws ServletException {
//        sellService = SpringContext.getBean(SellService.class);
//    }
//
//    // 1) ZAPISYWANIE – poprzedni SaveChartServlet przeniesiony do doPost
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//        String startDate = req.getParameter("startDate");
//        String endDate   = req.getParameter("endDate");
//
//        // walidacja dat…
//        if (startDate == null || endDate == null
//                || !startDate.matches("\\d{4}-\\d{2}-\\d{2}")
//                || !endDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            resp.setContentType("application/json;charset=UTF-8");
//            resp.getWriter().write(
//                    new JSONObject()
//                            .put("success", false)
//                            .put("error", "Parametry dat w formacie YYYY-MM-DD są wymagane")
//                            .toString()
//            );
//            return;
//        }
//
//        // pobranie danych i zbudowanie wykresu
//        List<Object[]> rawData = sellService
//                .getMonthlyAmountOfGasBottlesByDate(startDate, endDate);
//        if (rawData == null || rawData.isEmpty()) {
//            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
//            return;
//        }
//        CategoryChart chart = ChartGenerator
//                .buildMonthlyChart(rawData, startDate, endDate);
//
//        // przygotowanie ścieżki
//        String relFolder = "/statistics/pages/zapisane-wykresy/";
//        String absFolder = getServletContext().getRealPath(relFolder);
//        if (absFolder == null) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            return;
//        }
//        Path folder = Paths.get(absFolder);
//        if (!Files.exists(folder)) Files.createDirectories(folder);
//
//        // unikalna nazwa pliku
//        String fname = System.currentTimeMillis()
//                + "_butle_wykres_"
//                + startDate.replace('-', '_')
//                + "_"
//                + endDate.replace('-', '_')
//                + ".png";
//
//        // zapis do pliku
//        try (OutputStream os = Files.newOutputStream(folder.resolve(fname))) {
//            BitmapEncoder.saveBitmap(chart, os, BitmapEncoder.BitmapFormat.PNG);
//        }
//
//        // zwracamy JSON z nazwą
//        resp.setContentType("application/json;charset=UTF-8");
//        resp.getWriter().write(
//                new JSONObject()
//                        .put("success", true)
//                        .put("fileName", fname)
//                        .put("filePath", "zapisane-wykresy/" + fname)
//                        .toString()
//        );
//    }
//}