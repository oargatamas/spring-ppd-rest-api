package hu.medev.examples.ppdspringrestapi;

import hu.medev.examples.ppdspringrestapi.controller.PricePaidDataController;
import hu.medev.examples.ppdspringrestapi.repository.PricePaidDataRepository;
import hu.medev.examples.ppdspringrestapi.service.PricePaidDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class PpdSpringRestApiApplicationTests {


	@Autowired
	private PricePaidDataController controller;

	@Autowired
	private PricePaidDataService service;

	@MockBean
	private PricePaidDataRepository repository;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
		assertThat(service).isNotNull();
		assertThat(repository).isNotNull();
	}



}
