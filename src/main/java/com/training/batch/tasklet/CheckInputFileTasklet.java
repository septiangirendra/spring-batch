package com.training.batch.tasklet;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.batch.core.ExitStatus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class CheckInputFileTasklet implements Tasklet {

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("CheckInputFileTasklet");

        /*validation*/
       Path filePath = Paths.get("C:\\Users\\sgirendraw\\Downloads\\banking-transactions.csv");

        if(!Files.exists(filePath)){
            throw new IllegalArgumentException("File does not exist");
        }
        if(!Files.isReadable(filePath)){
            throw new IllegalArgumentException("File is not readable");
        }
        Stream<String> lines = Files.lines(filePath);
        boolean hasData = lines.skip(1).anyMatch(line -> !line.isBlank());

        if(!hasData){
            contribution.setExitStatus(new ExitStatus("EMPTY"));
            System.out.println("File is Empty");
            return RepeatStatus.FINISHED;
        }

        return RepeatStatus.FINISHED;
    }
}
