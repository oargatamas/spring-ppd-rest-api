package hu.medev.examples.ppdspringrestapi.config;

import hu.medev.examples.ppdspringrestapi.repository.csv.FileSystemCachedCsvPricePaidDataRepository;
import hu.medev.examples.ppdspringrestapi.repository.csv.RealTimeCsvPricePaidDataRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class RepositoryConfiguration {

    @Bean
    public FileSystemCachedCsvPricePaidDataRepository fileSystemCachedCsvPricePaidDataRepository(){
        return new FileSystemCachedCsvPricePaidDataRepository();
    }

    @Bean
    public RealTimeCsvPricePaidDataRepository realTimeCsvPricePaidDataRepository(){
        return new RealTimeCsvPricePaidDataRepository();
    }
}
