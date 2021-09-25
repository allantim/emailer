package com.example.emailer.core.usecase;

import com.example.emailer.core.dto.SendEmailRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.*;

class SendEmailRequestValidatorTest {

    private final SendEmailRequestValidator testee = new SendEmailRequestValidator(
        new IsEmailPredicate()
    );

    @Test
    void validate_nullToList_Exception() {

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> testee.validate(
                SendEmailRequest.builder()
                    .body("body")
                    .title("title")
                    .build()
            )
        );
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getMessage()).isEqualTo("400 BAD_REQUEST \"Must provide at least one email in the To list\"");
    }

    @Test
    void validate_nullBody_Exception() {

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> testee.validate(
                SendEmailRequest.builder()
                    .to(List.of("aa@aka.com"))
                    .title("title")
                    .build()
            )
        );
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getMessage()).isEqualTo("400 BAD_REQUEST \"Body must be populated\"");
    }

    @Test
    void validate_nullTitle_Exception() {

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> testee.validate(
                SendEmailRequest.builder()
                    .to(List.of("aa@aka.com"))
                    .body("AAA")
                    .build()
            )
        );
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getMessage()).isEqualTo("400 BAD_REQUEST \"Title must be populated\"");
    }

    @Test
    void validate_badEmailsCcBcc_exception() {

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> testee.validate(
                SendEmailRequest.builder()
                    .to(List.of("aa@aka.com"))
                    .cc(List.of("a*a@aka.com")) // Bad
                    .bcc(List.of("aa@aka.a")) // Bad
                    .body("AAA")
                    .title("title")
                    .build()
            )
        );
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getMessage()).isEqualTo("400 BAD_REQUEST \"The following emails are not valid [aa@aka.a, a*a@aka.com]\"");
    }

    @Test
    void validate() {
        assertThatNoException().isThrownBy(
            () -> testee.validate(
                SendEmailRequest.builder()
                    .to(List.of("aa@aka.com"))
                    .body("AAA")
                    .title("title")
                    .build()
            )
        );
    }

    @Test
    void validate_cc_bcc_validemails() {
        assertThatNoException().isThrownBy(
            () -> testee.validate(
                SendEmailRequest.builder()
                    .to(List.of("aa@aka.com", "bb@nan.com"))
                    .cc(List.of("aa@aka.com"))
                    .bcc(List.of("aa@aka.com"))
                    .body("AAA")
                    .title("title")
                    .build()
            )
        );
    }
}