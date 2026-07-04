package com.training.batch.job;

import com.training.batch.tasklet.*;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ImportCsvTransaction {

    @Bean
    public Job importCsvJob(JobRepository jobRepository
            , Step checkInputStep
            , Step readCsvFileStep
            , Step validateCsvStep
            , Step insertTransactionStep
            , Step archiveCsvStep) {

        return new JobBuilder("importCsvJob", jobRepository)
                .start(checkInputStep)
                .on("COMPLETED")
                .to(readCsvFileStep)
                .next(validateCsvStep)
                .next(insertTransactionStep)
                //.next(archiveCsvStep)

                .from(checkInputStep)
                .on("EMPTY")
                .to(archiveCsvStep)

                .from(checkInputStep)
                .on("FAILED")
                .fail()

                .end()
                .build();
    }





    @Bean
    public Step checkInputStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, CheckInputFileTasklet checkInputFileTasklet) {

        return new StepBuilder("checkInputStep", jobRepository)
                .tasklet(checkInputFileTasklet, transactionManager).build();
    }

    @Bean
    public Step readCsvFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, ReadCsvFileTasklet readCsvFileTasklet) {

        return new StepBuilder("readCsvFileStep", jobRepository)
                .tasklet(readCsvFileTasklet, transactionManager).build();
    }

    @Bean
    public Step validateCsvStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, ValidateCsvTasklet validateCsvTasklet) {

        return new StepBuilder("validateCsvStep", jobRepository)
                .tasklet(validateCsvTasklet, transactionManager).build();
    }

    @Bean
    public Step insertTransactionStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, InsertTransactionalTasklet insertTransactionalTasklet) {

        return new StepBuilder("insertTransactionStep", jobRepository)
                .tasklet(insertTransactionalTasklet, transactionManager).build();
    }

    @Bean
    public Step archiveCsvStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, ArchiveCsvTasklet archiveCsvTasklet) {

        return new StepBuilder("archiveCsvStep", jobRepository)
                .tasklet(archiveCsvTasklet, transactionManager).build();
    }
}
