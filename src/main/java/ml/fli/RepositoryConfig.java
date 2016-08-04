package ml.fli;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "ml.fli.db.repositories")
@EnableAutoConfiguration
@EntityScan(basePackages = {"ml.fli.db.models"})
public class RepositoryConfig {
}
