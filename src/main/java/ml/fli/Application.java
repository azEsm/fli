package ml.fli;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ScheduledExecutorService;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);


    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ScheduledExecutorService getScheduledExecutor() {
        ScheduledExecutorFactoryBean factory = new ScheduledExecutorFactoryBean();
        factory.setPoolSize(10);
        factory.setThreadNamePrefix("FLI-");
        factory.setWaitForTasksToCompleteOnShutdown(true);
        factory.setAwaitTerminationSeconds(60);
        factory.setContinueScheduledExecutionAfterException(true);

        return factory.getObject();
    }
}
