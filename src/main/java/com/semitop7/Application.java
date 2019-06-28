package com.semitop7;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.shell.jline.PromptProvider;

@SpringBootApplication
@EnableAsync
public class Application {

    public static void main(String[] args) {
        System.err.close();
        System.setErr(System.out);
        SpringApplication application = new SpringApplication(Application.class);
        application.setAddCommandLineProperties(true);
        application.run(args);
    }

    /**
     * Required by Spring Shell
     *
     * @return PromptProvider
     */
    @Bean
    public PromptProvider myPromptProvider() {
        return () -> new AttributedString("cmd:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    }

    @Bean("threadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }
}
