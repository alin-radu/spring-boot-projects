package com.dev.ecom_platform_2;

import com.dev.ecom_platform_2.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private ProductService productService;

	@Test
	void contextLoads() {
		assertThat(productService).isNotNull();
	}

}
