package com.example.emailer.core.usecase.emailsender;

import com.example.emailer.core.EmailSender;
import com.example.emailer.core.dto.SendEmailRequest;
import com.example.emailer.core.dto.SendEmailResponse;
import com.example.emailer.core.repository.EmailClient;

import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * This uses resilience4j Retry functionality
 * It will attempt to use Email supplier1 and if will retry a configurable number of times
 * Failing that it will try Email Supplier2, again with a configurable retry.
 *
 * Finally if that fails too, it will hit a hard coded Supplier which will produce
 * a hard coded message
 */
@Component
@Slf4j
public class RetryableEmailWithFallbackSender implements EmailSender {
    private final Retry sendEmailRetry;
    private final EmailClient mainSender;
    private final EmailClient secondarySender;

    public RetryableEmailWithFallbackSender(Retry sendEmailRetry,
                                            @Qualifier("emailClientMailGun") EmailClient mainSender,
                                            @Qualifier("emailClientTwo") EmailClient secondarySender) {
        this.sendEmailRetry = sendEmailRetry;
        this.mainSender = mainSender;
        this.secondarySender = secondarySender;
    }

    @Override
    public SendEmailResponse send(SendEmailRequest sendEmailRequest) {
        return Decorators
            .ofSupplier(
                () -> mainSender.send(sendEmailRequest)
            )
            .withRetry(sendEmailRetry)
            .withFallback(
                (t) -> secondarySupplier(t, sendEmailRequest)
            )
            .get();
    }

    private SendEmailResponse secondarySupplier(Throwable exc, SendEmailRequest sendEmailRequest) {
        log.info("primarySupplierFailed -> call secondary supplier");
        return Decorators
            .ofSupplier(
                () -> secondarySender.send(sendEmailRequest)
            )
            .withRetry(sendEmailRetry)
            .withFallback(
                (t) -> {
                    log.info("secondarySupplierFailed -> hard coded fallback");
                    return  SendEmailResponse.builder().message("Unable to send, please try later").build();
                }
            )
            .get();
    }
}
