package com.example.emailer.dataprovider;

import com.example.emailer.core.dto.SendEmailRequest;
import com.example.emailer.core.dto.SendEmailResponse;
import com.example.emailer.core.repository.EmailClient;
import org.springframework.stereotype.Repository;

import java.security.SecureRandom;

@Repository("emailClientTwo")
public class EmailClientTwo implements EmailClient {

    private final SecureRandom secureRandom;

    public EmailClientTwo() {
        this.secureRandom = new SecureRandom();
    }

    @Override
    public SendEmailResponse send(SendEmailRequest sendEmailRequest) {
        // TODO Need more time to get an email service working.
        // 90% chance of success
        return secureRandom.nextFloat() < 0.9
            ? SendEmailResponse.builder().message("Sender 2 - sent successfully").build()
            : SendEmailResponse.builder().message("Sender 2 - sending failed").build();
    }
}
