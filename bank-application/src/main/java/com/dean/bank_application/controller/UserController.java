package com.dean.bank_application.controller;

import com.dean.bank_application.config.LogInDto;
import com.dean.bank_application.dto.*;
import com.dean.bank_application.service.imple.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
            summary = "Creating new User Account...!",
            description = "Creating account with new Account ID"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PostMapping()
    public BankResponse createAccount(@RequestBody UserDto userDto) {
        return userService.createAccount(userDto);
    }


    @GetMapping(path = "/balance")
    @Operation(
            summary = "Balance Enquiry",
            description = "Checking bank balance"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Balance fetched and status code 200"
    )
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request){
        return userService.balanceEnquiry(request);
    }

    @GetMapping(path = "/name")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.nameEnquiry(enquiryRequest);
    }

    @PostMapping(path = "/credit")
    public BankResponse accountCredit(@RequestBody CreditDebitRequest request) {
        return userService.creditAccount(request);
    }

    @PostMapping(path = "/debit")
    public BankResponse accountDebit(@RequestBody CreditDebitRequest request) {
        return userService.debitAccount(request);
    }

    @PostMapping(path = "/login")
    public BankResponse login(@RequestBody LogInDto logInDto) {
        return userService.login(logInDto);
    }

    @PostMapping(path = "/transfer")
    public BankResponse transfer(@RequestBody TransferRequest request) {
        return userService.transfer(request);
    }
}
