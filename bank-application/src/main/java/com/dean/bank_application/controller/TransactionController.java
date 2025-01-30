package com.dean.bank_application.controller;

import com.dean.bank_application.entity.Transaction;
import com.dean.bank_application.service.imple.BankStatement;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping(path = "/bankStatement")
public class TransactionController {

    @Autowired
    private BankStatement bankStatement;

    @GetMapping
    public List<Transaction> generateStatement(@RequestParam String accountNumber, @RequestParam String startDate, @RequestParam String endDate) throws DocumentException, FileNotFoundException {
        return bankStatement.generateStatement(accountNumber, startDate, endDate);
    }
}
