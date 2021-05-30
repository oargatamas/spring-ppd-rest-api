package hu.medev.examples.ppdspringrestapi;

import hu.medev.examples.ppdspringrestapi.repository.PricePaidDataRepository;
import hu.medev.examples.ppdspringrestapi.repository.csv.FileSystemCachedCsvPricePaidDataRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
public class PpdSpringRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PpdSpringRestApiApplication.class, args);
	}

    @Bean
    public PricePaidDataRepository dataRepository(){
        //return new RealTimeCsvPricePaidDataRepository();
        return new FileSystemCachedCsvPricePaidDataRepository();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("hu.medev.examples.ppdspringrestapi.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
