package com.mycom.backenddaengplace.member.service;

import com.mycom.backenddaengplace.auth.email.RedisEmailAuthentication;
import com.mycom.backenddaengplace.member.dto.request.BaseEmailRequest;
import com.mycom.backenddaengplace.member.dto.request.EmailCodeCheckRequest;
import com.mycom.backenddaengplace.member.dto.response.EmailCodeCheckResponse;
import com.mycom.backenddaengplace.member.dto.response.EmailDuplicateCheckResponse;
import com.mycom.backenddaengplace.member.dto.response.EmailSendCodeResponse;
import com.mycom.backenddaengplace.member.exception.EmailAuthenticationCodeMismatchException;
import com.mycom.backenddaengplace.member.exception.EmailNotFoundException;
import com.mycom.backenddaengplace.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EmailService {

    @Value("${spring.mail.username}")
    private String senderEmail;

    private final MemberRepository memberRepository;
    private final JavaMailSender javaMailSender;
    private final RedisEmailAuthentication redisEmailAuthentication;

    public EmailDuplicateCheckResponse checkDuplicateEmail(BaseEmailRequest request) {
        boolean isValid = memberRepository.existsByEmail(request.getEmail());
        return EmailDuplicateCheckResponse.from(!isValid);
    }

    // 랜덤으로 숫자 생성
    public String createCode() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) { // 인증 코드 8자리
            int index = random.nextInt(3); // 0~2까지 랜덤, 랜덤값으로 switch문 실행
            switch (index) {
                case 0 -> key.append((char) (random.nextInt(26) + 97)); // 소문자
                case 1 -> key.append((char) (random.nextInt(26) + 65)); // 대문자
                case 2 -> key.append(random.nextInt(10)); // 숫자
            }
        }
        return key.toString();
    }

    public MimeMessage createMail(String mail, String code) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, mail);
        message.setSubject("댕댕플레이스 이메일 인증");
        String body = "";
        body += "<h1>안녕하세요. 댕댕플레이스 입니다.</h1>";
        body += "<h3>요청하신 인증 번호입니다.</h3>";
        body += "<h1>" + code + "</h1>";
        body += "<h3>인증 번호는 3분 뒤에 파기됩니다.</h3>";
        body += "<h3>감사합니다.</h3>";
        message.setText(body, "UTF-8", "html");
        return message;
    }

    // 메일 발송
    public EmailSendCodeResponse sendEmailCode(String sendEmail) throws MessagingException {
        String code = createCode();
        redisEmailAuthentication.setEmailAuthenticationExpire(sendEmail, code, 3L);
        MimeMessage message = createMail(sendEmail, code);
        try {
            log.debug("Attempting to send email to: {}", sendEmail);
            log.debug("Using sender email: {}", senderEmail);
            log.debug("Generated code: {}", code);
            javaMailSender.send(message);
            log.debug("Email sent successfully");
        } catch (MailException e) {
            log.error("Failed to send email", e);  // 구체적인 에러 로깅
            throw new IllegalArgumentException("메일 발송 중 오류가 발생했습니다.: " + e.getMessage());
        }
        return EmailSendCodeResponse.from(sendEmail, code);
    }

    /* 인증번호 확인 */
    public EmailCodeCheckResponse checkEmailCode(EmailCodeCheckRequest request) {

        String code = redisEmailAuthentication.getEmailAuthenticationCode(request.getEmail());

        if (code == null) {
            throw new EmailNotFoundException();
        }

        if (!code.equals(request.getCode())) {
            throw new EmailAuthenticationCodeMismatchException();
        }

        redisEmailAuthentication.setEmailAuthenticationComplete(request.getEmail());

        return EmailCodeCheckResponse.from(true);
    }

}