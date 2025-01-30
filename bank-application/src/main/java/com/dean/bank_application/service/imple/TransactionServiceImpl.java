package com.dean.bank_application.service.imple;

import com.dean.bank_application.dto.TransactionDto;
import com.dean.bank_application.entity.Transaction;
import com.dean.bank_application.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private TransactionRepository transactionRepository;



    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .status(transactionDto.getStatus())
                .amount(transactionDto.getAmount())
                .accountNumber(transactionDto.getAccountNumber())
                .build();
        transactionRepository.save(transaction);
        System.out.println("Transaction Saved Successfully...!");
    }

}
