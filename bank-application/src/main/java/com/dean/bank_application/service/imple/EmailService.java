package com.dean.bank_application.service.imple;

import com.dean.bank_application.dto.EmailDetails;

public interface EmailService {
    public void sendEmailAlerts(EmailDetails emailDetails);

    public void sendEmailWithAttachment(EmailDetails emailDetails);
}
