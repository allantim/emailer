package com.example.emailer.core.usecase;

import com.example.emailer.core.dto.SendEmailRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SendEmailRequestValidator {

    private final IsEmailPredicate isEmailPredicate;

    public SendEmailRequestValidator(IsEmailPredicate isEmailPredicate) {
        this.isEmailPredicate = isEmailPredicate;
    }

    public void validate(SendEmailRequest sendEmailRequest) {
        // To must be populated
        if (CollectionUtils.isEmpty(sendEmailRequest.getTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must provide at least one email in the To list");
        }
        // Title must be populated
        if (StringUtils.isEmpty(sendEmailRequest.getTitle())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title must be populated");
        }
        // Body must be populated
        if (StringUtils.isEmpty(sendEmailRequest.getBody())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body must be populated");
        }

        List<String> cc = sendEmailRequest.getCc() != null?sendEmailRequest.getCc(): Collections.emptyList();
        List<String> bcc = sendEmailRequest.getBcc() != null?sendEmailRequest.getBcc(): Collections.emptyList();
        List<String> allEmails = new ArrayList<>(sendEmailRequest.getTo());
        allEmails.addAll(cc);
        allEmails.addAll(bcc);
        Set<String> badEmails = allEmails.stream().filter(email -> !isEmailPredicate.test(email)).collect(Collectors.toSet());

        if (!badEmails.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                String.format("The following emails are not valid %s", badEmails)
            );
        }
    }
}
