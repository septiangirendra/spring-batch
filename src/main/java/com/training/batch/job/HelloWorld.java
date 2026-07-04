package com.training.batch.job;

import com.training.batch.tasklet.HelloWorldTasklet;
import com.training.batch.tasklet.ReadCsvTasklet;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class HelloWorld {

    // define job
    @Bean
    public Job helloWorldJob(JobRepository jobRepository,
                             Step helloWorldStep, Step readCsvStep) {

        return new JobBuilder("helloWorldJob", jobRepository)
                .start(helloWorldStep)
                .next(readCsvStep)
                .build();
    }

    // hello worl
    @Bean
    public Step helloWorldStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, HelloWorldTasklet helloWorldTasklet) {

        return new StepBuilder("helloWorldStep", jobRepository)
                .tasklet(helloWorldTasklet, transactionManager).build();
    }


    // read csv
    @Bean
    public Step readCsvStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, ReadCsvTasklet readCsvTasklet) {

        return new StepBuilder("readCsvStep", jobRepository)
                .tasklet(readCsvTasklet, transactionManager)
                .build();
    }

}
