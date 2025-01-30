package com.dean.bank_application.service.imple;

import com.dean.bank_application.config.LogInDto;
import com.dean.bank_application.dto.*;
import com.dean.bank_application.entity.Transaction;

import java.util.List;

public interface UserService {

    BankResponse createAccount(UserDto userDto);

    BankResponse balanceEnquiry(EnquiryRequest request);

    String nameEnquiry(EnquiryRequest request);

    BankResponse creditAccount(CreditDebitRequest request);

    BankResponse debitAccount(CreditDebitRequest request);

    BankResponse transfer(TransferRequest request);

    BankResponse login(LogInDto logInDto);
}
