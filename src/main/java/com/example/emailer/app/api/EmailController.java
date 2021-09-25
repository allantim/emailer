package com.example.emailer.app.api;

import com.example.emailer.core.dto.SendEmailRequest;
import com.example.emailer.core.dto.SendEmailResponse;
import com.example.emailer.core.usecase.SendEmailRequestValidator;
import com.example.emailer.core.usecase.emailsender.RetryableEmailWithFallbackSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/email")
public class EmailController {

    private final RetryableEmailWithFallbackSender retryableEmailWithFallbackSender;
    private final SendEmailRequestValidator sendEmailRequestValidator;


    public EmailController(RetryableEmailWithFallbackSender retryableEmailWithFallbackSender,
                           SendEmailRequestValidator sendEmailRequestValidator) {
        this.retryableEmailWithFallbackSender = retryableEmailWithFallbackSender;
        this.sendEmailRequestValidator = sendEmailRequestValidator;
    }

    @PostMapping
    public SendEmailResponse send(@RequestBody SendEmailRequest sendEmailRequest) {
        sendEmailRequestValidator.validate(sendEmailRequest);
        return retryableEmailWithFallbackSender.send(sendEmailRequest);
    }
}
