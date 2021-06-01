package hu.medev.examples.ppdspringrestapi.repository.csv;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;

public class RealTimeCsvPricePaidDataRepository extends CsvPricePaidDataRepository {

    @Value("${data.ppd.url}")
    private String PPD_URL;

    @Value("${data.ppd.latestFileName}")
    private String FILE_NAME;

    private final RestTemplate restTemplate;

    private File cache;

    public RealTimeCsvPricePaidDataRepository() {
        this.restTemplate = new RestTemplate();
    }

    protected Path fetchCSVData(){
        File file = restTemplate.execute(PPD_URL + "/" + FILE_NAME, HttpMethod.GET, null, clientHttpResponse -> {
            File ret = File.createTempFile(FILE_NAME, "tmp");
            StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
            return ret;
        });

        cache = file;
        assert file != null;
        return file.toPath();
    }

    @Override
    protected void cleanUp() {
        cache.delete();
    }
}
