package organization.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import organization.service.CategoryService;
import organization.util.SpringContext;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController{

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<String> getAllCategories() {
        return categoryService.getAllCategories();
    }
}


//package organization.servlet;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import organization.service.CategoryService;
//import organization.util.SpringContext;
//
//import java.io.IOException;
//import java.util.List;
//
//@WebServlet("/categories")
//public class CategoryServlet extends HttpServlet {
//
//    private CategoryService categoryService;
//
//    @Override
//    public void init() throws ServletException {
//        categoryService = SpringContext.getBean(CategoryService.class);
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        List<String> list = categoryService.getAllCategories();
//
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(list);
//
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(json);
//    }
//}
