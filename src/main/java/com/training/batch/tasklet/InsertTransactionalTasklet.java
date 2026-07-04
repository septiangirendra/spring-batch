package com.training.batch.tasklet;

import com.training.batch.Dto.BankingTransaction;
import com.training.batch.dao.BangkingTransactionDao;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class InsertTransactionalTasklet implements Tasklet {

    private final BangkingTransactionDao  dao;

    public InsertTransactionalTasklet(BangkingTransactionDao dao) {
        this.dao = dao;
    }

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("InsertTransactionalTasklet");

        // terima dari step sebelumnya
        ExecutionContext jobExecutionContext = chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext();

        Object rawData = jobExecutionContext.get("TRANSACTION_ROWS");
        ArrayList<BankingTransaction> bankingData = (ArrayList<BankingTransaction>) rawData;

        for (BankingTransaction bankingTransaction : bankingData) {
            dao.insert(bankingTransaction);
            contribution.incrementWriteCount(1);
        }

        return RepeatStatus.FINISHED;
    }
}
