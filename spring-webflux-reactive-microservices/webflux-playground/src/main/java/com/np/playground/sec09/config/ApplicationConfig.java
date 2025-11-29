package com.np.playground.sec09.config;

import com.np.playground.sec09.dto.ProductDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class ApplicationConfig {

    @Bean
    public Sinks.Many<ProductDTO> sink() {
        return Sinks.many().replay() // capable of replaying emitted messages to late subscribers
                .limit(1); // how many last messages to emit
    }
}
