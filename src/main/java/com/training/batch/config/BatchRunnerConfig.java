package com.training.batch.config;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchRunnerConfig {
    @Bean
    public ApplicationRunner runAllJobs(JobOperator jobOperator, Job helloWorldJob, Job importCsvJob) {

        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {

                JobParameters parameter = new JobParametersBuilder()
                        .addLong("param1",System.currentTimeMillis()) // this parameter based on input
                        .toJobParameters();
                //jobOperator.start(helloWorldJob, parameter);


                JobParameters parameter2 = new JobParametersBuilder()
                        .addLong("param2",System.currentTimeMillis()) // this parameter based on input
                        .toJobParameters();
                jobOperator.start(importCsvJob, parameter2);
            }
        };
    }
}
