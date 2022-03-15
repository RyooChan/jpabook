package com.example.jpabook;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpabookApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpabookApplication.class, args);
	}

	// Hibernate에서 지연 로딩은 가져올 때에 아무것도 뿌리지 않도록 선언해줄 수 있다.
	// 초기화된 프록시 객체만 노출하도록
	// 물론 이거 쓸이유는 없다. 실제로 엔티티를 노출할일이 없으니.
	@Bean
	Hibernate5Module hibernate5Module() {
		return new Hibernate5Module();
	}
}
