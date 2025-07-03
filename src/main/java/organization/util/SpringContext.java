package organization.util;

import org.springframework.context.ApplicationContext;

public class SpringContext {

    private static ApplicationContext context;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringContext.context = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }
}
