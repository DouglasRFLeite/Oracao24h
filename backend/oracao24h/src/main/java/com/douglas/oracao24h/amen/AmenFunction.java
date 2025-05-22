package com.douglas.oracao24h.amen;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmenFunction {

  @Bean
  public Function<String, String> amen() {
    return request -> "Amen";
  }

}
