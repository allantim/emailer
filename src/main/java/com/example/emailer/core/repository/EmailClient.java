package com.example.emailer.core.repository;

import com.example.emailer.core.dto.SendEmailRequest;
import com.example.emailer.core.dto.SendEmailResponse;

public interface EmailClient {
    SendEmailResponse send(SendEmailRequest sendEmailRequest);
}
