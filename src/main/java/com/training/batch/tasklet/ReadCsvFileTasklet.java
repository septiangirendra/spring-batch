package com.training.batch.tasklet;

import com.training.batch.Dto.BankingTransaction;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReadCsvFileTasklet implements Tasklet {
    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("ReadCsvFileTasklet");

        Path filePath = Paths.get("C:\\Users\\sgirendraw\\Downloads\\banking-transactions.csv");
        List<BankingTransaction> rawData = new ArrayList<BankingTransaction>();

        BufferedReader br = Files.newBufferedReader(filePath);
        String line;
        int lineNumber = 0;
        while ((line = br.readLine()) != null) {
            lineNumber++;
            if(lineNumber == 1) {
                continue;
            }
            if(line.trim().isEmpty()){
                continue;
            }

            contribution.incrementReadCount();
            String[] columns = line.split(",",-1);

            BankingTransaction bankingTransaction = new BankingTransaction();
            bankingTransaction.setTransactionId(columns[0].trim());
            bankingTransaction.setAccountNumber(columns[1].trim());
            bankingTransaction.setCustomerName(columns[2].trim());
            bankingTransaction.setTransactionType(columns[3].trim());
            if(columns[4] != "")
                bankingTransaction.setAmount(new BigDecimal(columns[4].trim()));
            else
                bankingTransaction.setAmount(BigDecimal.ZERO);
            bankingTransaction.setTransactionDate(LocalDate.parse(columns[5].replace("/","-")));
            bankingTransaction.setBranchCode(columns[6].trim());
            bankingTransaction.setDescription(columns[7].trim());
            rawData.add(bankingTransaction);
        }


        // Kiirm ke step beirkutnya
        ExecutionContext jobExecutionContext = chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext();

        jobExecutionContext.put("TRANSACTION_ROWS", rawData);

        return RepeatStatus.FINISHED;
    }
}
