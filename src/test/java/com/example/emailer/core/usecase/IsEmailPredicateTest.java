package com.example.emailer.core.usecase;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IsEmailPredicateTest {

    private final IsEmailPredicate isEmailPredicate = new IsEmailPredicate();

    @Test
    void validate() {
        assertThat(isEmailPredicate.test("AAA")).isFalse();
        assertThat(isEmailPredicate.test("AAA.COM")).isFalse();
        assertThat(isEmailPredicate.test("TIM@AAA.COM")).isTrue();
    }

    @Test
    void validate_lowercase() {
        assertThat(isEmailPredicate.test("tim@bbb.com")).isTrue();
        assertThat(isEmailPredicate.test("aa@aka.com")).isTrue();
    }

    @Test
    void validate_validspecialchars() {
        assertThat(isEmailPredicate.test("tim._%+-@b.-bb.com")).isTrue();
    }

    @Test
    void validate_invalidspecialchars() {
        assertThat(isEmailPredicate.test("tim*@b.-bb.com")).isFalse();
    }
}