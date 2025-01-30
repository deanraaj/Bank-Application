package com.dean.bank_application.service.imple;

import com.dean.bank_application.config.JwtTokenProvider;
import com.dean.bank_application.config.LogInDto;
import com.dean.bank_application.dto.*;
import com.dean.bank_application.entity.Users;
import com.dean.bank_application.repository.UserRepository;
import com.dean.bank_application.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PasswordEncoder encoder;

    public BankResponse createAccount(UserDto userDto) {

        if(userRepository.existsByEmail(userDto.getEmail())) {
            Users existedAccount = userRepository.findByEmail(userDto.getEmail());
            return response(AccountUtils.ACCOUNT_EXISTS_MESSAGE,AccountUtils.ACCOUNT_EXISTS_CODE, AccountInfo.builder()
                    .accountBalance(existedAccount.getAccountBalance())
                    .accountName(existedAccount.getFirstName() + " " + existedAccount.getLastName() + " " + existedAccount.getOtherName())
                    .accountNumber(existedAccount.getAccountNumber())
                    .build());
        }

        Users user = Users.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .otherName(userDto.getOtherName())
                .gender(userDto.getGender())
                .address(userDto.getAddress())
                .stateOfOrigin(userDto.getStateOfOrigin())
                .accountNumber(AccountUtils.getAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userDto.getEmail())
                .password(encoder.encode(userDto.getPassword()))
                .phoneNumber(userDto.getPhoneNumber())
                .alternatePhoneNumber(userDto.getAlternatePhoneNumber())
                .status("ACTIVE")
                .build();
        Users savedUser = userRepository.save(user);

        // Send Email
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Creation")
                .messageBody("Congratulations, Your account is created successfully in Bank. \n Your Account Details : \n"
                + "Account Name : " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName() + "\n Account Number: " + savedUser.getAccountNumber())
                .build();

        emailService.sendEmailAlerts(emailDetails);

        return response(AccountUtils.ACCOUNT_CREATION_MESSAGE, AccountUtils.ACCOUNT_CREATION_CODE, AccountInfo.builder()
                .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                .accountNumber(savedUser.getAccountNumber())
                .accountBalance(savedUser.getAccountBalance())
                .build());

    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());

        if(!isAccountExists) {
            return response(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE, AccountUtils.ACCOUNT_NOT_EXIST_CODE, null);
        }

        Users user = userRepository.findByAccountNumber(request.getAccountNumber());

        return response(AccountUtils.ACCOUNT_EXISTS_MESSAGE, AccountUtils.ACCOUNT_EXISTS_CODE, AccountInfo.builder()
                .accountNumber(user.getAccountNumber())
                .accountBalance(user.getAccountBalance())
                .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                .build());
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());

        if(!isAccountExists) {
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }

        Users user = userRepository.findByAccountNumber(request.getAccountNumber());
        return user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExists) {
            return response(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE, AccountUtils.ACCOUNT_NOT_EXIST_CODE, null);
        }

        Users user = userRepository.findByAccountNumber(request.getAccountNumber());
        user.setAccountBalance(user.getAccountBalance().add(request.getAmount()));
        userRepository.save(user);



        //saveTransaction
        saveTransaction(user.getAccountNumber(), request.getAmount(), "CREDIT");

        return response(AccountUtils.ACCOUNT_CREDIT_SUCCESS_MESSAGE, AccountUtils.ACCOUNT_CREDIT_SUCCESS, AccountInfo.builder()
                .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                .accountNumber(user.getAccountNumber())
                .accountBalance(user.getAccountBalance())
                .build());


    }

    // debit Account
    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExists) {
            return response(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE, AccountUtils.ACCOUNT_NOT_EXIST_CODE, null);
        }

        Users user = userRepository.findByAccountNumber(request.getAccountNumber());
        if(user.getAccountBalance().compareTo(request.getAmount()) < 0) {
            return response(AccountUtils.ACCOUNT_INSUFFICIENT_BALANCE_MESSAGE, AccountUtils.ACCOUNT_INSUFFICIENT_BALANCE_CODE, AccountInfo.builder()
                    .accountNumber(user.getAccountNumber())
                    .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                    .accountBalance(user.getAccountBalance())
                    .build());
        }
        user.setAccountBalance(user.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(user);

        //saveTransaction
        saveTransaction(user.getAccountNumber(), request.getAmount(), "DEBIT");


        return response(AccountUtils.ACCOUNT_DEBIT_SUCCESS_MESSAGE, AccountUtils.ACCOUNT_DEBIT_SUCCESS, AccountInfo.builder()
                .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                .accountNumber(user.getAccountNumber())
                .accountBalance(user.getAccountBalance())
                .build());
    }

    @Override
    public BankResponse transfer(TransferRequest request) {

        boolean isAccountExistsSource = userRepository.existsByAccountNumber(request.getSourceAccountNumber());
        boolean isAccountExistsDesti = userRepository.existsByAccountNumber(request.getSourceAccountNumber());
        if(!isAccountExistsSource) {
            return response(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE, AccountUtils.ACCOUNT_NOT_EXIST_CODE, null);
        }
        if(!isAccountExistsDesti) {
            return response(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE, AccountUtils.ACCOUNT_NOT_EXIST_CODE, null);
        }

        Users sourceAccount = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        if(sourceAccount.getAccountBalance().compareTo(request.getAmount()) < 0) {
            return response(AccountUtils.ACCOUNT_INSUFFICIENT_BALANCE_MESSAGE, AccountUtils.ACCOUNT_NOT_EXIST_CODE, AccountInfo.builder()
                    .accountBalance(sourceAccount.getAccountBalance())
                    .accountNumber(sourceAccount.getAccountNumber())
                    .accountName(sourceAccount.getFirstName() + " " + sourceAccount.getLastName() + " " + sourceAccount.getOtherName())
                    .build());
        }
        sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(sourceAccount);

        //saveTransaction
        saveTransaction(sourceAccount.getAccountNumber(), request.getAmount(), "DEBIT");


        EmailDetails debitAlert = EmailDetails.builder()
                .messageBody("The Amount of " + request.getAmount() + " is DEBITED from your Account.\n" + "Your Current Balance : " + sourceAccount.getAccountBalance())
                .subject("DEBIT ALERT...!")
                .recipient(sourceAccount.getEmail())
                .build();

        emailService.sendEmailAlerts(debitAlert);

//        EmailDetails emailDetails = EmailDetails.builder()
//                .recipient(savedUser.getEmail())
//                .subject("Account Creation")
//                .messageBody("Congratulations, Your account is created successfully in Bank. \n Your Account Details : \n"
//                        + "Account Name : " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName() + "\n Account Number: " + savedUser.getAccountNumber())
//                .build();
//
//        emailService.sendEmailAlerts(emailDetails);

        Users destiAccount = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destiAccount.setAccountBalance(destiAccount.getAccountBalance().add(request.getAmount()));

        userRepository.save(destiAccount);

        //saveTransaction
        saveTransaction(destiAccount.getAccountNumber(), request.getAmount(), "CREDIT");


        EmailDetails creditAlert = EmailDetails.builder()
                .messageBody("The Amount of " + request.getAmount() + " is CREDITED to your Account.\n" + "Your Current Balance : " + destiAccount.getAccountBalance())
                .subject("CREDIT ALERT...!")
                .recipient(destiAccount.getEmail())
                .build();

        emailService.sendEmailAlerts(creditAlert);

        return response(AccountUtils.ACCOUNT_TRANSFER_SUCCESSFUL_MESSAGE, AccountUtils.ACCOUNT_TRANSFER_SUCCESSFUL_CODE, null);

    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public BankResponse login(LogInDto logInDto) {
        UsernamePasswordAuthenticationToken uspa = new UsernamePasswordAuthenticationToken(logInDto.getEmail(), logInDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(uspa);
        if(authentication.isAuthenticated()){

            return BankResponse.builder().responseCode("200 OK").responseMessage(jwtTokenProvider.generateToken(logInDto.getEmail())).build();
        }
        return BankResponse.builder()
                .responseMessage("Wrong credentials")
                .responseCode("401")
        .build();
    }


    // Send Successful response
    private BankResponse response(String responseMessage, String responseCode, AccountInfo accountInfo) {
        return BankResponse.builder()
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .accountInfo(accountInfo)
                .build();
    }

    private void saveTransaction(String accountNumber, BigDecimal amount, String transactionType){
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(accountNumber)
                .transactionType(transactionType)
                .status("SUCCESS")
                .amount(amount)
                .build();
        transactionService.saveTransaction(transactionDto);
    }

}
