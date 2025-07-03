package organization;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import organization.service.CategoryService;
import organization.service.ProductService;
import organization.service.SellService;
import organization.util.JpaUtil;
import organization.util.SpringContext;

public class Main {
    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        SpringContext.setApplicationContext(context);
        CategoryService categoryService = SpringContext.getBean(CategoryService.class);
        ProductService productService = SpringContext.getBean(ProductService.class);
        SellService sellService = SpringContext.getBean(SellService.class);



        sellService.showTopSellingProductsLastMonth(10);
        sellService.showAmountOfSoldProdustsByDate(15, "2025-03-01", "2025-04-30", "Propan butan gaz butla"); //ASC lub DESC
        productService.showMargin("DESC",10, "2020-03-01", "2025-04-30", 11); //ASC lub DESC
        productService.showMargin("DESC",10, "2020-03-01", "2025-04-30", null); //ASC lub DESC

        JpaUtil.close();
            }
}

