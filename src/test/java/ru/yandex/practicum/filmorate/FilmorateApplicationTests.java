package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FilmorateApplicationTests {

	FilmorateApplication filmorateApplication;


	@BeforeEach
	public void create(){
		filmorateApplication = new FilmorateApplication();
		//SpringApplication.run(FilmorateApplication.class, );

	}

		@Test
	void contextLoads() {
	}

}
