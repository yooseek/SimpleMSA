package com.example.userservicemsa.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4JConfig {
    // circuitbreaker custom
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(4)    // Circuitbreaker 를 open하기 위한 확률 4/100 - default 50 / 100
                .waitDurationInOpenState(Duration.ofMillis(1000))   // open time 1second open - default 60seconds
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)  // 결과값 저장 타입 - default COUNT_BASED
                .slidingWindowSize(2)   // close 당시 호출 결과 저장 값의 단위, 2개를 저장하겠다.- default = 100
                .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4)) // time limit, 얼마 동안 응답이 없으면 open 할 것이냐 - dafault 1second
                .build();

        return factory -> factory.configureDefault(id-> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build()
        );
    }
}
