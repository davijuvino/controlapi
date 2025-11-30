package br.com.controlapi.service;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Locale;

import br.com.controlapi.model.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.internet.MimeMessage;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
public class EmailService {

    private final Logger log = LoggerFactory.getLogger(EmailService.class);

    private static final String USUARIO = "u";
    private static final String BASE_URL = "baseUrl";
    private static final String DATA_EVENTO = "dataEvento";

    private final MailProperties mailProperties;
    private final JavaMailSender javaMailSender;
    private final MessageSource messageSource;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.baseurl}")
    private String baseUrl;

    public EmailService(
            MailProperties mailProperties,
            JavaMailSender javaMailSender,
            MessageSource messageSource,
            SpringTemplateEngine templateEngine
    ) {
        this.mailProperties = mailProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());

            message.setTo(to);
            message.setFrom(mailProperties.getUsername()); // Ou use um email específico configurado
            message.setSubject(subject);
            message.setText(content, isHtml);

            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            log.debug("Email sending error details", e);
        }
    }

    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        if (user.getEmail() == null) {
            log.warn("Email address is null for user {}", user.getLogin());
            return;
        }

        Locale locale = Locale.forLanguageTag(user.getLangKey() != null ? user.getLangKey() : "pt");
        Context context = new Context(locale);
        context.setVariable(USUARIO, user);
        // Also expose the variable name `user` because some templates expect `${user}`
        context.setVariable("user", user);
        context.setVariable(BASE_URL, getBaseUrl());
        context.setVariable(DATA_EVENTO, ZonedDateTime.now());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);

        sendEmail(user.getEmail(), subject, content, false, true);
    }

    private String getBaseUrl() {
        return mailProperties.getProperties().getOrDefault("base-url", baseUrl);
    }

    @Async("emailExecutor")
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    }

    @Async("emailExecutor")
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
    }

    @Async("emailExecutor")
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
    }
}
