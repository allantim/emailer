package com.example.emailer.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SendEmailRequest {
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private String title;
    private String body;
}
