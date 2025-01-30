package com.dean.bank_application.service.imple;


import com.dean.bank_application.dto.EmailDetails;
import com.dean.bank_application.entity.Transaction;
import com.dean.bank_application.entity.Users;
import com.dean.bank_application.repository.TransactionRepository;
import com.dean.bank_application.repository.UserRepository;
import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@NoArgsConstructor
@Slf4j
@AllArgsConstructor
@Data
public class BankStatement {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private static final String FILE = "C:\\Users\\nagar\\Downloads\\Mybank Statements\\Mystatement.pdf";
    /*
    * Retrieve List of transactions within a date
    * Generate PDF file
    * send the file via mail
    */

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        Users user = userRepository.findByAccountNumber(accountNumber);
        String customerName = user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName();

        List<Transaction> transactions = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber)
                        && !transaction.getCreatedAt().isBefore(start) // On or after start date
                        && !transaction.getCreatedAt().isAfter(end))   // On or before end date
                .toList();



        Document document = new Document();
        log.info("Setting size of document");

        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();


        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("MyBank of Java"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("3-2A, Hyderaba, Telangana"));
        bankAddress.setBorder(0);

        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start date " + startDate));
        customerInfo.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);
        PdfPCell stopDate = new PdfPCell(new Phrase("End Date " + endDate));
        stopDate.setBorder(0);
        PdfPCell name = new PdfPCell(new Phrase(customerName));
        name.setBorder(0);
        PdfPCell space = new PdfPCell();
        space.setBorder(0);
        PdfPCell address = new PdfPCell(new Phrase("Customer Address " + user.getAddress()));
        address.setBorder(0);
//        PdfPCell accNumber = new PdfPCell(new Phrase("Account Number : " + user.getAccountNumber()));
//        accNumber.setBorder(0);
//        accNumber.setBackgroundColor(BaseColor.BLUE);

        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(stopDate);
        statementInfo.addCell(name);

        statementInfo.addCell(space);
        statementInfo.addCell(address);

        PdfPTable transactionTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBackgroundColor(BaseColor.BLUE);
        date.setBorder(0);
        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionType.setBackgroundColor(BaseColor.BLUE);
        transactionType.setBorder(0);
        PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
        transactionAmount.setBorder(0);
        transactionAmount.setBackgroundColor(BaseColor.BLUE);
        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
        status.setBackgroundColor(BaseColor.BLUE);
        status.setBorder(0);

        transactionTable.addCell(date);
        transactionTable.addCell(transactionType);
        transactionTable.addCell(transactionAmount);
        transactionTable.addCell(status);

        transactions.forEach(transaction -> {
            transactionTable.addCell(new Phrase(String.valueOf(transaction.getCreatedAt())));
            transactionTable.addCell(new Phrase(transaction.getTransactionType()));
            transactionTable.addCell(new Phrase(String.valueOf(transaction.getAmount())));
            transactionTable.addCell(new Phrase(transaction.getStatus()));
        });

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionTable);

        document.close();

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("STATEMENT OF ACCOUNT")
                .messageBody("Kindly find your requested account statement attached?")
                .attachment(FILE)
                .build();

        emailService.sendEmailWithAttachment(emailDetails);

        return transactions;
    }




}
