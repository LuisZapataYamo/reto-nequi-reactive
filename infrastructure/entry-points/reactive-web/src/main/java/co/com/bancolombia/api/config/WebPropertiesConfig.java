package co.com.bancolombia.api.config;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebPropertiesConfig {

    @Bean
    public WebProperties.Resources webResources() {
        return new WebProperties.Resources();
    }
}
