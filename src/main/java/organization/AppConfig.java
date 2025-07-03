package organization;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"organization.controller","organization.dao", "organization.service"})
public class AppConfig {
}
