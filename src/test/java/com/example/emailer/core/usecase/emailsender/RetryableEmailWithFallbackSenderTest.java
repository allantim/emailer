package com.example.emailer.core.usecase.emailsender;

import com.example.emailer.core.dto.SendEmailRequest;
import com.example.emailer.core.dto.SendEmailResponse;
import com.example.emailer.core.repository.EmailClient;
import com.example.emailer.core.usecase.emailsender.RetryableEmailWithFallbackSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
class RetryableEmailWithFallbackSenderTest {

	@MockBean
	@Qualifier("emailClientMailGun")
	private EmailClient emailClient1;

	@MockBean
	@Qualifier("emailClientTwo")
	private EmailClient emailClient2;

	@Autowired
	private RetryableEmailWithFallbackSender retryableEmailWithFallbackSender;

	@Test
	void client1NoIssue() {
		SendEmailRequest req = SendEmailRequest.builder().build();
		when(emailClient1.send(req))
			.thenReturn(SendEmailResponse.builder().message("client1Sent").build());

		assertThat(retryableEmailWithFallbackSender.send(req).getMessage()).isEqualTo("client1Sent");
		verifyNoInteractions(emailClient2);
	}

	@Test
	void client1_exceptionThenFine() {
		SendEmailRequest req = SendEmailRequest.builder().build();
		when(emailClient1.send(req))
			.thenThrow(new RuntimeException("client1Error"))
			.thenReturn(SendEmailResponse.builder().message("client1Sent").build());

		assertThat(retryableEmailWithFallbackSender.send(req).getMessage()).isEqualTo("client1Sent");

		verifyNoInteractions(emailClient2);
	}

	@Test
	void client1_twoExceptionsThenFine() {
		SendEmailRequest req = SendEmailRequest.builder().build();
		when(emailClient1.send(req))
			.thenThrow(new RuntimeException("client1Error"))
			.thenThrow(new RuntimeException("client1Error"))
			.thenReturn(SendEmailResponse.builder().message("client1Sent").build());

		assertThat(retryableEmailWithFallbackSender.send(req).getMessage()).isEqualTo("client1Sent");

		verifyNoInteractions(emailClient2);
	}

	@Test
	void client1ThreeExceptions_callClient2() {
		SendEmailRequest req = SendEmailRequest.builder().build();
		when(emailClient1.send(req))
			.thenThrow(new RuntimeException("client1Error"))
			.thenThrow(new RuntimeException("client1Error"))
			.thenThrow(new RuntimeException("client1Error"));

		when(emailClient2.send(req))
			.thenReturn(SendEmailResponse.builder().message("client2Sent").build());

		assertThat(retryableEmailWithFallbackSender.send(req).getMessage()).isEqualTo("client2Sent");

	}

	@Test
	void client1ThreeExceptions_callClient2HasTwoExceptionsThenSuccess() {
		SendEmailRequest req = SendEmailRequest.builder().build();
		when(emailClient1.send(req))
			.thenThrow(new RuntimeException("client1Error"))
			.thenThrow(new RuntimeException("client1Error"))
			.thenThrow(new RuntimeException("client1Error"));

		when(emailClient2.send(req))
			.thenThrow(new RuntimeException("client2Error"))
			.thenThrow(new RuntimeException("client2Error"))
			.thenReturn(SendEmailResponse.builder().message("client2Sent").build());

		assertThat(retryableEmailWithFallbackSender.send(req).getMessage()).isEqualTo("client2Sent");

	}

	@Test
	void client1ThreeExceptions_callClient3HasThreeExceptions_useBackup() {
		SendEmailRequest req = SendEmailRequest.builder().build();
		when(emailClient1.send(req))
			.thenThrow(new RuntimeException("client1Error"))
			.thenThrow(new RuntimeException("client1Error"))
			.thenThrow(new RuntimeException("client1Error"));

		when(emailClient2.send(req))
			.thenThrow(new RuntimeException("client2Error"))
			.thenThrow(new RuntimeException("client2Error"))
			.thenThrow(new RuntimeException("client2Error"));

		assertThat(retryableEmailWithFallbackSender.send(req).getMessage()).isEqualTo("Unable to send, please try later");

	}
}
