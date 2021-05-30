package hu.medev.examples.ppdspringrestapi;

import hu.medev.examples.ppdspringrestapi.repository.PricePaidDataRepository;
import hu.medev.examples.ppdspringrestapi.repository.csv.FileSystemCachedCsvPricePaidDataRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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

}
