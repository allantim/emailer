package com.example.emailer.core;

import com.example.emailer.core.dto.SendEmailRequest;
import com.example.emailer.core.dto.SendEmailResponse;

public interface EmailSender {
    SendEmailResponse send(SendEmailRequest sendEmailRequest);
}
