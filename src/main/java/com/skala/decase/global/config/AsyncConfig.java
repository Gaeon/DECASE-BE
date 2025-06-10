package com.skala.decase.global.config;

import com.skala.decase.domain.mockup.exception.MockupException;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("RFP-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean(name = "mockupTaskExecutor")
    public Executor mockupTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);        // 기본 스레드 수
        executor.setMaxPoolSize(5);         // 최대 스레드 수
        executor.setQueueCapacity(10);      // 큐 용량
        executor.setThreadNamePrefix("MockupGeneration-");
        executor.setRejectedExecutionHandler((r, executor1) -> {
            // 큐가 가득 찰 경우 처리 로직
            throw new MockupException("목업 생성 작업이 너무 많습니다. 잠시 후 다시 시도해주세요.", HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
        });
        executor.initialize();
        return executor;
    }
}
