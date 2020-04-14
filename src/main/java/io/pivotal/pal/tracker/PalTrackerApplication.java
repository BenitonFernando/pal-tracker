package io.pivotal.pal.tracker;

import static java.lang.System.getenv;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mysql.cj.jdbc.MysqlDataSource;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class PalTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PalTrackerApplication.class, args);
	}

	@Bean
    TimeEntryRepository timeEntryRepository() {
        if(getenv("ENV_NAME") != null && getenv("ENV_NAME").equalsIgnoreCase("test")) {
            return new InMemoryTimeEntryRepository();
        }
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(System.getenv("SPRING_DATASOURCE_URL"));
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        return new JdbcTimeEntryRepository(dataSource);
    }

    @Bean
    public ObjectMapper jsonObjectMapper() {
        return Jackson2ObjectMapperBuilder.json()
                .serializationInclusion(JsonInclude.Include.NON_NULL) 
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) 
                .modules(new JavaTimeModule())
                .build();
    }
}
