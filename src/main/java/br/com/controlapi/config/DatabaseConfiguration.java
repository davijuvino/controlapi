package br.com.controlapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories("br.com.controlapi.repository")
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
@EnableConfigurationProperties(DataSourceProperties.class)
public class DatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";

    /**
     * Configuração automática do DataSource usando propriedades do Spring Boot
     */
    @Bean
    @Profile(SPRING_PROFILE_DEVELOPMENT)
    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
        log.debug("Configuring DataSource using Spring Boot auto-configuration");
        log.debug("Database URL: {}", dataSourceProperties.getUrl());
        log.debug("Database Username: {}", dataSourceProperties.getUsername());

        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    /**
     * Bean para inicialização customizada (opcional)
     */
    @Bean
    @Profile(SPRING_PROFILE_DEVELOPMENT)
    public DatabaseInitializer databaseInitializer() {
        return new DatabaseInitializer();
    }

    public static class DatabaseInitializer {
        private final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

        public DatabaseInitializer() {
            log.debug("Database initializer created - ready for custom initialization");
        }
    }
}