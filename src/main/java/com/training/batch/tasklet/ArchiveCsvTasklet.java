package com.training.batch.tasklet;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ArchiveCsvTasklet implements Tasklet {


    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("ArchiveCsvTasklet");

        /*validation*/
        Path filePathOrigin = Paths.get("C:\\Users\\sgirendraw\\Downloads\\banking-transactions.csv");
        Path filePathDestination = Paths.get("C:\\Users\\sgirendraw\\Downloads\\test");
        Files.createDirectories(filePathDestination);
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        Path targetPath = filePathDestination.resolve("banking-transaction" + fileName + ".csv");
        Files.move(filePathOrigin, targetPath, StandardCopyOption.REPLACE_EXISTING);


        return RepeatStatus.FINISHED;
    }
}
