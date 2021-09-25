package com.example.emailer.dataprovider;

import com.example.emailer.core.dto.SendEmailRequest;
import com.example.emailer.core.dto.SendEmailResponse;
import com.example.emailer.core.repository.EmailClient;
import com.example.emailer.core.repository.EmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Repository("emailClientMailGun")
@Slf4j
public class EmailClientMailGun implements EmailClient {

    private final RestTemplate restTemplate;
    private final String url;
    private final String basicAuthUser;
    private final String basicAuthPassword;

    public EmailClientMailGun(RestTemplate restTemplate,
                              @Value("${app.email-client-mail-gun.url}") String url,
                              @Value("${app.email-client-mail-gun.auth-user}") String basicAuthUser,
                              @Value("${app.email-client-mail-gun.auth-password}") String basicAuthPassword) {
        this.restTemplate = restTemplate;
        this.url = url;
        this.basicAuthUser = basicAuthUser;
        this.basicAuthPassword = basicAuthPassword;
    }

    @Override
    public SendEmailResponse send(SendEmailRequest sendEmailRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();

            headers.setBasicAuth(basicAuthUser, basicAuthPassword);

            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            sendEmailRequest.getTo().stream().forEach(
                email -> map.add("to", email)
            );
            if (CollectionUtils.isNotEmpty(sendEmailRequest.getCc())) {
                sendEmailRequest.getCc().stream().forEach(
                    email -> map.add("cc", email)
                );
            }
            if (CollectionUtils.isNotEmpty(sendEmailRequest.getBcc())) {
                sendEmailRequest.getCc().stream().forEach(
                    email -> map.add("bcc", email)
                );
            }
            map.add("subject", sendEmailRequest.getTitle());
            map.add("text", sendEmailRequest.getBody());
            map.add("from", "allantim@hotmail.com");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<SendEmailResponse> response = restTemplate.postForEntity(url, request, SendEmailResponse.class);
            return response.getBody();
        } catch (Throwable t) {
            log.error("Exception calling MailGun", t);
            throw t;
        }
    }
}
