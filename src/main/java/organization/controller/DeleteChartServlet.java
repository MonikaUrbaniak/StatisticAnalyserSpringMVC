//package organization.servlet;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.*;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.IOException;
//
//@WebServlet("/deleteChart")
//public class DeleteChartServlet extends HttpServlet {
//
//    /**
//     * Ścieżka względem katalogu webapp, w którym znajdują się PNG
//     * (powinna wskazywać dokładnie ten sam katalog, który widzi PHP)
//     **/
//    private static final String REL_FOLDER = "/statistics/pages/zapisane-wykresy/";
//
//    @Override
//    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//
//        resp.setContentType("application/json;charset=UTF-8");
//        JSONObject json = new JSONObject();
//
//        // 1) pobierz param
//        String filename = req.getParameter("filename");
//        if (filename == null || !filename.matches("\\d+_butle_wykres_.*\\.png")) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            json.put("success", false)
//                    .put("error", "Nieprawidłowa nazwa pliku");
//            resp.getWriter().write(json.toString());
//            return;
//        }
//
//        // 2) odnajdź fizyczny katalog
//        String absFolder = getServletContext().getRealPath(REL_FOLDER);
//        if (absFolder == null) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            json.put("success", false)
//                    .put("error", "Nie znaleziono katalogu wykresów");
//            resp.getWriter().write(json.toString());
//            return;
//        }
//
//        // 3) zbuduj obiekt File, wrzuć debug info
//        File toDelete = new File(absFolder, filename);
//        json.put("path", toDelete.getAbsolutePath());
//        json.put("exists", toDelete.exists());
//
//        // 4) spróbuj usunąć
//        boolean deleted = false;
//        if (toDelete.exists() && toDelete.isFile()) {
//            deleted = toDelete.delete();
//        }
//
//        if (deleted) {
//            json.put("success", true);
//        } else {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            json.put("success", false)
//                    .put("error", "Nie udało się usunąć pliku");
//        }
//
//        resp.getWriter().write(json.toString());
//    }
//}
