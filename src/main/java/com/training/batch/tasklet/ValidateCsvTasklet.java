package com.training.batch.tasklet;

import com.training.batch.Dto.BankingTransaction;
import io.micrometer.common.util.StringUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

@Component
public class ValidateCsvTasklet implements Tasklet {
    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("ValidateCsvTasklet");

        // terima dari step sebelumnya
        ExecutionContext jobExecutionContext = chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext();

        Object rawData = jobExecutionContext.get("TRANSACTION_ROWS");
        ArrayList<BankingTransaction> bankingData = (ArrayList<BankingTransaction>) rawData;
        ArrayList<BankingTransaction> valDataBankingData = new ArrayList<BankingTransaction>();

        for (BankingTransaction bankingTransaction : bankingData) {


            if(StringUtils.isEmpty(bankingTransaction.getBranchCode())){
                contribution.incrementProcessSkipCount();
                continue;
            }
            if (bankingTransaction.getAmount().compareTo(BigDecimal.ZERO) <=0 || bankingTransaction.getAmount() == null){
                contribution.incrementProcessSkipCount();
                continue;
            }
            if(StringUtils.isEmpty(bankingTransaction.getAccountNumber())){
                contribution.incrementProcessSkipCount();
                continue;
            }

            valDataBankingData.add(bankingTransaction);;
        }

        // Kiirm ke step beirkutnya
        ExecutionContext jobExecutionContext1 = chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext();

        jobExecutionContext1.put("TRANSACTION_ROWS", valDataBankingData);

        return RepeatStatus.FINISHED;
    }
}
