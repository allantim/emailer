package com.example.emailer.core.usecase;


import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class IsEmailPredicate implements Predicate<String> {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    @Override
    public boolean test(String email) {
        return email.matches(EMAIL_REGEX);
    }
}
